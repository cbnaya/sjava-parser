package oop.ex6.validator;

import oop.ex6.ast.VarDeclarationNode;

/**
 * Is thrown when trying to declare a variable with an occupied name.
 */
public class VarDuplicateDeclaration extends VariableException {

	private static final long serialVersionUID = 1L;
	private static final String ERROR_MESSAGE_FORMAT = 
    		"variable name %s was declared already (%s)";

    /**
     * @param varDeclarationNode The node representing the problematic 
     * declaration.
     */
    public VarDuplicateDeclaration(final VarDeclarationNode varDeclarationNode)
    {
        super(String.format(ERROR_MESSAGE_FORMAT, varDeclarationNode.getName(),
                varDeclarationNode.getPosition().toString()));
    }
}
