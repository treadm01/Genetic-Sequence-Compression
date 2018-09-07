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

    @Override
    public String toString() {
        String s = String.valueOf(representation);
        if (isComplement) {
            s += "'";
        }
        s += getEdits();
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
            );
        }
        else {
            return ((representation == symbol.getRepresentation())
                    && (left.representation == (symbol.left.getRepresentation()))
                    && this.isComplement == symbol.isComplement
            );
        }
    }

    public void updateEdits(Symbol digramRight) {
        if (digramRight.isEdited) {
            this.setIsEdit(digramRight.editList);
        }
        if (digramRight.getLeft().isEdited) {
            this.setIsEdit(digramRight.getLeft().editList);
        }
    }

    public String getEdits() {
        String s = "";
        if (isEdited) {
            for (Edit e : editList) {
                s += "*" + e.index + e.symbol;
            }
        }
        return s;
    }


}