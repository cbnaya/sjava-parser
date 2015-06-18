package ast;

import callStack.CallStack;
import lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public abstract class AstNode {
    
	public AstNode(Position position)
    {
        //this.parent = parent;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public abstract void fillCallStack(CallStack callStack) throws Exception;
    
    //public AstNode getParent() {
    //    return parent;
    //}

    private Position position;
    //private AstNode parent;
    
}
