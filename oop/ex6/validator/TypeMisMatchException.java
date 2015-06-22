package oop.ex6.validator;

import oop.ex6.ast.ExpressionNode.ExpressionType;
import oop.ex6.lexer.Position;

/**
 * Is thrown when trying to assign a variable with an expression of type it's 
 * not defined to accept, or when using a String or a char at a condition.
 */
public class TypeMismatchException extends InvalidCodeException {

	private static final long serialVersionUID = 1L;
	private static final String ERROR_MESSAGE_FORMAT = 
			"%s type mismatch - type %s does not accept type %s (%s)";

	/**
	 * @param name The name of the variable trying assign to.
	 * @param requiredType This variable's type.
	 * @param actualType The type of the assigned expression.
	 * @param position The position in the file of the assignment.
	 */
	public TypeMismatchException(final String name, 
			final ExpressionType requiredType, final ExpressionType actualType, 
			final Position position) {
		super(String.format(ERROR_MESSAGE_FORMAT, name, requiredType.name(), 
				actualType.name(), position));
	}
}
