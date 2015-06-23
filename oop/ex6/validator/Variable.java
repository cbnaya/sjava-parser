package oop.ex6.validator;

import oop.ex6.ast.ExpressionNode.ExpressionType;

/**
 * Keeps the state of a single variable in the program.
 */
public class Variable {

	private final String name;
	private final ExpressionType type;
	private boolean hasValue;
	private final boolean isFinal;
	
	/**
	 * @param name The variable's name.
	 * @param type The variable's type.
	 * @param isFinal Whether the variable is assignable after declaration.
	 */
	public Variable(String name, ExpressionType type, 
			boolean isFinal) {
		this.name = name;
		this.type = type;
		this.isFinal = isFinal;
		hasValue = false;
	}
	
	/**
	 * Marks this variable as initialized with a value, if it's not final or the
	 * type does not match.
	 * @param valueType The type of the assigned expression.
	 * @return Whether the assign succeeded.
	 */
	public boolean assign(ExpressionType valueType) {
        if (type.accept(valueType) && !(hasValue && isFinal)) {
            hasValue = true;
            return true;
        }
        return false;
    }

	/**
	 * @return The variable's name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The variable's type.
	 */
	public ExpressionType getType() {
		return type;
	}
	
	/**
	 * @return Whether the variable was assigned.
	 */
	public boolean hasValue() {
		return hasValue;
	}
	
	/**
	 * @return Whether the variable is assignable after declaration.
	 */
	public boolean isFinal() {
		return isFinal;
	}
}
