package main.parser;

import java.awt.*;
import java.io.InputStream;

/**
 * author: Ruslan Sokolov
 * date: 4/1/14
 */
public class Parser {
    private static final Color NONTERM_COLOR = Color.black;
    private static final Color TERM_COLOR = Color.red;
    LexicalAnalyzer lex;

    private Tree E() throws ParseException {
        Tree r = new Tree(new Token("E", TokenType.NON_TERMINAL), NONTERM_COLOR);
        switch (lex.getToken().getType()) {
            case UNARY_MINUS:
                assert lex.getToken().getValue().equals("-");
                r.addChild(new Tree(lex.getToken(), TERM_COLOR));
                lex.nextToken();
            case LPAREN:
            case INTEGER:
                Tree t = T();
                r.addChild(t);
                Tree e = e();
                r.addChild(e);
                break;
            default:
                throw new ParseException("Expected '(', number or '-', but " + lex.getToken());
        }
        return r;
    }
    private Tree F() throws ParseException {
        Tree r = new Tree(new Token("F", TokenType.NON_TERMINAL), NONTERM_COLOR);
            switch(lex.getToken().getType()) {
                case INTEGER:
                case LPAREN:
                    Tree g = G();
                    r.addChild(g);
                    Tree h = f();
                    r.addChild(h);
                    break;
                case END:
                case MUL:
                case RPAREN:
                case UNARY_MINUS:
                case PLUS:
                case MINUS:
                    break;
                default:
                 throw new ParseException("Expected '(' or number, but " + lex.getToken());
            }
        return r;
    }

    //[$ ,* ,+ ,) ,- ,
    private Tree T() throws ParseException {
        Tree r = new Tree(new Token("T", TokenType.NON_TERMINAL), NONTERM_COLOR);
        switch (lex.getToken().getType()) {
            case INTEGER:
            case LPAREN:
                Tree f = F();
                r.addChild(f);
                Tree t = t();
                r.addChild(t);
                break;
            default:
                throw new ParseException("Expected '(' or number, but " + lex.getToken());
        }
        return r;
    }

    private Tree e() throws ParseException {
        Tree r = new Tree(new Token("e", TokenType.NON_TERMINAL), NONTERM_COLOR);
        switch (lex.getToken().getType()) {
            case MINUS:
            case PLUS:
                String tmp = lex.getToken().getValue();
                assert tmp.equals("+") || tmp.equals("-");
                r.addChild(new Tree(lex.getToken(), TERM_COLOR));
                lex.nextToken();
                Tree t = T();
                r.addChild(t);
                Tree e = e();
                r.addChild(e);
                break;
                case END:
                case RPAREN:
                    break;
                default:
                    throw new ParseException("Expected '+', '-', ')' or end, but " + lex.getToken());
        }
        return r;
    }

    private Tree t() throws ParseException {
        Tree r = new Tree(new Token("t", TokenType.NON_TERMINAL), NONTERM_COLOR);
        switch (lex.getToken().getType()) {
            case MUL:
                assert lex.getToken().getValue().equals("*");
                r.addChild(new Tree(lex.getToken(), TERM_COLOR));
                lex.nextToken();
                Tree f = F();
                r.addChild(f);
                Tree t = t();
                r.addChild(t);
                break;
            case END:
            case PLUS:
            case MINUS:
            case RPAREN:
                break;
            default:
                throw new ParseException("Expected '*', '+', '-', ')' or end, but " + lex.getToken());
        }
        return r;
    }

    private Tree f() throws ParseException {
        Tree r = new Tree(new Token("f", TokenType.NON_TERMINAL), NONTERM_COLOR);
        switch (lex.getToken().getType()) {
            case POWER:
                assert lex.getToken().getValue().equals("^");
                Token tmp = lex.getToken();
                lex.nextToken();
//                Tree g = G();
//                Tree f = f();
//                r.addChild(f);
//                r.addChild(g);
                r.addChild(new Tree(tmp, TERM_COLOR));
                Tree g = G();
                r.addChild(g);
                Tree f = f();
                r.addChild(f);
                break;
            case END:
            case PLUS:
            case MINUS:
            case RPAREN:
            case MUL:
                break;
            default:
                throw new ParseException("Expected '^' '*', '+', '-', ')' or end, but " + lex.getToken());

        }
        return r;
    }

    private Tree G() throws ParseException {
        Tree r = new Tree(new Token("G", TokenType.NON_TERMINAL), NONTERM_COLOR);
        switch (lex.getToken().getType()) {
            case INTEGER:
                r.addChild(new Tree(lex.getToken(), TERM_COLOR));
                lex.nextToken();
                break;
            case LPAREN:
                assert lex.getToken().getValue().equals("(");
                r.addChild(new Tree(lex.getToken(), TERM_COLOR));

                lex.nextToken();
                Tree e = E();
                r.addChild(e);

                assert lex.getToken().getValue().equals(")");
                r.addChild(new Tree(lex.getToken(), TERM_COLOR));
                lex.nextToken();
        }
        return r;
    }

    public Tree parse(InputStream in) throws ParseException {
        lex = new LexicalAnalyzer(in);
        lex.nextToken();
        Tree result = E();
        if(lex.getToken().getType().equals(TokenType.END)) {
            return result;
        } else {
            throw new ParseException("Expression is not correct");
        }
    }
}

