public class Rule extends Symbol implements Comparable {
    Symbol last;
    int count;
    Guard actualGuard;

    public Rule(Integer ruleNumber) {
        this.representation = String.valueOf(ruleNumber);
        actualGuard = new Guard("?");
        actualGuard.assignLeft(this); // reference back to rule
        assignLeft(actualGuard); // seems necessary for hashcode check, to check left symbol, use to point to guard
        //assignRight(actualGuard);
        last = actualGuard; // right right?
        //not going to assign guard.right
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        //TODO write out in english what is going on here again
        symbol.assignLeft(last);
        symbol.assignRight(actualGuard); // todo so this only ever adds at the end??
        last.assignRight(symbol);
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
    public void updateNonTerminal(NonTerminal nonTerminal, Symbol symbol) {
        //TODO need to clean up and make sure doing everything correctly

        // TODO need to clean up everything... issue here where there seems to be some loop between nonterminals
        if (symbol.right.isGuard()) {
            last = nonTerminal; //TODO not sure that this is right
        }
        else {
            symbol.right.assignLeft(nonTerminal);
        }
        symbol.left.left.assignRight(nonTerminal);

        // not sure if below is better as vacg test just hangins probably in generate rules
        //TODO swapped order of nontermian and symbol assignment to stop symbol.leftleft being null

        //TODO write out in english what is going on here again
        nonTerminal.assignRight(symbol.right);
        nonTerminal.assignLeft(symbol.left.left); // could be an issue here
    }

    /**
     * return the last element of the list, not the buffer
     * @return
     */
    public Symbol getLast() {
        return last;
    }

    @Override
    public int compareTo(Object o) {
        Rule rule = (Rule) o;
        if (rule.count < count) {
            return -1;
        }

        if (rule.count > count) {
            return 1;
        }

        return 0;
    }

//    @Override
//    public void assignLeft(Guard guard) {
//        this.left = guard;
//    }
}