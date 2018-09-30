package GrammarCoder;

import java.util.HashMap;
import java.util.Map;

public class Terminal extends Symbol {
    private long originalValue; // the original symbol from the input, representation is then assigned an odd number
    private static final Map<Character, Character> COMPLEMENTS = new HashMap<>();
    static {
        COMPLEMENTS.put('a', 't');
        COMPLEMENTS.put('t', 'a');
        COMPLEMENTS.put('c', 'g');
        COMPLEMENTS.put('g', 'c');
        COMPLEMENTS.put('A', 'T');
        COMPLEMENTS.put('T', 'A');
        COMPLEMENTS.put('C', 'G');
        COMPLEMENTS.put('G', 'C');
    } //initialize values for reverse complements in map

    public Terminal(long representation) {
        originalValue = representation;
        this.representation = (representation * 2) + 1; //terminals assigned an odd number, nonterminals even
        left = new Symbol();
    }

    @Override
    public String toString() {
        return String.valueOf((char) originalValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Terminal)) {
            return false;
        }
        else {
            Terminal symbol = (Terminal) obj;
            if (symbol.getLeft() instanceof NonTerminal) {
                // if digram contains a nonterminal on the left, additional checks for if it is a
                // reverse complement are required
                return ((representation == symbol.getRepresentation())
                        && (left.representation == (symbol.getLeft().getRepresentation()))
                        && left.isComplement == symbol.getLeft().isComplement
                );
            } else {
                return super.equals(obj);
            }
        }
    }

    /**
     * Returns the complement symbol of the base symbols of a,c,g and t
     * for upper and lower case or the original symbol if not applicable
     * @param symbol - any of
     * @return
     */
    public static char reverseSymbol(char symbol) {
        char complement = symbol;
        if (COMPLEMENTS.containsKey(complement)) {
            complement = COMPLEMENTS.get(complement);
        }
        return complement;
    }
}
