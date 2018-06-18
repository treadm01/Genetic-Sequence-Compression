import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RuleImplTest {
    public RuleInterface ruleInterface;
    public Terminal terminal, terminalTwo;
    public NonTerminalTwo nonTerminal;

    @Before
    public void initFields() {
        ruleInterface = new RuleImpl();
        terminal = new Terminal("a");
        terminalTwo = new Terminal("b");
        nonTerminal = new NonTerminalTwo("1");
    }

    @Test
    public void replaceDigram() {
        ruleInterface.addTerminal(terminal);
        ruleInterface.addTerminal(terminalTwo);
        ruleInterface.addTerminal(new Terminal("a"));
        ruleInterface.addTerminal(new Terminal("b"));

        ruleInterface.replaceDigram(ruleInterface.getTail(), nonTerminal);
        assertEquals("11", ruleInterface.toString());
    }

    @Test
    public void replaceNonTerminal() {
    }

    @Test
    public void checkDigram() {
        // add repeating a b a b - so should match a bigram
        ruleInterface.addTerminal(terminal);
        ruleInterface.addTerminal(terminalTwo);
        ruleInterface.addTerminal(new Terminal("a"));

        assertEquals(false, ruleInterface.checkDigram());

        ruleInterface.addTerminal(new Terminal("b"));
        System.out.println(ruleInterface);
        assertEquals(true, ruleInterface.checkDigram());

        ruleInterface.addTerminal(new Terminal("b"));
        assertEquals(false, ruleInterface.checkDigram());

        ruleInterface.addTerminal(new Terminal("b"));
        ruleInterface.addTerminal(new Terminal("b"));
        assertEquals(true, ruleInterface.checkDigram());

        System.out.println(ruleInterface);
    }

    @Test
    public void addTerminal() {
        // add terminal
        ruleInterface.addTerminal(terminal);
        assertEquals("a", ruleInterface.toString());
        Terminal z = new Terminal("b");

        // check second terminal has link to first
        ruleInterface.addTerminal(terminalTwo);
        assertEquals(ruleInterface.getTail().getLeftSymbol().toString(), "a");

        ruleInterface.addTerminal(new Terminal("a"));
        ruleInterface.addTerminal(new Terminal("b"));

        assertEquals("abab", ruleInterface.toString());
    }
}