package oop.ex6.ast;

import oop.ex6.ast.ExpressionNode.ExpressionType;
import oop.ex6.lexer.Position;

/**
 * Representing a declaration of a variable.
 */
public class VarDeclarationNode extends AstNode {
	 
    private final String name;
    private final ExpressionType type;
    // Whether the variable is assignable after declaration.
    private final boolean isFinal;
    
    /**
     * @param position The position in the file of the variable declaration.
     * @param type The variable's type. (e.g "int", "String", etc.)
     * @param name The variable's name.
     * @param isFinal Whether the variable is assignable after declaration.
     */
	public VarDeclarationNode(Position position, String type, String name, 
			boolean isFinal) {
        super(position);
        this.type = ExpressionType.valueOf(type.toUpperCase());
        this.name = name;
        this.isFinal = isFinal;
    }

    /**
     * @return The variable's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The variable's declared type.
     */
    public ExpressionType getType() {
        return type;
    }

    /**
     * @return Whether the variable is assignable after declaration.
     */
    public boolean isFinal() {
    	return isFinal;
    }
    
    /* (non-Javadoc)
     * @see oop.ex6.ast.AstNode#getNodeType()
     */
    @Override
    public NodeType getNodeType() {
        return NodeType.VAR_DECLARATION;
    }
}
