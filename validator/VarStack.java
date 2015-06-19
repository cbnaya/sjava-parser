package validator;

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

    public void add(Variable var)
    {
        if (null != getFromLevel(stack.size()-1, var.getName()))
        {
            //TODO : throw
        }
        stack.get(stack.size()-1).add(var);
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

    public Variable get(String name)
    {
        for (int i = stack.size()-1; i>=0; i--)
        {
            Variable var = getFromLevel(i, name);
            if (null != var)
            {
                return var;
            }
        }

        return null;
    }


    ArrayList<ArrayList<Variable>> stack;
    int currentIndex;
}
