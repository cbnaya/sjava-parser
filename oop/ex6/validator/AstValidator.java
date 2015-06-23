package oop.ex6.validator;

import oop.ex6.ast.*;
import oop.ex6.ast.ExpressionNode.ExpressionType;

import java.util.Iterator;
import java.util.List;

/**
 * Used for running over an AST to validate all references variables and methods
 * are legitimate, in a manner that represents running the code by checking all 
 * errors which can't be found during the tree creation, because types, 
 * duplicate declaration and other problems involving names references are still 
 * to be checked.
 */
public class AstValidator {

	// Used at exceptions which requiring variable names.
	public static final String CONDITION_NAME = "condition";
	// Keeps the variables state.
	private final VarStack stack;
	// Keeps the declared methods.
	private final List<MethodNode> methodList;

	/**
	 * @param stack A call stack to keep track of the state of the variable. 
	 * @param methodList All methods that could be called.
	 */
	public AstValidator(VarStack stack, List<MethodNode> methodList) {
		this.stack = stack;
		this.methodList = methodList;
	}

	/**
	 * Runs a scope without entering it. 
	 * @param codeScope A scope to validate, step by step.
	 * @throws InvalidCodeException If this scope was found invalid.
	 */
	public void run(ScopeNode codeScope) throws InvalidCodeException {
		validateCodeScope(codeScope, false);
	}

