package GrammarCoder;

import java.util.HashMap;
import java.util.Map;

public class DigramMap {
    private Map<Symbol, Symbol> digramMap;

    public DigramMap() {
        digramMap = new HashMap<>();
    }

    private void addToDigramMap(Symbol symbol) {
        digramMap.putIfAbsent(symbol, symbol);
    }

    public Boolean containsDigram(Symbol symbol) {
        return digramMap.containsKey(symbol);
    }

    public void addNewDigrams(Symbol symbol) {
        addToDigramMap(symbol);
        addToDigramMap(symbol.getReverseComplement());
    }

    public Symbol getExistingDigram(Symbol symbol) {
        return digramMap.get(symbol);
    }

    /**
     * when a digram has occurred but was first entered as a reverse complement, ie never explicitly seen,
     * the original digram it was created from needs to be used for the correct links and location
     * @return
     */
    public Symbol getOriginalDigram(Symbol digram) {
        Symbol symbol = getExistingDigram(digram);
        // if digram was created as complement, should have no right hand symbol..
        if (symbol.getRight() == null) {
            symbol = getExistingDigram(symbol.getLeft().complement);
        }
        return symbol;
    }

    /**
     * remove digram and corresponding reverse complement digram
     * @param digram
     */
    public void removeDigrams(Symbol digram) {
        digramMap.remove(digram);
        digramMap.remove(digram.getReverseComplement());
    }


    /**
     * when nonterminals are added or removed the old digrams either side must be removed from the map,
     * currently requires some extra checks for ensuring that the digrams being removed do not
     * correspond with the same digram that is overlapping
     * @param symbol
     */
    public void removeDigramsFromMap(Symbol symbol) {
        // don't remove digram if of an overlapping digram
        if (digramMap.containsKey(symbol.getLeft())){ // if it's in there and its not overlapping with a rule that you would want to keep, then remove it
            Symbol existing = digramMap.get(symbol.getLeft());
            if (existing == symbol.getLeft()) {
                removeDigrams(symbol.getLeft());
            }
        }


        if (!symbol.getRight().equals(symbol)) {
            removeDigrams(symbol.getRight());
            // todo don't remove rather than re-add
            // if removed a digram that was overlapping with itself
            if (symbol.getRight().equals(symbol.getRight().getRight())) {
                addToDigramMap(symbol.getRight().getRight());
                // whenever re-adding, add reverse complement too
                Symbol reverse = symbol.getRight().getRight().getReverseComplement();
                addToDigramMap(reverse);
            }
        }
    }

    public int getSize() {
        return digramMap.size();
    }

    /**
     * prints out all the digrams added to the digram map - used for debugging
     */
    public String printDigrams() {
        StringBuilder output = new StringBuilder();
        for (Symbol s : digramMap.values()) {
            output.append(s.getLeft()).append(" ").append(s).append(", ");
        }
        return output.toString();
    }

    /**
     * return the representation of a rule a symbol occurs in
     * @param symbol
     * @return
     */
    public long getRuleNumber(Symbol symbol) {
        while (!symbol.isGuard()) {
            symbol = symbol.getRight();
        }
        return ((Guard) symbol).getGuardRule().getRepresentation();
    }

}
