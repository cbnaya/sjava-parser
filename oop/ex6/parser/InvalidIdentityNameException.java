package oop.ex6.parser;

import oop.ex6.lexer.Token;

/**
 * will thrown in case that an identity is invalid
 * e.g. var that called _, member that start with number and so on
 */
public class InvalidIdentityNameException extends ParsingFailedException {

    private static final long serialVersionUID = 1L;
    public static final String ERROR_MESSAGE_FORMAT =
            "invalid identity name: %s (%s)";

    /**
     * Ctor
     *
     * @param tok - the problematic identity token
     */
    public InvalidIdentityNameException(Token tok) {
        super(String.format(ERROR_MESSAGE_FORMAT, tok.getData(),
                tok.getStartPosition().toString()));
    }
}
