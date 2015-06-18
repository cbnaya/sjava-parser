package ast;

import lexer.Position;
import types.Type;
import callStack.CallStack;

public class LiteralExpressionNode extends ExpressionNode {

	private final Type TYPE;

	public LiteralExpressionNode(Position position, Type type) {
		super(position);
		TYPE = type;
	}

	@Override
	public Type getType(CallStack callStack) {
		return TYPE;
	}

	@Override
	public void fillCallStack(CallStack callStack) throws Exception {
		return;
	}

}
