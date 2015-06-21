package validator;

import ast.AssignmentNode;

/**
 * Created by cbnaya on 21/06/2015.
 */
public class FinalAssignmentException extends Exception {
    public FinalAssignmentException(AssignmentNode assignmentNode) {
        super(String.format("Assignment to final %s is not allowed (%s)", assignmentNode.getName(),
                assignmentNode.getPosition().toString()));
    }
}
