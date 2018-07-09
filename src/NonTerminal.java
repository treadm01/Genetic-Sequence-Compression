public class NonTerminal extends Symbol {
    // keep reference to nonTerminal this occurs in??
    Rule rule; // the nonTerminal the rule points to

    public NonTerminal(Rule rule) {
        this.rule = rule;
        this.rule.count++; // increase use count
        representation = rule.representation; // rule has the same symbol rep as it's nonterminal...
    }

    /**
     * assign the links of those elements this rule points to to the
     * elements either side of this rule
     */
    public void removeRule() {
        //TODO need to ensure all links are working correctly
        // if nonterminal being removed is not at the end of a rule or start:
        // update right.left and left.right
        if (!this.right.isGuard()) {
            right.assignLeft(rule.getLast());
            left.assignRight(rule.actualGuard.right);
        }
        else {
            left.assignRight(rule.actualGuard.right);
        }

        rule.getLast().assignRight(right); // set right symbol of current last symbol in rule to this symbols right
        //TODO similar problem as below, this seems like a bug, first element should never be a guard
        // unless it is empty or something else has gone wrong
        if (!rule.actualGuard.right.isGuard()) {
            rule.actualGuard.right.assignLeft(left); // set first symbol in rule's left to this left
        }
    }

    public Rule getRule() {
        return this.rule;
    }
}