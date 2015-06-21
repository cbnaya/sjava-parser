package oop.ex6.ast;

import oop.ex6.lexer.Position;

import java.util.List;

public class WhileNode extends ConditionalNode {

	public WhileNode(Position position, ExpressionNode condition, List<AstNode> body)
    {
        super(position, condition, body);
	}

    @Override
    public NodeType getNodeType() {
        return NodeType.WHILE;
    }
}
