import org.junit.Test;

import static org.junit.Assert.*;

public class ReverseComplementTest {

    //a == t and c == g, the swapped values and reverse order
    // reverse oder first and then switch
    // so cg = gc convert cg, either either, gc -> swap cg
    @Test
    public void checkDigrams() {
        String input = "acg";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals("g t, a c, c g, ", c.printDigrams());
    }


    @Test
    public void simpleReverseComplement() {
        String input = "acgtcgacgt";

    }
}
