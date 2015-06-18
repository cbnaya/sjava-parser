package ast;

import callStack.CallStack;
import lexer.Position;

/**
 * Created by cbnaya on 15/06/2015.
 */
public class ReturnNode extends AstNode{
    public ReturnNode(Position position) {
        super(position);
    }

	@Override
	public void fillCallStack(CallStack callStack) {}
}
