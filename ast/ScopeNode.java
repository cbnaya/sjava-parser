package ast;

import lexer.Position;

import java.util.List;

public class ScopeNode extends AstNode {

	private List<AstNode> body;

	public ScopeNode(Position position, List<AstNode> body) {
		super(position);
		this.body = body;
	}
	
	public List<AstNode> getBody() {
		return body;
	}


    @Override
    public NodeType getNodeType() {
        return NodeType.CODE_SCOPE;
    }
}
