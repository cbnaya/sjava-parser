package ast;

import types.Type;
import identities.variables.Variable;
import callStack.CallStack;
import lexer.Position;

public class VarExpressionNode extends ExpressionNode {

	private final String NAME;
	
	public VarExpressionNode(Position position, String varName) {
		super(position);
		NAME = varName;
	}

	@Override
	public void fillCallStack(CallStack callStack) throws Exception {
		callStack.search(Variable.getFullName(NAME));
	}

	@Override
	public Type getType(CallStack callStack) {
		return callStack.search(Variable.getFullName(NAME)).getType();
	}

}
