package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public abstract class BinaryOpNode extends ExpressionNode {

    public BinaryOpNode(Position position, ExpressionNode left, 
    		ExpressionNode right) {
        super(position);
        this.left = left;
        this.right = right;
    }

    private ExpressionNode left;
    private ExpressionNode right;


    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }

    public ExpressionType getType()
    {
        return ExpressionType.BOOLEAN;
    }

}
