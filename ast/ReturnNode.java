package ast;

import lexer.Position;

/**
 * Created by cbnaya on 15/06/2015.
 */
public class ReturnNode extends AstNode{
    public ReturnNode(Position position) {
        super(position);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.RETURN;
    }

}
