package ast;

import types.Type;
import identities.variables.Variable;
import callStack.CallStack;
import lexer.Position;

public class AssignmentNode extends IdentityNode {

	private ExpressionNode value;

	public AssignmentNode(Position position, String name, ExpressionNode value)
	{
		super(position, name);
		this.value = value;
	}
	
	public ExpressionNode getValue() {
		return value;
	}

	@Override
	public void fillCallStack(CallStack callStack) throws Exception {
		Variable target = (Variable) 
				callStack.search(Variable.getFullName(getName()));
		Type assigned = value.getType(callStack);
		target.assign(assigned);
	}

}
