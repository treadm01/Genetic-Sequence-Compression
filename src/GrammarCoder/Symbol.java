package GrammarCoder;

public class Symbol {
    Symbol left, right;
    long representation;
    private static final long PRIME = 2265539; // from sequitur
    public Boolean isComplement = false;
    Boolean isEdited = false;
    String edits;
    public Symbol complement;
    Integer index;
    Symbol editSymbol; // todo will require a list for multiple edits?

    public void setIsEdit(String edits) {
        this.edits = edits;
        isEdited = true;
    }

    public Boolean isGuard() {
        return representation == 0;
    }

    @Override
    public String toString() {
        return String.valueOf(representation);
    } //TODO convert to necessary symbol

    @Override
    public int hashCode() {
        long code;
        //from sequitur
        long a = this.representation;
        long b = left.representation; // switched check to look at left symbol rather than right
        code = ((21599 * a) + (20507 * b));
        code = code % PRIME;
        return (int)code;
    }


    @Override
    public boolean equals(Object obj) {
        //TODO add all checks
        Symbol symbol = (Symbol) obj;

        return ((representation == symbol.getRepresentation())
                && (left.representation == (symbol.left.getRepresentation()))
        ); // switched check to look at left symbol rather than right
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
}
