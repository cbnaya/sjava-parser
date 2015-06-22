package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Representing a return statement.
 */
public class ReturnNode extends AstNode {
	
    /**
     * @param position The position in the file of the statement.
     */
    public ReturnNode(final Position position) {
        super(position);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RETURN;
    }

}
