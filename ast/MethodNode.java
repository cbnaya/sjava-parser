package ast;

import lexer.Position;

import java.util.List;

public class MethodNode extends ScopeNode{

    public MethodNode(Position position, String methodName, List<ArgumentNode> methodArgs, List<AstNode>
            methodBody) {
        super(position, methodBody);
        name = methodName;
        arguments = methodArgs;
    }

    public List<ArgumentNode> getArgs() {
        return arguments;
    }

    public String getName() {
        return name;
    }


    private final List<ArgumentNode> arguments;
    private final String name;


    @Override
    public NodeType getNodeType() {
        return NodeType.METHOD;
    }
}