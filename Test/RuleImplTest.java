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
        RuleInterface ntRule = new RuleImpl();
        ntRule.addTerminal(new Terminal("a"));
        ntRule.addTerminal(new Terminal("b"));
        nonTerminal.setRule((RuleImpl) ntRule);
    }

    @Test
    public void replaceDigram() {
        ruleInterface.addTerminal(terminal);
        ruleInterface.addTerminal(terminalTwo);
        ruleInterface.addTerminal(new Terminal("a"));
        ruleInterface.addTerminal(new Terminal("b"));
        System.out.println(ruleInterface.getSymbolHashMap());

        System.out.println(ruleInterface);

        ruleInterface.replaceDigram(nonTerminal);

        System.out.println(ruleInterface);
        System.out.println(ruleInterface.getSymbolHashMap());
        assertEquals("11", ruleInterface.toString());
    }

    @Test
    public void replaceMoreComplexDigramHashMap() {
        ruleInterface.addTerminal(new Terminal("e"));
        ruleInterface.addTerminal(new Terminal("z"));

        ruleInterface.addTerminal(new Terminal("a"));
        ruleInterface.addTerminal(new Terminal("b"));

        ruleInterface.addTerminal(new Terminal("q"));
        ruleInterface.addTerminal(new Terminal("r"));

        ruleInterface.addTerminal(new Terminal("a"));
        ruleInterface.addTerminal(new Terminal("b"));

        System.out.println(ruleInterface.getSymbolHashMap());
        System.out.println(ruleInterface);

        ruleInterface.replaceDigram(nonTerminal);

        assertEquals("ez1qr1", ruleInterface.toString());

        System.out.println(ruleInterface.getSymbolHashMap());
        System.out.println(ruleInterface);
    }

    @Test
    public void replaceNonTerminal() {
        ruleInterface.addTerminal(new Terminal("e"));
        ruleInterface.addTerminal(new Terminal("z"));

        ruleInterface.addTerminal(new Terminal("a"));
        ruleInterface.addTerminal(new Terminal("b"));

        ruleInterface.addTerminal(new Terminal("q"));
        ruleInterface.addTerminal(new Terminal("r"));

        ruleInterface.addTerminal(new Terminal("a"));
        ruleInterface.addTerminal(new Terminal("b"));
        System.out.println(ruleInterface.getSymbolHashMap());
        System.out.println(ruleInterface.getNonTerminalHashMap());
        System.out.println(ruleInterface);

        ruleInterface.replaceDigram(nonTerminal);

        System.out.println(ruleInterface.getSymbolHashMap());
        System.out.println(ruleInterface.getNonTerminalHashMap());
        System.out.println(ruleInterface);

        ruleInterface.replaceNonTerminal(nonTerminal);

        System.out.println(ruleInterface.getSymbolHashMap());
        System.out.println(ruleInterface.getNonTerminalHashMap());
        System.out.println(ruleInterface);

        assertEquals("ezabqrab", ruleInterface.toString());
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