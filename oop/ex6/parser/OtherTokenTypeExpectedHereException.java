package oop.ex6.parser;

import oop.ex6.lexer.Token;

/**
 * will be thrown if required a other type of token
 * e.g. after "if" required "(", after ";" required new line and so on
 */
public class OtherTokenTypeExpectedHereException extends ParsingFailedException {

    public static final String ERROR_MESSAGE_FORMAT = "%s need here instead of %s(%s) (%s)";

    /**
     * Ctor
     *
     * @param tok  - the actual token
     * @param type - the required token type
     */
    public OtherTokenTypeExpectedHereException(Token tok, Token.TokenType type) {
        super(String.format(ERROR_MESSAGE_FORMAT,   type.name(),
                                                    tok.getType().name(),
                                                    tok.getData(),
                                                    tok.getStartPosition().toString()));
    }
}
