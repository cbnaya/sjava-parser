package lexer;

/**
 * Created by cbnaya on 14/06/2015.
 */
public class Position {
    public Position(int line, int column)
    {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String toString()
    {
        return String.format("line %d column %d", line, column);
    }

    public static Position createFromOffset(int offset, String input)
    {
        if (offset>input.length())
        {
            throw new IndexOutOfBoundsException();
        }

        if (offset == input.length())
        {
            offset--;
        }
        
        String inputUntilPosition = input.substring(0, offset + 1);
        String[] lines = inputUntilPosition.split("\r?\n");

        int line = lines.length;
        int column = lines[lines.length-1].length();

        if ((input.charAt(offset) == '\n')||(input.charAt(offset) == '\r'))
        {
            column++;
        }
        return new Position(line, column);
    }

    private int line;
    private int column;

}
