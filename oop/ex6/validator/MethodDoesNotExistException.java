package oop.ex6.validator;

import oop.ex6.ast.CallMethodNode;

/**
 * Is thrown when trying to call a non-exist method.
 */
public class MethodDoesNotExistException extends MethodException {

	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSAGE_FORMAT = 
			"method %s was not declared (%s)";

    /**
     * @param callMethodNode The node representing the method call.
     */
    public MethodDoesNotExistException(CallMethodNode callMethodNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, callMethodNode.getName(), 
        		callMethodNode.getPosition()));
    }
}
