package ast;

import lexer.Position;

public class AndNode extends BinaryOpNode {

	public AndNode(Position position, ExpressionNode left, ExpressionNode right)
	{
		super(position, left, right);
	}

}
