package validator;

import ast.*;

import java.util.Iterator;
import java.util.List;

public class AstValidator {

    public static final String CONDITION_NAME = "condition";
    private VarStack stack;
    private List<MethodNode> methodList;

    public AstValidator(VarStack stack, List<MethodNode> methodList)
    {
        this.stack = stack;
        this.methodList = methodList;

    }

    public void run(ScopeNode codeScope) throws VarDuplicateDeclaration, RequiredVarDoseNotExistException, MethodNotExistsException, TypeMisMatchException, VarNeverAssignedException, NumOfArgumentsNotMatchException, FinalAssignmentException {
        validateCodeScope(codeScope, false);
    }


    public void run(MethodNode method) throws VarDuplicateDeclaration, MethodNotExistsException, TypeMisMatchException, VarNeverAssignedException, RequiredVarDoseNotExistException, MethodMustEndWithReturnException, NumOfArgumentsNotMatchException, FinalAssignmentException {
        validateFunction(method);
    }

    public VarStack getStack()
    {
        return stack;
    }

    public static void globalValidate(GlobalNode globalNode) throws VarDuplicateDeclaration,
            RequiredVarDoseNotExistException, MethodNotExistsException, TypeMisMatchException, VarNeverAssignedException, NumOfArgumentsNotMatchException, MethodMustEndWithReturnException, FinalAssignmentException {
        for (MethodNode method:globalNode.getMethods())
        {
            VarStack varStack = getGlobalVariableStack(globalNode);
            AstValidator astValidator = new AstValidator(varStack, globalNode.getMethods());
            astValidator.run(method);
        }
    }

    public static VarStack getGlobalVariableStack(GlobalNode globalNode) throws VarDuplicateDeclaration, MethodNotExistsException, TypeMisMatchException, VarNeverAssignedException, RequiredVarDoseNotExistException, NumOfArgumentsNotMatchException, FinalAssignmentException {
        VarStack stack = new VarStack();
        stack.enterScope();

        AstValidator astValidator = new AstValidator(stack, globalNode.getMethods());
        astValidator.run(globalNode);

        return astValidator.getStack();
    }

    private void validateFunction(MethodNode methodNode) throws VarDuplicateDeclaration, MethodMustEndWithReturnException, NumOfArgumentsNotMatchException, MethodNotExistsException, RequiredVarDoseNotExistException, VarNeverAssignedException, TypeMisMatchException, FinalAssignmentException {
        stack.enterScope();

        for (ArgumentNode arg : methodNode.getArgs()) {
            Variable var = stack.add(arg);
            var.assign(var.getType());
        }

        validateCodeScope(methodNode, false);
        stack.exitScope();

        List<AstNode> s = methodNode.getBody();
        if (s.get(s.size() - 1).getNodeType() != AstNode.NodeType.RETURN) {
            throw new MethodMustEndWithReturnException(methodNode);
        }
    }

    private void validateCodeScope(ScopeNode scopeNode) throws VarDuplicateDeclaration,
            RequiredVarDoseNotExistException,
            MethodNotExistsException,
            TypeMisMatchException,
            VarNeverAssignedException,
            NumOfArgumentsNotMatchException, FinalAssignmentException {
        validateCodeScope(scopeNode, true);
    }

