package GrammarCoder;

import java.util.ArrayList;
import java.util.List;

public class Rule extends Symbol implements Comparable {
    int count;
    Guard guard;
    static Integer ruleNumber = 0;
    List<NonTerminal> nonTerminalList = new ArrayList<>();

    // for decompressing
    Boolean compressed = false;

    // for encoding...
    int timeSeen = 0;
    int position;
    int length; // length of compressed rule
    String symbolRule; // length of uncompressed rule

    public Rule() {
        index = 0; // todo not sure this is used anymore
        this.representation = ruleNumber;
        ruleNumber += 2;
        guard = new Guard(this);
        assignRight(guard);
        assignLeft(guard); // seems necessary for hashcode check, to check left symbol, use to point to guard
        guard.assignRight(guard);
        guard.assignLeft(guard);
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        symbol.assignLeft(guard.left); // left of symbol is current last, actualguard.left
        symbol.assignRight(guard); // symbol right should be actual guard
        guard.left.assignRight(symbol); // assign current last right to this symbol
        guard.assignLeft(symbol); // assign last to symbol

    }

    /**
     * add the two symbols from a digram to a nonTerminal
     * @param left
     * @param right
     */
    public void addSymbols(Symbol left, Symbol right) {
        // set the edits to false so subrule is generic
        left.isEdited = false;
        right.isEdited = false;
        this.addNextSymbol(left);
        this.addNextSymbol(right);
    }

    /**
     * return the last element
     * @return
     */
    public Symbol getLast() {
        return guard.left;
    }

    public Symbol getFirst() {return getGuard().getRight();}

    public Guard getGuard() { return guard; }

    public int getCount() { return count; }

    public void incrementCount() { count++;}

    public void decrementCount() { count--;}


    /**
     * get string of the rule at nonterminal and terminal level, used in printing rules
     * @return
     */
    public String getRuleString() {
        String symbols = "";
        Symbol first = guard.getRight();
        while (!first.isGuard()) {
            symbols += " " + first.toString();
            first = first.getRight();
        }
        return symbols;
    }

    // retrieves rule length at nonterminal level, used in implicit encoding for length of implicit rule
    public int getRuleLength() {
        int ruleLength = 0;
        Symbol current = this.getGuard().getRight();
        while (!current.isGuard()) {
            ruleLength++;
            current = current.getRight();
        }
        return ruleLength;
    }

    /**
     * returns a string of the symbols at the terminal level
     * @param rule
     * @param complement
     * @return
     */
    //todo clean up, used by decompress... better position for pieces if split up
    public String getSymbolString(Rule rule, Boolean complement) {
        Symbol s;
        StringBuilder output = new StringBuilder();
        if (complement) {
            s = rule.getLast();
        }
        else {
            s = rule.getFirst();
        }

        do {
            if (s instanceof Terminal) {
                if (complement) {
                    output.append(Terminal.reverseSymbol(s.toString().charAt(0)));
                }
                else {
                    output.append(s.toString());
                }

                if (complement) {
                    s = s.getLeft();
                }
                else {
                    s = s.getRight();
                }
            }
            else { // IF NONTERMINAL //TODO IF EDIT, THEN GET THE STRING AND DO EDITS AFTERWARDS...
                int currentLength = output.length(); // length before start of edited rule
                if (complement) {
                    output.append(getSymbolString(((NonTerminal) s).getRule(), !s.isComplement));
                }
                else {
                    output.append(getSymbolString(((NonTerminal) s).getRule(), s.isComplement));
                }

                if (s.isEdited) {
//                    System.out.println(currentLength);
//                    System.out.println(output);
                    for (Edit e : (((NonTerminal)s).editList)) {
                        output.replace(currentLength + e.index, currentLength + e.index + 1, e.symbol);
                    }
                }

                if (complement) {
                    s = s.getLeft();
                }
                else {
                    s = s.getRight();
                }
            }

        } while (!s.isGuard());
        return output.toString();
    }


    @Override
    public int compareTo(Object o) {
        Rule compareRule = (Rule) o;
        if (count < compareRule.count) {
            return 1;
        }

        else if (count == compareRule.count) {
            return 0;
        }
        return -1;
    }
//    public int compareTo(Object o) {
//        Rule r = (Rule) o;
//        if (r.symbolRule.length() > symbolRule.length()) {
//            return 1;
//        }
//        else if (r.symbolRule.length() < symbolRule.length()) {
//            return -1;
//        }
//        return 0;
//    }
}