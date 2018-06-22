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
        System.out.println(ruleInterface.getSymbolHashMap());

        ruleInterface.replaceDigram(ruleInterface.getTail(), nonTerminal);
        System.out.println(ruleInterface.getSymbolHashMap());

        System.out.println(ruleInterface);
        //assertEquals("11", ruleInterface.toString());
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
        System.out.println(ruleInterface.getSymbolHashMap());

        assertEquals(false, ruleInterface.checkDigram());

        ruleInterface.addTerminal(new Terminal("b"));
        System.out.println(ruleInterface.getSymbolHashMap());
        assertEquals(true, ruleInterface.checkDigram());

        ruleInterface.addTerminal(new Terminal("b"));
        assertEquals(false, ruleInterface.checkDigram());

        ruleInterface.addTerminal(new Terminal("b"));
        ruleInterface.addTerminal(new Terminal("b"));
        assertEquals(true, ruleInterface.checkDigram());

        System.out.println(ruleInterface.getSymbolHashMap());
        System.out.println(ruleInterface);
    }

    @Test
    public void addTerminal() {
        // add terminal
        ruleInterface.addTerminal(terminal);
        assertEquals("a", ruleInterface.toString());

        // check second terminal has link to first
        ruleInterface.addTerminal(terminalTwo);

        ruleInterface.addTerminal(new Terminal("a"));
        ruleInterface.addTerminal(new Terminal("b"));

        assertEquals("abab", ruleInterface.toString());

        System.out.println(ruleInterface);

        System.out.println(ruleInterface.getSymbolHashMap());
    }
}