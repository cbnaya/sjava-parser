package oop.ex6.ast;

import oop.ex6.lexer.Position;

import java.util.List;

/**
 * Created by cbnaya on 14/06/2015.
 */

public abstract class ConditionalNode extends ScopeNode {

    private final ExpressionNode condition;

    public ConditionalNode(Position position, ExpressionNode condition, List<AstNode> body)
    {
        super(position, body);
        this.condition= condition;
    }


    public ExpressionNode getCondition() {
        return condition;
    }

}
