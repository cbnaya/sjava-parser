package oop.ex6.parser;

import oop.ex6.lexer.Position;
import oop.ex6.lexer.Token;
import oop.ex6.lexer.Tokenizer;
import oop.ex6.ast.*;
import oop.ex6.ast.ExpressionNode.ExpressionType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static oop.ex6.lexer.Token.TokenType;

/**
 * the second part of the code analysis is build an AST from the sequence of tokens.
 * AST is a tree representation of the abstract syntactic structure of source code bijectively.
 * each code could have only one way to be interpreted as a tree and vice versa.
 * if the build of the AST success it is ensure that the code syntax structure is valid, i.e. the
 * while statement is ok, all the brackets is ok and so on, but it is not enough to ensure that the
 * code is ok, the types is not checked duplicate declaration is not checked and so on.
 * <p/>
 * this parser get Tokenizer and create GlobalNode the root node of the ast.
 */
public class Parser {

    public static final String VALID_IDENTITY_CHARS_REGEX_PATTERN = "\\w+";
    public static final String UNDERSCORE_STRING = "_";
    public static final String START_WITH_NUMBER_REGEX_PATTERN = "\\d+\\w+";
    public static final String START_WITH_UNDERSCORE_REGEX_PATTERN = "[^_]+.*";
    //save the tokenizer
    private ComplexIterator<Token> tokenizer;

