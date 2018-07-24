import org.junit.Test;

import static org.junit.Assert.*;

public class ArithmeticEncoderTest {

    @Test
    public void testArithmeticEncoder() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "210";
        ae.processInput(input);
        assertEquals("10110", ae.getBinaryString());
    }

}