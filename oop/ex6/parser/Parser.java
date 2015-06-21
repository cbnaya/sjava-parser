package oop.ex6.parser;

import oop.ex6.lexer.Token;
import oop.ex6.lexer.Tokenizer;
import oop.ex6.ast.*;
import oop.ex6.ast.ExpressionNode.ExpressionType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static oop.ex6.lexer.Token.TokenType;

public class Parser {
    public Parser(Tokenizer tokenizer)
    {
        this.tokenizer = new ComplexItrator<Token>(tokenizer);
    }

    private void validateTokenType(Token tok, TokenType requiredType) throws OtherTokenTypeNeedHereException {
        if (tok.getType() != requiredType)
        {
            throw new OtherTokenTypeNeedHereException(tok, requiredType);
        }
    }

    private void validateNextTokenIs(TokenType requiredType) throws OtherTokenTypeNeedHereException {
        Token tok = tokenizer.next();
        if(tok.getType() != requiredType)
        {
            throw new OtherTokenTypeNeedHereException(tok, requiredType);
        }
    }
    
    public GlobalNode parseGlobal() throws NotAllowedInThisContextException, OtherTokenTypeNeedHereException, InvalidIdentityNameException
    {
        List<AstNode> body = new LinkedList<AstNode>();
        List<MethodNode> methods = new ArrayList<MethodNode>();
        while (tokenizer.hasNext()) {
            Token tok = tokenizer.next();
            switch (tok.getType()) {
                case FUNCTION_DECLARE: {
                    methods.add(parseFunctionDeclaration());
                }
                break;
                case VAR_TYPE:
                case FINAL:
                {
                    body.addAll(parseVariablesDeceleration());
                }
                break;
                case IDENTITY:
                {
                    //if the global call method is not allowed
                    body.add(parseAssignment());
                    validateNextTokenIs(TokenType.END_OF_STATEMENT);
                    validateNextTokenIs(TokenType.NEW_LINE);
                }
                break;
                case NEW_LINE:
                    break;
                case EOF:
                    break;
                default: {
                    throw new NotAllowedInThisContextException(tok);
                }
            }
        }
        return new GlobalNode(body, methods);
    }

    private MethodNode parseFunctionDeclaration() throws NotAllowedInThisContextException,
            OtherTokenTypeNeedHereException, InvalidIdentityNameException {
        validateTokenType(tokenizer.current(), Token.TokenType.FUNCTION_DECLARE);

        Token tok = tokenizer.next();
        validateTokenType(tok, TokenType.IDENTITY);

        String methodName = tok.getData();
        if (!isValidIdentityName(methodName))
        {
            throw new InvalidIdentityNameException(tok);
        }

        List<ArgumentNode> args = parseDecelerationArgs();
        List<AstNode> body = codeSegment();
        return new MethodNode(tok.getStartPosition(), methodName,args, body);
    }


    private List<AstNode> codeSegment() throws NotAllowedInThisContextException,
            OtherTokenTypeNeedHereException, InvalidIdentityNameException {
        validateNextTokenIs(TokenType.OPEN_BRACES);
        validateNextTokenIs(TokenType.NEW_LINE);

        Token tok = tokenizer.next();
        List<AstNode> body = new LinkedList<AstNode>();
        do
        {
            switch(tok.getType())
            {
                case IF:
                {
                    body.add(parseIf());
                }
                break;
                case WHILE_LOOP:
                {
                    body.add(parseWhile());
                }
                break;
                case VAR_TYPE:
                case FINAL:
				{
                    body.addAll(parseVariablesDeceleration());
                }
                break;
                case IDENTITY:
                {
                    body.add(parseIdentity());
                }
                break;
                case RETURN_OP:
                {
                    body.add(parseReturn());
                }
                break;
                case CLOSE_BRACES:
                {
                    continue;
                }
                default:
                {
                    throw new NotAllowedInThisContextException(tok);
                }
            }

            tok = tokenizer.next();
        } while (tok.getType() != TokenType.CLOSE_BRACES);

        validateNextTokenIs(TokenType.NEW_LINE);
        return body;
    }

    private ReturnNode parseReturn() throws OtherTokenTypeNeedHereException {
        validateTokenType(tokenizer.current(), TokenType.RETURN_OP);
        validateNextTokenIs(TokenType.END_OF_STATEMENT);
        validateNextTokenIs(TokenType.NEW_LINE);
        return new ReturnNode(tokenizer.current().getStartPosition());
    }