    /**
     * ctor
     *
     * @param tokenizer - the iterator of all the tokens of the code
     */
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = new ComplexIterator<Token>(tokenizer);
    }

    /**
     * validate the type of given token
     *
     * @param tok          - the token to check
     * @param requiredType - the required type
     * @throws OtherTokenTypeExpectedHereException - if the token type is not match the required type
     */
    private void validateTokenType(Token tok, TokenType requiredType) throws OtherTokenTypeExpectedHereException {
        if (tok.getType() != requiredType) {
            throw new OtherTokenTypeExpectedHereException(tok, requiredType);
        }
    }

    /**
     * validate the type of next token
     * this function move the tokenizer forward
     *
     * @param requiredType - the required type
     * @throws OtherTokenTypeExpectedHereException - if the next token is not match the required type
     */
    private void validateNextTokenIs(TokenType requiredType) throws OtherTokenTypeExpectedHereException {
        Token tok = tokenizer.next();
        validateTokenType(tok, requiredType);
    }

    /**
     * validate that this is ond of statement
     * this function have to be called when the next token is END_OF_STATEMENT
     * after the function the iterator will point on the new line
     *
     * @throws OtherTokenTypeExpectedHereException - if the next tokens is not END_OF_STATEMENT and NEW_LINE
     */
    private void validEndOfStatement() throws OtherTokenTypeExpectedHereException {
        validateNextTokenIs(TokenType.END_OF_STATEMENT);
        validateNextTokenIs(TokenType.NEW_LINE);
    }

    /**
     * parse the global scope of the code - its include all the global code and all the methods
     *
     * @return GlobalNode - the root node of the ast (contains to global code and methods)
     * @throws ParsingFailedException - if the parse will failed
     */
    public GlobalNode parseGlobal() throws ParsingFailedException {
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
                case FINAL: {
                    body.addAll(parseVariablesDeceleration());
                }
                break;
                case IDENTITY: {
                    //if the global call method is not allowed so it must be assignment
                    body.add(parseAssignment());
                    validEndOfStatement();
                }
                break;
                case NEW_LINE:
                case EOF:
                    break;
                default: {
                    throw new NotAllowedInThisContextException(tok);
                }
            }
        }
        return new GlobalNode(body, methods);
    }

    /**
     * parse function deceleration (include the function code)
     * this function have to be called when the iterator point of FUNCTION_DECLARE token
     * in the end of this function the tokenizer will point on the end of the method
     *
     * @return MethodNode - an AST Node that represent method
     * @throws ParsingFailedException - if the parsing failed
     */
    private MethodNode parseFunctionDeclaration() throws ParsingFailedException {
        validateTokenType(tokenizer.current(), Token.TokenType.FUNCTION_DECLARE);

        Position functionDeclarationPosition = tokenizer.current().getStartPosition();

        Token tok = tokenizer.next();
        validateTokenType(tok, TokenType.IDENTITY);

        String methodName = tok.getData();
        if (!isValidFunctionName(methodName)) {
            throw new InvalidIdentityNameException(tok);
        }

        List<ArgumentNode> args = parseDecelerationArgs();
        List<AstNode> body = codeSegment();
        return new MethodNode(functionDeclarationPosition, methodName, args, body);
    }

    /**
     * parsing code segment (method, while or if)
     * this function have to be called when the next token of the iterator is OPEN_BRACES token
     * after this function the iterator will point on the new line after the code segment
     *
     * @return List<AstNode>
     * @throws ParsingFailedException - if the parse failed
     */
    private List<AstNode> codeSegment() throws ParsingFailedException {
        validateNextTokenIs(TokenType.OPEN_BRACES);
        validateNextTokenIs(TokenType.NEW_LINE);

        Token tok = tokenizer.next();
        List<AstNode> body = new LinkedList<AstNode>();
        do {
            switch (tok.getType()) {
                case IF: {
                    body.add(parseIf());
                }
                break;
                case WHILE_LOOP: {
                    body.add(parseWhile());
                }
                break;
                case VAR_TYPE:
                case FINAL: {
                    body.addAll(parseVariablesDeceleration());
                }
                break;
                case IDENTITY: {
                    body.add(parseIdentity());
                }
                break;
                case RETURN_OP: {
                    body.add(parseReturn());
                }
                break;
                case NEW_LINE:
                    break;
                case CLOSE_BRACES: {
                    continue;
                }
                default: {
                    throw new NotAllowedInThisContextException(tok);
                }
            }

            tok = tokenizer.next();
        } while (tok.getType() != TokenType.CLOSE_BRACES);

        validateNextTokenIs(TokenType.NEW_LINE);
        return body;
    }

    /**
     * parse the return statement
     * this function have to call when the iterator point on RETURN_OP token
     * after this function the iterator will point on the new line after the return
     *
     * @return ReturnNode - the return AST node
     * @throws OtherTokenTypeExpectedHereException - if the current token is not return
     */
    private ReturnNode parseReturn() throws OtherTokenTypeExpectedHereException {
        validateTokenType(tokenizer.current(), TokenType.RETURN_OP);
        Position returnPosition = tokenizer.current().getStartPosition();

        validEndOfStatement();

        return new ReturnNode(returnPosition);
    }

    /**
     * parse identities (names) e.g. var name of function name
     * identity without context can be only call for function or assignment
     * this function have to call when the iterator point on IDENTITY token
     * after this function the iterator will point on the new line after
     *
     * @return the relevant AST node
     * @throws ParsingFailedException - if the parse failed
     */
    private AstNode parseIdentity() throws ParsingFailedException {
        validateTokenType(tokenizer.current(), TokenType.IDENTITY);

        switch (tokenizer.getNext().getType()) {
            case OPEN_PARENTHESES: {
                return parseCallMethod();
            }
            case ASSIGNMENT_OP: {
                AssignmentNode assignmentNode = parseAssignment();
                validEndOfStatement();
                return assignmentNode;
            }
            default: {
                throw new NotAllowedInThisContextException(tokenizer.getNext());
            }
        }
    }

    /**
     * parse a while loop
     * this function have to call when the iterator point on WHILE_LOOP
     * after this function the iterator will point on the new line after the while loop
     *
     * @return WhileNode an AST node that represent while loop
     * @throws ParsingFailedException - in the parsing is failed
     */
    private WhileNode parseWhile() throws ParsingFailedException {
        validateTokenType(tokenizer.current(), TokenType.WHILE_LOOP);
        Position whilePosition = tokenizer.current().getStartPosition();


        ExpressionNode condition = parseCondition();
        List<AstNode> body = codeSegment();

        return new WhileNode(whilePosition, condition, body);
    }


    /**
     * parse an if
     * this function have to call when the iterator point on IF
     * after this function the iterator will point on the new line after the if scope
     *
     * @return IfNode an AST node that represent if
     * @throws ParsingFailedException - in the parsing is failed
     */
    private IfNode parseIf() throws ParsingFailedException {
        validateTokenType(tokenizer.current(), TokenType.IF);
        Position ifPosition = tokenizer.current().getStartPosition();

        ExpressionNode condition = parseCondition();
        List<AstNode> body = codeSegment();

        return new IfNode(ifPosition, condition, body);
    }

    /**
     * parse call to a method
     * this function have to call when the iterator is point on the function identity (IDENTITY)
     * after this function the iterator will point on the new line after the call
     *
     * @return CallMethodNode - an AST node that represent call to a method
     * @throws ParsingFailedException - if the parse failed
     */
    private CallMethodNode parseCallMethod() throws ParsingFailedException {
        validateTokenType(tokenizer.current(), TokenType.IDENTITY);
        Position callMethodPosition = tokenizer.current().getStartPosition();

        String methodName = tokenizer.current().getData();
        List<ExpressionNode> args = parseCallArguments();
        validEndOfStatement();

        return new CallMethodNode(callMethodPosition, methodName, args);
    }

    /**
     * parse an arguments value
     * this function have to call when the next token is OPEN_PARENTHESES
     * after this function the iterator will point on CLOSE_PARENTHESES
     *
     * @return List<ExpressionNode> - list of expression nodes
     * @throws ParsingFailedException - if the parse failed
     */
    private List<ExpressionNode> parseCallArguments() throws ParsingFailedException {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);
        List<ExpressionNode> args = new ArrayList<ExpressionNode>();

        Token tok;
        do {
            tok = tokenizer.next();
            if (tok.getType() == TokenType.CLOSE_PARENTHESES) {
                continue;
            }
            args.add(convertValueTokenToExpression(tok));

            tok = tokenizer.next();
        } while (tok.getType() == TokenType.COMMA);

        validateTokenType(tok, TokenType.CLOSE_PARENTHESES);
        return args;
    }

    /**
     * parse single condition (without binary operators)
     *
     * @return ExpressionNode - an AST node that represent expression
     * @throws NotAllowedInThisContextException - if the condition is not expression of the correct type
     */
    private ExpressionNode parseSingleCondition() throws NotAllowedInThisContextException {
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

    /**
     * parse condition  (relevant to if and while)
     * this function have to call when the next token is OPEN_PARENTHESES
     * after this function the iterator will point on CLOSE_PARENTHESES
     *
     * @return ExpressionNode - an AST node that represent expression
     * @throws ParsingFailedException - if the parsing is failed
     */
    private ExpressionNode parseCondition() throws ParsingFailedException {
        validateNextTokenIs(TokenType.OPEN_PARENTHESES);

        ExpressionNode condition = parseSingleCondition();

        Token tok = tokenizer.next();
        while (tok.getType() != TokenType.CLOSE_PARENTHESES) {
            switch (tok.getType()) {
                case OR_OP: {
                    condition = new OrNode(tok.getStartPosition(), condition, parseSingleCondition());
                }
                break;
                case AND_OP: {
                    condition = new AndNode(tok.getStartPosition(), condition, parseSingleCondition());
                }
                break;
                default: {
                    throw new NotAllowedInThisContextException(tok);
                }
            }
            tok = tokenizer.next();
        }
        return condition;
    }

    /**
     * parse declaration of function argument
     * this function have to call when the next token is OPEN_PARENTHESES
     * after this function the iterator will point on CLOSE_PARENTHESES
     *
     * @return list of argument nodes
     * @throws ParsingFailedException - if the parsing is failed
     */
    private List<ArgumentNode> parseDecelerationArgs() throws ParsingFailedException {
        List<ArgumentNode> args = new ArrayList<ArgumentNode>();
        Token tok;

        validateNextTokenIs(TokenType.OPEN_PARENTHESES);

        do {
            tok = tokenizer.next();
            if (tok.getType() == TokenType.CLOSE_PARENTHESES) {
                continue;
            }

            boolean isFinal = isCurrentTokenIsFinal();

            validateTokenType(tok, TokenType.VAR_TYPE);
            String type = tok.getData();

            tok = tokenizer.next();
            validateTokenType(tok, TokenType.IDENTITY);
            String name = tok.getData();
            if (!isValidIdentityName(name)) {
                throw new InvalidIdentityNameException(tok);
            }

            args.add(new ArgumentNode(tok.getStartPosition(), type, tok.getData(), isFinal));

            tok = tokenizer.next();
        } while (tok.getType() == TokenType.COMMA);

        validateTokenType(tok, TokenType.CLOSE_PARENTHESES);

        return args;
    }

    /**
     * parse variable declaration
     * this function have to call when the next token is VAR_TYPE or FINAL
     * after this function the iterator will point on the new line
     *
     * @return list of AST node that contains var declarations and assignment
     * @throws ParsingFailedException - if the parsing failed
     */
    private List<AstNode> parseVariablesDeceleration() throws ParsingFailedException {
        List<AstNode> result = new LinkedList<AstNode>();

        boolean isFinal = isCurrentTokenIsFinal();

        validateTokenType(tokenizer.current(), TokenType.VAR_TYPE);
        String type = tokenizer.current().getData();

        Token tok;
        do {
            tok = tokenizer.next();
            validateTokenType(tok, TokenType.IDENTITY);
            String varName = tok.getData();

            if (!isValidIdentityName(varName)) {
                throw new InvalidIdentityNameException(tok);
            }
            result.add(new VarDeclarationNode(tok.getStartPosition(), type, varName, isFinal));

            if (isFinal || tokenizer.getNext().getType() == TokenType.ASSIGNMENT_OP) {
                result.add(parseAssignment());
            }

            tok = tokenizer.next();
        } while (tok.getType() == TokenType.COMMA);

        validEndOfStatement();
        return result;
    }

    /**
     * parse assignment
     * this function have to call when the current token is IDENTITY
     * after this function the iterator will point on value token
     *
     * @return AssignmentNode - an AST node that represent assignment
     * @throws ParsingFailedException - if the parse failed
     */
    private AssignmentNode parseAssignment() throws ParsingFailedException {
        Token targetTok = tokenizer.current();
        validateTokenType(targetTok, TokenType.IDENTITY);

        validateNextTokenIs(TokenType.ASSIGNMENT_OP);

        Token valueTok = tokenizer.next();


        ExpressionNode valueExpr = convertValueTokenToExpression(valueTok);

        return new AssignmentNode(targetTok.getStartPosition(), targetTok.getData(), valueExpr);
    }

    /**
     * covert a token that contains value (e.g. literal or variable)
     *
     * @param valueTok - the token to convert
     * @return - ExpressionNode - an AST node that represent expression
     * @throws NotAllowedInThisContextException - if this token is not variable or literal
     */
    private ExpressionNode convertValueTokenToExpression(Token valueTok) throws NotAllowedInThisContextException {

        if (valueTok.getType() == TokenType.IDENTITY) {
            if (tokenizer.getNext().getType() == Token.TokenType.OPEN_PARENTHESES) {
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

    /**
     * covert the type of literal value token to ExpressionType - enum of the AST of the variable types
     *
     * @param tok - the literal value token
     * @return ExpressionType - enum of the variable types
     * @throws NotAllowedInThisContextException - if the token is not token of literal value
     */
    private static ExpressionType convertTokenTypeToExpressionType(Token tok) throws
            NotAllowedInThisContextException {
        switch (tok.getType()) {
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

    /**
     * return if token is token of literal value
     *
     * @param tok - the token to check
     * @return if token is token of literal value
     */
    private static boolean isLiteralData(Token tok) {
        switch (tok.getType()) {
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

    /**
     * check if a string is valid as variable name
     *
     * @param identity - the string to check
     * @return if this string is valid variable name
     */
    private static boolean isValidIdentityName(String identity) {
        return (Pattern.matches(VALID_IDENTITY_CHARS_REGEX_PATTERN, identity)) &&
                (!identity.equals(UNDERSCORE_STRING)) &&
                (!Pattern.matches(START_WITH_NUMBER_REGEX_PATTERN, identity));
    }

    /**
     * check if a string is valid as function name
     *
     * @param methodName - the string to check
     * @return if this string is valid function name
     */

    private boolean isValidFunctionName(String methodName) {
        return (isValidIdentityName(methodName) &&
                (Pattern.matches(START_WITH_UNDERSCORE_REGEX_PATTERN, methodName)));
    }

    /**
     * check if the current token is FINAL token
     * if the current token is final the function move the iterator forward
     *
     * @return if the current token is FINAL token
     */
    private boolean isCurrentTokenIsFinal() {
        if (tokenizer.current().getType() == TokenType.FINAL) {
            tokenizer.next();
            return true;
        }
        return false;
    }
}
