package ast;

import lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public class ArgumentNode extends AstNode {
    public ArgumentNode(Position position, String argType, String argName, boolean isFinal) {
        super(position);
        type = ExpressionNode.ExpressionType.valueOf(argType.toUpperCase());
        name = argName;
        this.isFinal = isFinal;
    }

    private ExpressionNode.ExpressionType type;
    private String name;
    private boolean isFinal;

    public ExpressionNode.ExpressionType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.ARGUMENT;
    }
}
