package GrammarCoder;
import java.util.ArrayList;

public class NonTerminal extends Symbol {
    Rule rule; // the nonTerminal the rule points to
    //Integer index; // location of rule in main input string
    Boolean indexFound; // have to use to break out of recursive index find
    String editIndexes = "";

    public NonTerminal(Rule rule) {
        editSymbols = new ArrayList<>(); // init to store edits
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
            editIndexes = "";
//            int count = 0;
//            for (Symbol symbol : editSymbols) {
//                indexFound = false;
//                editIndexes += getEditIndex(getRule(), isComplement, symbol, 0);
//                editIndexes += String.valueOf(edits.charAt(count));
//            }
            s += "*" + edits;
        }
        return s;
    }

    // might not be able to do direct link to symbol, when a rule contains two instances of same rule,
    // which one will you find???? - todo will have to add on the offset of rules, keep the exact index
//    public int getEditIndex(Rule rule, Boolean complement, Symbol editSymbol, int editIndex) {
//        Symbol s;
//
//        if (complement) {
//            s = rule.getLast();
//        } else {
//            s = rule.getGuard().getRight();
//        }
//
//        if (s.equals(editSymbol)) {
//            indexFound = true;
//            return editIndex;
//        }
//
//        while (!s.isGuard() && !indexFound) {
//            if (s instanceof Terminal) {
//
//                if (s.equals(editSymbol)) {
//                    indexFound = true;
//                } else {
//                    editIndex++;
//                }
//                if (complement) {
//                    s = s.getLeft();
//                } else {
//                    s = s.getRight();
//                }
//
//            }
//            else { // IF NONTERMINAL //TODO IF EDIT, THEN GET THE STRING AND DO EDITS AFTERWARDS...
//                if (complement) {
//                    editIndex = getEditIndex(((NonTerminal) s).getRule(), !s.isComplement, editSymbol, editIndex);
//                } else {
//                    editIndex = getEditIndex(((NonTerminal) s).getRule(), s.isComplement, editSymbol, editIndex);
//                }
//
//                    if (complement) {
//                        s = s.getLeft();
//                    } else {
//                        s = s.getRight();
//                    }
//            }
//        }
//        return editIndex;
//    }

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