package GrammarCoder;

import java.util.HashMap;
import java.util.Map;

public class DigramMap {
    public Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol

    public DigramMap() {
        digramMap = new HashMap<>();
    }

    public void addToDigramMap(Symbol symbol) {
        digramMap.putIfAbsent(symbol, symbol);
    }

    public Boolean existingDigram(Symbol symbol) {
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
     * when a digram has occured but was first entered as a reverse complement, ie never seen
     * the original digram it was created from needs to be used for the correct links and location
     * @return
     */
    public Symbol getOriginalDigram(Symbol digram) {
        Symbol symbol = getExistingDigram(digram);
        // if digram was created as complement, should have no right hand symbol
        if (symbol.getRight() == null) {//symbol.isComplement) { // can't really use is complement in this way as nonterminals might be part of a complement digram, but not a complement
            symbol = getExistingDigram(symbol.getLeft().complement);
        }
        return symbol;
    }

    public void removeDigrams(Symbol digram) {
        digramMap.remove(digram);
        //todo creating via getReveseComplement to remove, if created with the objects could add that way too
        //todo removing complement, even if reverse still exists....
        digramMap.remove(digram.getReverseComplement()); // can't just access the link?
    }


    /**
     * when nonterminals are added or removed the old digrams must be removed from the map
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

        if (!symbol.getRight().equals(symbol)) { // should this be getright.getrigh? both
            removeDigrams(symbol.getRight());

            // if removed a digram that was overlapping with itself //todo don't remove rather than re -add
            if (symbol.getRight().equals(symbol.getRight().getRight())) {
                addToDigramMap(symbol.getRight().getRight());
                // whenever adding, add reverse complement
                Symbol reverse = symbol.getRight().getRight().getReverseComplement();
                addToDigramMap(reverse);
            }
        }
    }

    /**
     * prints out all the digrams added to the digram map
     */
    public String printDigrams() {
        String output = "";
        for (Symbol s : digramMap.values()) {
            output += s.getLeft() + " " + s + ", ";
        }
        return output;
    }

}
