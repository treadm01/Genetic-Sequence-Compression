public class NonTerminal extends Symbol implements Cloneable {
    Symbol guard, last;
    Rule rule;

    public NonTerminal(Rule rule) {
        this.rule = rule;
        this.representation = String.valueOf(this.rule.ruleNumber);
        left = new Terminal("?");
        right = new Terminal("?");
        guard = new Terminal("!");
        guard.left = left;
        guard.right = right;
        last = guard.left;
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
//    public void addNextSymbol(Symbol symbol) {
//        //TODO write out in english what is going on here again
//        symbol.left = last;
//        symbol.right = guard.right;
//        last.right = symbol;
//
//        last = symbol;
//    }

    /**
     * add the two symbols from a digram to a nonTerminal
     * @param left
     * @param right
     */
//    public void addSymbols(Symbol left, Symbol right) {
//        this.addNextSymbol(left);
//        this.addNextSymbol(right);
//    }

    /**
     * update this nonTerminal/rules digram with a nonTerminal
     * @param symbol
     */
//    public void updateNonTerminal(NonTerminal nonTerminal, Symbol symbol) {
//        if (symbol == left) { // if symbol is last update last to the new nonterminal
//            left = nonTerminal;
//        }
//
//        //TODO write out in english what is going on here again
//        nonTerminal.right = symbol.right.right;
//        nonTerminal.left = symbol.left;
//
//        symbol.right.right.left = nonTerminal;
//        symbol.left.right = nonTerminal;
//    }

    /**
     * return the last element of the list, not the buffer
     * @return
     */
//    public Symbol getLast() {
//        return last;
//    }
}
