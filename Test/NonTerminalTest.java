//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class NonTerminalTest {
//    GrammarCoder.NonTerminal nt;
//
//    @Before
//    public void setUp() throws Exception {
//        nt = new GrammarCoder.NonTerminal(0);
//    }
//
//    @Test
//    public void addNextSymbol() {
////        nt.addNextSymbol(new GrammarCoder.Terminal("a"));
////        nt.addNextSymbol(new GrammarCoder.Terminal("b"));
////        nt.addNextSymbol(new GrammarCoder.Terminal("c"));
//
//        GrammarCoder.Symbol s = nt.guard.left.right;
//        String output = "";
//        do {
//            output += s.toString();
//            s = s.right;
//        } while (!s.representation.equals("?"));
//
//        assertEquals("abc", output);
//    }
//
//    @Test
//    public void addSymbols() {
////        nt.addSymbols(new GrammarCoder.Terminal("a"), new GrammarCoder.Terminal("b"));
//
//        GrammarCoder.Symbol s = nt.guard.left.right;
//        String output = "";
//        do {
//            output += s.toString();
//            s = s.right;
//        } while (!s.representation.equals("?"));
//
//        assertEquals("ab", output);
//    }
//
//    @Test
//    public void updateNonTerminal() {
////        GrammarCoder.Terminal b = new GrammarCoder.Terminal("b");
////        GrammarCoder.Terminal c = new GrammarCoder.Terminal("c");
////        nt.addNextSymbol(new GrammarCoder.Terminal("a"));
////        nt.addNextSymbol(new GrammarCoder.Terminal("b"));
////        nt.addNextSymbol(new GrammarCoder.Terminal("c"));
////        nt.addNextSymbol(b);
//        nt.addNextSymbol(c);
//
//        GrammarCoder.NonTerminal ntRule = new GrammarCoder.NonTerminal(0);
//        ntRule.addSymbols(b, c);
//        GrammarCoder.Rule rule = new GrammarCoder.Rule(ntRule);
//
//        nt.updateNonTerminal(rule, c);
//
//        GrammarCoder.Symbol s = nt.guard.left.right;
//        String output = "";
//        do {
//            output += s.toString();
//            s = s.right;
//        } while (!s.representation.equals("?"));
//
//        assertEquals("a0bc", output);
//    }
//}