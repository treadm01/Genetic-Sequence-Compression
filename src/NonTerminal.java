public class NonTerminal extends Symbol implements Cloneable {
    Rule rule;

    public NonTerminal(Rule rule) {
        this.rule = rule;
        this.rule.count++;
        this.representation = String.valueOf(this.rule.ruleNumber);
    }

    /**
     * assign the links of those elements this rule points to to the
     * elements either side of this rule
     */
    public void removeRule() {
        //TODO not sure this is setting ALL of the links
        right.left = rule.left; // symbol currently right of nonterminals left, set to the last element of rule
        left.right = rule.guard.right; // right of left set to first of rule
        rule.left.right = right; // last of rules right assigned to this places right
        rule.guard.right.left = left;
        //TODO relink the first elements of rule left???
    }
}
