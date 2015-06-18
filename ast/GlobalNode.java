package ast;

import java.util.List;

import callStack.CallStack;
import lexer.Position;

public class GlobalNode extends ScopeNode {

	private List<MethodNode> methods;
	
	public GlobalNode(Position position, List<AstNode> body, 
			List<MethodNode> methods) {
		super(position, body);
		this.methods = methods;
	}
	
	public List<MethodNode> getMethods() {
		return methods;
	}
	
	@Override
	public void fillCallStack(CallStack callStack) throws Exception {
		super.fillCallStack(callStack);
		for (MethodNode method : methods) {
			method.fillCallStack(callStack);
		}
	}

}