    private void validateCodeScope(ScopeNode scopeNode, boolean isDifferentNameScope)
            throws VarDuplicateDeclaration,
            VarNeverAssignedException,
            NumOfArgumentsNotMatchException,
            MethodNotExistsException,
            TypeMisMatchException,
            RequiredVarDoseNotExistException, FinalAssignmentException {
        if (isDifferentNameScope) {
            stack.enterScope();
        }

        for (AstNode node : scopeNode.getBody()) {
            switch (node.getNodeType()) {
                case VAR_DECLARATION: {
                    VarDeclarationNode varDeclaration = (VarDeclarationNode) node;
                    stack.add(varDeclaration);
                }
                break;
                case ASSIGNMENT: {
                    AssignmentNode assignmentNode = (AssignmentNode) node;

                    ExpressionNode value = assignmentNode.getValue();
                    validateExpression(value);
                    ExpressionNode.ExpressionType valueType = getExpressionType(value);
                    Variable var = stack.get(assignmentNode);

                    if ((var.isFinal()) && (var.hasValue()))
                    {
                        throw new FinalAssignmentException(assignmentNode);
                    }
                    if(!var.assign(valueType))
                    {
                        throw new TypeMisMatchException(var.getName(), var.getType(), valueType,
                                assignmentNode.getPosition());
                    }
                }
                break;

                case WHILE:
                case IF: {
                    ConditionalNode conditionalNode = (ConditionalNode) node;
                    validateCondition(conditionalNode.getCondition());
                    validateCodeScope(conditionalNode);
                }
                break;
                case CALL_METHOD: {
                    CallMethodNode callMethodNode = (CallMethodNode) node;
                    validateCallMethod(callMethodNode);
                }
                break;
                case RETURN:
                    break;
            }
        }

        if (isDifferentNameScope) {
            stack.exitScope();
        }
    }

    private void validateExpression(ExpressionNode value) throws VarNeverAssignedException,
                                                                RequiredVarDoseNotExistException {
        if (value.getNodeType() == AstNode.NodeType.VAR_VAL) {
            VarExpressionNode var = (VarExpressionNode) value;
            Variable valueVar = stack.get(var);

            if (!valueVar.hasValue()) {
                throw new VarNeverAssignedException(var);
            }
        }
    }

    private void validateCallMethod(CallMethodNode callMethodNode) throws NumOfArgumentsNotMatchException,
                                                                            TypeMisMatchException,
                                                                            MethodNotExistsException {
        MethodNode methodNode = getMethod(callMethodNode);

        if (callMethodNode.getArgs().size() != methodNode.getArgs().size()) {
            throw new NumOfArgumentsNotMatchException(callMethodNode, methodNode);
        }

        //Iterator<ExpressionNode> argValue = callMethodNode.getArgs().iterator();
        Iterator<ArgumentNode> args  = methodNode.getArgs().iterator();

        for (ExpressionNode argValue : callMethodNode.getArgs()) {
            ArgumentNode argumentNode = args.next();
            if(!argumentNode.getType().accept(argValue.getType()))
            {
                throw new TypeMisMatchException(argumentNode.getName(), argumentNode.getType(),
                                                            argValue.getType(),argValue.getPosition());
            }
        }
    }

    private MethodNode getMethod(CallMethodNode callMethodNode) throws MethodNotExistsException {
        for (MethodNode methodNode : methodList) {
            if (methodNode.getName().equals(callMethodNode.getName())) {
                return methodNode;
            }
        }
        throw new MethodNotExistsException(callMethodNode);
    }

    private void validateCondition(ExpressionNode condition) throws VarNeverAssignedException,
            TypeMisMatchException, RequiredVarDoseNotExistException {
        if ((condition.getNodeType() == AstNode.NodeType.OR)||
                (condition.getNodeType() == AstNode.NodeType.AND))
        {
            BinaryOpNode binaryOpNode = (BinaryOpNode) condition;
            validateCondition(binaryOpNode.getLeft());
            validateCondition(binaryOpNode.getRight());
        }
        else
        {
            validateExpression(condition);
            ExpressionNode.ExpressionType expressionType = getExpressionType(condition);
            if (!ExpressionNode.ExpressionType.BOOLEAN.accept(expressionType))
            {
                throw new TypeMisMatchException(CONDITION_NAME, ExpressionNode.ExpressionType.BOOLEAN,
                                                                    expressionType, condition.getPosition());
            }
        }
    }


    private ExpressionNode.ExpressionType getExpressionType(ExpressionNode value) throws RequiredVarDoseNotExistException {
        if (value.getNodeType() == AstNode.NodeType.VAR_VAL) {
            VarExpressionNode valueVarNode = (VarExpressionNode) value;
            Variable valueVar = stack.get(valueVarNode);
            return valueVar.getType();
        }

        return value.getType();
    }
}
