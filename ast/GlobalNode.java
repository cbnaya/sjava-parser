package ast;

import lexer.Position;

import java.util.List;

public class GlobalNode extends AstNode {

    private List<MethodNode> methods;
    private ScopeNode body;
	
	public GlobalNode(ScopeNode globalBody, List<MethodNode> globalMethods) {
		super(new Position(0,0));

		methods = globalMethods;
        body = globalBody;
	}
	
	public List<MethodNode> getMethods() {
		return methods;
	}

    public ScopeNode getBody() {
        return body;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.GLOBAL;
    }

}
