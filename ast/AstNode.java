package ast;

import lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public class AstNode {
    public AstNode(Position position)
    {
        //this.parent = parent;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    //public AstNode getParent() {
    //    return parent;
    //}

    private Position position;
    //private AstNode parent;
}
