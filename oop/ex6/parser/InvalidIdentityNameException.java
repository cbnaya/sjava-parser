package oop.ex6.parser;

import oop.ex6.lexer.Token;

/**
 * Created by cbnaya on 15/06/2015.
 */
public class InvalidIdentityNameException extends Exception {
    public InvalidIdentityNameException(Token tok) {
        super(String.format("error (%s) invalid var name: %s ",
                tok.getStartPosition().toString(),tok.getData()));
    }
}
