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

    public String encode(String input) {
        Integer lowerBound = 0;
        Integer upperBound = WHOLE;
        Integer numberOfMiddleRepeats = 0;
        Integer widthBetweenBounds;
        String output = "";

        for (int i = 0; i < input.length(); i++) { // loop over the input
            ArithmeticSymbol as = sourceAlphabet.get(input.charAt(i));
            //System.out.println("symbol is " + as.representation);
            widthBetweenBounds = upperBound - lowerBound;
            upperBound = lowerBound + Math.round((float)(widthBetweenBounds * as.getSegmentEnd()) / denominator);
            lowerBound = lowerBound + Math.round((float) (widthBetweenBounds * as.getSegmentStart()) / denominator);
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
        return output;
    }

    public void decode(String input) {
        Integer lowerBound = 0;
        Integer upperBound = WHOLE;
        Integer inputValue = 0;
        Integer widthBetweenBounds;// = upperBound - lowerBound;
        Integer inputIndex = 0;
        Integer limitOfInput = input.length();
        String output = "";

        while (inputIndex <= PRECISION && inputIndex < limitOfInput) { // approximate the float corresponding value
            if (input.charAt(inputIndex) == '1') {
                inputValue += 2 ^ (PRECISION - inputIndex);
            }
            inputIndex++;
        }

        System.out.println("INPUT VALUE " + inputValue);

        //inputValue = Math.round((widthBetweenBounds * inputValue) / denominator); // rounding the inputvalue as well

      //  System.out.println("THINK CHECK THIS " + inputValue);

        Character endLoop = '!';

        //while (true) { //find a proper condition, check for end of file symbol
        for (int x = 0; x < 2; x++) {
            for (ArithmeticSymbol as : sourceAlphabet.values()) {
                System.out.println("symbol being checked " + as.representation);
                widthBetweenBounds = upperBound - lowerBound;
                Integer upperBoundCheck = lowerBound + Math.round((float) (widthBetweenBounds * as.getSegmentEnd()) / denominator);
                Integer lowerBoundCheck = lowerBound + Math.round((float) (widthBetweenBounds * as.getSegmentStart()) / denominator);

                System.out.println("lower " + lowerBoundCheck);
                System.out.println("input value " + inputValue);
                System.out.println("upper " + upperBoundCheck);
                if (lowerBoundCheck <= inputValue && inputValue < upperBoundCheck) {
                    output += as.representation;
                    System.out.println(as.representation);
//                    endLoop = as.representation;
                    lowerBound = lowerBoundCheck;
                    upperBound = upperBoundCheck;
                    if (as.representation == END_OF_FILE_SYMBOL) {
                        System.out.println(output);
                        break;
                    }
                }
            }

            while (upperBound < HALF || lowerBound > HALF) { // rescaling
                if (upperBound < HALF) {
                    lowerBound = 2 * lowerBound;
                    upperBound = 2 * upperBound;
                    inputValue = 2 * inputValue;
                } else if (lowerBound > HALF) {
                    lowerBound = 2 * (lowerBound - HALF);
                    upperBound = 2 * (upperBound - HALF);
                    inputValue = 2 * (inputValue - HALF);
                }

                if (inputIndex < limitOfInput && input.charAt(inputIndex) == '1') {
                    inputValue++;
                }
                inputIndex++;
            }

            while (lowerBound > QUARTER && upperBound < 3 * QUARTER) {
                lowerBound = 2 * (lowerBound - QUARTER);
                upperBound = 2 * (upperBound - QUARTER);
                inputValue = 2 * (inputValue - QUARTER);

                if (inputIndex < limitOfInput && input.charAt(inputIndex) == '1') {
                    inputValue++;
                }
                inputIndex++;
            }
        }
    }
}
