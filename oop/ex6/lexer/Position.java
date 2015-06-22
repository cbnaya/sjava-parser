package oop.ex6.lexer;

/**
 * this class represent a location in the file
 */
public class Position {
    //the to string message format
    public static final String TO_STRING_FORMAT = "line %d column %d";
    //regex of new line of the split
    public static final String NEW_LINE_REGEX = "\r?\n";

    //save the line number
    private final int line;
    //save the column
    private final int column;

    /**
     * Ctor
     * @param line - the line number
     * @param column - the column in the line
     */
    public Position(int line, int column)
    {
        this.line = line;
        this.column = column;
    }

    /**
     * return the line number
     * @return the line number
     */
    public int getLine() {
        return line;
    }

    /**
     * return the column in the line
     * @return the column in the line
     */
    public int getColumn() {
        return column;
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return String.format(TO_STRING_FORMAT, line, column);
    }

    /**
     * convert offset in the file to position (line and column)
     * @param offset - the offset in the file
     * @param input - the file content
     * @return - position object
     */
    public static Position createFromOffset(int offset, String input)
    {
        if (offset>input.length())
        {
            throw new IndexOutOfBoundsException();
        }

        //in case of EOF we decrease the offset to point to the last char in the file
        if (offset == input.length())
        {
            offset--;
        }
        
        String inputUntilPosition = input.substring(0, offset + 1);
        String[] lines = inputUntilPosition.split(NEW_LINE_REGEX);

        int line = lines.length;
        int column = lines[lines.length-1].length();

        //if the offset is point to new line we represent him as the last char of the prev line
        if (Character.toString(input.charAt(offset)).matches(NEW_LINE_REGEX))
        {
            column++;
        }

        return new Position(line, column);
    }
}
