package GrammarCoder;

import java.util.List;

public class ApproxRepeat implements Comparable {
    List<Symbol> firstSymbolList;
    List<Symbol> secondSymbolList;
    String edits;
    int length; //to compare to each other use longest encoding first

    public ApproxRepeat(List<Symbol> firstSymbols, List<Symbol> secondSymbols, String edits, int length) {
        this.firstSymbolList = firstSymbols;
        this.secondSymbolList = secondSymbols;
        this.edits = edits;
        this.length = length;
    }


    @Override
    public int compareTo(Object o) {
        ApproxRepeat ar = (ApproxRepeat) o;

        if (ar.length > length) {
            return 1;
        }
        else if (ar.length < length) {
            return -1;
        }
        return 0;
    }
}
