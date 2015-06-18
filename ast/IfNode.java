package ast;

import java.util.List;

import lexer.Position;

public class IfNode extends ConditionalNode {

	public IfNode(Position position, List<AstNode> body, 
			ExpressionNode condition)
    {
        super(position, body, condition);
	}


}
