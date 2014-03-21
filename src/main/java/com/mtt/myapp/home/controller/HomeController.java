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

import java.util.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mtt.myapp.common.constant.SystemConstants;
import com.mtt.myapp.common.controller.BaseController;
import com.mtt.myapp.common.util.ThreadUtil;
import com.mtt.myapp.home.model.PanelEntry;
import com.mtt.myapp.home.service.HomeService;
import com.mtt.myapp.infra.schedule.ScheduledTaskService;
import com.mtt.myapp.user.model.User;
import com.mtt.myapp.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Home index page controller.
 *
 * @author JunHo Yoon
 * @since 3.0
 */
@Controller
@RequestMapping("home")
public class HomeController extends BaseController implements SystemConstants {

	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private HomeService homeService;

	@Autowired
	private UserService userService;

	private static final String TIMEZONE_ID_PREFIXES = "^(Africa|America|Asia|Atlantic|"
			+ "Australia|Europe|Indian|Pacific)/.*";

	private List<TimeZone> timeZones = null;

	@Autowired
	private ScheduledTaskService scheduledTaskService;

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

	@ResponseBody
	@RequestMapping("/panel_entries")
	public String getPanelEntries() {
		Map<String, Object> entries = new HashMap<>();
		entries.put("left_panel_entries", getLeftPanelEntries());
		entries.put("right_panel_entries", getRightPanelEntries());

		entries.put("see_more_question_url", config.getSystemProperties().getProperty(PROP_SYSTEM_FRONT_PAGE_QNA_MORE));
		entries.put("see_more_resources_url", config.getSystemProperties().getProperty
				(PROP_SYSTEM_FRONT_PAGE_RESOURCES_MORE));
		return toJson(entries);
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
	@RequestMapping("/healthcheck")
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
	@RequestMapping("/healthcheck_slow")
	public HttpEntity<String> healthCheckSlowly(@RequestParam(value = "delay", defaultValue = "1000") int sleep,
												HttpServletResponse response) {
		ThreadUtil.sleep(sleep);
		return healthCheck(response);
	}

	@ResponseStatus( HttpStatus.OK )
	@RequestMapping("/set_language")
	public void setLanguage(String lan, HttpServletResponse response, HttpServletRequest request) {
		LocaleResolver localeResolver = checkNotNull(RequestContextUtils.getLocaleResolver(request),
				"No LocaleResolver found!");
		LocaleEditor localeEditor = new LocaleEditor();
		String language = StringUtils.defaultIfBlank(lan, config.getDefaultLanguage());
		localeEditor.setAsText(language);
		localeResolver.setLocale(request, response, (Locale) localeEditor.getValue());
	}


	@RequestMapping("/signup")
	public String signup(@ModelAttribute("user") User newUser) {
		userService.save(newUser);
		return "redirect:/index.html";
	}

}
