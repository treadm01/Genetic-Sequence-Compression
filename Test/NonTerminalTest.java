import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NonTerminalTest {
    NonTerminal nt;

    @Before
    public void setUp() throws Exception {
        nt = new NonTerminal(0);
    }

    @Test
    public void addNextSymbol() {
        nt.addNextSymbol(new Terminal("a"));
        nt.addNextSymbol(new Terminal("b"));
        nt.addNextSymbol(new Terminal("c"));

        Symbol s = nt.guard.left.right;
        String output = "";
        do {
            output += s.toString();
            s = s.right;
        } while (!s.representation.equals("?"));

        assertEquals("abc", output);
    }

    @Test
    public void addSymbols() {
        nt.addSymbols(new Terminal("a"), new Terminal("b"));

        Symbol s = nt.guard.left.right;
        String output = "";
        do {
            output += s.toString();
            s = s.right;
        } while (!s.representation.equals("?"));

        assertEquals("ab", output);
    }

    @Test
    public void updateNonTerminal() {
        Terminal b = new Terminal("b");
        Terminal c = new Terminal("c");
        nt.addNextSymbol(new Terminal("a"));
        nt.addNextSymbol(new Terminal("b"));
        nt.addNextSymbol(new Terminal("c"));
        nt.addNextSymbol(b);
        nt.addNextSymbol(c);

        NonTerminal ntRule = new NonTerminal(0);
        ntRule.addSymbols(b, c);
        Rule rule = new Rule(ntRule);

        nt.updateNonTerminal(rule, c);

        Symbol s = nt.guard.left.right;
        String output = "";
        do {
            output += s.toString();
            s = s.right;
        } while (!s.representation.equals("?"));

        assertEquals("a0bc", output);
    }
}