package oop.ex6.ast;

import java.util.List;

import oop.ex6.lexer.Position;

/**
 * An inner code block, starts with a condition.
 */
public abstract class ConditionalNode extends ScopeNode {

	private final ExpressionNode condition;

	/**
	 * @param position The position in the file this scope starts.
	 * @param condition The condition of this scope.
	 * @param body The nodes representing the code inside this scope.
	 */
	public ConditionalNode(final Position position, 
			final ExpressionNode condition, final List<AstNode> body) {
		super(position, body);
		this.condition = condition;
	}

	/**
	 * @return The condition of this scope.
	 */
	public ExpressionNode getCondition() {
		return condition;
	}

}
