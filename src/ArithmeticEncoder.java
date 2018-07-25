import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class ArithmeticEncoder {

    static final Integer PRECISION = 32; // use long could get closer to 64?
    static final Integer WHOLE = 2 ^ PRECISION;
    static final Integer HALF = WHOLE / 2;
    static final Integer QUARTER = WHOLE / 4;
    static final char END_OF_FILE_SYMBOL = '0';//will be'!';
    Integer denominator; // this is the value of all probabilities given and used to compute ratio
    // todo will need to be able to represent integers from 0 up to denominator * whole, consider number of symbols
    String input = "210"; // having to put this here for now as the order alphabter is generated depends on string
    BitSet binaryEncoding = new BitSet();

    Map<Character, ArithmeticSymbol> sourceAlphabet = new LinkedHashMap<>();

    public void processInput(String input) {

    }

    public String getBinaryString() {
        String s = "";
        return s;
    }


    //not entirely sure how useful this will be, but to get dynamic alphabet from input
    // depends on how rigid the alphabet will be and how long this method takes
    public void setSourceAlphabet(String input) {
        Character c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            if (!sourceAlphabet.containsKey(input.charAt(i))) {
                ArithmeticSymbol as = new ArithmeticSymbol(c);
                as.setProbability(getProbability(c));
                sourceAlphabet.put(c, as);
            }
        }
    }

    // hard coding for now just to get initial values
    // must be values that all add up to one, kept as ratios
    public Integer getProbability(char c) {
        if (c == '0') {
            return 2;
        }
        else {
            return 4;
        }
    }

    public Integer calculateRationalDenominator() {
        Integer sumOfProbabilities = 0;
        for (ArithmeticSymbol as : sourceAlphabet.values()) {
            sumOfProbabilities+= as.getProbability();
        }
        return sumOfProbabilities;
    }

    //todo will probably need rounding, needs to always equal 1
    public void calculateSymbolProbabilityRatio() {
        denominator = calculateRationalDenominator(); // use getter and setter for global variable
        for (ArithmeticSymbol as : sourceAlphabet.values()) {
            as.setProbabiltiyRatio((double) as.getProbability() / denominator);
        }
    }

    // calculating the start integers of segments, which correspond to the offset of the next
    // first is 0, second would be 2 in this instance, then 6, storing as the integer values for now
    //used to compute segments
    public void setSymbolSegment() {
        int sumOfSegmentStarts = 0;
        ArithmeticSymbol as;
        for (Map.Entry<Character, ArithmeticSymbol> entry : sourceAlphabet.entrySet()) {
            as = entry.getValue();
            as.setSegmentStart(sumOfSegmentStarts);
            as.setSegmentEnd(sumOfSegmentStarts + as.getProbability());
            sumOfSegmentStarts += as.getProbability();
        }
    }

    public void encode(String input) {
        Integer lowerBound = 0;
        Integer upperBound = WHOLE;
        Integer numberOfMiddleRepeats = 0;
        Integer widthBetweenBounds;
        String output = "";

        for (int i = 0; i < input.length(); i++) { // loop over the input
            ArithmeticSymbol as = sourceAlphabet.get(input.charAt(i));
            //System.out.println("symbol is " + as.representation);
            widthBetweenBounds = upperBound - lowerBound;
            upperBound = lowerBound + Math.round((widthBetweenBounds * as.getSegmentEnd()) / denominator);
            lowerBound = lowerBound + Math.round((widthBetweenBounds * as.getSegmentStart()) / denominator);
           // System.out.println("upper " + upperBound);
            //System.out.println("lower " + lowerBound);
            while (upperBound < HALF || lowerBound > HALF) {
                if (upperBound < HALF) {
                    output += "0";
                    for (int j = 0; j < numberOfMiddleRepeats; j++) {
                        output += "1";
                    }
                    numberOfMiddleRepeats = 0;
                    lowerBound = 2 * lowerBound;
                    upperBound = 2 * upperBound;
                }
                else if (lowerBound > HALF) {
                    output += "1";
                    for (int j = 0; j < numberOfMiddleRepeats; j++) {
                        output += "0";
                    }
                    numberOfMiddleRepeats = 0;
                    lowerBound = 2 * (lowerBound - HALF);
                    upperBound = 2 * (upperBound - HALF);
                }
            }
            while (lowerBound > QUARTER && upperBound < 3 * QUARTER) {
                numberOfMiddleRepeats += 1;
                lowerBound = 2 * (lowerBound - QUARTER);
                upperBound = 2 * (upperBound - QUARTER);
            }
        }
        numberOfMiddleRepeats += 1;
        if (lowerBound <= QUARTER) {
            output += "0";
            for (int j = 0; j < numberOfMiddleRepeats; j++) {
                output += "1";
            }
        }
        else {
            output += "1";
            for (int j = 0; j < numberOfMiddleRepeats; j++) {
                output += "0";
            }
        }
        System.out.println("output " + output);
    }

}
