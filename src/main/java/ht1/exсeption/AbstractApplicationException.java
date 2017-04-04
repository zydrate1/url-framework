package ht1.exсeption;

/**
 * Abstract exception class
 * It will inherit by all 
 * application exception classes
 */
public abstract class AbstractApplicationException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message exception message
	 */
	public AbstractApplicationException(String message) {
		super(message);
	}

	/**
	 * @param message exception message
	 * @param cause Throwable inner 
	 * exception instance, which is 
	 * the cause of this exception
	 */
	public AbstractApplicationException(String message, Throwable cause) {
		super(message, cause);
	}
}
