package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Represents a "&&" binary operator.
 */
public class AndNode extends BinaryOpNode {

    /**
     * If (x && y),
     *
     * @param position The position in the file of &&.
     * @param left     x
     * @param right    y
     */
    public AndNode(Position position, ExpressionNode left, ExpressionNode right) {
        super(position, left, right);
    }

    /* (non-Javadoc)
     * @see oop.ex6.ast.AstNode#getNodeType()
     */
    @Override
    public NodeType getNodeType() {
        return NodeType.AND;
    }
}
