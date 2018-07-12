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

        return ((representation.equals(symbol.toString()))
                && (left.representation.equals(symbol.left.toString()))
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
