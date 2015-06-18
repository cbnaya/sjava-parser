package parser;

import lexer.Token;

/**
 * Created by cbnaya on 15/06/2015.
 */
public class InvalidIdentityName extends Throwable {
    public InvalidIdentityName(Token tok) {
        super(String.format("error (%s) invalid var name: %s ",
                tok.getStartPosition().toString(),tok.getData()));
    }
}
