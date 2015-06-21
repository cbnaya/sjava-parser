package oop.ex6.lexer;

import java.util.regex.Pattern;

/**
 * this class represent a token in the s-java language
 */
public class Token {

    /**
     * enum of all the tokens types, for each token save the match regex
     */
    public static enum TokenType {
        COMMENT("//.*?\r?\n"),
        END_OF_STATEMENT(";"),
        NEW_LINE("\r?\n"),
        OPEN_PARENTHESES("\\("),
        CLOSE_PARENTHESES("\\)"),
        OPEN_BRACES("\\{"),
        CLOSE_BRACES("\\}"),
        WHILE_LOOP("while\\b"),
        IF("if\\b"),
        FUNCTION_DECLARE("void\\b"),
        ASSIGNMENT_OP("="),
        AND_OP("\\&\\&"),
        OR_OP("\\|\\|"),
        RETURN_OP("return\\b"),
        COMMA(","),
        FINAL("final\\b"),
        VAR_TYPE("(boolean\\b)|(char\\b)|(String\\b)|(int\\b)|double\\b"),
        LITERAL_DOUBLE_NUMBER("-?\\d+\\.\\d+"),
        LITERAL_INT_NUMBER("-?\\d+"),
        LITERAL_BOOLEAN("(true\\b)|(false\\b)"),
        LITERAL_STRING("\"(.*?)\""),
        LITERAL_CHAR("\'(.)\'"),
        WHITE_SPACE("[ \t]+"),
        IDENTITY("\\w+"),
        EOF("\\z"),
        UNKNOWN(".+?");

        /**
         * Ctor
         * @param pattern - the regex pattern
         */
        private TokenType(String pattern) {
            this.pattern = pattern;
        }

        /**
         * return the pattern of the token type
         * @return the pattern of the token type
         */
        public final String matchPattern()
        {
            return pattern;
        }

        /**
         * return legal group name
         *
         * @return legal group name
         */
        public final String getGroupName() {
            return Pattern.compile(ILLEGAL_CHARS_PATTERN).matcher(name()).replaceAll("");
        }

        //pattern of illegal chars in the group name
        public static final String ILLEGAL_CHARS_PATTERN = "[^a-zA-Z0-9]+";

        //save the pattern
        private final String pattern;
        }

    Token(TokenType tokenType, String tokenData, Position startPosition, Position endPosition)
    {
        type = tokenType;
        data = tokenData;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * return the start position of the token
     * @return the start position of the token
     */
    public Position getStartPosition() {
        return startPosition;
    }

    /**
     * return the end position of the token
     * @return the end position of the token
     */
    public Position getEndPosition() {
        return endPosition;
    }

    /**
     * return the type of the token
     * @return the type of the token
     */
    public TokenType getType() {
        return type;
    }

    /**
     * return the data of the token
     * @return the data of the token
     */
    public String getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return String.format(TO_STRING_PATTERN, type.name(), data,
                                            startPosition.toString(), endPosition.toString());
    }

    //save the pattern of the toString format
    public static final String TO_STRING_PATTERN = "%s : %s  (%s - %s)";

    //save the token type
    private TokenType type;
    //save the token data
    private String data;
    //save the start position
    private Position startPosition;
    //save the end position
    private Position endPosition;
}
