public class Rule extends Symbol {
    //Symbol last;
    int count;
    Guard actualGuard;

    public Rule(Integer ruleNumber) {
        this.representation = String.valueOf(ruleNumber);
        actualGuard = new Guard("?");
        actualGuard.guardRule = this;
        //actualGuard.assignLeft(this); // reference back to rule
        assignRight(actualGuard);
        assignLeft(actualGuard); // seems necessary for hashcode check, to check left symbol, use to point to guard
        //assignRight(actualGuard);
        actualGuard.right = actualGuard;
        actualGuard.left = actualGuard;
        //not going to assign guard.right
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        //TODO write out in english what is going on here again
        symbol.assignLeft(actualGuard.left);
        symbol.assignRight(actualGuard); // todo so this only ever adds at the end??
        actualGuard.left.right = symbol;
        actualGuard.assignLeft(symbol);
        //last = symbol;
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
     * update this nonTerminal/rules digram with a nonTerminal
     * @param nonTerminal
     * @param symbol
     */
    public void updateNonTerminal(NonTerminal nonTerminal, Symbol symbol) {
        //TODO write out in english what is going on here again
        // assign links to nonterminal
        nonTerminal.assignRight(symbol.right);
        nonTerminal.assignLeft(symbol.left.left); // could be an issue here

        // reassign links of symbols either side
        //TODO need to clean up and make sure doing everything correctly
        symbol.left.left.assignRight(nonTerminal);
        symbol.right.assignLeft(nonTerminal);

    }

    /**
     * return the last element of the list, not the buffer
     * @return
     */
    public Symbol getLast() {
        return actualGuard.left;
    }

    public void setLast(Symbol symbol) {
        actualGuard.left = symbol;
    }
}