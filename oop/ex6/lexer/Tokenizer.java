package oop.ex6.lexer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The first part of the code analysis is the Lexical analysis.
 * in this process we convert to code to sequence of tokens, i.e. meaningful
 * strings. Each token is atom in the language syntax e.g. the key word "if",
 * open parentheses, identity, new line and so on.
 * For each token we save the string, the type, and the position.
 * The tokenizer is actually iterator of tokens.
 * <p/>
 * We implement a tokenizer based on the regex named capturing groups.
 */
public class Tokenizer implements Iterator<Token> {

    //save the pattern of optional regex group
    public static final String REGEX_OPTION_GROUP = "|(?<%s>%s)";
    //the error message in case that any token is not match
    public static final String NOT_MATCH_ANY_PATTERN_ERROR_MSG =
            "match data does not match to any token pattern";
    //the offset of the second char in the string
    public static final int SECOND_CHAR_OFFSET = 1;
    //save the last token
    private Token lastToken;
    //the regex object of the match
    private Matcher tokenMatcher;
    //the whole code file content
    private final String fileContent;

    /**
     * Ctor
     *
     * @param input - the code file content
     */
    public Tokenizer(String input) {
        Pattern tokenPattern = createTokensPattern();
        tokenMatcher = tokenPattern.matcher(input);
        fileContent = input;
    }

    /**
     * create a regex pattern that contain all the tokens patterns
     *
     * @return the regex pattern that contain all the token types patterns
     */
    private static Pattern createTokensPattern() {
        StringBuilder tokenPatternsString = new StringBuilder();
        for (Token.TokenType type : Token.TokenType.values()) {
            tokenPatternsString.append(String.format(REGEX_OPTION_GROUP,
                                                            type.getGroupName(), type.matchPattern()));
        }

        //in the first group the or is redundant so we start from the second char
        return Pattern.compile(tokenPatternsString.substring(SECOND_CHAR_OFFSET));
    }

    /**
     * convert a match of regex to Token object
     *
     * @param match - the regex match object
     * @return the corresponding token object
     */
    private Token createTokenFromMatch(Matcher match) {
        for (Token.TokenType type : Token.TokenType.values()) {
            String result = match.group(type.getGroupName());
            if (result != null) {
                return new Token(type, result,
                        Position.createFromOffset(match.start(), fileContent),
                        Position.createFromOffset(match.end() - 1, fileContent));
            }
        }

        throw new IllegalArgumentException(NOT_MATCH_ANY_PATTERN_ERROR_MSG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return (null == lastToken) || (lastToken.getType() != Token.TokenType.EOF);
    }

    /**
     * Return whether a token type is ignored by the iterator. Because not all
     * the tokens are relevant for the later processes, we ignore them in the
     * iterator.
     *
     * @param tokenType The type to check if ignored.
     * @return Whether ignore a token type by the iterator.
     */
    private boolean ifIgnore(Token.TokenType tokenType) {
        switch (tokenType) {
            case WHITE_SPACE:
            case COMMENT:
                return true;
            default:
                return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Token next() {
        if (!tokenMatcher.find()) {
            throw new NoSuchElementException();
        }

        lastToken = createTokenFromMatch(tokenMatcher);
        if (ifIgnore(lastToken.getType())) {
            return next();
        }

        return lastToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
