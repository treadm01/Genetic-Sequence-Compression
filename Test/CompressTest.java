import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompressTest {
    Compress c;

    @Before
    public void setUp() throws Exception {
        c = new Compress();
    }

    @Test
    public void processInput() {
        c.processInput("abcdbc");

        //0 -> a 1 d 1
        //1 -> b c
    }

}