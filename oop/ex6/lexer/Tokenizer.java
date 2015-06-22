package oop.ex6.lexer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * the first part of the code analysis is the Lexical analysis.
 * in this process we convert to code to sequence of tokens, i.e. meaningful strings.
 * each token is atom in the language syntax e.g. the key word "if", open parentheses,
 * identity, new line and so on.
 * for each token we save the string, the type, and the position.
 * the tokenizer is actually iterator of tokens.
 *
 * we implement a tokenizer based on the regex named capturing groups.
 */
public class Tokenizer implements Iterator<Token>{

    public static final String REGEX_OPTION_GROUP = "|(?<%s>%s)";
    public static final String NOT_MATCH_ANY_PATTERN_ERROR_MSG = "match data not match to any token pattern";
    //save the last token
    private Token lastToken;
    //the regex object of the match
    private Matcher tokenMatcher;
    //the whole code file content
    private final String fileContent;

    /**
     * Ctor
     * @param input - the code file content
     */
    public Tokenizer(String input)
    {
        Pattern tokenPattern = createTokensPattern();
        tokenMatcher = tokenPattern.matcher(input);
        fileContent = input;
    }

    /**
     * create a regex pattern that contain all the tokens patterns
     * @return the regex pattern that contain all the token types patterns
     */
    private static Pattern createTokensPattern()
    {
        StringBuilder tokenPatternsString = new StringBuilder();
        for(Token.TokenType type: Token.TokenType.values())
        {
            tokenPatternsString.append(String.format(REGEX_OPTION_GROUP,type.getGroupName(),
                                                                        type.matchPattern()));
        }

        //in the first group the or is redundant so we start from the second char
        return Pattern.compile(tokenPatternsString.substring(1));
    }

    /**
     * convert a match of regex to Token object
     * @param match - the regex match object
     * @return the corresponding token object
     */
    private Token createTokenFromMatch(Matcher match)
    {
        for (Token.TokenType type: Token.TokenType.values())
        {
            String result = match.group(type.getGroupName());
            if (result != null)
            {
                return new Token(type, result,
                                    Position.createFromOffset(match.start(), fileContent),
                                    Position.createFromOffset(match.end() -1, fileContent));
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
     * return if a token type is ignored by the iterator
     * because not all the token relevant for the later processes we ignore them in the iterator
     * @param tokenType - the type to check if ignore
     * @return if ignore a token type by the iterator
     */
    private boolean ifIgnore(Token.TokenType tokenType)
    {
        switch (tokenType)
        {
            case WHITE_SPACE:
            case COMMENT:
            {
                return true;
            }
            default:
            {
                return false;
            }
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
        if (ifIgnore(lastToken.getType()))
        {
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
