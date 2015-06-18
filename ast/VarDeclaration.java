package ast;

import identities.variables.VariableFactory;
import callStack.CallStack;
import lexer.Position;

public class VarDeclaration extends IdDeclarationNode {

	public VarDeclaration(Position position, String type, String name, 
    		boolean isFinal) {
        super(position, name);
        this.type = type;
        this.isFinal = isFinal;
    }

    private String type;
    private boolean isFinal;

    public String getType() {
        return type;
    }
    
    public boolean isFinal() {
    	return isFinal;
    }
    
	@Override
	public void fillCallStack(CallStack callStack) throws Exception {
		if (!callStack.add(VariableFactory.createVar(this))) {
			throw new DuplicateIdentityException(getPosition(), getName());
		}
	}
}
