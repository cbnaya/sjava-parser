package ast;

import lexer.Position;

public class AssignmentNode extends AstNode {

	private ExpressionNode value;

	public AssignmentNode(Position position, String name, ExpressionNode value)
	{
		super(position);
        this.name = name;
		this.value = value;
	}

    private String name;

    public String getName() {
        return name;
    }

	public ExpressionNode getValue() {
		return value;
	}

    @Override
    public NodeType getNodeType() {
        return NodeType.ASSIGNMENT;
    }

}
