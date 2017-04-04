package ht1.framework.exception;

import ht1.exсeption.AbstractApplicationException;

/**
 * error class inherits <code>AbstractApplicationException<code>
 * Class to create <code>FrameworkException</code> exceptions objects
 */
public class FrameworkException extends AbstractApplicationException{

	private static final long serialVersionUID = 3994042606177114730L;

	/**
	 * @param message exception message
	 */
	public FrameworkException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * @param message exception message
	 * @param cause Throwable inner 
	 * exception instance, which is 
	 * the cause of this exception
	 */
	public FrameworkException(String message) {
		super(message);
	}	

}
