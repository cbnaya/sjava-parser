package ast;

import lexer.Position;

public class OrNode extends BinaryOpNode {

	public OrNode(Position position, ExpressionNode left, ExpressionNode right)
	{
		super(position, left, right);
	}

    @Override
    public NodeType getNodeType() {
        return NodeType.OR;
    }
}
