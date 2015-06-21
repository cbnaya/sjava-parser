package oop.ex6.ast;

import oop.ex6.lexer.Position;

import java.util.List;

public class IfNode extends ConditionalNode {

	public IfNode(Position position, ExpressionNode condition, List<AstNode> body)
    {
        super(position, condition, body);
    }


    @Override
    public NodeType getNodeType() {
        return NodeType.IF;
    }
}