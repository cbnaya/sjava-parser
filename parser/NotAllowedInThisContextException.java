package parser;

import lexer.Token;

/**
 * Created by cbnaya on 14/06/2015.
 */
public class NotAllowedInThisContextException extends Exception {
    public NotAllowedInThisContextException(Token tok) {
        super(String.format("error (%s) token of type %s (%s) not allowed in this context",
                tok.getStartPosition().toString(), tok.getType().name(),  tok.getData()));
    }
}
