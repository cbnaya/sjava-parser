package oop.ex6.ast;

import java.util.List;

import oop.ex6.lexer.Position;

/**
 * A method declaration.
 */
public class MethodNode extends ScopeNode {

	private final List<ArgumentNode> arguments;
	private final String name;

	/**
	 * @param position The position in the file the method starts.
	 * @param methodName The method's name.
	 * @param methodArgs The method's arguments.
	 * @param methodBody The method's code body.
	 */
	public MethodNode(final Position position, final String methodName, 
			final List<ArgumentNode> methodArgs, final List<AstNode> methodBody)
	{
		super(position, methodBody);
		name = methodName;
		arguments = methodArgs;
	}

	/**
	 * @return The method's arguments.
	 */
	public List<ArgumentNode> getArgs() {
		return arguments;
	}
	
	/**
	 * @return The method's name.
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see oop.ex6.ast.ScopeNode#getNodeType()
	 */
	@Override
	public NodeType getNodeType() {
		return NodeType.METHOD;
	}
}