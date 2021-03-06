package com.mtt.myapp.common.util;

import java.util.Properties;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenient class for property extraction.
 * 
 * @author Mavlarn
 */
public class PropertiesWrapper {
	private final Properties properties;
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesWrapper.class);

	private static final String DEFAULT_ERROR_MESSGAE = "The {} is not defined in conf file. Use {} instead.";

	/**
	 * Constructor.
	 * 
	 * @param properties
	 *            {@link java.util.Properties} which will be used for data retrieval.
	 */
	public PropertiesWrapper(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Get property.
	 * 
	 * @param key
	 *            property key
	 * @param defaultValue
	 *            default value when data is not available
	 * @param errorMsgTemplate
	 *            error msg
	 * @return property value
	 */
	public String getProperty(String key, String defaultValue, String errorMsgTemplate) {
		String value = this.properties.getProperty(key);
		if (value == null) {
			LOGGER.trace(errorMsgTemplate, key, defaultValue);
			value = defaultValue;
		} else {
			value = value.trim();
		}
		return value;
	}

	/**
	 * Get property.
	 * 
	 * @param key
	 *            property key
	 * @param defaultValue
	 *            default value when data is not available
	 * @return property value
	 */
	public String getProperty(String key, String defaultValue) {
		return StringUtils.trim(getProperty(key, defaultValue, DEFAULT_ERROR_MESSGAE));
	}

	public String getProperty(String key) {
		return StringUtils.trim(this.properties.getProperty(key));
	}

	/**
	 * Add property.
	 * 
	 * @param key
	 *            property key
	 * @param value
	 *            property value
	 */
	public void addProperty(String key, String value) {
		this.properties.put(key, value);
	}

	/**
	 * Get property as integer.
	 * 
	 * @param key
	 *            property key
	 * @param defaultValue
	 *            default value when data is not available
	 * @return property integer value
	 */
	public int getPropertyInt(String key, int defaultValue) {
		String property = getProperty(key, String.valueOf(defaultValue), DEFAULT_ERROR_MESSGAE);
		return NumberUtils.toInt(property, defaultValue);
	}

	/**
	 * Get property as boolean.
	 * 
	 * @param key
	 *            property key
	 * @param defaultValue
	 *            default value when data is not available
	 * @return property boolean value
	 */
	public boolean getPropertyBoolean(String key, boolean defaultValue) {
		String property = getProperty(key, String.valueOf(defaultValue), DEFAULT_ERROR_MESSGAE);
		return BooleanUtils.toBoolean(property);
	}

	/**
	 * Set property.
	 * 
	 * @param key
	 *            key
	 * @param value
	 *            value to be stored.
	 */
	public void setProperty(String key, String value) {
		this.properties.setProperty(key, value);
	}
}
