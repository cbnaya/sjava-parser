package oop.ex6.ast;

import java.util.List;

import oop.ex6.lexer.Position;

/**
 * An inner code scope, starts with "while (condition)".
 */
public class WhileNode extends ConditionalNode {

	/**
	 * @param position The position in the file the while scope starts.
	 * @param condition The condition of the while.
	 * @param body The nodes representing the code inside the while scope.
	 */
	public WhileNode(final Position position, final ExpressionNode condition, 
			final List<AstNode> body) {
        super(position, condition, body);
	}

    @Override
    public NodeType getNodeType() {
        return NodeType.WHILE;
    }
}
