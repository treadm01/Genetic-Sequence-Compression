import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RuleImplTest {
    public RuleInterface ruleInterface;
    public Terminal terminal, terminalTwo;

    @Before
    public void initFields() {
        ruleInterface = new RuleImpl();
        terminal = new Terminal("a");
        terminalTwo = new Terminal("b");
    }

    @Test
    public void replaceDigram() {
    }

    @Test
    public void replaceNonTerminal() {
    }

    @Test
    public void checkDigram() {
        // add repeating a b a b - so should match a bigram
        ruleInterface.addTerminal("a");
        ruleInterface.addTerminal("b");
        ruleInterface.addTerminal("a");

        assertEquals(false, ruleInterface.checkDigram());

        ruleInterface.addTerminal("b");
        assertEquals(true, ruleInterface.checkDigram());

        ruleInterface.addTerminal("b");
        assertEquals(false, ruleInterface.checkDigram());

        ruleInterface.addTerminal("b");
        ruleInterface.addTerminal("b");
        assertEquals(true, ruleInterface.checkDigram());
    }

    @Test
    public void addTerminal() {
        // add terminal
        ruleInterface.addTerminal("a");
        assertEquals("a", ruleInterface.toString());

        // check second terminal has link to first
        ruleInterface.addTerminal("b");
        assertEquals(ruleInterface.getTail().getLeftSymbol().toString(), "a");
    }
}