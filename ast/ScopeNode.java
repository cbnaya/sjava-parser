package ast;

import lexer.Position;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ScopeNode extends AstNode {

	private List<AstNode> body;

    public ScopeNode(Position position) {
        super(position);
        this.body= new LinkedList<AstNode>();
    }

	public ScopeNode(Position position, List<AstNode> body) {
		super(position);
		this.body = body;
	}
	
	public List<AstNode> getBody() {
		return body;
	}

    public void add(AstNode node)
    {
        body.add(node);
    }

    public void addAll(Collection<? extends AstNode> nodes)
    {
        body.addAll(nodes);
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.CODE_SCOPE;
    }
}
