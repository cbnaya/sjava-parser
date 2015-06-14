package parser;

import lexer.Token;

public class OtherTokenTypeNeedHere extends Throwable {
    public OtherTokenTypeNeedHere(Token tok, Token.TokenType type)
    {
        super(String.format("error (%d) need here token of type %s instead of %s (%s)",
                tok.getStartPosition(), type.name(),tok.getType().name(),tok.getData()));
    }
}
