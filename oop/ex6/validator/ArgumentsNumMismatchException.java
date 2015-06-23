package oop.ex6.validator;

import oop.ex6.ast.CallMethodNode;
import oop.ex6.ast.MethodNode;

/**
 * Is thrown when trying to call a method with a wrong number of arguments.
 */
public class ArgumentsNumMismatchException extends MethodException {

	private static final long serialVersionUID = 1L;
	public static final String ERROR_MESSAGE_FORMAT ="expected %d arguments "
			+ "instead of %d for method %s (%s)";

	/**
	 * @param callMethodNode The node representing calling the method.
	 * @param methodNode The node representing the method's declaration.
	 */
	public ArgumentsNumMismatchException(CallMethodNode callMethodNode, 
			MethodNode methodNode) {
		super(String.format(ERROR_MESSAGE_FORMAT, methodNode.getArgs().size(), 
				callMethodNode.getArgs().size(), callMethodNode.getName(),
				callMethodNode.getPosition()));
	}
}