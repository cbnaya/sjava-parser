package ast;

import lexer.Position;

public class WhileNode extends ConditionalNode {

	public WhileNode(Position position, ExpressionNode condition, ScopeNode body)
    {
        super(position, condition, body);
	}

    @Override
    public NodeType getNodeType() {
        return NodeType.WHILE;
    }
}
