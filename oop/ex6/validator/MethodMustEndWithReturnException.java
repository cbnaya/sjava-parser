package oop.ex6.validator;

import oop.ex6.ast.MethodNode;

/**
 * Created by cbnaya on 21/06/2015.
 */
public class MethodMustEndWithReturnException extends Exception {

    public static final String ERROR_MESSAGE_FORMAT = "method %s does not end with return statement (%s)";

    public MethodMustEndWithReturnException(MethodNode methodNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, methodNode.getName(), methodNode.getPosition()));
    }
}
