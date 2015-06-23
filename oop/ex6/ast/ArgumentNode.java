package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * An argument of a method.
 */
public class ArgumentNode extends VarDeclarationNode {

	/**
	 * @param position The position in the file of the argument declaration.
	 * @param argType The argument's type. (e.g int, String, etc.)
	 * @param argName The argument's name.
	 * @param isFinal Whether the argument is assignable.
	 */
	public ArgumentNode(final Position position, final String argType, 
			final String argName, final boolean isFinal) {
		super(position, argType, argName, isFinal);
	}

	/* (non-Javadoc)
	 * @see oop.ex6.ast.VarDeclarationNode#getNodeType()
	 */
	@Override
	public NodeType getNodeType() {
		return NodeType.ARGUMENT;
	}
}
