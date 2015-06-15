package parser;

import lexer.Token;
import lexer.Tokenizer;
import static lexer.Token.*;

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
                case VAR_TYPE: {
                    parseVariableDeceleration();
                }
                break;
                case IDENTITY:
                {
                    //if the global call method is not allowed
                    parseAssignment();
                }
                case FINAL:
                {
                    //TODO:
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
        validateTokenType(tokenizer.current(), Token.TokenType.FUNCTION_DECLARE);

        Token tok = tokenizer.next();
        validateTokenType(tok, TokenType.IDENTITY);

        String methodName = tok.getData();
        parseDecelerationArgs();
        codeSegment();
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
                    parseIf();
                }
                break;
                case WHILE_LOOP:
                {
                    parseWhile();
                }
                break;
                case VAR_TYPE:
                {
                    parseVariableDeceleration();
                }
                break;
                case IDENTITY:
                {
                    parseIdentity();
                }
                break;
                case RETURN_OP:
                {
                    parseReturn();
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
        }while (tok.getType() != TokenType.CLOSE_BRACES);

        validateNextTokenIs(TokenType.NEW_LINE);
    }

    private void parseReturn() throws OtherTokenTypeNeedHere {
        validateTokenType(tokenizer.current(), TokenType.RETURN_OP);
        validateNextTokenIs(TokenType.END_OF_STATEMENT);
        validateNextTokenIs(TokenType.NEW_LINE);
    }

    private void parseIdentity() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        validateTokenType(tokenizer.current(), TokenType.IDENTITY);

        switch (tokenizer.getNext().getType())
        {
            case OPEN_PARENTHESES:
            {
                parseCallMethod();
            }
            break;
            case ASSIGNMENT_OP:
            {
                parseAssignment();
            }
            break;
            default:
            {
                throw new NotAllowedInThisContext(tokenizer.getNext());
            }
        }
    }

    private void parseWhile() throws NotAllowedInThisContext, OtherTokenTypeNeedHere {
        validateTokenType(tokenizer.current(), TokenType.WHILE_LOOP);
        parseCondition();
        codeSegment();
    }

    private void parseIf() throws NotAllowedInThisContext, OtherTokenTypeNeedHere {
        validateTokenType(tokenizer.current(), TokenType.IF);
        parseCondition();
        codeSegment();
    }

    private void parseCallMethod() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        validateTokenType(tokenizer.current(), TokenType.IDENTITY);
        String methodName = tokenizer.current().getData();
        parseCallArguments();
        validateNextTokenIs(Token.TokenType.END_OF_STATEMENT);
        validateNextTokenIs(Token.TokenType.NEW_LINE);
    }

    private void parseCallArguments() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);
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

                }
                break;
                case LITERAL_DOUBLE_NUMBER:
                case LITERAL_INT_NUMBER:
                case LITERAL_BOOLEAN:
                case LITERAL_STRING:
                {

                }break;
                default:
                {
                    throw new NotAllowedInThisContext(tok);
                }
            }
            tok = tokenizer.next();
        } while (tok.getType() == TokenType.COMMA);

        validateTokenType(tok,TokenType.CLOSE_PARENTHESES);
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
        Token prevCondition;
        Token tok;
        do
        {
            parseSingleCondition();
            tok = tokenizer.next();
            //TODO : when create the tree this code have to be more complex
        }while ((tok.getType() == TokenType.OR_OP) || (tok.getType() == TokenType.AND_OP));

        validateTokenType(tok, TokenType.CLOSE_PARENTHESES);
    }

    private void parseDecelerationArgs() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);
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

            tok = tokenizer.next();
        }while (tok.getType() == TokenType.COMMA);

        validateTokenType(tok, TokenType.CLOSE_PARENTHESES);
    }

    private void parseVariableDeceleration() throws OtherTokenTypeNeedHere, NotAllowedInThisContext {
        validateTokenType(tokenizer.current(), TokenType.VAR_TYPE);
        String Type = tokenizer.current().getData();

        Token tok;
        do {
            tok =  tokenizer.next();
            validateTokenType(tok, TokenType.IDENTITY);
            String varName = tok.getData();

            if (tokenizer.getNext().getType() == TokenType.ASSIGNMENT_OP)
            {
                   parseAssignment();
            }

            tok = tokenizer.next();
        }while(tok.getType() == TokenType.COMMA);

        validateTokenType(tok, TokenType.END_OF_STATEMENT);
        validateNextTokenIs(TokenType.NEW_LINE);
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
                return true;
            default:
                return false;
        }
    }

    private ComplexItrator<Token> tokenizer;
}
