package oop.ex6.validator;

import oop.ex6.ast.MethodNode;

/**
 * Is thrown when a a method does not end with a return statement.
 */
public class MissingReturnException extends MethodException {

	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSAGE_FORMAT = 
			"method %s does not end with a return statement (%s)";

    public MissingReturnException(final MethodNode methodNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, methodNode.getName(), 
        		methodNode.getPosition()));
    }
}
