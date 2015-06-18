package ast;

import lexer.Position;

public class TypeMismatchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TypeMismatchException(Position position, String type) {
		super(String.format("error (%s) %s type needed here.", 
				position.toString(), type));
	}
}
