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
        WHILE_LOOP("while"),
        IF("if"),
        FUNCTION_DECLARE("void"),
        DOUBLE_TYPE("double"),
        IT_TYPE("int"),
        BOOLEAN_TYPE("boolean"),
        STRING_TYPE("String"),
        LITERAL_DOUBLE_NUMBER("-?\\d+\\.\\d+"),
        LITERAL_INT_NUMBER("-?\\d+"),
        LITERAL_BOOLEAN("(true\\b)|(false\\b)"),
        LITERAL_STRING("\"(.*?)\""),
        WHITE_SPACE("[ \t]+"),
        IDENTITY("\\w+"),
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

    Token(TokenType tokenType, String tokenData)
    {
        type = tokenType;
        data = tokenData;
    }

    public TokenType getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    private TokenType type;
    private String data;
}
