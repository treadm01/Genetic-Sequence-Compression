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
        rule.getGuard().getRight().assignLeft(left); // set first symbol in rule's left to this left
    }

    public Rule getRule() {
        return this.rule;
    }

    @Override
    public String toString() {
        String s = super.toString();
        if (isComplement) {
            s += "'";
        }
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        //TODO add all checks
        Symbol symbol = (Symbol) obj;

        if (symbol.getLeft() instanceof NonTerminal) {
            return ((representation == symbol.getRepresentation())
                    && (left.representation == (symbol.left.getRepresentation()))
                    && this.isComplement == symbol.isComplement
                    && left.isComplement == symbol.left.isComplement
            ); // switched check to look at left symbol ra
        }
        else {
            return ((representation == symbol.getRepresentation())
                    && (left.representation == (symbol.left.getRepresentation()))
                    && this.isComplement == symbol.isComplement
            ); // switched check to look at left symbol rather than right
        }
    }
}