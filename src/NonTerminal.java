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
//        System.out.println("rule first " + rule.actualGuard.right);
//        System.out.println("rule last " + rule.getLast());

        rule.getLast().assignRight(right); // set right symbol of current last symbol in rule to this symbols right
        //TODO same problem as below
        if (!rule.actualGuard.right.isGuard()) {
            rule.actualGuard.right.assignLeft(left); // set first symbol in rule's left to this left
        }

        //TODO another guard check - basically removing a terminal that is last in a rule causes a crash
        // if the terminal is last in a rule you don't want to assign it's rights left, there is no way back
        // from guard to last
        if (!this.right.isGuard()) {
            right.assignLeft(rule.getLast());
        }
        left.assignRight(rule.actualGuard.right);
    }

    public Rule getRule() {
        return this.rule;
    }
}