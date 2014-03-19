package com.mtt.myapp.common.excpetion;

/**
 * Configuration Exception.
 * 
 * @author Mavlarn
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ConfigurationException extends CustomRuntimeException {

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            message
	 * @param t
	 *            root cause
	 */
	public ConfigurationException(String message, Throwable t) {
		super(message, t);
	}
}
