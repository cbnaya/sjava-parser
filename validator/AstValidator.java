package validator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import lexer.Tokenizer;
import ast.*;

public class AstValidator {

	public static void main(String[] args) {
		for (int i = 1; i < 10; i++) {
			System.out.println("20"+i);
			String fileName = "C:/Users/Noam/SkyDrive/JavaWorkspace"
					+ "/ex6_sJavaVerifier/tests/test20" + i + ".sjava";
			try {
				AstValidator validator = new AstValidator(fileName);
				validator.validate();
			} catch (Exception | NotAllowedInThisContext | OtherTokenTypeNeedHere | InvalidIdentityName e) {
				e.printStackTrace();
			}
		}
	}

	private GlobalNode root;
	
	public AstValidator(String fileName) throws IOException, NotAllowedInThisContext, OtherTokenTypeNeedHere, InvalidIdentityName {
		String fileData = new String(Files.readAllBytes( Paths.get(fileName)), 
				StandardCharsets.UTF_8);
		root = new Parser(new Tokenizer(fileData)).parseGlobal();
	}
	
	public void validate() throws HowDidYouGetHereException {
		System.out.println(root);
		ScopeNode scope = root.getBody();
		System.out.println(" "+scope);
		for (AstNode node : scope.getBody()) {
			System.out.println("  "+node);
			variablesCheck(node, "  ");
		}
		for (MethodNode method : root.getMethods()) {
			methodIterate(method, "  ");
		}
	}
	
	private void variablesCheck(AstNode node, String spaces) throws HowDidYouGetHereException {
		switch (node.getNodeType()) {
		case VAR_DECLARATION:
			varDeclarationCheck((VarDeclaration) node, spaces+" ");
			break;
		case ASSIGNMENT:
			assignmentCheck((AssignmentNode) node, spaces+" ");
			break;
		default:
			throw new HowDidYouGetHereException();
		}
	}

	private void methodIterate(MethodNode method, String spaces) throws HowDidYouGetHereException {
		System.out.println(spaces+method);
		System.out.println(spaces+method.getName());
		for (ArgumentNode arg : method.getArgs()) {
			argCheck(arg, spaces+" ");
		}
		scopeIterate(method.getBody(), spaces+" ");
	}
	
	private void argCheck(ArgumentNode arg, String spaces) {
		System.out.println(spaces+arg);
		System.out.println(spaces+arg.isFinal() + " " + arg.getType() + " " + 
				arg.getName());
	}
	
	private void scopeIterate(ScopeNode scope, String spaces) throws HowDidYouGetHereException {
		System.out.println(spaces+scope);
		for (AstNode node : scope.getBody()) {
			astIterate(node, spaces+" ");
		}
	}

	private void astIterate(AstNode node, String spaces) throws HowDidYouGetHereException {
		System.out.println(spaces+node);
		switch (node.getNodeType()) {
		case WHILE:
		case IF:
			conditionalIterate((ConditionalNode) node, spaces+" ");
			break;
		case CALL_METHOD:
			callIterate((CallMethodNode) node, spaces+" ");
			break;
		case RETURN:
			break;
		default:
			variablesCheck(node, spaces);
			break;
		}
	}
	
	private void varDeclarationCheck(VarDeclaration var, String spaces) {
		System.out.println(spaces+var.isFinal() + " " + var.getType() + " " + 
				var.getName());
	}
	
	private void assignmentCheck(AssignmentNode assignment, String spaces) throws HowDidYouGetHereException {
		System.out.println(spaces+assignment.getName());
		expressionIterate(assignment.getValue(), spaces+" ");
	}
	
	private void conditionalIterate(ConditionalNode conditional, String spaces) throws HowDidYouGetHereException {
		expressionIterate(conditional.getCondition(), spaces+" ");
		scopeIterate(conditional.getBody(), spaces+" ");
	}

	private void callIterate(CallMethodNode call, String spaces) throws HowDidYouGetHereException {
		System.out.println(spaces+call.getName());
		for (ExpressionNode arg : call.getArgs()) {
			expressionIterate(arg, spaces+" ");
		}	
	}

	private void expressionIterate(ExpressionNode expression, String spaces) throws HowDidYouGetHereException {
		System.out.println(spaces+expression);
		switch (expression.getNodeType()) {
		case LITERAL:
			literalCheck((LiteralExpressionNode) expression, spaces+" ");
			break;
		case VAR_VAL:
			varValueCheck((VarExpressionNode) expression, spaces+" ");
			break;
		case OR:
		case AND:
			binaryOperatorIterate((BinaryOpNode) expression, spaces+" ");
			break;
		default:
			throw new HowDidYouGetHereException();
		}		
	}
	
	private void literalCheck(LiteralExpressionNode literal, String spaces) {
		System.out.println(spaces+literal.getType() + " " + literal.getValue());
	}

	private void varValueCheck(VarExpressionNode varExpr, String spaces) {
		System.out.println(spaces+varExpr.getName());
	}
	
	private void binaryOperatorIterate(BinaryOpNode operator, String spaces) throws HowDidYouGetHereException {
		expressionIterate(operator.getLeft(), spaces+" ");
		expressionIterate(operator.getRight(), spaces+" ");
	}

}
