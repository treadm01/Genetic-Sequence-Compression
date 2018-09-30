package GrammarCoder;

import java.util.*;

public class ImplicitEncoder {
    private int markerNum = 0; // used for implicit numbering of nonterminals
    private List<Character> encodingSymbols; // list of symbols required to be encoded by arithmetic
    private List<Integer> adjustedMarkers; // for encoding index of rule created used rather than symbol
    public int highestRule; // used for arithmetic coding highest rule will be the number of symbols needed
    private Set<Character> uniqueSymbols = Set.of('*', '!', '?', '%', '[', '{', '\'', '^', '#');

    public ImplicitEncoder(Rule grammar) {
        encodingSymbols = new ArrayList<>();
        adjustedMarkers = new ArrayList<>();
        getEncodingSymbols(grammar.getFirst());

        //highest rule gathered below for the correct size of int array in binary compression
        for (Character c : encodingSymbols) {
            if (c > highestRule) {
                highestRule = c;
            }
        }
    }

    /**
     * traverse through the grammar processing terminals and nonterminals
     * for the corresponding implicitly encoded stream
     * @param symbol
     */
    private void getEncodingSymbols(Symbol symbol) {
        Symbol current = symbol;
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                NonTerminal nt = (NonTerminal) current;
                // nonterminal seen for first time in the grammar
                if (nt.rule.timeSeen == 0) {
                    nt.rule.timeSeen++; // count for number of times rule has been seen
                    nt.rule.position = markerNum; // 'position' really an indicator of the marker assigned to it
                    adjustedMarkers.add(markerNum); // add number for index of list, when removed, corresponds with list
                    markerNum += 2; // implicit symbol for nonterminal, incremented by two for use with binary encoding method

                    // rule length used for implicit rule construction
                    int length = nt.getRule().getRuleLength();
                    if (length == 2) { // frequently of length 2, so can send a single symbol to reduce characters
                        encodingSymbols.add('^');
                    }
                    else {
                        encodingSymbols.add('#');
                        encodingSymbols.add((char)length);
                    }

                    // have to add edits for rules that are first time seen with edits
                    if (nt.getIsEdited()) {
                        addEdits(nt.editList);
                    }

                    // if symbol is a nonterminal need to process its rule
                    getEncodingSymbols(nt.getRule().getGuard().getRight());
                }
                else if (nt.rule.timeSeen == 1) { // nonterminal seen again, use index in list, and provide necessary unique symbols
                    int index = adjustedMarkers.indexOf(nt.getRule().position);
                    getImplicitSymbolList(index, nt);
                    adjustedMarkers.remove(index);// remove when used
                    nt.getRule().timeSeen++; // count for number of times rule has been seen
                }
                else { // nonterminal seen any more times can just send the symbol
                    getImplicitSymbolList(nt.getRule().position, nt);
                }
            }
            else { // if terminal symbol add directly
                if (uniqueSymbols.contains(current.toString().charAt(0))) {
                    encodingSymbols.add((char) 0);
                }
                encodingSymbols.add(current.toString().charAt(0));
            }
            current = current.getRight(); // move to next symbol
        }
    }


    /**
     * retrieve individual variables for each edit of a nonterminal
     * and add to the stream list
     * @param editList
     */
    private void addEdits(List<Edit> editList) {
        for (Edit e : editList) {
            encodingSymbols.add('*'); // has to be added each time for arithmetic coding
            encodingSymbols.add((char) e.index);
            encodingSymbols.add(e.symbol.charAt(0));
        }
    }

    /**
     * processes indicator symbols for nonterminals seen the second time
     * including whether or not they are reverse complements
     * @param nt
     * @param index
     * @return
     */
    private List<Character> nonterminalSeenOnce(NonTerminal nt, int index) {
        List<Character> nonterminalSymbols = new ArrayList<>();
        char complementIndicator;
        if (index == 1) { // frequency of index being 1 enough to send unique symbol and achieve better compression
            if (nt.getIsComplement()) {
                complementIndicator = '%';
            }
            else {
                complementIndicator = '[';
            }
            nonterminalSymbols.add(complementIndicator);
        }
        else {
            if (nt.getIsComplement()) {
                complementIndicator = '{';
            }
            else {
                complementIndicator = '?';
            }
            nonterminalSymbols.add(complementIndicator);
            nonterminalSymbols.add((char)index);
        }
        return nonterminalSymbols;
    }

    /**
     * process indicator symbols for nonterminals seen more than twice
     * @param nt
     * @param position
     * @return
     */
    private List<Character> nonterminalSeenMoreThanOnce(NonTerminal nt, int position) {
        List<Character> nonterminalSymbols = new ArrayList<>();
        char complementIndicator;
        if (nt.getIsComplement()) {
            complementIndicator = '\'';
        }
        else { // if standard still needs an indicator
            complementIndicator = '!';
        }
        nonterminalSymbols.add(complementIndicator);
        nonterminalSymbols.add((char) position);
        return nonterminalSymbols;
    }


    /**
     * process correct symbols for the nonterminals seen in a grammar depending on
     * the number of times they have been seen. Also deal with any potential
     * edits in the nonterminals.
     * @param index
     * @param nt
     */
    private void getImplicitSymbolList(int index, NonTerminal nt) {
        if (nt.rule.timeSeen == 1) {
            encodingSymbols.addAll(nonterminalSeenOnce(nt, index));
        }
        else {
            encodingSymbols.addAll(nonterminalSeenMoreThanOnce(nt, index));
        }

        if (nt.getIsEdited()) {
            addEdits(nt.editList);
        }
    }

    /**
     * returns string of all symbols of encoding
     * used for debugging implicit stream without binary compression
     * @return
     */
    private String encode() {
        StringBuilder output = new StringBuilder();
        for (Character s : getSymbolList()) {
            output.append(s);
        }
        return output.toString();
    }

    public String getEncodedOutput() {
        return encode();
    }

    public List<Character> getSymbolList() {
        return encodingSymbols;
    }
}