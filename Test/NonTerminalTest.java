import org.junit.Test;

import static org.junit.Assert.*;

public class NonTerminalTest {

    NonTerminal nt = new NonTerminal();

    @Test
    public void updateBigramsTerminal() {
        nt.addValues(new Terminal("a"));
        nt.addValues(new Terminal("b"));
        nt.addValues(new Terminal("b"));

        nt.getBigramMap();
    }

    @Test

}