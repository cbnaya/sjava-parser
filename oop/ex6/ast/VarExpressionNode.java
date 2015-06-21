package oop.ex6.ast;

import oop.ex6.lexer.Position;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
        throw new NotImplementedException();
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.VAR_VAL;
    }
}
