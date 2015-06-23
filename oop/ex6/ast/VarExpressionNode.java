package oop.ex6.ast;

import oop.ex6.lexer.Position;
import java.lang.UnsupportedOperationException;

/**
 * An expression of referencing a name of a variable.
 */
public class VarExpressionNode extends ExpressionNode {

	private final String name;

	/**
	 * @param position The position in the file the variable is mentioned.
	 * @param varName The name of the mentioned variable.
	 */
	public VarExpressionNode(Position position, String varName) {
		super(position);
		name = varName;
	}

	/**
	 * @return The name of variable.
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see oop.ex6.ast.AstNode#getNodeType()
	 */
	@Override
	public NodeType getNodeType() {
		return NodeType.VAR_VAL;
	}

	/* (non-Javadoc)
	 * @see oop.ex6.ast.ExpressionNode#getType()
	 */
	@Override
	public ExpressionType getType() {
		throw new UnsupportedOperationException();
	}
}
