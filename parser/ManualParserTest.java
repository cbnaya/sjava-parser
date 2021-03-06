package parser;

import lexer.Tokenizer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by cbnaya on 14/06/2015.
 */
public class ManualParserTest {
    public static void main (String[] arg) throws IOException, NotAllowedInThisContext, OtherTokenTypeNeedHere, InvalidIdentityName {
        String fileData = new String(Files.readAllBytes(Paths.get(arg[0])), StandardCharsets.UTF_8);
        Tokenizer t = new Tokenizer(fileData);

        Parser p = new Parser(t);

        p.parseGlobal();
    }

}
