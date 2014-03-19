package com.mtt.myapp.common.excpetion;

/**
 * CustomRuntimeException. This is for translating a general exception to {@link RuntimeException}
 * .
 * 
 * @author Mavlarn
 * @since 1.0
 */
public class CustomRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 8662535812004958944L;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            message
	 */
	public CustomRuntimeException(String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            message
	 * @param t
	 *            root cause
	 */
	public CustomRuntimeException(String message, Throwable t) {
		super(message, t);
	}
}
