/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.mtt.myapp.home.controller;


import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mtt.myapp.common.constant.SystemConstants;
import com.mtt.myapp.common.controller.BaseController;
import com.mtt.myapp.common.excpetion.CustomRuntimeException;
import com.mtt.myapp.common.util.PropertiesWrapper;
import com.mtt.myapp.common.util.ThreadUtil;
import com.mtt.myapp.home.model.PanelEntry;
import com.mtt.myapp.home.service.HomeService;
import com.mtt.myapp.infra.schedule.ScheduledTaskService;
import com.mtt.myapp.user.model.Role;
import com.mtt.myapp.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.http.HttpEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Home index page controller.
 *
 * @author JunHo Yoon
 * @since 3.0
 */
@Controller
public class HomeController extends BaseController implements SystemConstants {

	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private HomeService homeService;

	private static final String TIMEZONE_ID_PREFIXES = "^(Africa|America|Asia|Atlantic|"
			+ "Australia|Europe|Indian|Pacific)/.*";

	private List<TimeZone> timeZones = null;

	@Autowired
	private ScheduledTaskService scheduledTaskService;

	private static Gson rawObjectJsonSerializer = new GsonBuilder().setPrettyPrinting().create();

	/**
	 * Initialize {@link HomeController}.
	 */
	@PostConstruct
	public void init() {
		timeZones = new ArrayList<TimeZone>();
		final String[] timeZoneIds = TimeZone.getAvailableIDs();
		for (final String id : timeZoneIds) {
			if (id.matches(TIMEZONE_ID_PREFIXES) && !TimeZone.getTimeZone(id).getDisplayName().contains("GMT")) {
				timeZones.add(TimeZone.getTimeZone(id));
			}
		}
		Collections.sort(timeZones, new Comparator<TimeZone>() {
			public int compare(final TimeZone a, final TimeZone b) {
				return a.getID().compareTo(b.getID());
			}
		});
		scheduledTaskService.runAsync(new Runnable() {
			@Override
			public void run() {
				getLeftPanelEntries();
				getRightPanelEntries();
			}
		});
	}

	/**
	 * Return nGrinder index page.
	 *
	 * @param user      user
	 * @param exception exception if it's redirected from exception handler
	 * @param region    region. where this access comes from. it's optional
	 * @param model     model
	 * @param response  response
	 * @param request   request
	 * @return "index" if already logged in. Otherwise "login".
	 */
	@RequestMapping(value = {"/home", "/"})
	public String home(User user, @RequestParam(value = "exception", defaultValue = "") String exception,
					   @RequestParam(value = "region", defaultValue = "") String region, ModelMap model,
					   HttpServletResponse response, HttpServletRequest request) {
		try {
			Role role;
			try {
				// set local language
				setLanguage(getCurrentUser().getUserLanguage(), response, request);
				setLoginPageDate(model);
				role = user.getRole();
			} catch (AuthenticationCredentialsNotFoundException e) {
				return "login";
			}
			setPanelEntries(model);

			if (StringUtils.isNotBlank(exception)) {
				model.addAttribute("exception", exception);
			}
			if (role == Role.ADMIN || role == Role.SUPER_USER || role == Role.USER) {
				return "index";
			} else {
				LOG.info("Invalid user role:{}", role.getFullName());
				return "login";
			}
		} catch (Exception e) {
			// Make the home reliable...
			model.addAttribute("exception", e.getMessage());
			return "index";
		}
	}

	private void setPanelEntries(ModelMap model) {
		model.addAttribute("left_panel_entries", getLeftPanelEntries());
		model.addAttribute("right_panel_entries", getRightPanelEntries());

		model.addAttribute("see_more_question_url", config.getSystemProperties().getProperty(PROP_SYSTEM_FRONT_PAGE_QNA_MORE));
		model.addAttribute("see_more_resources_url", config.getSystemProperties().getProperty
				(PROP_SYSTEM_FRONT_PAGE_RESOURCES_MORE));


	}

	private List<PanelEntry> getRightPanelEntries() {
			// Get nGrinder Resource RSS
			String rightPanelRssURL = config.getSystemProperties().getProperty(PROP_SYSTEM_FRONT_PAGE_RESOURCES_RSS);
			return homeService.getRightPanelEntries(rightPanelRssURL);
	}

	private List<PanelEntry> getLeftPanelEntries() {
			// Make admin configure the QnA panel.
			String leftPanelRssURL = config.getSystemProperties().getProperty(PROP_SYSTEM_FRONT_PAGE_QNA_RSS);
			return homeService.getLeftPanelEntries(leftPanelRssURL);
	}

	/**
	 * Return the health check message. If there is shutdown lock, it returns
	 * 503. Otherwise it returns region lists.
	 *
	 * @param response response
	 * @return region list
	 */
	@RequestMapping("/check/healthcheck")
	public HttpEntity<String> healthCheck(HttpServletResponse response) {
		return successJsonHttpEntity();
	}

	/**
	 * Return health check message with 1 sec delay. If there is shutdown lock,
	 * it returns 503. Otherwise, it returns region lists.
	 *
	 * @param sleep    in milliseconds.
	 * @param response response
	 * @return region list
	 */
	@ResponseBody
	@RequestMapping("/check/healthcheck_slow")
	public HttpEntity<String> healthCheckSlowly(@RequestParam(value = "delay", defaultValue = "1000") int sleep,
												HttpServletResponse response) {
		ThreadUtil.sleep(sleep);
		return healthCheck(response);
	}

	private void setLanguage(String lan, HttpServletResponse response, HttpServletRequest request) {
		LocaleResolver localeResolver = checkNotNull(RequestContextUtils.getLocaleResolver(request),
				"No LocaleResolver found!");
		LocaleEditor localeEditor = new LocaleEditor();
		String language = StringUtils.defaultIfBlank(lan, config.getDefaultLanguage());
		localeEditor.setAsText(language);
		localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());
	}

	/**
	 * Return the login page.
	 *
	 * @param model model
	 * @return "login" if not logged in. Otherwise, "/"
	 */
	@RequestMapping(value = "/login")
	public String loginForm(ModelMap model) {
		setLoginPageDate(model);
		model.addAttribute("signUpEnabled", config.isSignUpEnabled());
		final String defaultLang = config.getDefaultLanguage();
		model.addAttribute("defaultLang", defaultLang);
		try {
			getCurrentUser();
		} catch (Exception e) {
			LOG.info("Login Failure " + e.getMessage());
			return "login";
		}
		model.clear();
		return "redirect:/";
	}

	private void setLoginPageDate(ModelMap model) {
		TimeZone defaultTime = TimeZone.getDefault();
		model.addAttribute("timezones", timeZones);
		model.addAttribute("defaultTime", defaultTime.getID());
	}

	/**
	 * Error redirection to 404.
	 *
	 * @return "redirect:/doError"
	 */
	@RequestMapping(value = "/error_404")
	public String error404(ModelMap model) {
		model.clear();
		return "redirect:/doError?type=404";
	}

	/**
	 * Error redirection as a second phase.
	 *
	 * @param user     user
	 * @param model    model
	 * @param response response
	 * @param request  request
	 * @return "index"
	 */
	@RequestMapping(value = "/doError")
	public String second(User user, ModelMap model, HttpServletResponse response, HttpServletRequest request) {
		String parameter = request.getParameter("type");
		if ("404".equals(parameter)) {
			model.addAttribute("exception", new CustomRuntimeException("Requested URL does not exist"));
		}
		return home(user, null, null, model, response, request);
	}

}
