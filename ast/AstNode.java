package ast;

import lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public abstract class AstNode {

    public static enum NodeType
    {
        RETURN,
        CODE_SCOPE,
        METHOD,
        GLOBAL,
        WHILE,
        IF,
        VAR_DECLARATION,
        CALL_METHOD,
        ASSIGNMENT,
        LITERAL
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
