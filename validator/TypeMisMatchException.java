package validator;

import ast.ExpressionNode;
import lexer.Position;

/**
 * Created by cbnaya on 21/06/2015.
 */
public class TypeMisMatchException extends Exception {

    public static final String ERROR_MESSAGE_FORMAT = "%s type mismatch type %s does not accept type %s (%s)";

    public TypeMisMatchException(String name, ExpressionNode.ExpressionType requiredType,
                                 ExpressionNode.ExpressionType acrualType, Position position) {
        super(String.format(ERROR_MESSAGE_FORMAT, name, requiredType.name(), acrualType.name(), position));
    }
}
