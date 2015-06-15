package parser;

import lexer.Token;
import lexer.Tokenizer;

import static lexer.Token.*;
import static lexer.Token.TokenType.CLOSE_BRACES;

public class Parser {
    public Parser(Tokenizer tokenizer)
    {
        this.tokenizer = new ComplexItrator<Token>(tokenizer);
    }

    public void parseGlobal() throws   NotAllowedInThisContext,
                                        OtherTokenTypeNeedHere
    {
        while (tokenizer.hasNext()) {
            Token tok = tokenizer.next();

            switch (tok.getType()) {
                case FUNCTION_DECLARE: {
                    parseFunctionDeclaration();
                }
                break;
                case VAR_DECELERATION: {
                    parseVariableDeceleration(tok.getData());
                }
                break;
                case FINAL:
                {
                    //TODO
                }
                break;
                case NEW_LINE:
                break;
                case EOF: {
                    return;
                }
                default: {
                    throw new NotAllowedInThisContext(tok);
                }
            }
        }
    }

    private void parseFunctionDeclaration() throws  NotAllowedInThisContext,
                                                    OtherTokenTypeNeedHere
    {
        Token tok = tokenizer.next();
        validateTokenType(tok, TokenType.IDENTITY);

        String methodName = tok.getData();
        parseDecelerationArgs();
        codeSegment();
    }

    private void validateTokenType(Token tok, TokenType requiredType) throws OtherTokenTypeNeedHere {
        if (tok.getType() != requiredType)
        {
            throw new OtherTokenTypeNeedHere(tok, requiredType);
        }
    }

    private void codeSegment() throws NotAllowedInThisContext, OtherTokenTypeNeedHere {
        validateNextTokenIs(TokenType.OPEN_BRACES);
        validateNextTokenIs(TokenType.NEW_LINE);

        Token tok = tokenizer.next();
        do
        {
            switch(tok.getType())
            {
                case IF:
                {
                    parseCondition();
                    codeSegment();
                }
                break;
                case WHILE_LOOP:
                {
                    parseCondition();
                    codeSegment();
                }
                break;
                case VAR_DECELERATION:
                {
                    parseVariableDeceleration(tok.getData());
                }
                break;
                case IDENTITY:
                {
                    Token identity = tok;
                    tok = tokenizer.next();
                    if (tok.getType() == TokenType.ASSIGNMENT_OP)
                    {
                        parseAssigment(identity.getData());
                    }
                    else if (tok.getType() == TokenType.OPEN_PARENTHESES)
                    {
                        parseCallMethod(identity.getData());
                    }
                    else
                    {
                        throw new NotAllowedInThisContext(tok);
                    }
                }
                break;
                case RETURN_OP:
                {
                    validateNextTokenIs(TokenType.END_OF_STATEMENT);
                    validateNextTokenIs(TokenType.NEW_LINE);
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
        }while (tok.getType() != CLOSE_BRACES);

        validateNextTokenIs(TokenType.NEW_LINE);
    }

    private void parseCallMethod(String data) throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        parseCallArguments();
        validateNextTokenIs(Token.TokenType.END_OF_STATEMENT);
        validateNextTokenIs(Token.TokenType.NEW_LINE);
    }

    private void parseCallArguments() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        Token tok = tokenizer.next();
       do
        {
            if (tok.getType() == Token.TokenType.COMMA)
            {
                tok = tokenizer.next();
            }
            switch(tok.getType())
            {
                case IDENTITY:
                {

                }
                break;
                case LITERAL_DOUBLE_NUMBER:
                case LITERAL_INT_NUMBER:
                case LITERAL_BOOLEAN:
                case LITERAL_STRING:
                {

                }break;
                case AND_OP:
                {
                    //TODO
                    tok = tokenizer.next();
                }
                break;
                case OR_OP:
                {
                    //TODO
                    tok = tokenizer.next();
                }
                break;
                default:
                {
                    throw new NotAllowedInThisContext(tok);
                }
            }
            tok = tokenizer.next();
        } while (tok.getType() != Token.TokenType.CLOSE_PARENTHESES);
    }

    private void parseSingleCondition() throws NotAllowedInThisContext, OtherTokenTypeNeedHere {
        Token tok = tokenizer.next();
        switch (tok.getType()) {
            case LITERAL_BOOLEAN: {

            }
            break;
            case LITERAL_INT_NUMBER: {

            }
            break;
            case LITERAL_DOUBLE_NUMBER: {

            }
            break;
            case IDENTITY: {
                if (tokenizer.getNext().getType() == Token.TokenType.OPEN_PARENTHESES)
                {
                    throw new NotAllowedInThisContext(tok);
                }
            }
            break;
            default: {
                throw new NotAllowedInThisContext(tok);
            }
        }

    }
    private void parseCondition() throws NotAllowedInThisContext, OtherTokenTypeNeedHere {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);
        Token tok;
        do
        {
            parseSingleCondition();
            tok = tokenizer.next();
        }while ((tok.getType() == TokenType.OR_OP) || (tok.getType() == TokenType.AND_OP));

        validateTokenType(tok, TokenType.CLOSE_PARENTHESES);
    }

    private void validateNextTokenIs(TokenType requiredType) throws OtherTokenTypeNeedHere {
        Token tok = tokenizer.next();
        if(tok.getType() != requiredType)
        {
            throw new OtherTokenTypeNeedHere(tok, requiredType);
        }
    }

    private void parseDecelerationArgs() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);

        Token tok = tokenizer.next();
        do {
            if(tok.getType() == TokenType.COMMA)
            {
                tok = tokenizer.next();
            }

            if (tok.getType() == TokenType.CLOSE_PARENTHESES)
            {
                break;
            }
            boolean isFinal = false;
            if (tok.getType() == TokenType.FINAL)
            {
                isFinal = true;
                tok = tokenizer.next();
            }

            validateTokenType(tok, TokenType.VAR_DECELERATION);
            Token type = tok;

            tok = tokenizer.next();
            validateTokenType(tok, TokenType.IDENTITY);

            tok = tokenizer.next();

        }while (tok.getType() == TokenType.COMMA);

        validateTokenType(tok, TokenType.CLOSE_PARENTHESES);
    }

    private void parseVariableDeceleration(String type) throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        Token tok = tokenizer.next();
        do {
            validateTokenType(tok, TokenType.IDENTITY);
            String varName = tok.getData();

            tok = tokenizer.next();
            switch (tok.getType())
            {
                case COMMA:
                {
                    break;
                }
                case ASSIGNMENT_OP:
                {
                    parseAssigment(varName);
                }
                default:
                {
                    //TODO:
                }
            }
            tok = tokenizer.next();
        }while(tok.getType() != TokenType.END_OF_STATEMENT);
        validateNextTokenIs(TokenType.NEW_LINE);
    }

    private void parseAssigment(String varName) throws NotAllowedInThisContext {
        Token tok = tokenizer.next();
        if(tok.getType() == TokenType.IDENTITY)
        {
            //TODO
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
                return true;
            default:
                return false;
        }
    }

    private ComplexItrator<Token> tokenizer;
}
