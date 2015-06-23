package oop.ex6.ast;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import oop.ex6.lexer.Position;

/**
 * A code block like global, if, while and methods.
 */
public class ScopeNode extends AstNode {

	// The nodes representing the code inside this scope.
	private final List<AstNode> body;

	/**
	 * @param position The position in the file the scope starts.
	 */
	public ScopeNode(final Position position) {
		super(position);
		this.body= new LinkedList<AstNode>();
	}

	/**
	 * @param position The position in the file of the node's creation.
	 * @param body The nodes representing the code inside this scope.
	 */
	public ScopeNode(final Position position, final List<AstNode> body) {
		super(position);
		this.body = body;
	}

	/**
	 * @param node A new child to the scope body.
	 */
	public void add(final AstNode node)
	{
		body.add(node);
	}

	/**
	 * @param nodes new children to the scope body.
	 */
	public void addAll(final Collection<? extends AstNode> nodes)
	{
		body.addAll(nodes);
	}

	/**
	 * @return The nodes representing the code inside this scope.
	 */
	public List<AstNode> getBody() {
		return body;
	}

	/* (non-Javadoc)
	 * @see oop.ex6.ast.AstNode#getNodeType()
	 */
	@Override
	public NodeType getNodeType() {
		return NodeType.CODE_SCOPE;
	}
}