    private AstNode parseIdentity() throws OtherTokenTypeNeedHereException, NotAllowedInThisContextException {
        validateTokenType(tokenizer.current(), TokenType.IDENTITY);

        switch (tokenizer.getNext().getType())
        {
            case OPEN_PARENTHESES:
            {
                return parseCallMethod();
            }
            case ASSIGNMENT_OP:
            {
                AssignmentNode assignmentNode =  parseAssignment();
                validateNextTokenIs(TokenType.END_OF_STATEMENT);
                validateNextTokenIs(TokenType.NEW_LINE);
                return assignmentNode;
            }
            default:
            {
                throw new NotAllowedInThisContextException(tokenizer.getNext());
            }
        }
    }

    private WhileNode parseWhile() throws NotAllowedInThisContextException, OtherTokenTypeNeedHereException,
            InvalidIdentityNameException {
        validateTokenType(tokenizer.current(), TokenType.WHILE_LOOP);
        ExpressionNode condition = parseCondition();
        List<AstNode> body = codeSegment();

        return new WhileNode(tokenizer.current().getStartPosition(), condition, body);
    }


    private IfNode parseIf() throws NotAllowedInThisContextException, OtherTokenTypeNeedHereException, InvalidIdentityNameException {
        validateTokenType(tokenizer.current(), TokenType.IF);
        ExpressionNode condition = parseCondition();
        List<AstNode> body = codeSegment();

        return new IfNode(tokenizer.current().getStartPosition(), condition, body);
    }

    private CallMethodNode parseCallMethod() throws OtherTokenTypeNeedHereException, NotAllowedInThisContextException {
        validateTokenType(tokenizer.current(), TokenType.IDENTITY);
        String methodName = tokenizer.current().getData();
        List<ExpressionNode> args = parseCallArguments();
        validateNextTokenIs(Token.TokenType.END_OF_STATEMENT);
        validateNextTokenIs(Token.TokenType.NEW_LINE);

        return new CallMethodNode(tokenizer.current().getStartPosition(),
                methodName, args);
    }

    private List<ExpressionNode> parseCallArguments() throws OtherTokenTypeNeedHereException, NotAllowedInThisContextException {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);
        List<ExpressionNode> args = new ArrayList<ExpressionNode>();

        Token tok;
       do
        {
            tok = tokenizer.next();
            if (tok.getType() == TokenType.CLOSE_PARENTHESES)
            {
                continue;
            }
            args.add(convertValueTokenToExpression(tok));

            tok = tokenizer.next();
        } while (tok.getType() == TokenType.COMMA);

