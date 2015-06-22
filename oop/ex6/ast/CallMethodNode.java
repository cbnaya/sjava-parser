package oop.ex6.ast;

import java.util.List;

import oop.ex6.lexer.Position;

/**
 * A node for calling a method.
 */
public class CallMethodNode extends AstNode {

	private final List<ExpressionNode> args;
	private final String name;

    /**
     * @param position The position in the file the method is called.
     * @param methodName The name of the called method.
     * @param methodArgs The arguments the method is called with.
     */
    public CallMethodNode(final Position position, final String methodName, 
    		final List<ExpressionNode> methodArgs) {
		super(position);
        name = methodName;
		args = methodArgs;
	}
    
	/**
	 * @return The arguments the method is called with.
	 */
	public List<ExpressionNode> getArgs() {
        return args;
    }
	
    /**
     * @return The name of the called method.
     */
    public String getName() {
        return name;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.CALL_METHOD;
    }

}
