package ast;

import types.Type;
import callStack.CallStack;
import lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */

public abstract class ExpressionNode extends AstNode {
	
    public ExpressionNode(Position position) {
        super(position);
    }

	public abstract Type getType(CallStack callStack);
}
