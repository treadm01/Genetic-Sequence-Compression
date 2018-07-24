import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class ArithmeticEncoder {

    static final Integer PRECISION = 32; // use long could get closer to 64?
    static final Integer WHOLE = 2 ^ PRECISION;
    static final Integer HALF = WHOLE / 2;
    static final Integer QUARTER = WHOLE / 4;
    static final char END_OF_FILE_SYMBOL = '!';

    Map<Character, ArithmeticSymbol> sourceAlphabet = new HashMap();

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

    public void calculateSymbolProbabilityRatio() {
        Integer denominator = calculateRationalDenominator();
        for (ArithmeticSymbol as : sourceAlphabet.values()) {
            as.setProbabiltiyRatio((double) as.getProbability() / denominator);
        }
    }

    // need r for precision
    //todo how best to store this??
    // guess it depends which value would be used more often
    //todo will probably need rounding, needs to always equal 1
    // have to store real number of ri that creates the probability of of the symbol when divided by the
    // sum of all the ri numbers 0 = 1, 1 = 2, 2 = 3 = r = 6 so
    // 0 = 0.16... 1 = 0.333... 2 = 0.5
    // or to get the values to check
    // 0 = 2, 1 = 4, 2 = 4 R = 10
}
