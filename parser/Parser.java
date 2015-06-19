package parser;

import ast.*;
import lexer.Position;
import lexer.Token;
import lexer.Tokenizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static lexer.Token.TokenType;

public class Parser {
    public Parser(Tokenizer tokenizer)
    {
        this.tokenizer = new ComplexItrator<Token>(tokenizer);
    }

    private void validateTokenType(Token tok, TokenType requiredType) throws OtherTokenTypeNeedHere {
        if (tok.getType() != requiredType)
        {
            throw new OtherTokenTypeNeedHere(tok, requiredType);
        }
    }

    private void validateNextTokenIs(TokenType requiredType) throws OtherTokenTypeNeedHere {
        Token tok = tokenizer.next();
        if(tok.getType() != requiredType)
        {
            throw new OtherTokenTypeNeedHere(tok, requiredType);
        }
    }
    public GlobalNode parseGlobal() throws NotAllowedInThisContext, OtherTokenTypeNeedHere, InvalidIdentityName
    {
        ScopeNode body = new ScopeNode(new Position(0,0));
        List<MethodNode> methods = new ArrayList<MethodNode>();
        while (tokenizer.hasNext()) {
            Token tok = tokenizer.next();
            switch (tok.getType()) {
                case FUNCTION_DECLARE: {
                    methods.add(parseFunctionDeclaration());
                }
                break;
                case VAR_TYPE: {
                    body.addAll(parseVariablesDeceleration(false));
                }
                break;
                case IDENTITY:
                {
                    //if the global call method is not allowed
                    body.add(parseAssignment());
                }
                break;
                case FINAL:
                {
                    body.addAll(parseVariablesDeceleration(true));
                }
                break;
                case NEW_LINE:
                    break;
                case EOF:
                    break;
                default: {
                    throw new NotAllowedInThisContext(tok);
                }
            }
        }
        return new GlobalNode(body, methods);
    }

    private MethodNode parseFunctionDeclaration() throws NotAllowedInThisContext,
            OtherTokenTypeNeedHere, InvalidIdentityName {
        validateTokenType(tokenizer.current(), Token.TokenType.FUNCTION_DECLARE);

        Token tok = tokenizer.next();
        validateTokenType(tok, TokenType.IDENTITY);

        String methodName = tok.getData();
        if (isValidateIdentityName(methodName))
        {
            throw new InvalidIdentityName(tok);
        }

        List<ArgumentNode> args = parseDecelerationArgs();
        ScopeNode body = codeSegment();
        return new MethodNode(tok.getStartPosition(), methodName,args, body);
    }


