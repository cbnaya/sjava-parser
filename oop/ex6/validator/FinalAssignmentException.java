package oop.ex6.validator;

import oop.ex6.ast.AssignmentNode;

/**
 * Is thrown when trying to assign a final variable after initialization.
 */
public class FinalAssignmentException extends VariableException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param assignmentNode The node containing the problematic assignment.
	 */
	public FinalAssignmentException(final AssignmentNode assignmentNode) {
		super(String.format("Assignment to final %s is not allowed (%s)", 
				assignmentNode.getName(), 
				assignmentNode.getPosition().toString()));
	}
}