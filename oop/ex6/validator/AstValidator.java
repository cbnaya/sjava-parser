package oop.ex6.validator;

import oop.ex6.ast.*;
import oop.ex6.ast.ExpressionNode.ExpressionType;

import java.util.Iterator;
import java.util.List;

public class AstValidator {

	public static final String CONDITION_NAME = "condition";
	private final VarStack stack;
	private final List<MethodNode> methodList;

	public AstValidator(final VarStack stack, final List<MethodNode> methodList)
	{
		this.stack = stack;
		this.methodList = methodList;

	}

	public void run(final ScopeNode codeScope) throws InvalidFileException {
		validateCodeScope(codeScope, false);
	}


	public void run(final MethodNode method) throws InvalidFileException {
		validateFunction(method);
	}

	public VarStack getStack()
	{
		return stack;
	}

	public static void globalValidate(final GlobalNode globalNode) 
			throws InvalidFileException {
		for (final MethodNode method:globalNode.getMethods()) {
			final VarStack varStack = getGlobalVariableStack(globalNode);
			final AstValidator astValidator = new AstValidator(varStack, 
					globalNode.getMethods());
			astValidator.run(method);
		}
	}

	public static VarStack getGlobalVariableStack(final GlobalNode globalNode) 
			throws InvalidFileException {
		final VarStack stack = new VarStack();
		stack.enterScope();

		final AstValidator astValidator = new AstValidator(stack, 
				globalNode.getMethods());
		astValidator.run(globalNode);

		return astValidator.getStack();
	}

	private void validateFunction(final MethodNode methodNode) 
			throws InvalidFileException {
		stack.enterScope();

		for (final ArgumentNode arg : methodNode.getArgs()) {
			final Variable var = stack.add(arg);
			var.assign(var.getType());
		}

		validateCodeScope(methodNode, false);
		stack.exitScope();

		final List<AstNode> s = methodNode.getBody();
		if (s.get(s.size() - 1).getNodeType() != AstNode.NodeType.RETURN) {
			throw new MissingReturnException(methodNode);
		}
	}

	private void validateCodeScope(final ScopeNode scopeNode) 
			throws InvalidFileException {
		validateCodeScope(scopeNode, true);
	}

	private void validateCodeScope(final ScopeNode scopeNode, 
			final boolean isDifferentNameScope) throws InvalidFileException {
		if (isDifferentNameScope) {
			stack.enterScope();
		}

		for (final AstNode node : scopeNode.getBody()) {
			switch (node.getNodeType()) {
			case VAR_DECLARATION: {
				final VarDeclarationNode varDeclaration = 
						(VarDeclarationNode) node;
				stack.add(varDeclaration);
			}
			break;
			case ASSIGNMENT: {
				final AssignmentNode assignmentNode = (AssignmentNode) node;

				final ExpressionNode value = assignmentNode.getValue();
				validateExpression(value);
				final ExpressionNode.ExpressionType valueType = 
						getExpressionType(value);
				final Variable var = stack.get(assignmentNode);

				if ((var.isFinal()) && (var.hasValue())) {
					throw new FinalAssignmentException(assignmentNode);
				} if(!var.assign(valueType)) {
					throw new TypeMismatchException(var.getName(), 
							var.getType(), valueType, 
							assignmentNode.getPosition());
				}
			}
			break;

			case WHILE:
			case IF: {
				final ConditionalNode conditionalNode = (ConditionalNode) node;
				validateCondition(conditionalNode.getCondition());
				validateCodeScope(conditionalNode);
			}
			break;
			case CALL_METHOD: {
				final CallMethodNode callMethodNode = (CallMethodNode) node;
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

	private void validateExpression(final ExpressionNode value) 
			throws VariableException {
		if (value.getNodeType() == AstNode.NodeType.VAR_VAL) {
			final VarExpressionNode var = (VarExpressionNode) value;
			final Variable valueVar = stack.get(var);

			if (!valueVar.hasValue()) {
				throw new VarNeverAssignedException(var);
			}
		}
	}

	private void validateCallMethod(final CallMethodNode callMethodNode) 
			throws MethodException, TypeMismatchException {
		final MethodNode methodNode = getMethod(callMethodNode);

		if (callMethodNode.getArgs().size() != methodNode.getArgs().size()) {
			throw new ArgumentsNumMismatchException(callMethodNode, methodNode);
		}

		//Iterator<ExpressionNode> argValue = callMethodNode.getArgs().iterator();
		final Iterator<ArgumentNode> args  = methodNode.getArgs().iterator();

		for (final ExpressionNode argValue : callMethodNode.getArgs()) {
			final ArgumentNode argumentNode = args.next();
			if(!argumentNode.getType().accept(argValue.getType())) {
				throw new TypeMismatchException(argumentNode.getName(), 
						argumentNode.getType(), argValue.getType(), 
						argValue.getPosition());
			}
		}
	}

	private MethodNode getMethod(final CallMethodNode callMethodNode) throws 
			MethodDoesNotExistException {
		for (final MethodNode methodNode : methodList) {
			if (methodNode.getName().equals(callMethodNode.getName())) {
				return methodNode;
			}
		}
		throw new MethodDoesNotExistException(callMethodNode);
	}

	private void validateCondition(final ExpressionNode condition) 
			throws TypeMismatchException, VariableException {
		if ((condition.getNodeType() == AstNode.NodeType.OR)||
				(condition.getNodeType() == AstNode.NodeType.AND)) {
			final BinaryOpNode binaryOpNode = (BinaryOpNode) condition;
			validateCondition(binaryOpNode.getLeft());
			validateCondition(binaryOpNode.getRight());
		} else {
			validateExpression(condition);
			final ExpressionNode.ExpressionType expressionType = 
					getExpressionType(condition);
			if (!ExpressionNode.ExpressionType.BOOLEAN.accept(expressionType)) {
				throw new TypeMismatchException(CONDITION_NAME, 
						ExpressionNode.ExpressionType.BOOLEAN,
						expressionType, condition.getPosition());
			}
		}
	}


	private ExpressionType getExpressionType( final ExpressionNode value) throws
			VarDoesNotExistException {
		if (value.getNodeType() == AstNode.NodeType.VAR_VAL) {
			final VarExpressionNode valueVarNode = (VarExpressionNode) value;
			final Variable valueVar = stack.get(valueVarNode);
			return valueVar.getType();
		}
		return value.getType();
	}
}
