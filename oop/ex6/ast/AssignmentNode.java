package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Representing an assignment of a variable with an expression.
 */
public class AssignmentNode extends AstNode {

	private final ExpressionNode value;
	private final String name;
	
    /**
     * @param position The position in the file of the assignment.
     * @param name The name of the variable assigning to.
     * @param value The assigned expression.
     */
    public AssignmentNode(final Position position, final String name, 
    		final ExpressionNode value) {
		super(position);
        this.name = name;
		this.value = value;
	}

    /**
     * @return The name of the variable assigning to.
     */
    public String getName() {
        return name;
    }

	@Override
    public NodeType getNodeType() {
        return NodeType.ASSIGNMENT;
    }

    /**
     * @return The assigned expression.
     */
    public ExpressionNode getValue() {
		return value;
	}

}
