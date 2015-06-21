package ast;

import lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public class ArgumentNode extends VarDeclarationNode {
    public ArgumentNode(Position position, String argType, String argName, boolean isFinal) {
        super(position, argType, argName, isFinal);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ARGUMENT;
    }
}
