package oop.ex6.lexer;

import junit.framework.TestCase;

public class PositionTest extends TestCase {
    public void testSingleLineFindLinePosition()
    {
        String s = "bbbbbbsgsdgsdfg";
        Position p = Position.createFromOffset(5, s);

        assertEquals(p.getLine(),1);
        assertEquals(p.getColumn(),6);
    }

    public void testMultiLineFindLinePosition()
    {
        String s = "bbbbbb\nsgsdgsdfg";
        Position p = Position.createFromOffset(9, s);

        assertEquals(p.getLine(),2);
        assertEquals(p.getColumn(),3);
    }

    public void testOneBase()
    {
        String s= "bbbbbb\nsgsdgsdfg";
        Position p = Position.createFromOffset(0, s);

        assertEquals(p.getLine(),1);
        assertEquals(p.getColumn(),1);

    }

    public void testPositionOfNewLine()
    {
        String s= "bbbbbb\nsgsdgsdfg";
        Position p = Position.createFromOffset(6, s);

        assertEquals(p.getLine(),1);
        assertEquals(p.getColumn(),7);
    }

    public void testFirstCharAtLine()
    {
        String s= "bbbbbb\nsgsdgsdfg";
        Position p = Position.createFromOffset(7, s);

        assertEquals(p.getLine(),2);
        assertEquals(p.getColumn(),1);
    }

}