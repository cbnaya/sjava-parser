package oop.ex6.validator;

import oop.ex6.ast.ExpressionNode;

public class Variable {

	private final String name;
	private final ExpressionNode.ExpressionType type;
	private boolean hasValue, isFinal;
	
	public Variable(String name, ExpressionNode.ExpressionType type, boolean isFinal) {
		this.name = name;
		this.type = type;
		hasValue = false;
		this.isFinal = isFinal;
	}
	
	public boolean assign(ExpressionNode.ExpressionType valueType) {
        if (type.accept(valueType) && (!hasValue || !isFinal)) {
            hasValue = true;
            return true;
        }
        return false;
    }

	public String getName() {
		return name;
	}
	
	public ExpressionNode.ExpressionType getType() {
		return type;
	}
	
	public boolean hasValue() {
		return hasValue;
	}
	
	public boolean isFinal() {
		return isFinal;
	}
}
