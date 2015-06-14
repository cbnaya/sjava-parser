package lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cbnaya on 12/06/2015.
 */
public class Tokenizer{

    private final int inputSize;
    private Matcher tokenMatcher;

    public Tokenizer(String input)
    {
        Pattern tokenPattern = createTokensPattern();
        tokenMatcher = tokenPattern.matcher(input);
        inputSize = input.length();
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
                return new Token(type, result);
            }
        }

        throw new IllegalArgumentException("TODO");
    }

    public Token next() {
        if (!tokenMatcher.find()) {
            return null;
        }

        return createTokenFromMatch(tokenMatcher);
    }
}
