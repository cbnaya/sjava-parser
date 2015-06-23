package oop.ex6.validator;

/**
 * Is thrown when there's some problem regarding a s-java file.
 */
public abstract class InvalidCodeException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message.
     */
    public InvalidCodeException(String message) {
        super(message);
    }
}
