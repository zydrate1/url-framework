package ht1.framework.exception;

import ht1.exсeption.AbstractApplicationException;

/**
 * error class inherits <code>AbstractApplicationException<code>
 * Class to create <code>TestDispatcherException</code> exceptions objects
 */
public class TestStarterException extends AbstractApplicationException{

	private static final long serialVersionUID = 5764386790258573452L;

	/**
	 * @param message exception message
	 */
	public TestStarterException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * @param message exception message
	 * @param cause Throwable inner 
	 * exception instance, which is 
	 * the cause of this exception
	 */
	public TestStarterException(String message) {
		super(message);
	}

}
