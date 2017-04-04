package ht1.file.exception;

import ht1.exсeption.AbstractApplicationException;

/**
 * error class inherits <code>AbstractApplicationException<code>
 * Class to create <code>FileException</code> exceptions objects
 */
public class FileException extends AbstractApplicationException {

	private static final long serialVersionUID = -3095767010774712207L;

	/**
	 * @param message exception message
	 */
	public FileException(String message) {
		super(message);
	}
	
	/**
	 * @param message exception message
	 * @param cause Throwable inner 
	 * exception instance, which is 
	 * the cause of this exception
	 */
	public FileException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
