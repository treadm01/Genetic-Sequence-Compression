import org.junit.Test;

import static org.junit.Assert.*;

public class FindPatternTest {
    FindPattern fp = new FindPattern();


    @Test
    public void getRepeatOfPairs() {
        assertEquals("bc", fp.getRepeat("abcdbc").get(0));
        assertEquals("ac", fp.getRepeat("accdac").get(0));
        assertEquals("cc", fp.getRepeat("abdccbecc").get(0));
        assertEquals("cc", fp.getRepeat("cccc").get(0));
        assertEquals("cc", fp.getRepeat("ccccd").get(0));
        assertEquals("cc", fp.getRepeat("ccccde").get(0));
    }
}