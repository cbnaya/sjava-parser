package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Representing a valuable expression.
 */
public abstract class ExpressionNode extends AstNode {

	/**
	 * An expression or a variable can have one of those types.
	 */
	public static enum ExpressionType {
		INT,
		DOUBLE,
		CHAR,
		STRING,
		BOOLEAN;

		/**
		 * @param expType The accepted or not expression type.
		 * @return Whether a variable with this type can be assigned with an
		 * expression of expType.
		 */
		public boolean accept(final ExpressionType expType) {
			return (this == expType) ||
					(this == DOUBLE && expType == INT) ||
					(this == BOOLEAN && (expType == INT || expType == DOUBLE));
		}
	}


	/**
	 * @param position The position in the file the expression is mentioned.
	 */
	public ExpressionNode(final Position position) {
		super(position);
	}

	/**
	 * (optional)
	 * @return The type of the expression, if known.
	 */
	public abstract ExpressionType getType();
}
