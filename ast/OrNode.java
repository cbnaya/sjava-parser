package ast;

import lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public class OrNode extends AstNode{

    public OrNode(Position position, AstNode left, AstNode right) {
        super(position);
        this.left = left;
        this.rigth = right;
    }

    private AstNode left;
    private AstNode rigth;

    public AstNode getLeft() {
        return left;
    }

    public AstNode getRigth() {
        return rigth;
    }
}
