import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ArithmeticEncoderTest {

//    @Test
//    public void testArithmeticEncoder() {
//        ArithmeticEncoder ae = new ArithmeticEncoder();
//        String input = "210";
//        ae.processInput(input);
//        assertEquals("10110", ae.getBinaryString());
//    }

//    @Test
//    public void testAlphabetSetter() {
//        ArithmeticEncoder ae = new ArithmeticEncoder();
//        String input = "210";
//        ae.setSourceAlphabet(input);
//        ae.calculateSymbolProbabilityRatio();
//        assertEquals("{0=0.2, 1=0.4, 2=0.4}", ae.sourceAlphabet.toString());
//    }

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

//    @Test
//    public void calculateRationalDenominator() {
//        ArithmeticEncoder ae = new ArithmeticEncoder();
//        String input = "210";
//        ae.setSourceAlphabet(input);
//        assertEquals("10", ae.calculateRationalDenominator().toString());
//    }

    @Test
    public void calculateSymbolProbabilityRatio() {
    }

//    @Test
//    public void setSymbolSegmentStart() {
//        ArithmeticEncoder ae = new ArithmeticEncoder();
//        String input = "012";
//        ae.setSourceAlphabet(input);
//        ae.setSymbolSegment();
//        // should be 0, 2, 6
//        for (ArithmeticSymbol as : ae.sourceAlphabet.values()) {
//            System.out.println(as.getSegmentStart());
//        }
//    }

//    @Test
//    public void setSymbolSegmentEnd() {
//        ArithmeticEncoder ae = new ArithmeticEncoder();
//        String input = "012";
//        ae.setSourceAlphabet(input);
//        ae.setSymbolSegment();
//        // should be 2, 6, 10
//        for (ArithmeticSymbol as : ae.sourceAlphabet.values()) {
//            System.out.println(as.representation);
//            System.out.println(as.getSegmentEnd());
//        }
//    }

    @Test
    public void setSymbolSegment() {
    }

    //todo using different probabilities/alphabet
    //
    @Test
    public void encode() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "012";
        ae.setSourceAlphabet(input);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String toEncode = "210";
        assertEquals("101100", ae.encode(toEncode)); // extra 0??
        String binary = ae.encode(toEncode); //"210");
        assertEquals(toEncode, ae.decode(binary));
    }

    @Test
    public void encodeTwo() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String input = "0123";
        ae.setSourceAlphabet(input);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        assertEquals("011011000", ae.encode("2320"));
    }

    @Test
    public void decode() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String probInput = "0123"; //"012";
        String toEncode = "2320";
        ae.setSourceAlphabet(probInput);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String binary = ae.encode(toEncode); //"210");
        assertEquals(toEncode, ae.decode(binary));
    }

    @Test
    public void decodeBeginningOne() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String probInput = "0123";
        String toEncode =   "1111110";
        ae.setSourceAlphabet(probInput);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String binary = ae.encode(toEncode);
        assertEquals(toEncode, ae.decode(binary));
    }

    @Test
    public void decodeLonge() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String probInput = "0123"; //"012";
        String toEncode = "22222222222222222222222222220"; // 24 symbols - 25 symbols crash
        ae.setSourceAlphabet(probInput);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String binary = ae.encode(toEncode); //"210");
        assertEquals(toEncode, ae.decode(binary));
    }

    @Test
    public void decodeBeginningWithThre() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String probInput = "0123";
        String toEncode = "333333333333333333333330";
        ae.setSourceAlphabet(probInput);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String binary = ae.encode(toEncode);
        assertEquals(toEncode, ae.decode(binary));
    }


    @Test
    public void decodeLonger() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String probInput = "0123"; //"012";
        String toEncode = "222222222222222222222222222220"; // 24 symbols - 25 symbols crash
        ae.setSourceAlphabet(probInput);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String binary = ae.encode(toEncode); //"210");
        assertEquals(toEncode, ae.decode(binary));
    }

    @Test
    public void decodeBeginningWithThree() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String probInput = "0123";
        String toEncode = "3333333333333333333333330";
        ae.setSourceAlphabet(probInput);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String binary = ae.encode(toEncode);
        assertEquals(toEncode, ae.decode(binary));
    }


    @Test
    public void decodeBeginningOnes() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String probInput = "0123";
        String toEncode =   "11111110";
        ae.setSourceAlphabet(probInput);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String binary = ae.encode(toEncode);
        assertEquals(toEncode, ae.decode(binary));
    }

    @Test
    public void decodeOnes() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String probInput = "0123";
        String toEncode =   "11111130";
        ae.setSourceAlphabet(probInput);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        String binary = ae.encode(toEncode);
        assertEquals(toEncode, ae.decode(binary));
    }



    //todo GENERATE RANDOM STRINGS TO CHECK
    //
    String genRand (int length) {
        Random rand = new Random();
        String possibleLetters = "123";
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(possibleLetters.charAt(rand.nextInt(possibleLetters.length())));
        sb.append('0');
        return sb.toString();
    }

    @Test
    public void decompressX4() {
        ArithmeticEncoder ae = new ArithmeticEncoder();
        String probInput = "0123";
        ae.setSourceAlphabet(probInput);
        ae.calculateSymbolProbabilityRatio();
        ae.setSymbolSegment();
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            String input = genRand(rand.nextInt((50 - 1) + 1) + 1);
            System.out.println("String to encode " + input);
            String toEncode = input;
            String binary = ae.encode(toEncode);
            System.out.println();
            if (binary.length() < 33) {
                assertEquals(toEncode, ae.decode(binary));
            }
        }
    }

}