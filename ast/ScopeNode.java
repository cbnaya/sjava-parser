package ast;

import java.util.List;

import callStack.CallStack;
import lexer.Position;

public abstract class ScopeNode extends AstNode {

	private List<AstNode> body;

	public ScopeNode(Position position, List<AstNode> body) {
		super(position);
		this.body = body;
	}
	
	public List<AstNode> getBody() {
		return body;
	}
	
	@Override
	public void fillCallStack(CallStack callStack) throws Exception {
		for (AstNode node : body) {
			node.fillCallStack(callStack);
		}
	}

}
