package oop.ex6.validator;

/**
 * Is thrown when there's a problem with a variable.
 */
public abstract class VariableException extends InvalidFileException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new exception with the specified detail message.
	 * @param message The detail message.
	 */
	public VariableException(final String message) {
		super(message);
	}

}
