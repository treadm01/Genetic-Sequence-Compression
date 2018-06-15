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
    public void updateBigramsBirgram() {
        nt.addValues(1);
        nt.addValues(2);
        nt.addValues(3);
        nt.addValues(4);
        nt.addValues(5);
        nt.addValues(6);

        nt.getBigramMap();
    }

    @Test
    public void removeBigram() {
        nt.addValues(1);
        nt.addValues(2);
        nt.addValues(3);
        nt.addValues(4);
        nt.addValues(5);
        nt.addValues(6);
        nt.values.remove(3);

        System.out.println(nt.values);
        System.out.println(nt.bigramMap.size());

        nt.getBigramMap();

        assertEquals(nt.values.size()-1, nt.bigramMap.size());
    }
}