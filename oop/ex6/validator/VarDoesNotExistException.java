package oop.ex6.validator;

import oop.ex6.ast.VarExpressionNode;
import oop.ex6.lexer.Position;

/**
 * Is thrown when trying to reference a non-exist variable.
 */
public class VarDoesNotExistException extends VariableException {

    private static final long serialVersionUID = 1L;
    private static final String ERROR_MESSAGE_FORMAT =
            "variable name %s was not declared (%s)";

    /**
     * @param varExpressionNode The expression node containing the referenced
     *                          variable.
     */
    public VarDoesNotExistException(final VarExpressionNode varExpressionNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, varExpressionNode.getName(),
                varExpressionNode.getPosition().toString()));
    }

    /**
     * @param name The name of the referenced variable.
     * @param pos  The position of using that name.
     */
    public VarDoesNotExistException(final String name, final Position pos) {
        super(String.format(ERROR_MESSAGE_FORMAT, name, pos.toString()));
    }
}
