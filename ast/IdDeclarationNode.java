package ast;

import lexer.Position;

public abstract class IdDeclarationNode extends AstNode {
	
	private final String NAME;
	
	public IdDeclarationNode(Position position, String name) {
		super(position);
		NAME = name;
	}
	
	public abstract String getType();
	
	public String getName() {
		return NAME;
	}

}