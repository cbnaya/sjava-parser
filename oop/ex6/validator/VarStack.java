package oop.ex6.validator;

import oop.ex6.ast.AssignmentNode;
import oop.ex6.ast.VarDeclarationNode;
import oop.ex6.ast.VarExpressionNode;
import oop.ex6.lexer.Position;

import java.util.ArrayList;

/**
 * Keeps the variables, their state - initialized or not - and the scope of
 * their declaration.
 */
public class VarStack {

    // The built-in java stack is the inner implementation of this.
    ArrayList<ArrayList<Variable>> stack;

    /**
     * Ctor
     */
    public VarStack() {
        stack = new ArrayList<>();
    }

    /**
     * Adds a new variable to the stack.
     *
     * @param varDeclarationNode The declaration of that variable.
     * @return The new Variable object.
     * @throws VarDuplicateDeclaration If a variable with the same name was
     *                                 already declared at the same scope.
     */
    public Variable add(VarDeclarationNode varDeclarationNode) throws
            VarDuplicateDeclaration {
        if (null != getFromLevel(stack.size() - 1, varDeclarationNode.getName())) {
            throw new VarDuplicateDeclaration(varDeclarationNode);
        }
        Variable var = new Variable(varDeclarationNode.getName(),
                varDeclarationNode.getType(), varDeclarationNode.isFinal());
        stack.get(stack.size() - 1).add(var);
        return var;
    }

    /**
     * Enters a new code scope.
     */
    public void enterScope() {
        stack.add(new ArrayList<Variable>());
    }

    /**
     * Exits the current code scope, and forgets all its variables.
     */
    public void exitScope() {
        stack.remove(stack.size() - 1);
    }

    /*
     * Searches for a variable at a specific scope.
     * @param level The level of the inspected scope. The global level is 0.
     * @param name The name of the searched var.
     * @return The searched var.
     */
    private Variable getFromLevel(int level, String name) {
        for (Variable var : stack.get(level)) {
            if (var.getName().equals(name)) {
                return var;
            }
        }

        return null;
    }

    /*
     * @param name A name of required variable.
     * @param pos In case something will go wrong.
     * @return The variable.
     * @throws VarDoesNotExistException If this variable was never declared.
     */
    private Variable get(String name, Position pos)
            throws VarDoesNotExistException {
        for (int i = stack.size() - 1; i >= 0; i--) {
            Variable var = getFromLevel(i, name);
            if (null != var) {
                return var;
            }
        }
        throw new VarDoesNotExistException(name, pos);
    }

    /**
     * @param assignmentNode An ast node representing an assignment.
     * @return The variable assigning to at the given node.
     * @throws VarDoesNotExistException If this variable was never declared.
     */
    public Variable get(AssignmentNode assignmentNode)
            throws VarDoesNotExistException {
        return get(assignmentNode.getName(), assignmentNode.getPosition());
    }

    /**
     * @param varExpressionNode An ast node representing a variable expression.
     * @return The referenced variable.
     * @throws VarDoesNotExistException If this variable was never declared.
     */
    public Variable get(VarExpressionNode varExpressionNode)
            throws VarDoesNotExistException {
        return get(varExpressionNode.getName(),
                varExpressionNode.getPosition());
    }
}
