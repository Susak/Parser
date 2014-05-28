package test;

import main.parser.ParseException;
import main.parser.Parser;
import main.parser.Tree;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

/**
 * author: Ruslan Sokolov
 * date: 4/22/14
 */
public class ParserTest {
    private static final Parser parser = new Parser();
    @Test
    public void testPlus() throws ParseException {
        String expression1 = "1+1";
        String expression2 = "1+2+3+4+5+6+(-1)+(-50)";
        String expression3 = "(1+2+(-3+(-4-(-5+5)-2)))";
        Tree t = parser.parse(new ByteArrayInputStream(expression1.getBytes()));
        assertTrue(expression1.equals((t.getExpression())));

        t = parser.parse(new ByteArrayInputStream(expression2.getBytes()));
        assertTrue(expression2.equals(t.getExpression()));

        t = parser.parse(new ByteArrayInputStream(expression3.getBytes()));
        assertTrue(expression3.equals(t.getExpression()));
    }

    @Test
    public void testMinus() throws ParseException {
        String expression1 = "-1";
        String expression2 = "-(-(-(-(-(1-1)))))";
        String expression3 = "1-2-3-4-5-(-1-2-4)+4-5";
        String expression4 = "1-1";
        String expression5 = "-(4+1)";
        Tree t = parser.parse(new ByteArrayInputStream(expression1.getBytes()));
        assertTrue(expression1.equals((t.getExpression())));

        t = parser.parse(new ByteArrayInputStream(expression2.getBytes()));
        assertTrue(expression2.equals(t.getExpression()));

        t = parser.parse(new ByteArrayInputStream(expression3.getBytes()));
        assertTrue(expression3.equals(t.getExpression()));

        t = parser.parse(new ByteArrayInputStream(expression4.getBytes()));
        assertTrue(expression4.equals(t.getExpression()));

        t = parser.parse(new ByteArrayInputStream(expression5.getBytes()));
        assertTrue(expression5.equals(t.getExpression()));
    }

    @Test
    public void testMultiply() throws ParseException {
        String expression1 = "25*5";
        String expression2 = "(-2*5)*20*10*(-2)*2";
        String expression3 = "1*2*3*4*5*6*7*8*(10-13*3*3)*(1)";
        Tree t = parser.parse(new ByteArrayInputStream(expression1.getBytes()));
        assertTrue(expression1.equals((t.getExpression())));

        t = parser.parse(new ByteArrayInputStream(expression2.getBytes()));
        assertTrue(expression2.equals(t.getExpression()));

        t = parser.parse(new ByteArrayInputStream(expression3.getBytes()));
        assertTrue(expression3.equals(t.getExpression()));
    }

    @Test
    public void testGeneral() throws ParseException {
        String expression1 = "(8-1+3)*6-((3+7)*2)";
        String expression2 = "5-2+4*(8-(-5+(-1)))+9";
        String expression3 = "5-2+4*(8-(-5+(-1)))+9*5-2+4*(8-(-5+(-1)))+(912+52-232+432*(28-(-15+(-1)))+(-12))";
        Tree t = parser.parse(new ByteArrayInputStream(expression1.getBytes()));
        assertTrue(expression1.equals((t.getExpression())));

        t = parser.parse(new ByteArrayInputStream(expression2.getBytes()));
        assertTrue(expression2.equals(t.getExpression()));

        t = parser.parse(new ByteArrayInputStream(expression3.getBytes()));
        assertTrue(expression3.equals(t.getExpression()));
    }
}