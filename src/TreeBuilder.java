import main.parser.FirstFollow;
import main.parser.ParseException;
import main.parser.Parser;
import main.parser.Tree;
import org.StructureGraphic.v1.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * author: Ruslan Sokolov
 * date: 3/31/14
 */
public class TreeBuilder {

    public static void main(String[] args) throws ParseException, FileNotFoundException {
        FirstFollow ff = new FirstFollow(new FileInputStream("grammatic"));
        PrintWriter pw = new PrintWriter("FIRST");
        pw.print("FIRST\n" + ff.toString(ff.getFIRST()));
        pw.close();
        pw = new PrintWriter("FOLLOW");
        pw.print("FOLLOW\n" + ff.toString(ff.getFOLLOW()));
        pw.close();
        if (args.length > 0) {
            Parser p = new Parser();
            Tree t = p.parse(new ByteArrayInputStream(args[0].getBytes()));
            System.out.println(t.getExpression());
            DSutils.show(t, 40, 20);
        } else {
            System.out.println("Usage : java TreeBuilder <expression>");
        }
    }
}

