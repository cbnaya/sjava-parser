package oop.ex6.validator;

import oop.ex6.ast.VarExpressionNode;

/**
 * Is thrown when trying to use a value of a variable without initialization.
 */
public class VarNeverAssignedException extends VariableException {

	private static final long serialVersionUID = 1L;
	private static final String ERROR_MESSAGE_FORMAT =
			"the variable %s was never assigned before %s";

	/**
	 * @param varExpressionNode The expression node containing the referenced
	 * variable.
	 */
	public VarNeverAssignedException(final VarExpressionNode varExpressionNode) 
	{
		super(String.format(ERROR_MESSAGE_FORMAT, varExpressionNode.getName(),
				varExpressionNode.getPosition().toString()));
	}
}
