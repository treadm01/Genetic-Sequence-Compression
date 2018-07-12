public class Symbol {
    Symbol left, right;
    String representation;
    private static final int PRIME = 2265539; // from sequitur

    public Boolean isGuard() {
        return false;
    }

    @Override
    public String toString() {
        return this.representation;
    }

    @Override
    public int hashCode() {
        long code;
        //from sequitur
        int a = (int) this.toString().charAt(0);
        int b = (int) left.toString().charAt(0); // switched check to look at left symbol rather than right
        code = ((21599 * (long)a) + (20507 * (long)b));
        code = code % (long)PRIME;
        return (int)code;
    }


    @Override
    public boolean equals(Object obj) {
        //TODO add all checks

        Symbol symbol = (Symbol) obj;

        // if this symbol is also the right hand side of a digram, then they are not equal
        // as they're dependent on each other
        //TODO dealing with overlap in check digrams causes occasional crashes, digrams have to be re-added
        // todo look into how the digrams are removed when creating a rule
       // if (this == symbol.right) { return false; } // is this causing duplicate digrams to be made?

        return ((representation.equals(symbol.toString()))
                && (left.representation.equals(symbol.left.toString()))
        //&& (right.representation.equals(((Symbol)obj).right.representation))
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
}
