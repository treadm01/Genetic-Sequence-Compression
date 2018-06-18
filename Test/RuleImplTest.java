import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RuleImplTest {
    public RuleInterface ruleInterface;
    public SymbolInterface terminal;

    @Before
    public void initFields() {
        ruleInterface = new RuleImpl();
        terminal = new Terminal("a");
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
        ruleInterface.addTerminal();

    }
}