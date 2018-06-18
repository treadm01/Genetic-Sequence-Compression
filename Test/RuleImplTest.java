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
    }

    @Test
    public void addTerminal() {
        ruleInterface.addTerminal(terminal);
        assertEquals("a", ruleInterface.toString());

        ruleInterface.addTerminal(terminalTwo);
        System.out.println(ruleInterface);
    }
}