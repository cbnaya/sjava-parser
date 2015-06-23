package oop.ex6.main;

import oop.ex6.ast.GlobalNode;
import oop.ex6.lexer.Tokenizer;
import oop.ex6.parser.Parser;
import oop.ex6.validator.AstValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by cbnaya on 21/06/2015.
 */
public class Sjavac {

    public static final String IO_ERROR_MESSAGE = "2";

    public static void main(String[] args)
    {
        try
        {
            String fileData = readFile(args[0]);

            Parser p = new Parser(new Tokenizer(fileData));

            GlobalNode globalNode = p.parseGlobal();

            AstValidator.globalValidate(globalNode);

            print("0");
        }
        catch (IOException e)
        {
            print(IO_ERROR_MESSAGE);
            return;
        }
        catch (ParsingFailedException|InvalidCodeException e)
        {
            print("1");
            printError(e.getMessage());
            return;
        }
    }

    private static void print(String msg){
        System.out.println(msg);
    }
    private static void printError(String msg)
    {
        System.err.println(msg);
    }

    private static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);

    }
}
