package validator;

import lexer.Position;

public class Variable {

	private final String name;
	private final ExpressionType type;
	private boolean hasValue, isFinal;
	
	public Variable(String name, ExpressionType type, boolean isFinal) {
		this.name = name;
		this.type = type;
		value = false;
		this.isFinal = isFinal;
	}
	
	public boolean assign(expressionType valueType) {
		if (type.accept(value) && (!hasValue || !isFinal)) {
			hasValue = true;
			return true;
		}
		return false;
		}
	}

	public String getName() {
		return name;
	}
	
	public ExpressionType getType() {
		return type;
	}
	
	public boolean hasValue() {
		return hasValue;
	}
	
	public boolean isFinal() {
		return isFinal;
	}
}
