package parser;

import lexer.Token;

/**
 * Created by cbnaya on 14/06/2015.
 */
public class NotAllowedInThisContext extends Throwable {
    public NotAllowedInThisContext(Token tok) {
        super(String.format("error (%d) token of type %s (%s) not allowed in this context",
                tok.getStartPosition(), tok.getType().name(),  tok.getData()));
    }
}
