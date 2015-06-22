package oop.ex6.ast;

import oop.ex6.lexer.Position;

/**
 * Representing a declaration of a variable.
 */
public class VarDeclarationNode extends AstNode {

	private final boolean isFinal;
    private final String name;
    private final ExpressionNode.ExpressionType type;
    
    /**
     * @param position The position in the file of the variable declaration.
     * @param type The variable's type. (e.g int, String, etc.)
     * @param name The variable's name.
     * @param isFinal Whether the variable is assignable after delaration.
     */
	public VarDeclarationNode(final Position position, final String type, 
			final String name, final boolean isFinal) {
        super(position);
        this.type = ExpressionNode.ExpressionType.valueOf(type.toUpperCase());
        this.name = name;
        this.isFinal = isFinal;
    }

    /**
     * @return The variable's name.
     */
    public String getName() {
        return name;
    }
    
    @Override
    public NodeType getNodeType() {
        return NodeType.VAR_DECLARATION;
    }

    /**
     * @return The variable's declared type.
     */
    public ExpressionNode.ExpressionType getType() {
        return type;
    }

    /**
     * @return Whether the variable is assignable after delaration.
     */
    public boolean isFinal() {
    	return isFinal;
    }
}
