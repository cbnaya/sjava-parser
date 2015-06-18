package ast;

import lexer.Position;

public class DuplicateIdentityException extends Exception {

	public DuplicateIdentityException(Position position, String varName) {
		super(String.format("error (%s) variable %s already exists", 
				position.toString(), varName));
	}

	private static final long serialVersionUID = 1L;

}
