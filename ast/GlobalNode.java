package ast;

import lexer.Position;

import java.util.List;

public class GlobalNode extends ScopeNode {

    private List<MethodNode> methods;
	
	public GlobalNode(List<AstNode> globalBody, List<MethodNode> globalMethods) {
		super(new Position(0,0), globalBody);

		methods = globalMethods;
	}
	
	public List<MethodNode> getMethods() {
		return methods;
	}

    @Override
    public NodeType getNodeType() {
        return NodeType.GLOBAL;
    }

}
