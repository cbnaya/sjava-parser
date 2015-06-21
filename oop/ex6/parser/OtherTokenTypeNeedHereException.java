package oop.ex6.parser;

import oop.ex6.lexer.Token;

public class OtherTokenTypeNeedHereException extends Exception {
    public OtherTokenTypeNeedHereException(Token tok, Token.TokenType type)
    {
        super(String.format("error (%s) need here token of type %s instead of %s (%s)",
                tok.getStartPosition().toString(), type.name(),tok.getType().name(),tok.getData()));
    }
}
