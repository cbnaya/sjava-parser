package lexer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ManualTester {
    public static void main (String[] arg) throws IOException {
        String fileData = new String(Files.readAllBytes(Paths.get(arg[0])), StandardCharsets.UTF_8);
        Tokenizer t = new Tokenizer(fileData);

        System.out.println(fileData);
        while (t.hasNext())
        {
            Token tok = t.next();
            if (tok.getType() != Token.TokenType.WHITE_SPACE) {
                System.out.println(tok);
            }
        }
    }
}
