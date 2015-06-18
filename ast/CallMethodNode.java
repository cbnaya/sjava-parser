package ast;

import identities.Method;

import java.util.List;

import callStack.CallStack;
import lexer.Position;

public class CallMethodNode extends IdentityNode {

	public CallMethodNode(Position position, String methodName, 
			List<CallArgumentNode> methodArgs) {
		super(position, methodName);
		args = methodArgs;
	}
	
	public List<CallArgumentNode> getArgs() {
        return args;
    }
	
	private List<CallArgumentNode> args;

	//TODO
	@Override
	public void fillCallStack(CallStack callStack) throws Exception {
		Method method = (Method) callStack.search(Method.getFullName(getName()));
		
	}
	
	public class CallArgumentNode extends AssignmentNode {

		public CallArgumentNode(Position position, String name, 
				ExpressionNode value) {
			super(position, name, value);
		}

	}

}
