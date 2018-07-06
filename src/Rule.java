public class Rule extends Symbol {
    // keep reference to nonTerminal this occurs in??
    NonTerminal nonTerminal; // the nonTerminal the rule points to
    Symbol guard;


    public Rule() {
        guard = new Symbol();
        guard.right = guard;
        right = guard; // always points to guard
        left = guard; // end pointer that shifts
//        this.containingRule = containingRule;
//        this.nonTerminal = nonTerminal;
//        this.nonTerminal.count++; // increase use count
//        representation = nonTerminal.representation; // rule has the same symbol rep as it's nonterminal...
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        //TODO write out in english what is going on here again
        symbol.right = guard.right; // set new symbols right back to guard
        symbol.left = left;
        left.right = symbol; // set current ends right to new symbol
        left = symbol; // set left of guard, or end to new symbol
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
     * assign the links of those elements this rule points to to the
     * elements either side of this rule
     */
    public void removeRule() {
        nonTerminal.last.right = right;
        nonTerminal.guard.left.right.left = left;

        right.left = nonTerminal.last;
        left.right = nonTerminal.guard.left.right;
    }

    /**
     * return the last element of the list, not the buffer
     * @return
     */
    public Symbol getLast() {
        return left;
    }
}
