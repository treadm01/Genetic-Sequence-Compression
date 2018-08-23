import GrammarCoder.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class SearchTest {

    @Test
    public void searchSmallString() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        assertTrue(c.search("cgca"));
    }
}
