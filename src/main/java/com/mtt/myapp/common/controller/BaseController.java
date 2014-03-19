package com.mtt.myapp.common.controller;

import static com.mtt.myapp.common.util.NoOp.noOp;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mtt.myapp.common.constant.SystemConstants;
import com.mtt.myapp.common.excpetion.CustomRuntimeException;
import com.mtt.myapp.infra.config.AppConfig;
import com.mtt.myapp.infra.config.AppHome;
import com.mtt.myapp.operation.service.AnnouncementService;
import com.mtt.myapp.user.model.User;
import com.mtt.myapp.user.service.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller base containing widely used methods.
 *
 * @author JunHo Yoon
 * @since 3.0
 */
public class BaseController implements SystemConstants {
	private static String successJson;
	private static String errorJson;
	private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserContext userContext;

	@Autowired
	protected AppConfig config;

	@Autowired
	protected AppHome home;

	@Autowired
	private AnnouncementService announcementService;

	@PostConstruct
	void initJSON() {
		JsonObject rtnJson = new JsonObject();
		rtnJson.addProperty(JSON_SUCCESS, true);
		successJson = rtnJson.toString();
		rtnJson.addProperty(JSON_SUCCESS, false);
		errorJson = rtnJson.toString();
	}

	/**
	 * Get the current user.
	 *
	 * @return current user
	 */
	public User getCurrentUser() {
		return userContext.getCurrentUser();
	}

	protected void putPageIntoModelMap(ModelMap model, Pageable pageable) {
		model.addAttribute("page", pageable);
		final Iterator<Sort.Order> iterator = pageable.getSort().iterator();
		if (iterator.hasNext()) {
			Sort.Order sortProp = iterator.next();
			model.addAttribute("sortColumn", sortProp.getProperty());
			model.addAttribute("sortDirection", sortProp.getDirection());
		}
	}

	/**
	 * Provide the current login user as a model attribute. If it's not found, return empty user.
	 *
	 * @return login user
	 */
	@ModelAttribute("currentUser")
	public User currentUser() {
		try {
			return getCurrentUser();
		} catch (AuthenticationCredentialsNotFoundException e) {
			// Fall through
			noOp();
		}
		return new User();
	}

	/**
	 * Provide the announcement content as a model attribute.
	 *
	 * @return announcement content
	 */
	@ModelAttribute("announcement")
	public Map<String, String> announcement() {
		return announcementService.getAnnouncement();
	}

	/**
	 * Get the message from messageSource by the given key.
	 *
	 * @param key key of message
	 * @return the found message. If not found, the error message will return.
	 */
	protected String getMessages(String key) {
		String userLanguage = getCurrentUser().getUserLanguage();
		Locale locale = new Locale(userLanguage);
		return messageSource.getMessage(key, null, locale);
	}

	/**
	 * Return the success json message.
	 *
	 * @param message message
	 * @return json message
	 */
	public String returnSuccess(String message) {
		JsonObject rtnJson = new JsonObject();
		rtnJson.addProperty(JSON_SUCCESS, true);
		rtnJson.addProperty(JSON_MESSAGE, message);
		return rtnJson.toString();
	}

	/**
	 * Return the error json message.
	 *
	 * @param message message
	 * @return json message
	 */
	public String returnError(String message) {
		JsonObject rtnJson = new JsonObject();
		rtnJson.addProperty(JSON_SUCCESS, false);
		rtnJson.addProperty(JSON_MESSAGE, message);
		return rtnJson.toString();
	}

	/**
	 * Return the raw success json message.
	 *
	 * @return json message
	 */
	public String returnSuccess() {
		return successJson;
	}

	/**
	 * Return the raw error json message.
	 *
	 * @return json message
	 */
	public String returnError() {
		return errorJson;
	}

	/**
	 * Convert the given list into a json message.
	 *
	 * @param list list
	 * @return json message
	 */
	public String toJson(List<?> list) {
		return gson.toJson(list);
	}

	/**
	 * Convert the given object into a json message.
	 *
	 * @param obj object
	 * @return json message
	 */
	public String toJson(Object obj) {
		return gson.toJson(obj);
	}

	/**
	 * Convert the given object into a json message.
	 *
	 * @param <T>     content type
	 * @param content content
	 * @param header  header value map
	 * @return json message
	 */
	public <T> HttpEntity<T> toHttpEntity(T content, MultiValueMap<String, String> header) {
		return new HttpEntity<T>(content, header);
	}

	/**
	 * Convert the given object into a {@link HttpEntity} containing the converted json message.
	 *
	 * @param content content
	 * @return {@link HttpEntity} class containing the converted json message
	 */
	public HttpEntity<String> toJsonHttpEntity(Object content) {
		return toJsonHttpEntity(content, gson);
	}

	/**
	 * Convert the given object into a {@link HttpEntity} containing the converted json message.
	 *
	 * @return {@link HttpEntity} class containing the converted json message
	 */
	public HttpEntity<String> successJsonHttpEntity() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("content-type", "application/json; charset=UTF-8");
		responseHeaders.setPragma("no-cache");
		return toHttpEntity(successJson, responseHeaders);
	}

	/**
	 * Convert the given object into a {@link HttpEntity} containing the converted json message.
	 *
	 * @return {@link HttpEntity} class containing the converted json message
	 */
	public HttpEntity<String> errorJsonHttpEntity() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("content-type", "application/json; charset=UTF-8");
		responseHeaders.setPragma("no-cache");
		return toHttpEntity(errorJson, responseHeaders);
	}

	/**
	 * Convert the object with the given serializer into {@link HttpEntity}.
	 *
	 * @param content    content
	 * @param serializer custom JSON serializer
	 * @return json message
	 */
	public HttpEntity<String> toJsonHttpEntity(Object content, Gson serializer) {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("content-type", "application/json; charset=UTF-8");
		responseHeaders.setPragma("no-cache");
		return toHttpEntity(serializer.toJson(content), responseHeaders);
	}


	/**
	 * Exception handler to forward to front page showing the error message box.
	 *
	 * @param e occurred exception
	 * @return modal and view having the exception message
	 */
	@ExceptionHandler({CustomRuntimeException.class})
	public ModelAndView handleException(CustomRuntimeException e) {
		//noinspection SpringMVCViewInspection
		ModelAndView modelAndView = new ModelAndView("forward:/");
		modelAndView.addObject("exception", e.getMessage());
		return modelAndView;
	}


}
