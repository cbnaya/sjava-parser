package ast;

import lexer.Position;

public class LiteralExpressionNode extends ExpressionNode {

	private final ExpressionType type;
    private final String value;

	public LiteralExpressionNode(Position position, String value, ExpressionType type) {
		super(position);
		this.type = type;
        this.value = value;
    }

    public NodeType getNodeType() {
        return NodeType.LITERAL;
    }

    @Override
    public ExpressionType getType() {
        return type;
    }
}
