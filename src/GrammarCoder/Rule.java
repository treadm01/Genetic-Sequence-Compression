package GrammarCoder;

import java.util.*;

public class Rule extends Symbol{
    int count;
    private Guard guard;
    static Integer ruleNumber = 0;
    Set<NonTerminal> nonTerminalList;
    private static final long PRIME = 2265539;
    // for decompressing
    Boolean compressed = false;
    Rule removed;

    // for encoding...
    int timeSeen = 0;
    int position;
    int length; // length of compressed rule

    public Rule() {
        this.representation = ruleNumber;
        ruleNumber += 2;
        guard = new Guard(this);
        assignRight(guard);
        assignLeft(guard);
        guard.assignRight(guard);
        guard.assignLeft(guard);
        nonTerminalList = new LinkedHashSet<>();
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

    // like a clone used in search
    public void addAllSymbols(Symbol left) {
        if (left == null) {
            left = left.getRight();
        }
        while (left != null && !left.isGuard()) {
            if (left instanceof Terminal) {
                Terminal t = new Terminal(left.toString().charAt(0));
                addNextSymbol(t);
                left = left.getRight();
            }
            else if (left instanceof NonTerminal) {
                NonTerminal nt = new NonTerminal(((NonTerminal) left).getRule());
                nt.isComplement = left.isComplement;
                addNextSymbol(nt);
                left = left.getRight();
            }
        }
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
     * get string of the rule at nonterminal and terminal level, used in printing rules
     * @return
     */
    public String getRuleString() {
        StringBuilder symbols = new StringBuilder();
        Symbol first = guard.getRight();
        while (!first.isGuard()) {
            symbols.append(" ").append(first.toString());
            first = first.getRight();
        }
        return symbols.toString();
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
            else {
                int currentLength = output.length(); // length before start of edited rule
                if (complement) {
                    output.append(getSymbolString(((NonTerminal) s).getRule(), !s.isComplement));
                }
                else {
                    output.append(getSymbolString(((NonTerminal) s).getRule(), s.isComplement));
                }

                if (s.isEdited) {
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

    void incrementCount() { count++;}

    void decrementCount() { count--;}

}