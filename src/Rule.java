public class Rule extends Symbol {
    Symbol guard, last;
    int count;

    public Rule(Integer ruleNumber) {
        this.representation = String.valueOf(ruleNumber);
        left = new Terminal("?", ruleNumber);
        right = new Terminal("?", ruleNumber);
        guard = new Terminal("!", ruleNumber);
        guard.left = left;
        guard.right = right;
        last = guard.left;
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        //TODO write out in english what is going on here again
        symbol.left = last;
        symbol.right = guard.right;
        last.right = symbol;

        last = symbol;
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
     * @param rule
     * @param symbol
     */
    public void updateNonTerminal(NonTerminal rule, Symbol symbol) {
        if (symbol == last) {
            last = rule;
        }

        //TODO write out in english what is going on here again
        rule.right = symbol.right;
        rule.left = symbol.left.left;

        symbol.right.left = rule;
        symbol.left.left.right = rule;
    }

    /**
     * return the last element of the list, not the buffer
     * @return
     */
    public Symbol getLast() {
        return last;
    }
}