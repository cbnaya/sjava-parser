package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * A specific value expression.
 */
public class LiteralExpressionNode extends ExpressionNode {

	private final ExpressionType type;
    /*
     * The value of the expression in a string format, as was
	 * written in the file.
     */
	private final String value;

	/**
	 * @param position The position in the file the expression is mentioned.
	 * @param value The value of the expression in a string format, as was
	 * written in the file.
	 * @param type The actual type of the value.
	 */
	public LiteralExpressionNode(Position position, String value, 
			ExpressionType type) {
		super(position);
		this.type = type;
        this.value = value;
    }

    /* (non-Javadoc)
     * @see oop.ex6.ast.AstNode#getNodeType()
     */
    @Override
	public NodeType getNodeType() {
        return NodeType.LITERAL;
    }

    /* (non-Javadoc)
     * @see oop.ex6.ast.ExpressionNode#getType()
     */
    @Override
    public ExpressionType getType() {
        return type;
    }

	/**
	 * @return The value of the expression in a string format, as was originally
	 * written in the file.
	 */
	public String getValue() {
		return value;
	}
}
