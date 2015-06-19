package ast;

import lexer.Position;

public abstract class IdentityNode extends AstNode {

	public IdentityNode(Position position, String name) {
		super(position);
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	private String name;
	

}