        validateTokenType(tok,TokenType.CLOSE_PARENTHESES);
        return args;
    }

    private ExpressionNode parseSingleCondition() throws NotAllowedInThisContextException, OtherTokenTypeNeedHereException {
        Token tok = tokenizer.next();

        switch (tok.getType()) {
            case LITERAL_BOOLEAN:
            case LITERAL_INT_NUMBER:
            case LITERAL_DOUBLE_NUMBER:
            case IDENTITY: {
                return convertValueTokenToExpression(tok);
            }
            default: {
                throw new NotAllowedInThisContextException(tok);
            }
        }

    }
    
	private ExpressionNode parseCondition() throws NotAllowedInThisContextException, OtherTokenTypeNeedHereException {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);

        ExpressionNode condition = parseSingleCondition();

        Token tok = tokenizer.next();
        while (tok.getType() != TokenType.CLOSE_PARENTHESES) {
        	switch (tok.getType())
            {
                case OR_OP:
                {
                    condition = new OrNode(tok.getStartPosition(),condition, parseSingleCondition());
                }break;
                case AND_OP:
                {
                    condition = new AndNode(tok.getStartPosition(),condition, parseSingleCondition());
                }
                break;
                default:
                {
                    throw new NotAllowedInThisContextException(tok);
                }
            }
            tok = tokenizer.next();
        }
        return condition;
    }

	private List<ArgumentNode> parseDecelerationArgs() throws OtherTokenTypeNeedHereException, NotAllowedInThisContextException, InvalidIdentityNameException {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);
        List<ArgumentNode> args = new ArrayList<ArgumentNode>();
        Token tok;

        do
        {
            tok = tokenizer.next();
            if (tok.getType() == TokenType.CLOSE_PARENTHESES)
            {
                continue;
            }

            boolean isFinal = false;

            if (tok.getType() == TokenType.FINAL)
            {
                isFinal = true;
                tok = tokenizer.next();
            }

            validateTokenType(tok, TokenType.VAR_TYPE);
            String type = tok.getData();

            tok = tokenizer.next();
            validateTokenType(tok, TokenType.IDENTITY);
            String name = tok.getData();
            if(!isValidIdentityName(name))
            {
                throw new InvalidIdentityNameException(tok);
            }

            args.add(new ArgumentNode(tok.getStartPosition(), type, tok.getData(), isFinal));

            tok = tokenizer.next();
        }while (tok.getType() == TokenType.COMMA);

        validateTokenType(tok, TokenType.CLOSE_PARENTHESES);
        return args;
    }

    private List<AstNode> parseVariablesDeceleration() throws OtherTokenTypeNeedHereException, NotAllowedInThisContextException, InvalidIdentityNameException {
        boolean isFinal = false;
        List<AstNode> result = new LinkedList<AstNode>();

        if (tokenizer.current().getType() == TokenType.FINAL)
        {
            isFinal = true;
            tokenizer.next();
        }

        validateTokenType(tokenizer.current(), TokenType.VAR_TYPE);
        String type = tokenizer.current().getData();

        Token tok;
        do {
            tok =  tokenizer.next();
            validateTokenType(tok, TokenType.IDENTITY);
            String varName = tok.getData();

            if (!isValidIdentityName(varName))
            {
                throw new InvalidIdentityNameException(tok);
            }
            result.add(new VarDeclarationNode(tok.getStartPosition(), type,varName, isFinal));

            if (tokenizer.getNext().getType() == TokenType.ASSIGNMENT_OP)
            {
                   result.add(parseAssignment());
            }

            tok = tokenizer.next();
        }while(tok.getType() == TokenType.COMMA);

        validateTokenType(tok, TokenType.END_OF_STATEMENT);
        validateNextTokenIs(TokenType.NEW_LINE);

        return result;
    }
    
    private AssignmentNode parseAssignment() throws NotAllowedInThisContextException, OtherTokenTypeNeedHereException {
        Token targetTok = tokenizer.current();
    	validateTokenType(targetTok, TokenType.IDENTITY);

        validateNextTokenIs(TokenType.ASSIGNMENT_OP);

        Token valueTok = tokenizer.next();


        ExpressionNode valueExpr = convertValueTokenToExpression(valueTok);
        
        return new AssignmentNode(targetTok.getStartPosition(), targetTok.getData(), valueExpr);
    }

    private ExpressionNode convertValueTokenToExpression(Token valueTok) throws NotAllowedInThisContextException {
        if (valueTok.getType() == TokenType.IDENTITY) {
            if (tokenizer.getNext().getType() == Token.TokenType.OPEN_PARENTHESES)
            {
                throw new NotAllowedInThisContextException(valueTok);
            }

            return new VarExpressionNode(valueTok.getStartPosition(), valueTok.getData());
        } else if (isLiteralData(valueTok)) {
        	return new LiteralExpressionNode(valueTok.getStartPosition(), valueTok.getData(),
                                                convertTokenTypeToExpressionType(valueTok));
        } else {
            throw new NotAllowedInThisContextException(valueTok);
        }
    }

    private ExpressionType convertTokenTypeToExpressionType(Token tok) throws NotAllowedInThisContextException {
		switch (tok.getType()) 
		{
		case LITERAL_BOOLEAN:
			return ExpressionType.BOOLEAN;
        case LITERAL_DOUBLE_NUMBER:
        	return ExpressionType.DOUBLE;
        case LITERAL_INT_NUMBER:
        	return ExpressionType.INT;
        case LITERAL_STRING:
        	return ExpressionType.STRING;
        case LITERAL_CHAR:
        	return ExpressionType.CHAR;
    	default:
        	throw new NotAllowedInThisContextException(tok);
		}
	}

	private boolean isLiteralData(Token tok) {
        switch (tok.getType())
        {
            case LITERAL_BOOLEAN:
            case LITERAL_DOUBLE_NUMBER:
            case LITERAL_INT_NUMBER:
            case LITERAL_STRING:
            case LITERAL_CHAR:
                return true;
            default:
                return false;
        }
    }

    private boolean isValidIdentityName(String identity)
    {
    	return (Pattern.matches("\\w+", identity))&&
                (!identity.equals("_")) &&
                (!Pattern.matches("\\d+\\w+", identity));
    }
    
    private ComplexItrator<Token> tokenizer;
}
