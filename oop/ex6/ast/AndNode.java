package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Represents a "&&" binary operator.
 */
public class AndNode extends BinaryOpNode {

	/**
	 * If (x && y), 
	 * @param position The position in the file of &&.
	 * @param left x
	 * @param right y
	 */
	public AndNode(final Position position, final ExpressionNode left, 
			final ExpressionNode right) {
		super(position, left, right);
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.AND;
	}
}
