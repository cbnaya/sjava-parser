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
 * the class that contain the entry point
 */
public class Sjavac {

    //message in case of legal code
    public static final String CODE_IS_LEGAL_MESSAGE = "0";
    //message in case of illegal code
    public static final String CODE_IS_NOT_LEGAL_MESSAGE = "1";
    //message in case of io error
    public static final String IO_ERROR_MESSAGE = "2";

    /**
     * the entry point
     * get a sjava file path and check if the code is valid.
     * if the code is invalid will print message that describe the problem
     * expect to path in the first command line argument
     *
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        try {
            String fileData = readFile(args[0]);

            Parser astParser = new Parser(new Tokenizer(fileData));

            GlobalNode astGlobalNode = astParser.parseGlobal();

            AstValidator.globalValidate(astGlobalNode);

            print(CODE_IS_LEGAL_MESSAGE);
        } catch (IOException e) {
            print(IO_ERROR_MESSAGE);
        } catch (Exception e) {
            print(CODE_IS_NOT_LEGAL_MESSAGE);
            printError(e.getMessage());
        }
    }

    private static void print(String msg) {
        System.out.println(msg);
    }

    private static void printError(String msg) {
        System.err.println(msg);
    }

    private static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}
