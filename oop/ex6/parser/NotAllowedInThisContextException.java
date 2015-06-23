package oop.ex6.parser;

import oop.ex6.lexer.Token;

/**
 * will thrown in case of token that invalid in this context
 * e.g. comma in the if condition or method declaration in while scope and so on
 */
public class NotAllowedInThisContextException extends ParsingFailedException {

    public static final String ERROR_MESSAGE_FORMAT = "token of type %s (%s) not allowed in this context (%s) ";

    /**
     * Ctor
     *
     * @param tok - the token that not allow
     */
    public NotAllowedInThisContextException(Token tok) {
        super(String.format(ERROR_MESSAGE_FORMAT, tok.getType().name(), tok.getData(),
                                                            tok.getStartPosition().toString()));
    }
}
