package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public abstract class AstNode {

    public static enum NodeType
    {
        RETURN,
        CODE_SCOPE,
        METHOD,
        ARGUMENT,
        GLOBAL,
        WHILE,
        IF,
        VAR_DECLARATION,
        CALL_METHOD,
        ASSIGNMENT,
        LITERAL,
        VAR_VAL,
        OR,
        AND
    }

	public AstNode(Position position)
    {
        this.position = position;
    }

    public abstract NodeType getNodeType();

    public Position getPosition() {
        return position;
    }


    private Position position;

}
