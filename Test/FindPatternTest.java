import org.junit.Test;

import static org.junit.Assert.*;

public class FindPatternTest {

    @Test
    public void getRepeat() {
        FindPattern fp = new FindPattern();
        assertEquals("bc", fp.getRepeat("abcdbc"));
    }
}