    private ScopeNode codeSegment() throws NotAllowedInThisContext,
            OtherTokenTypeNeedHere, InvalidIdentityName {
        validateNextTokenIs(TokenType.OPEN_BRACES);
        validateNextTokenIs(TokenType.NEW_LINE);

        Token tok = tokenizer.next();
        ScopeNode body = new ScopeNode(tok.getStartPosition());
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
                {
                    body.addAll(parseVariablesDeceleration(false));
                }
                break;
                case FINAL:
                    body.addAll(parseVariablesDeceleration(true));
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
                    throw new NotAllowedInThisContext(tok);
                }
            }

            tok = tokenizer.next();
        } while (tok.getType() != TokenType.CLOSE_BRACES);

        validateNextTokenIs(TokenType.NEW_LINE);
        return body;
    }

    private ReturnNode parseReturn() throws OtherTokenTypeNeedHere {
        validateTokenType(tokenizer.current(), TokenType.RETURN_OP);
        validateNextTokenIs(TokenType.END_OF_STATEMENT);
        validateNextTokenIs(TokenType.NEW_LINE);
        return new ReturnNode(tokenizer.current().getStartPosition());
    }

    private AstNode parseIdentity() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        validateTokenType(tokenizer.current(), TokenType.IDENTITY);

        switch (tokenizer.getNext().getType())
        {
            case OPEN_PARENTHESES:
            {
                return parseCallMethod();
            }
            case ASSIGNMENT_OP:
            {
                return parseAssignment();
            }
            default:
            {
                throw new NotAllowedInThisContext(tokenizer.getNext());
            }
        }
    }

    private WhileNode parseWhile() throws NotAllowedInThisContext, OtherTokenTypeNeedHere,
            InvalidIdentityName {
        validateTokenType(tokenizer.current(), TokenType.WHILE_LOOP);
        ExpressionNode condition = parseCondition();
        ScopeNode body = codeSegment();

        return new WhileNode(tokenizer.current().getStartPosition(), condition, body);
    }


    private IfNode parseIf() throws NotAllowedInThisContext, OtherTokenTypeNeedHere, InvalidIdentityName {
        validateTokenType(tokenizer.current(), TokenType.IF);
        ExpressionNode condition = parseCondition();
        ScopeNode body = codeSegment();

        return new IfNode(tokenizer.current().getStartPosition(), condition, body);
    }

    private CallMethodNode parseCallMethod() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        validateTokenType(tokenizer.current(), TokenType.IDENTITY);
        String methodName = tokenizer.current().getData();
        List<ExpressionNode> args = parseCallArguments();
        validateNextTokenIs(Token.TokenType.END_OF_STATEMENT);
        validateNextTokenIs(Token.TokenType.NEW_LINE);

        return new CallMethodNode(tokenizer.current().getStartPosition(),
                methodName, args);
    }

    private List<ExpressionNode> parseCallArguments() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);
        List<ExpressionNode> args = new ArrayList<ExpressionNode>();

        Token tok;
       do
        {
            tok = tokenizer.next();
            switch(tok.getType())
            {
                case CLOSE_PARENTHESES:
                {
                    continue;
                }
                case IDENTITY:
                {
                    if (tokenizer.getNext().getType() == Token.TokenType.OPEN_PARENTHESES)
                    {
                        throw new NotAllowedInThisContext(tok);
                    }
                    args.add(new VarExpressionNode(tok.getStartPosition(), tok.getData()));
                }
                break;
                case LITERAL_DOUBLE_NUMBER:
                {
                    args.add(new LiteralExpressionNode(tok.getStartPosition(), tok.getData(),
                            ExpressionNode.ExpressionType.DOUBLE));
                }
                break;
                case LITERAL_INT_NUMBER:
                {
                    args.add(new LiteralExpressionNode(tok.getStartPosition(), tok.getData(),
                            ExpressionNode.ExpressionType.INT));
                }
                break;
                case LITERAL_BOOLEAN:
                {
                    args.add(new LiteralExpressionNode(tok.getStartPosition(), tok.getData(),
                            ExpressionNode.ExpressionType.BOOLEAN));
                }
                break;
                case LITERAL_STRING:
                {
                    args.add(new LiteralExpressionNode(tok.getStartPosition(), tok.getData(),
                            ExpressionNode.ExpressionType.STRING));
                }
                break;
                case LITERAL_CHAR:
                {
                    args.add(new LiteralExpressionNode(tok.getStartPosition(), tok.getData(),
                            ExpressionNode.ExpressionType.CHAR));
                }
                break;
                default:
                {
                    throw new NotAllowedInThisContext(tok);
                }
            }
            tok = tokenizer.next();
        } while (tok.getType() == TokenType.COMMA);

        validateTokenType(tok,TokenType.CLOSE_PARENTHESES);
        return args;
    }

    private ExpressionNode parseSingleCondition() throws NotAllowedInThisContext, OtherTokenTypeNeedHere {
        Token tok = tokenizer.next();
        switch (tok.getType()) {
            case LITERAL_BOOLEAN: {
                return new LiteralExpressionNode(tok.getStartPosition(), tok.getData(),
                        ExpressionNode.ExpressionType.BOOLEAN);
            }
            case LITERAL_INT_NUMBER: {
                return new LiteralExpressionNode(tok.getStartPosition(), tok.getData(),
                        ExpressionNode.ExpressionType.INT);
            }
            case LITERAL_DOUBLE_NUMBER: {
                return new LiteralExpressionNode(tok.getStartPosition(), tok.getData(),
                        ExpressionNode.ExpressionType.DOUBLE);
            }
            case IDENTITY: {
                if (tokenizer.getNext().getType() == Token.TokenType.OPEN_PARENTHESES)
                {
                    throw new NotAllowedInThisContext(tok);
                }
                return new VarExpressionNode(tok.getStartPosition(), tok.getData());
            }
            default: {
                throw new NotAllowedInThisContext(tok);
            }
        }

    }
    private ExpressionNode parseCondition() throws NotAllowedInThisContext, OtherTokenTypeNeedHere {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);
        Token prevCondition;
        Token tok;
        do
        {
            parseSingleCondition();
            tok = tokenizer.next();
            //TODO : when create the tree this code have to be more complex
        }while ((tok.getType() == TokenType.OR_OP) || (tok.getType() == TokenType.AND_OP));

        validateTokenType(tok, TokenType.CLOSE_PARENTHESES);

        //TODO:
        return null;
    }

    private List<ArgumentNode> parseDecelerationArgs() throws OtherTokenTypeNeedHere, NotAllowedInThisContext, InvalidIdentityName {
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
            if(!isValidateIdentityName(name))
            {
                throw new InvalidIdentityName(tok);
            }

            args.add(new ArgumentNode(tok.getStartPosition(), type, tok.getData(), isFinal));

            tok = tokenizer.next();
        }while (tok.getType() == TokenType.COMMA);

        validateTokenType(tok, TokenType.CLOSE_PARENTHESES);
        return args;
    }

    private List<AstNode> parseVariableDeceleration() throws OtherTokenTypeNeedHere, NotAllowedInThisContext, InvalidIdentityName {
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

            if (isValidateIdentityName(varName))
            {
                throw new InvalidIdentityName(tok);
            }
            result.add(new VarDeclaration(tok.getStartPosition(), type,varName, isFinal));

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

    private void parseAssignment() throws NotAllowedInThisContext, OtherTokenTypeNeedHere {
        validateTokenType(tokenizer.current(), TokenType.IDENTITY);
        String varName = tokenizer.current().getData();

        validateNextTokenIs(TokenType.ASSIGNMENT_OP);

        Token tok = tokenizer.next();
        if(tok.getType() == TokenType.IDENTITY)
        {

        }
        else if (isLiteralData(tok))
        {

        }
        else
        {
            throw new NotAllowedInThisContext(tok);
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

    private boolean isValidateIdentityName(String identity)
    {
        if (    (!Pattern.matches("\\w+", identity))    ||
                (identity == "_")                       ||
                (Pattern.matches("\\d+\\w+", identity)))
        {
            return false;
        }

        return true;
    }

    private ComplexItrator<Token> tokenizer;
}
