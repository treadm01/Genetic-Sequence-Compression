public class Rule extends Symbol {
    // keep reference to nonTerminal this occurs in??
    //NonTerminal nonTerminal; // the nonTerminal the rule points to
    Symbol guard;
    Integer ruleNumber;
    int count;


    public Rule(Integer ruleNumber) {
        this.ruleNumber = ruleNumber;
        this.representation = String.valueOf(this.ruleNumber); //TODO doing too often....
        guard = new Symbol();
        guard.representation = "?";
        guard.right = guard;
        guard.left = this; // keep a reference to get to this rule....
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

        symbol.right = guard; // set new symbols right back to guard
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
     * update this nonTerminal/rules digram with a nonTerminal
     * @param symbol
     */
    public void updateNonTerminal(NonTerminal nonTerminal, Symbol symbol) {
        if (symbol.right == left) { // if symbol is last update last to the new nonterminal
            left = nonTerminal;
        }

        //TODO write out in english what is going on here again
        nonTerminal.right = symbol.right.right;
        nonTerminal.left = symbol.left;

        symbol.right.right.left = nonTerminal;
        symbol.left.right = nonTerminal;
    }

    /**
     * return the last element of the list, not the buffer
     * @return
     */
    public Symbol getLast() {
        return left;
    }
}
