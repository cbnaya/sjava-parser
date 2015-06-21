package oop.ex6.ast;

import oop.ex6.lexer.Position;

public class VarDeclarationNode extends AstNode {

	public VarDeclarationNode(Position position, String type, String name, boolean isFinal) {
        super(position);
        this.type = ExpressionNode.ExpressionType.valueOf(type.toUpperCase());
        this.name = name;
        this.isFinal = isFinal;
    }

    private ExpressionNode.ExpressionType type;
    private boolean isFinal;
    private String name;

    public ExpressionNode.ExpressionType getType() {
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
