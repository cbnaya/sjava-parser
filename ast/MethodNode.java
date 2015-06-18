package ast;

import lexer.Position;
import identities.Method;
import identities.variables.Variable;
import identities.variables.VariableFactory;

import java.util.ArrayList;
import java.util.List;

import callStack.CallStack;

public class MethodNode extends ScopeNode {

    private final MethodSignatureNode SIGNATURE;

	public MethodNode(Position position, List<AstNode> methodBody, 
    		List<ArgumentNode> methodArgs, String methodName) {
        super(position, methodBody);
        DECLARATION_ARGS = methodArgs;
        NAME = methodName;
        SIGNATURE = new MethodSignatureNode(position);
    }

    public List<ArgumentNode> getArgs() {
        return DECLARATION_ARGS;
    }
    
    public String getName() {
    	return NAME;
    }
    
    @Override
    public void fillCallStack(CallStack callStack) throws Exception {
    	ArrayList<Variable> args = new ArrayList<>();
    	for (ArgumentNode arg : DECLARATION_ARGS) {
    		args.add(VariableFactory.createVar(arg));
    		arg.fillCallStack(callStack);
    	}
    //	callStack.add(new Method(name, VariableFactory.createVar(declaration)))
    }

    private final List<ArgumentNode> DECLARATION_ARGS;
    private final String NAME;
    
	public class MethodSignatureNode extends IdDeclarationNode {
		
		private MethodSignatureNode(Position position) {
			super(position, NAME);
		}
		
		@Override
		public String getType() {
			return "void";
		}

		public List<ArgumentNode> getArgs() {
	        return DECLARATION_ARGS;
	    }

		@Override
		public void fillCallStack(CallStack callStack) throws Exception {
	    	ArrayList<Variable> args = new ArrayList<>();
	    	for (ArgumentNode arg : DECLARATION_ARGS) {
	    		args.add(VariableFactory.createVar(arg));
	    	}
			if (!callStack.add(new Method(getName(), args))) {
				throw new DuplicateIdentityException(getPosition(), getName());
			}
		}
	}

	public MethodSignatureNode getSignature() {
		return SIGNATURE;
	}
	
	public static class ArgumentNode extends VarDeclaration {
	    public ArgumentNode(Position position, String argType, String argName, 
	    		boolean isFinal) {
	        super(position, argType, argName, isFinal);
	    }
	}
}