package oop.ex6.validator;

import oop.ex6.ast.VarDeclarationNode;

/**
 * Created by cbnaya on 21/06/2015.
 */
public class VarDuplicateDeclaration extends Exception {
    public static final String ERROR_MESSAGE_FORMAT = "variable name %s already declared (%s)";

    public VarDuplicateDeclaration(VarDeclarationNode varDeclarationNode) {
        super(String.format(ERROR_MESSAGE_FORMAT, varDeclarationNode.getName(),
                varDeclarationNode.getPosition().toString()));
    }
}