	/**
	 * @param method A method to validate, step by step.
	 * @throws InvalidCodeException If this method was found invalid.
	 */
	public void run(MethodNode method) throws InvalidCodeException {
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
	public static void globalValidate(GlobalNode globalNode) 
			throws InvalidCodeException {
        getGlobalVariableStack(globalNode);
		for (MethodNode method : globalNode.getMethods()) {
			VarStack varStack = getGlobalVariableStack(globalNode);
			AstValidator astValidator = new AstValidator(varStack, 
					globalNode.getMethods());
			astValidator.run(method);
		}
	}

	/**
	 * @param globalNode An ast root.
	 * @return A stack containing all global variables.
	 * @throws InvalidCodeException If there is a problem at the global scope.
	 */
	public static VarStack getGlobalVariableStack(GlobalNode globalNode) 
			throws InvalidCodeException {
		VarStack stack = new VarStack();
		stack.enterScope();

		AstValidator astValidator = new AstValidator(stack, 
				globalNode.getMethods());
		astValidator.run(globalNode);

		return astValidator.getStack();
	}

	/*
	 * An helper method for the overloading run(MethodNode).
	 * Validates the method code was declared right.
	 * @throws InvalidCodeException If not.
	 */
	private void validateFunction(MethodNode methodNode) 
			throws InvalidCodeException {
		stack.enterScope();

		for (ArgumentNode arg : methodNode.getArgs()) {
			Variable var = stack.add(arg);
			var.assign(var.getType());
		}

		validateCodeScope(methodNode, false);
		stack.exitScope();

		List<AstNode> s = methodNode.getBody();
		if (s.get(s.size() - 1).getNodeType() != AstNode.NodeType.RETURN) {
			throw new MissingReturnException(methodNode);
		}
	}

	/*
	 * Validates the a code scope was declared right.
	 * @param isDifferentNameScope Whether the stack should behave as it entered
	 * a new scope.
	 * @throws InvalidCodeException If it was not right.
	 */
	private void validateCodeScope(ScopeNode scopeNode, 
			boolean isDifferentNameScope) throws InvalidCodeException {
		if (isDifferentNameScope) {
			stack.enterScope();
		}

		for (AstNode node : scopeNode.getBody()) {
			switch (node.getNodeType()) {
			case VAR_DECLARATION: 
				VarDeclarationNode varDeclaration = (VarDeclarationNode) node;
				stack.add(varDeclaration);
				break;
			case ASSIGNMENT:
				validateAssignment((AssignmentNode) node);
				break;

			case WHILE:
			case IF: 
				ConditionalNode conditionalNode = (ConditionalNode) node;
				validateCondition(conditionalNode.getCondition());
				validateCodeScope(conditionalNode, true);
				break;
			case CALL_METHOD:
				CallMethodNode callMethodNode = (CallMethodNode) node;
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
	
	/*
	 * An helper method for validateCodeScope(ScopeNode, boolean).
	 * Validates an assignment as properly done.
	 * @throws VariableException If there is an error with one of the variables
	 * at the assignment.
	 * @throws TypeMismatchException If the value type is not acceptable by
	 * the target variable.
	 */
	private void validateAssignment(AssignmentNode assignmentNode) 
			throws VariableException, TypeMismatchException {
		ExpressionNode value = assignmentNode.getValue();
		validateExpression(value);
		ExpressionNode.ExpressionType valueType = getExpressionType(value);
		Variable var = stack.get(assignmentNode);

		if (var.isFinal()) {
			throw new FinalAssignmentException(assignmentNode);
		} if(!var.assign(valueType)) {
			throw new TypeMismatchException(var.getName(), 
					var.getType(), valueType, 
					assignmentNode.getPosition());
		}
	}

	/*
	 * Validates an expression exists and has a value.
	 * @throws VariableException If it does not.
	 */
	private void validateExpression(ExpressionNode value) 
			throws VariableException {
		if (value.getNodeType() == AstNode.NodeType.VAR_VAL) {
			VarExpressionNode var = (VarExpressionNode) value;
			Variable valueVar = stack.get(var);

			if (!valueVar.hasValue()) {
				throw new VarNeverAssignedException(var);
			}
		}
	}

	/*
	 * An helper method for validateCodeScope(ScopeNode, boolean).
	 * @param callMethodNode A call to be validated.
	 * @throws TypeMismatchException, 
	 * @throws VarDoesNotExistException If there is a problem with a specific 
	 * argument value.
	 * @throws MethodException If there is a general problem with the call.
	 */
	private void validateCallMethod(CallMethodNode callMethodNode)
            throws MethodException, TypeMismatchException, 
            VarDoesNotExistException {
		MethodNode methodNode = getMethod(callMethodNode);

		if (callMethodNode.getArgs().size() != methodNode.getArgs().size()) {
			throw new ArgumentsNumMismatchException(callMethodNode, methodNode);
		}

		Iterator<ArgumentNode> args  = methodNode.getArgs().iterator();

		for (ExpressionNode argValue : callMethodNode.getArgs()) {
			ArgumentNode argumentNode = args.next();
			if(!argumentNode.getType().accept(getExpressionType(argValue))) {
				throw new TypeMismatchException(argumentNode.getName(), 
						argumentNode.getType(), getExpressionType(argValue),
						argValue.getPosition());
			}
		}
	}

	/*
	 * An helper method for validateCallMethod(CallMethodNode)
	 * @return The declaration of the called method callMethodNode.
	 * @throws MethodDoesNotExistException If it was never declared.
	 */
	private MethodNode getMethod(CallMethodNode callMethodNode) throws 
			MethodDoesNotExistException {
		for (MethodNode methodNode : methodList) {
			if (methodNode.getName().equals(callMethodNode.getName())) {
				return methodNode;
			}
		}
		throw new MethodDoesNotExistException(callMethodNode);
	}

	/*
	 * A recursive  helper method for validateCodeScope(ScopeNode, boolean).
	 * @param condition A boolean expression to be checked.
	 * @throws TypeMismatchException If some of the expression is neither 
	 * boolean nor numeric.
	 * @throws VariableException Thrown by validateExpression().
	 */
	private void validateCondition(ExpressionNode condition) 
			throws TypeMismatchException, VariableException {
		if ((condition.getNodeType() == AstNode.NodeType.OR)||
				(condition.getNodeType() == AstNode.NodeType.AND)) {
			BinaryOpNode binaryOpNode = (BinaryOpNode) condition;
			validateCondition(binaryOpNode.getLeft());
			validateCondition(binaryOpNode.getRight());
		} else {
			validateExpression(condition);
			ExpressionNode.ExpressionType expressionType = 
					getExpressionType(condition);
			if (!ExpressionNode.ExpressionType.BOOLEAN.accept(expressionType)) {
				throw new TypeMismatchException(CONDITION_NAME, 
						ExpressionNode.ExpressionType.BOOLEAN,
						expressionType, condition.getPosition());
			}
		}
	}

	/*
	 * @return The type of value.
	 * @throws VarDoesNotExistException If values is a VarExpressionNode of an
	 * undeclared var.
	 */
	private ExpressionType getExpressionType(ExpressionNode value) throws
			VarDoesNotExistException {
		if (value.getNodeType() == AstNode.NodeType.VAR_VAL) {
			VarExpressionNode valueVarNode = (VarExpressionNode) value;
			Variable valueVar = stack.get(valueVarNode);
			return valueVar.getType();
		}
		return value.getType();
	}
}
