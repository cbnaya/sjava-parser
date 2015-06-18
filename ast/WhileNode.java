package ast;

import java.util.List;

import lexer.Position;

public class WhileNode extends ConditionalNode {

	public WhileNode(Position position, List<AstNode> body, 
			ExpressionNode condition)
    {
        super(position, body, condition);
	}

}
