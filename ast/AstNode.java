package ast;

import lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public abstract class AstNode {

    public static enum NodeType
    {
        RETURN,
        CODE_SCOPE
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
