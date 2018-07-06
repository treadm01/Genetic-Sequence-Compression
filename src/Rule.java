public class Rule extends Symbol {
    // keep reference to nonTerminal this occurs in??
    NonTerminal nonTerminal; // the nonTerminal the rule points to


    public Rule(NonTerminal nonTerminal, int containingRule) {
        Symbol guard = new Symbol();
        guard.right = guard;
        right = guard;
        this.containingRule = containingRule;
        this.nonTerminal = nonTerminal;
        this.nonTerminal.count++; // increase use count
        representation = nonTerminal.representation; // rule has the same symbol rep as it's nonterminal...
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
}
