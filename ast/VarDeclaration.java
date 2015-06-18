package ast;

import lexer.Position;

public class VarDeclaration extends AstNode {

	public VarDeclaration(Position position, String type, String name, boolean isFinal) {
        super(position);
        this.type = type;
        this.name = name;
        this.isFinal = isFinal;
    }

    private String type;
    private boolean isFinal;
    private String name;

    public String getType() {
        return type;
    }
    
    public boolean isFinal() {
    	return isFinal;
    }

    public String getName() {
        return name;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.VAR_DECLARATION;
    }
}
