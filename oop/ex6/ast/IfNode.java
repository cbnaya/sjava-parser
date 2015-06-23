package oop.ex6.ast;

import java.util.List;

import oop.ex6.lexer.Position;

/**
 * An inner code scope, starts with "if (condition)".
 */
public class IfNode extends ConditionalNode {

	/**
	 * @param position The position in the file the if scope starts.
	 * @param condition The condition of the if.
	 * @param body The nodes representing the code inside the if scope.
	 */
	public IfNode(Position position, ExpressionNode condition, 
			List<AstNode> body) {
        super(position, condition, body);
    }


    /* (non-Javadoc)
     * @see oop.ex6.ast.ScopeNode#getNodeType()
     */
    @Override
    public NodeType getNodeType() {
        return NodeType.IF;
    }
}
