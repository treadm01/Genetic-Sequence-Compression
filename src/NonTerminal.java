public class NonTerminal extends Symbol {
    // keep reference to nonTerminal this occurs in??
    Rule rule; // the nonTerminal the rule points to

    public NonTerminal(Rule rule, int containingRule) {
        this.containingRule = containingRule;
        this.rule = rule;
        this.rule.count++; // increase use count
        representation = rule.representation; // rule has the same symbol rep as it's nonterminal...
    }

    /**
     * assign the links of those elements this rule points to to the
     * elements either side of this rule
     */
    public void removeRule() {
        rule.last.right = right;
        rule.guard.left.right.left = left;

        right.left = rule.last;
        left.right = rule.guard.left.right;
    }
}