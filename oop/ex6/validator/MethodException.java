package oop.ex6.validator;

/**
 * Is thrown when there's a problem with a method.
 */
public abstract class MethodException extends InvalidCodeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message.
     */
    public MethodException(String message) {
        super(message);
    }
}
