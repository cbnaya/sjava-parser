package oop.ex6.ast;

import oop.ex6.lexer.Position;

public class VarExpressionNode extends ExpressionNode {

	private final String name;
	
	public VarExpressionNode(Position position, String varName) {
		super(position);
		name = varName;
	}

    public String getName()
    {
        return name;
    }
    
    @Override
    public ExpressionType getType() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.VAR_VAL;
    }
}
