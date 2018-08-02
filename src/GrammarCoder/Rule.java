package GrammarCoder;

public class Rule extends Symbol implements Comparable {
    int count;
    Guard guard;
    static Integer ruleNumber = 0;

    // for decompressing
    Boolean compressed = false;

    // for encoding...
    int timeSeen = 0;
    int position;
    int length; // length of compressed rule
    String symbolRule; // length of uncompressed rule

    public Rule() {
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

    public Guard getGuard() { return guard; }

    public int getCount() { return count; }

    public void incrementCount() { count++;}

    public void decrementCount() { count--;}


    /**
     * for debugging can get string output of an entire rule
     * @return
     */
    public String getRuleString() {
        String symbols = "";
        Symbol first = guard.getRight();
        while (!first.isGuard()) {
            symbols += first.toString();
            first = first.getRight();
        }
        return symbols;
    }

    // get the actual symbols string the rule encodes - this won't work for reverse complements at the moment
    public String getSymbolString(Symbol symbol) {
        String symbols = "";
        Symbol first = symbol;//guard.getRight();
        while (!first.isGuard()) {
            if (first instanceof NonTerminal) {
                symbols += getSymbolString(((NonTerminal) first).getRule().getGuard().getRight());
            }
            else {
                symbols += first.toString();
            }
            first = first.getRight();
        }
        return symbols;
    }

    @Override
    public int compareTo(Object o) {
        Rule r = (Rule) o;
        if (r.symbolRule.length() > symbolRule.length()) {
            return 1;
        }
        else if (r.symbolRule.length() < symbolRule.length()) {
            return -1;
        }
        return 0;
    }
}