public class Rule extends Symbol {
    //Symbol last;
    int count;
    Guard actualGuard;

    public Rule(Integer ruleNumber) {
        this.representation = String.valueOf(ruleNumber);
        actualGuard = new Guard("?");
        actualGuard.guardRule = this;
        assignRight(actualGuard);
        assignLeft(actualGuard); // seems necessary for hashcode check, to check left symbol, use to point to guard
        actualGuard.assignRight(actualGuard);
        actualGuard.assignLeft(actualGuard);

        if (representation.equals("4053")) {
            System.out.println("init");
        }
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        //TODO write out in english what is going on here again
        symbol.assignLeft(actualGuard.left);
        symbol.assignRight(actualGuard); // todo so this only ever adds at the end??
        actualGuard.left.assignRight(symbol);
        actualGuard.assignLeft(symbol);

        if (representation.equals("4053")) {
            System.out.println("add next symbol" + symbol);
        }
    }

    /**
     * add the two symbols from a digram to a nonTerminal
     * @param left
     * @param right
     */
    public void addSymbols(Symbol left, Symbol right) {
        if (representation.equals("4053")) {
            System.out.println("ADD SYMBOLS");
        }
        this.addNextSymbol(left);
        this.addNextSymbol(right);
    }

//    /**
//     * update this nonTerminal/rules digram with a nonTerminal
//     * @param nonTerminal
//     * @param symbol
//     */
//    public void updateNonTerminal(NonTerminal nonTerminal, Symbol symbol) {
//        //TODO write out in english what is going on here again
//        // assign links to nonterminal
//
//        if (representation.equals("4053")) {
//            System.out.println("update nonterminal");
//        }
//
//        nonTerminal.assignRight(symbol.right);
//        nonTerminal.assignLeft(symbol.left.left); // could be an issue here
//
//        // reassign links of symbols either side
//        //TODO need to clean up and make sure doing everything correctly
//        symbol.left.left.assignRight(nonTerminal);
//        symbol.right.assignLeft(nonTerminal);
//
//    }

    /**
     * return the last element of the list, not the buffer
     * @return
     */
    public Symbol getLast() {
        return actualGuard.left;
    }
}