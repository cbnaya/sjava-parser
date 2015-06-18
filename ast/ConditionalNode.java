package ast;

import lexer.Position;
/**
 * Created by cbnaya on 14/06/2015.
 */

public abstract class ConditionalNode extends AstNode {

    private final ExpressionNode condition;
    private ScopeNode body;

    public ConditionalNode(Position position, ExpressionNode condition, ScopeNode body)
    {
        super(position);
        this.condition= condition;
        this.body = body;
    }


    public ExpressionNode getCondition() {
        return condition;
    }

    public ScopeNode getBody() {
        return body;
    }
}
