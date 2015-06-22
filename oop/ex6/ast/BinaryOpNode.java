package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * A node of binary oprator, consisting two boolean expressions.
 */
public abstract class BinaryOpNode extends ExpressionNode {

    private final ExpressionNode left, right;
    
    /**
     * @param position The position in the file the operator is used.
     * @param left The left side of the operation.
     * @param right The right side of the operation.
     */
    public BinaryOpNode(final Position position, final ExpressionNode left, 
    		final ExpressionNode right) {
        super(position);
        this.left = left;
        this.right = right;
    }


    /**
     * @return The left side of the operation.
     */
    public ExpressionNode getLeft() {
        return left;
    }

    /**
     * @return The right side of the operation.
     */
    public ExpressionNode getRight() {
        return right;
    }

    @Override
	public ExpressionType getType()
    {
        return ExpressionType.BOOLEAN;
    }

}
