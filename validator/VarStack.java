package validator;

import ast.AssignmentNode;
import ast.VarDeclarationNode;
import ast.VarExpressionNode;
import lexer.Position;

import java.util.ArrayList;

/**
 * Created by cbnaya on 19/06/2015.
 */
public class VarStack {
    public VarStack()
    {
        stack = new ArrayList<ArrayList<Variable>>();
    }

    public void enterScope()
    {
        stack.add(new ArrayList<Variable>());
    }

    public void exitScope()
    {
        stack.remove(stack.size()-1);
    }

    public Variable add(VarDeclarationNode varDeclarationNode) throws VarDuplicateDeclaration {
        if (null != getFromLevel(stack.size()-1, varDeclarationNode.getName()))
        {
            throw new VarDuplicateDeclaration(varDeclarationNode);
        }
        Variable var = new Variable(varDeclarationNode.getName(), varDeclarationNode.getType(),
                varDeclarationNode.isFinal());
        stack.get(stack.size()-1).add(var);
        return var;
    }

    private Variable getFromLevel(int level, String name)
    {
        for(Variable var:stack.get(level))
        {
            if (var.getName().equals(name))
            {
                return var;
            }
        }

        return null;
    }

    private Variable get(String name, Position pos) throws RequiredVarDoseNotExistException {
        for (int i = stack.size() - 1; i >= 0; i--) {
            Variable var = getFromLevel(i, name);
            if (null != var) {
                return var;
            }
        }

        throw new RequiredVarDoseNotExistException(name, pos);
    }



    public Variable get(AssignmentNode assignmentNode) throws RequiredVarDoseNotExistException {
        return get(assignmentNode.getName(), assignmentNode.getPosition());
    }

    public Variable get(VarExpressionNode varExpressionNode) throws RequiredVarDoseNotExistException {
        return get(varExpressionNode.getName(), varExpressionNode.getPosition());
    }


    ArrayList<ArrayList<Variable>> stack;
}
