package oop.ex6.lexer;

import junit.framework.TestCase;

public class TokenizerTest extends TestCase {
    public void testSignleToken_returnMatchedSingleToken()
    {
        String s= "{";
        Tokenizer t = new Tokenizer(s);

        Token token = t.next();

        assertEquals(token.getType(), Token.TokenType.OPEN_BRACES);
        assertEquals(t.next(), null);
    }

    public void testTwoToken_TokenAfterToken()
    {
        String s= "{}";
        Tokenizer t = new Tokenizer(s);

        assertEquals(t.next().getType(), Token.TokenType.OPEN_BRACES);
        assertEquals(t.next().getType(), Token.TokenType.CLOSE_BRACES);

        assertEquals(t.next(), null);
    }

    public void testFindUnknown()
    {
        String s = "{בל{}";
        Tokenizer t = new Tokenizer(s);

        assertEquals(t.next().getType(), Token.TokenType.OPEN_BRACES);
        Token tt = t.next();
        assertEquals(tt.getType(), Token.TokenType.UNKNOWN);
    }

    public void testDoubleNumber()
    {
        String s = "22.3fasfdasd";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.LITERAL_DOUBLE_NUMBER);
        assertEquals(tok.getData(), "22.3");
    }

    public void testNegativeDoubleNumber()
    {
        String s = "-22.3fasfdasd";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.LITERAL_DOUBLE_NUMBER);
        assertEquals(tok.getData(), "-22.3");
    }

    public void testNumber()
    {
        String s = "22fasfdasd";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.LITERAL_INT_NUMBER);
        assertEquals(tok.getData(), "22");
    }

    public void testNegativeNumber()
    {
        String s = "-22fasfdasd";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.LITERAL_INT_NUMBER);
        assertEquals(tok.getData(), "-22");
    }

    public void testBooleanTrue()
    {

        String s = "true ";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.LITERAL_BOOLEAN);
        assertEquals(tok.getData(), "true");
    }

    public void testBooleanTrueEndOfString()
    {
        String s = "true";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.LITERAL_BOOLEAN);
        assertEquals(tok.getData(), "true");
    }

    public void testBooleanTrueInString()
    {
        String s = "trueasdfsfdsf";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

       // assertEquals(tok.getType(), Token.TokenType.IDENTITY);
    }


    public void testBooleanFalse()
    {
        String s = "false";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.LITERAL_BOOLEAN);
        assertEquals(tok.getData(), "false");
    }


    public void testSring()
    {
        String s = "\"blabla\"";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.LITERAL_STRING);
        assertEquals(tok.getData(), "\"blalba\"");
    }

    public void testComment()
    {
        String s="//blablabla\n";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.COMMENT);
        assertEquals(tok.getData(), "//blablabla\n");
    }


    public void testCommentOtherNewLine()
    {
        String s="//blablabla\r\n";
        Tokenizer t = new Tokenizer(s);

        Token tok = t.next();

        assertEquals(tok.getType(), Token.TokenType.COMMENT);
        assertEquals(tok.getData(), "//blablabla\r\n");
    }

    public void testGetTokenGroupName()
    {
        assertEquals("ENDOFSTATEMENT", Token.TokenType.END_OF_STATEMENT.getGroupName());
    }



}