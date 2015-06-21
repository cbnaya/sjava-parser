package oop.ex6.lexer;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cbnaya on 12/06/2015.
 */
public class Tokenizer implements Iterator<Token>{

    private Token lastToken;
    private Matcher tokenMatcher;
    private String fileContent;

    public Tokenizer(String input)
    {
        Pattern tokenPattern = createTokensPattern();
        tokenMatcher = tokenPattern.matcher(input);
        fileContent = input;
    }

    private static Pattern createTokensPattern()
    {
        StringBuffer tokenPatternsString = new StringBuffer();
        for(Token.TokenType type: Token.TokenType.values())
        {
            tokenPatternsString.append(String.format("|(?<%s>%s)",type.getGroupName(), type.matchPattern()));
        }

        return Pattern.compile(tokenPatternsString.substring(1));
    }

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

        throw new IllegalArgumentException("TODO");
    }

    @Override
    public boolean hasNext() {
        if (null == lastToken)
        {
            return true;
        }
        return lastToken.getType() != Token.TokenType.EOF;
    }

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

    @Override
    public void remove() {
        throw new NotImplementedException();
    }
}
