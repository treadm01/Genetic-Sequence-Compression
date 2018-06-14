import org.junit.Test;

import static org.junit.Assert.*;

public class TerminalTest {

    @Test
    public void equals() {
        Terminal t = new Terminal("a");
        Terminal t2 = new Terminal("a");
        assertEquals(t, t2);
    }
}