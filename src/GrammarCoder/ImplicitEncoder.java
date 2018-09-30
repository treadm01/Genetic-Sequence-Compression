package GrammarCoder;

import java.util.*;

public class ImplicitEncoder {
    private int markerNum = 0;
    private List<Character> encodingSymbols; // list of symbols required to be encoded by arithmetic
    private List<Integer> adjustedMarkers; // for encoding index of rule created used rather than symbol
    public int highestRule; // used for arithmetic coding highest rule will be the number of symbols needed
    private String encodedOutput;
    public Map<String, Integer> allSymbols = new HashMap<>();
    private Set<Character> uniqueSymbols = Set.of('*', '!', '?', '%', '[', '{', '\'', '^', '#');

    public ImplicitEncoder(Rule grammar) {
        encodingSymbols = new ArrayList<>();
        adjustedMarkers = new ArrayList<>();
        getEncodingSymbols(grammar.getFirst());
        encodedOutput = encode();

        for (Character c : encodingSymbols) {
            if (c > highestRule) {
                highestRule = c;
            }
        }
    }

    private String encode() {
        StringBuilder output = new StringBuilder();
        for (Character s : getSymbolList()) {
            output.append(s);
        }
        return output.toString();
    }

    private void addEdits(List<Edit> editList) {
        for (Edit e : editList) {
            encodingSymbols.add('*'); // has to be added each time for arithmetic coding
            encodingSymbols.add((char)e.index);
            encodingSymbols.add(e.symbol.charAt(0));
        }
    }

    private void getNonTerminalString(int index, NonTerminal nt) {
        if (index > highestRule) {
            highestRule = index;
        }

        char complementIndicator;
        if (nt.rule.timeSeen == 1) { // from the stack of rules rather than the symbol needs separate indicator
            if (nt.getIsComplement()) {
                complementIndicator = '{';
            }
            else { // if standard still needs an indicator
                complementIndicator = '?';
            }

            if (index == 1) {
                if (nt.getIsComplement()) {
                    complementIndicator = '%';
                }
                else {
                    complementIndicator = '[';
                }
                encodingSymbols.add(complementIndicator);
            }
            else {
                encodingSymbols.add(complementIndicator);
                encodingSymbols.add((char)index);
            }
        }
        else {
            if (nt.getIsComplement()) {
                complementIndicator = '\'';
            }
            else { // if standard still needs an indicator
                complementIndicator = '!';
            }
            encodingSymbols.add(complementIndicator);
            encodingSymbols.add((char) index);
        }


        if (nt.getIsEdited()) {
            addEdits(nt.editList);
        }
    }

    private void getEncodingSymbols(Symbol symbol) {
        Symbol current = symbol;
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                NonTerminal nt = (NonTerminal) current;
                if (nt.rule.timeSeen == 0) {
                    nt.rule.timeSeen++; // count for number of times rule has been seen
                    nt.rule.position = markerNum; // 'position' really an indicator of the marker assigne to it
                    adjustedMarkers.add(markerNum); // add number for index of list, when removed, corresponds with list
                    markerNum += 2;
                    int length = nt.getRule().getRuleLength();
                    if (length == 2) {
                        encodingSymbols.add('^');
                    }
                    else {
                        encodingSymbols.add('#');
                        encodingSymbols.add((char)length);
                    }

                    // have to add edits for rules that are first time see with edits
                    if (nt.getIsEdited()) {
                        addEdits(nt.editList);
                    }

                    getEncodingSymbols(nt.getRule().getGuard().getRight()); // if nonterminal need to recurse back
                }
                else if (nt.rule.timeSeen == 1) {
                    int index = adjustedMarkers.indexOf(nt.rule.position);
                    getNonTerminalString(index, nt);
                    adjustedMarkers.remove(index);// remove when used
                    nt.rule.timeSeen++; // count for number of times rule has been seen
                }
                else {
                    getNonTerminalString(nt.rule.position, nt);
                }
            }
            else {
                if (uniqueSymbols.contains(current.toString().charAt(0))) {
                    encodingSymbols.add((char) 0);
                }
                encodingSymbols.add(current.toString().charAt(0));
            }
            current = current.getRight(); // move to next symbol
        }
    }

    public String getEncodedOutput() {
        return encodedOutput;
    }

    public List<Character> getSymbolList() {
        return encodingSymbols;
    }
}