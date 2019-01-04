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
      //  System.out.println("removing digram "+ digram);
        digramMap.remove(digram);
        digramMap.remove(digram.getReverseComplement());
    }


    /**
     * when nonterminals are added or removed the old digrams either side must be removed from the map,
     * currently requires some extra checks for ensuring that the digrams being removed do not
     * correspond with the same digram that is overlapping
     * @param symbol
     */
    //TODO proper implementation of removing and managing the digram map when doing so for left and right
    // dirgrams are removed for each digram being checked
    // need to check left and right for each - break in to two, one for left and right
    public void removeDigramsFromMap(Symbol symbol) {
        // don't remove digram if of an overlapping digram
        //todo it is in abc example, tt to left removed although tt overlap
        Symbol leftDigram = symbol.getLeft();
        Symbol rightDigram = symbol.getRight();
//        System.out.println("ld " + leftDigram.getLeft() +  leftDigram);
//        System.out.println("lld " + leftDigram.getLeft().getLeft() + leftDigram.getLeft());
//        System.out.println(leftDigram.equals(leftDigram.getLeft()));

        // is digram in map
        // if it's overlapping and not the one in map then don't remove
        // if its overlapping and the one in map then remove and add the overlap

        //&& !leftDigram.equals(leftDigram.getLeft())
        if (digramMap.containsKey(leftDigram)){ // if it's in there and its not overlapping with a rule that you would want to keep, then remove it
            Symbol existing = digramMap.get(leftDigram);
            //&& !symbol.getLeft().equals(symbol.getLeft().getLeft())
            if (leftDigram.equals(leftDigram.getLeft())) {
                if (existing == leftDigram) {
                    removeDigrams(leftDigram);
                    addNewDigrams(leftDigram.getLeft());
                }
            }
            else {
                removeDigrams(leftDigram);
            }
        }

//        if (digramMap.containsKey(symbol.getRight()) && !symbol.getRight().equals(symbol.getRight().getRight())){ // if it's in there and its not overlapping with a rule that you would want to keep, then remove it
//            Symbol existing = digramMap.get(symbol.getRight());
//            //&& !symbol.getLeft().equals(symbol.getLeft().getLeft())
//            if (existing == symbol.getRight() ) {
//                removeDigrams(symbol.getRight());
//            }
//        }

        if (!rightDigram.equals(symbol)) {
            //System.out.println("RIGHT");
            removeDigrams(rightDigram);
            // todo don't remove rather than re-add
            // if removed a digram that was overlapping with itself
            if (rightDigram.equals(rightDigram.getRight())) {
                addToDigramMap(rightDigram.getRight());
                // whenever re-adding, add reverse complement too
                Symbol reverse = rightDigram.getRight().getReverseComplement();
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
