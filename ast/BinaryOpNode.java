package ast;

import types.Bool;
import types.Type;
import callStack.CallStack;
import lexer.Position;

/**
 * Created by cbnaya on 14/06/2015.
 */
public abstract class BinaryOpNode extends ExpressionNode {

    public BinaryOpNode(Position position, ExpressionNode left, 
    		ExpressionNode right) {
        super(position);
        this.left = left;
        this.right = right;
        type = Bool.instance();
    }

    private ExpressionNode left;
    private ExpressionNode right;
    private Bool type;

    public ExpressionNode getLeft() {
        return left;
    }

    public ExpressionNode getRight() {
        return right;
    }
    
    @Override
	public Type getType(CallStack callStack) {
		return type;
	}
    
    @Override
	public void fillCallStack(CallStack callStack) throws Exception {
		if (!type.accept(left.getType(callStack)) || 
				!type.accept(right.getType(callStack))) {
			throw new TypeMismatchException(getPosition(), "boolean");
		}
		left.fillCallStack(callStack);
		right.fillCallStack(callStack);
	}
}
