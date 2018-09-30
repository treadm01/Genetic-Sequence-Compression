package GrammarCoder;

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
        left.assignRight(rule.getFirst());

        rule.getLast().assignRight(right); // set right symbol of current last symbol in rule to this symbols right
        rule.getFirst().assignLeft(left); // set first symbol in rule's left to this left
    }

    public Rule getRule() {
        return this.rule;
    }

    /**
     * add edits from merging nonterminals
     * @param digramRight
     */
    void updateEdits(Symbol digramRight) {
        if (digramRight.getIsEdited()) {
            this.setIsEdit(digramRight.editList);
        }
        if (digramRight.getLeft().getIsEdited()) {
            this.setIsEdit(digramRight.getLeft().editList);
        }
    }

    /**
     * returns a string representation of all edits to be included in the
     * implicit stream
     * @return
     */
    private String getEdits() {
        String s = "";
        if (getIsEdited()) {
            for (Edit e : editList) {
                s += "*" + e.index + e.symbol;
            }
        }
        return s;
    }

    @Override
    public String toString() {
        String s = String.valueOf(representation);
        if (getIsComplement()) {
            s += "'";
        }
        s += getEdits();
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NonTerminal)) {
            return false;
        }
        else {
            NonTerminal symbol = (NonTerminal) obj;

            if (symbol.getLeft() instanceof NonTerminal) {
                return ((representation == symbol.getRepresentation())
                        && (left.representation == (symbol.getLeft().getRepresentation()))
                        && this.getIsComplement() == symbol.getIsComplement()
                        && left.getIsComplement() == symbol.getLeft().getIsComplement()
                );
            } else {
                return ((representation == symbol.getRepresentation())
                        && (left.representation == (symbol.getLeft().getRepresentation()))
                        && this.getIsComplement() == symbol.getIsComplement()
                );
            }
        }
    }
}