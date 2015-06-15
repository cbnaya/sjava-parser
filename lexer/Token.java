package lexer;

import java.util.regex.Pattern;

public class Token {
    public static enum TokenType {
        COMMENT("//"),
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
        FINAL("final\b"),
        //DOUBLE_TYPE("double\\b"),
        //INT_TYPE("int\\b"),
        VAR_DECELERATION("(boolean\\b)|(String\\b)|(int\\b)|double\\b"),
        //BOOLEAN_TYPE("boolean\\b"),
        //STRING_TYPE("String\\b"),
        LITERAL_DOUBLE_NUMBER("-?\\d+\\.\\d+"),
        LITERAL_INT_NUMBER("-?\\d+"),
        LITERAL_BOOLEAN("(true\\b)|(false\\b)"),
        LITERAL_STRING("\"(.*?)\""),
        WHITE_SPACE("[ \t]+"),
        IDENTITY("\\w+"),
        EOF("\\z"),
        UNKNOWN(".+?");

        private TokenType(String pattern) {
            this.pattern = pattern;
        }

        public final String matchPattern()
        {
            return pattern;
        }
        public final String getGroupName() {
            return Pattern.compile("[^a-zA-Z0-9]+").matcher(name()).replaceAll("");
        }

        private final String pattern;
        }

    Token(TokenType tokenType, String tokenData, Position startPosition, Position endPosition)
    {
        type = tokenType;
        data = tokenData;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public TokenType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public String toString()
    {
        return String.format("%s : %s  (%s - %s)", type.name(), data,
                                            startPosition.toString(), endPosition.toString());
    }

    private TokenType type;
    private String data;
    private Position startPosition;
    private Position endPosition;
}
