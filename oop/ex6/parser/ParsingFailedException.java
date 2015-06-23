package oop.ex6.parser;

/**
 * Will be thrown in case that the parsing failed.
 * This exception is the root in the exception hierarchy of the
 * parsing exceptions.
 */
public class ParsingFailedException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
     * Ctor
     * @param errorMsg the error message
     */
    public ParsingFailedException(String errorMsg) {
        super(errorMsg);
    }
}
