import org.junit.Test;

import static org.junit.Assert.*;

public class DecompressTest {

    @Test
    public void buildGrammar() {
        Decompress d = new Decompress();
        assertEquals("abcdbc", d.decompress(d.buildGrammar("abcd(1,2)")));
    }

    @Test
    public void decompress() {
    }
}