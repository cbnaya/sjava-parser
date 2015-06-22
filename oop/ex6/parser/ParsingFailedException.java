package oop.ex6.parser;

/**
 * will thrown in case that the parsing is failed
 * this exception is the root in the exception hierarchy of the
 * parsing exceptions
 */
public class ParsingFailedException extends Exception{

    /**
     * Ctor
     * @param errorMsg - the error message
     */
    public ParsingFailedException(String errorMsg)
    {
        super(errorMsg);
    }
}
