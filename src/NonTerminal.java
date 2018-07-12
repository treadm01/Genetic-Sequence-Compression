public class NonTerminal extends Symbol {
    Rule rule; // the nonTerminal the rule points to

    public NonTerminal(Rule rule) {
        this.rule = rule;
        this.rule.incrementCount(); // increase use count
        representation = rule.representation; // rule has the same symbol rep as it's nonterminal...
    }

    /**
     * assign the links of those elements this rule points to to the
     * elements either side of this rule
     */
    public void removeRule() {
        right.assignLeft(rule.getLast());
        left.assignRight(rule.getGuard().right);

        rule.getLast().assignRight(right); // set right symbol of current last symbol in rule to this symbols right
        rule.getGuard().right.assignLeft(left); // set first symbol in rule's left to this left
    }

    public Rule getRule() {
        return this.rule;
    }
}