package ast;

import lexer.Position;

public class IfNode extends ConditionalNode {

	public IfNode(Position position, ExpressionNode condition, ScopeNode body)
    {
        super(position, condition, body);
    }


    @Override
    public NodeType getNodeType() {
        return NodeType.IF;
    }
}
