package ast;

import lexer.Position;

import java.util.List;

public class MethodNode extends AstNode {

    public MethodNode(Position position, String methodName, List<ArgumentNode> methodArgs, ScopeNode
            methodBody) {
        super(position);
        name = methodName;
        arguments = methodArgs;
        body = methodBody;
    }

    public List<ArgumentNode> getArgs() {
        return arguments;
    }

    public String getName() {
        return name;
    }

    public ScopeNode getBody() {
        return body;
    }

    private final List<ArgumentNode> arguments;
    private final String name;
    private final ScopeNode body;

    @Override
    public NodeType getNodeType() {
        return NodeType.METHOD;
    }
}