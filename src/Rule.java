public class Rule extends Symbol {
    //Symbol last;
    int count;
    Guard actualGuard;
    static Integer ruleNumber = 0;

    public Rule() {
        this.representation = String.valueOf(ruleNumber);
        ruleNumber++;
        actualGuard = new Guard("?");
        actualGuard.guardRule = this;
        assignRight(actualGuard);
        assignLeft(actualGuard); // seems necessary for hashcode check, to check left symbol, use to point to guard
        actualGuard.assignRight(actualGuard);
        actualGuard.assignLeft(actualGuard);
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        symbol.assignLeft(actualGuard.left); // left of symbol is current last, actualguard.left
        symbol.assignRight(actualGuard); // symbol right should be actual guard
        actualGuard.left.assignRight(symbol); // assign current last right to this symbol
        actualGuard.assignLeft(symbol); // assign last to symbol
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
     * return the last element of the list, not the buffer
     * @return
     */
    public Symbol getLast() {
        return actualGuard.left;
    }
}