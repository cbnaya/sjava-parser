package validator;

import ast.VarExpressionNode;

/**
 * Created by cbnaya on 21/06/2015.
 */
public class RequiredVarDoseNotExistException extends Exception {

    public static final String ERROR_MESSAGE_FORMAT = "variable name %s dose not declared (%s)";

    public RequiredVarDoseNotExistException(VarExpressionNode varExpressionNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, varExpressionNode.getName(),
                                                    varExpressionNode.getPosition().toString()));
    }
}
