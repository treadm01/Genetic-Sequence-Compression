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
        //System.out.println("some kind of loop with careless else wiping??? " + symbol.left.left);
        // think through what has to be done for each condition
        if (symbol.right.isGuard()) {
            last = nonTerminal; //TODO not sure that this is right
          //  System.out.println("some kind of loop with careless else wiping??? " + symbol.left.left);
        }
        else {
            symbol.right.assignLeft(nonTerminal); // moving this here to not reassign the left pointer
            // of the guard to something other than the rule ... is it necessary to have one?
            //System.out.println("some kind of loop with careless else wiping??? " + symbol.left.left);
        }

        //TODO write out in english what is going on here again
        nonTerminal.assignRight(symbol.right);
       // System.out.println("ANYTHING " + symbol.left.left);
        nonTerminal.assignLeft(symbol.left.left); // could be an issue here

        // getting a null when replacing digram with a nonterminal
        // the symbol before the digram is null, coming from create rule, for the first/existing digram
        // so guessing it is the first in a rule
        symbol.left.left.assignRight(nonTerminal);
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