package ast;

import java.util.List;

import callStack.CallStack;
import lexer.Position;
/**
 * Created by cbnaya on 14/06/2015.
 */

public class ConditionalNode extends ScopeNode {

    public ConditionalNode(Position position, List<AstNode> body, 
    		ExpressionNode condition)
    {
        super(position, body);
        this.CONDITION = condition;
    }

    private final ExpressionNode CONDITION;

    public ExpressionNode getCondition() {
        return CONDITION;
    }
    
    @Override
    public void fillCallStack(CallStack callStack) throws Exception {
    	CONDITION.fillCallStack(callStack);
    	super.fillCallStack(callStack);
    }
}
