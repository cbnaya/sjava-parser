package ast;

import lexer.Position;

import java.util.List;

public class CallMethodNode extends AstNode {

	public CallMethodNode(Position position, String methodName, List<ExpressionNode> methodArgs) {
		super(position);
        name = methodName;
		args = methodArgs;
	}
	
	public List<ExpressionNode> getArgs() {
        return args;
    }

    public String getName() {
        return name;
    }
	private List<ExpressionNode> args;
    private String name;


    @Override
    public NodeType getNodeType() {
        return NodeType.CALL_METHOD;
    }

}
