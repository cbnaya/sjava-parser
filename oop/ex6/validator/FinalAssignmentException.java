package oop.ex6.validator;

import oop.ex6.ast.AssignmentNode;

/**
 * Is thrown when trying to assign a final variable after initialization.
 */
public class FinalAssignmentException extends VariableException {

    private static final long serialVersionUID = 1L;
    private static final String ERROR_MESSAGE_FORMAT =
            "Assignment to final %s is not allowed (%s)";

    /**
     * @param assignmentNode The node containing the problematic assignment.
     */
    public FinalAssignmentException(AssignmentNode assignmentNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, assignmentNode.getName(),
                assignmentNode.getPosition().toString()));
    }
}