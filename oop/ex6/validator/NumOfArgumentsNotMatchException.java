package oop.ex6.validator;

import oop.ex6.ast.CallMethodNode;
import oop.ex6.ast.MethodNode;

/**
 * Created by cbnaya on 21/06/2015.
 */
public class NumOfArgumentsNotMatchException extends Exception {

    public static final String ERROR_MESSAGE_FORMAT =
            "number of arguments is not match function %s required %d actual %d (%s)";

    public NumOfArgumentsNotMatchException(CallMethodNode callMethodNode, MethodNode methodNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, callMethodNode.getName(),
                methodNode.getArgs().size(),
                callMethodNode.getArgs().size(),
                callMethodNode.getPosition()));
    }
}