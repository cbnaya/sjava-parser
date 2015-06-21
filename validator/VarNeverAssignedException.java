package validator;

import ast.VarExpressionNode;

/**
 * Created by cbnaya on 21/06/2015.
 */
public class VarNeverAssignedException extends Exception {

    public static final String ERROR_MESSAGE_FORMAT = "var never assigned before %s %s";

    public VarNeverAssignedException(VarExpressionNode varExpressionNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, varExpressionNode.getName(),
                                                    varExpressionNode.getPosition().toString()));
    }
}
