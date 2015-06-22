package oop.ex6.ast;

import java.util.List;

import oop.ex6.lexer.Position;

/**
 * Representing the AST root, the global code scope.
 */
public class GlobalNode extends ScopeNode {

    private final List<MethodNode> methods;
	
	/**
	 * @param globalBody All global code (as opposed to code inside methods).
	 * @param globalMethods Method declarations.
	 */
	public GlobalNode(final List<AstNode> globalBody, 
			final List<MethodNode> globalMethods) 
	{
		super(new Position(0,0), globalBody);
		methods = globalMethods;
	}
	
	/**
	 * @return The methods subtrees.
	 */
	public List<MethodNode> getMethods() {
		return methods;
	}

    @Override
    public NodeType getNodeType() {
        return NodeType.GLOBAL;
    }

}
