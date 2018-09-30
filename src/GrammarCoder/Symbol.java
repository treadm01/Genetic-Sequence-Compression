package GrammarCoder;

import java.util.List;

public class Symbol {
    Symbol left, right; // left and right links of symbols
    long representation; // used throughout to compare symbol objects
    private static final long PRIME = 2265539; //hashcode implementation from Java sequitur implementation available at http://www.sequitur.info/
    public Boolean isComplement = false; // symbol is a reverse complement or not
    Boolean isEdited = false; // symbol has been edited or is a nonterminal with one or more edits
    public Symbol complement; // link to the symbols corresponding reverse complement
    int symbolIndex = 0; // to keep location of actual symbol for edit
    List<Edit> editList; // list of possible edits

    /**
     * method to quickly check if a symbol being checked is a guard
     * for traversing rules, a Guard's representations is 0
     * @return
     */
    public Boolean isGuard() {
        return representation == 0;
    }

    public void assignLeft(Symbol left) {
        this.left = left;
    }

    public void assignRight(Symbol right) {
        this.right = right;
    }

    public Symbol getLeft() {
        return left;
    }

    public Symbol getRight() {
        return right;
    }

    public long getRepresentation() {
        return representation;
    }

    /**
     * check whether the symbol/digram being called from constitutes
     * an entire sub-rule
     * @return
     */
    public Boolean isARule() {
        return this.getLeft().getLeft().isGuard()
                && this.getRight().isGuard();
    }

    /**
     * Method to check that a digram is not overlapping an instance of itself
     * that is a a a, where the two digrams are the same
     * @param symbol
     * @return
     */
    Boolean isNotOverlapping(Symbol symbol) {
        return this.getRight() != symbol
                && this.getLeft() != symbol;
    }


    /**
     * callable from any symbol object to create the corresponding symbols
     * for the reverse complement digram. Also assigns the links between the
     * new symbols.
     * @return
     */
    public Symbol getReverseComplement() {
        Symbol left = createReverseComplement(this);
        Symbol right = createReverseComplement(this.getLeft());
        right.assignLeft(left);
        left.assignRight(right);
        left.assignLeft(new Terminal('!'));
        return right;
    }


    /**
     * Creates the reverse complement symbol for a symbol whether terminal or nonterminal
     * assigning the link between the two complement symbols and the boolean
     * isComplement value
     * @param currentSymbol
     * @return
     */
    private Symbol createReverseComplement(Symbol currentSymbol) {
        Symbol reverse = new Symbol();
        if (currentSymbol instanceof Terminal) { // right hand side of digram
            reverse = new Terminal(Terminal.reverseSymbol(currentSymbol.toString().charAt(0)));
        }
        else if (currentSymbol instanceof NonTerminal) {
            reverse = new NonTerminal(((NonTerminal) currentSymbol).getRule());
            ((NonTerminal) currentSymbol).getRule().decrementCount();
        }

        reverse.isComplement = !currentSymbol.isComplement;
        reverse.complement = currentSymbol;
        currentSymbol.complement = reverse;

        return reverse;
    }

    /**
     * takes a list of edit objects gathered during grammar construction
     * and assigns them to this particular symbol
     * @param edits
     */
    void setIsEdit(List<Edit> edits) {
        if (editList == null) {
            // used to avoid instantiating
            // an edit list for each symbol
            // when not needed.
            editList = edits;
        }
        else {
            this.editList.addAll(edits);
        }
        isEdited = true;
    }

    @Override
    public String toString() {
        return String.valueOf(representation);
    }

    @Override
    public int hashCode() {
        long code;
        //hashcode implementation from Java sequitur implementation available at http://www.sequitur.info/
        long a = this.representation;
        long b = left.representation;
        code = ((21599 * a) + (20507 * b));
        code = code % PRIME;
        return (int)code;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Symbol)) {
            return false;
        }
        else {
            Symbol symbol = (Symbol) obj;
            return ((representation == symbol.getRepresentation())
                    && (left.representation == (symbol.left.getRepresentation()))
            );
        }
    }
}
