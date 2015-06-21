package validator;

import ast.CallMethodNode;

/**
 * Created by cbnaya on 21/06/2015.
 */
public class MethodNotExistsException extends Exception {

    public static final String ERROR_MESSAGE_FORMAT = "method %s is not declare (%s)";

    public MethodNotExistsException(CallMethodNode callMethodNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, callMethodNode.getName(), callMethodNode.getPosition()));
    }
}
