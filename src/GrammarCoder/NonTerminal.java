package GrammarCoder;

public class NonTerminal extends Symbol {
    Rule rule; // the nonTerminal the rule points to
    //Integer index; // location of rule in main input string

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
        String s = String.valueOf(representation);
        if (isComplement) {
            s += "'";
        }
        if (isEdited) {
            s += "*" + getEditIndex(getRule(), isComplement) + edits;
        }
        return s;
    }

    public int getEditIndex(Rule rule, Boolean complement) {
        int editIndex = -1;
        Symbol s;
        if (complement) {
            s = rule.getLast();
        }
        else {
            s = rule.getGuard().getRight();
        }

        do {
            if (s instanceof Terminal) {
                editIndex++;
                if (complement) {
                    s = s.getLeft();
                }
                else {
                    s = s.getRight();
                }
            }
            else { // IF NONTERMINAL //TODO IF EDIT, THEN GET THE STRING AND DO EDITS AFTERWARDS...
                if (complement) {
                    getEditIndex(((NonTerminal) s).getRule(), !s.isComplement);
                }
                else {
                    getEditIndex(((NonTerminal) s).getRule(), s.isComplement);
                }

                if (complement) {
                    s = s.getLeft();
                }
                else {
                    s = s.getRight();
                }
            }

        } while (!s.isGuard() && !s.equals(editSymbol));
        return editIndex;
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