import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class uncompressTest {

    @Test
    public void processInput() {
        String expected = "abcdbc";
        compress c = new compress();
        List<rule> rules = c.processInput(expected);
        uncompress u = new uncompress();
        assertEquals(expected, u.processInput(rules));

        rules.clear();

        expected = "abcdbcabc";
        c = new compress();
        rules = c.processInput(expected);
        u = new uncompress();
        assertEquals(expected, u.processInput(rules));

        rules.clear();

        expected = "abcdbcabcd";
        c = new compress();
        rules = c.processInput(expected);
        u = new uncompress();
        assertEquals(expected, u.processInput(rules));

    }

    @Test
    public void getOutput() {
    }
}