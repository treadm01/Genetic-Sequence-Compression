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

    @Test
    public void testAlphabetSetter() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "210";
        ae.setSourceAlphabet(input);
        ae.calculateSymbolProbabilityRatio();
        assertEquals("{0=0.2, 1=0.4, 2=0.4}", ae.sourceAlphabet.toString());
    }

    @Test
    public void processInput() {
    }

    @Test
    public void getBinaryString() {
    }

    @Test
    public void setSourceAlphabet() {
    }

    @Test
    public void getProbability() {

    }

    @Test
    public void calculateRationalDenominator() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "210";
        ae.setSourceAlphabet(input);
        assertEquals("10", ae.calculateRationalDenominator().toString());
    }

    @Test
    public void calculateSymbolProbabilityRatio() {
    }

    @Test
    public void setSymbolSegmentStart() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "012";
        ae.setSourceAlphabet(input);
        ae.setSymbolSegment();
        // should be 0, 2, 6
        for (ArithmeticSymbol as : ae.sourceAlphabet.values()) {
            System.out.println(as.getSegmentStart());
        }
    }

    @Test
    public void setSymbolSegmentEnd() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "012";
        ae.setSourceAlphabet(input);
        ae.setSymbolSegment();
        // should be 2, 6, 10
        for (ArithmeticSymbol as : ae.sourceAlphabet.values()) {
            System.out.println(as.representation);
            System.out.println(as.getSegmentEnd());
        }
    }

    @Test
    public void setSymbolSegment() {
    }

    @Test
    public void encode() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "012";
        ae.setSourceAlphabet(input);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        ae.encode("210");
        assertEquals("101100", ae.encode("210")); // extra 0??
    }

    @Test
    public void encodeTwo() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "0123";
        ae.setSourceAlphabet(input);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        assertEquals("011011000", ae.encode("2320"));
        //assertEquals("10110", ae.encode("210"));
    }

    @Test
    //todo check that the bpounds are equalling the same at each stage for encode and decode
    public void decode() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "0123"; //"012";
        ae.setSourceAlphabet(input);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String binary  = ae.encode("2320"); //"210");
        ae.decode(binary);
        //858993454

        // should be a value in between
        //l 39321
        //u 65536
        // and less than 65536, currently 90112

        //2147483648 half

        // 1073741824 quarter
    }
}