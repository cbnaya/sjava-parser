package oop.ex6.validator;

import oop.ex6.ast.*;
import oop.ex6.ast.ExpressionNode.ExpressionType;

import java.util.Iterator;
import java.util.List;

/**
 * Used for running over an AST to validate all references variables and methods
 * are legitimate, by checking all errors which can't be found during the tree
 * creation.
 */
public class AstValidator {

	public static final String CONDITION_NAME = "condition";
	private final VarStack stack;
	private final List<MethodNode> methodList;

	/**
	 * @param stack A call stack to keep track of the state of the variable. 
	 * @param methodList All methods that could be called.
	 */
	public AstValidator(final VarStack stack, final List<MethodNode> methodList)
	{
		this.stack = stack;
		this.methodList = methodList;
	}

	/**
	 * Runs a scope without entering it. 
	 * @param codeScope A scope to validate, step by step.
	 * @throws InvalidCodeException If this scope was found invalid.
	 */
	public void run(final ScopeNode codeScope) throws InvalidCodeException {
		validateCodeScope(codeScope, false);
	}

	/**
	 * @param method A method to validate, step by step.
	 * @throws InvalidCodeException If this method was found invalid.
	 */
	public void run(final MethodNode method) throws InvalidCodeException {
		validateFunction(method);
	}

	/**
	 * @return The stack which keeps the current state of the program.
	 */
	public VarStack getStack() {
		return stack;
	}

	/**
	 * Validates all the code.
	 * @param globalNode The root of the ast representing the code.
	 * @throws InvalidCodeException If the code is found invalid.
	 */
	/*
	 * The global level of the stack is created each time, to match the 
	 * requirement of ignoring changes done to global variables at methods and 
	 * the general assume the order of method declarations is not considered.
	 */
	public static void globalValidate(final GlobalNode globalNode) 
			throws InvalidCodeException {
        getGlobalVariableStack(globalNode);
		for (final MethodNode method : globalNode.getMethods()) {
			final VarStack varStack = getGlobalVariableStack(globalNode);
			final AstValidator astValidator = new AstValidator(varStack, 
					globalNode.getMethods());
			astValidator.run(method);
		}
	}

	/**
	 * @param globalNode An ast root.
	 * @return A stack containing all global variables.
	 * @throws InvalidCodeException If there is a problem at the global scope.
	 */
	public static VarStack getGlobalVariableStack(final GlobalNode globalNode) 
			throws InvalidCodeException {
		final VarStack stack = new VarStack();
		stack.enterScope();

		final AstValidator astValidator = new AstValidator(stack, 
				globalNode.getMethods());
		astValidator.run(globalNode);

		return astValidator.getStack();
	}

	private void validateFunction(final MethodNode methodNode) 
			throws InvalidCodeException {
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
			throws InvalidCodeException {
		validateCodeScope(scopeNode, true);
	}

	private void validateCodeScope(final ScopeNode scopeNode, 
			final boolean isDifferentNameScope) throws InvalidCodeException {
		if (isDifferentNameScope) {
			stack.enterScope();
		}

		for (final AstNode node : scopeNode.getBody()) {
			switch (node.getNodeType()) {
			case VAR_DECLARATION: 
				final VarDeclarationNode varDeclaration = 
						(VarDeclarationNode) node;
				stack.add(varDeclaration);
				break;
			case ASSIGNMENT:
				validateAssignment((AssignmentNode) node);
				break;

			case WHILE:
			case IF: 
				final ConditionalNode conditionalNode = (ConditionalNode) node;
				validateCondition(conditionalNode.getCondition());
				validateCodeScope(conditionalNode);
				break;
			case CALL_METHOD:
				final CallMethodNode callMethodNode = (CallMethodNode) node;
				validateCallMethod(callMethodNode);
				break;
			case RETURN:
				break;
			}
		}

		if (isDifferentNameScope) {
			stack.exitScope();
		}
	}
	
	private void validateAssignment(AssignmentNode assignmentNode) 
			throws VariableException, TypeMismatchException {
		final ExpressionNode value = assignmentNode.getValue();
		validateExpression(value);
		final ExpressionNode.ExpressionType valueType = 
				getExpressionType(value);
		final Variable var = stack.get(assignmentNode);

		if (var.isFinal()) {
			throw new FinalAssignmentException(assignmentNode);
		} if(!var.assign(valueType)) {
			throw new TypeMismatchException(var.getName(), 
					var.getType(), valueType, 
					assignmentNode.getPosition());
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
            throws MethodException, TypeMismatchException, VarDoesNotExistException {
		final MethodNode methodNode = getMethod(callMethodNode);

		if (callMethodNode.getArgs().size() != methodNode.getArgs().size()) {
			throw new ArgumentsNumMismatchException(callMethodNode, methodNode);
		}

		//Iterator<ExpressionNode> argValue = callMethodNode.getArgs().iterator();
		final Iterator<ArgumentNode> args  = methodNode.getArgs().iterator();

		for (final ExpressionNode argValue : callMethodNode.getArgs()) {
			final ArgumentNode argumentNode = args.next();
			if(!argumentNode.getType().accept(getExpressionType(argValue))) {
				throw new TypeMismatchException(argumentNode.getName(), 
						argumentNode.getType(), getExpressionType(argValue),
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
