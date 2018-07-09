import org.junit.Test;

import static org.junit.Assert.*;

public class GuardTest {

    @Test
    public void assignLeft() {
        Guard g = new Guard("!");
        Symbol s = new Symbol();
        g.assignLeft(s);
    }
}