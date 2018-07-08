public class Symbol {
    Symbol left, right;
    String representation;
    private static final int PRIME = 2265539; // from sequitur
    //int containingRule;

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
        if (this == symbol.right) { return false; }

        return ((representation.equals(symbol.toString()))
                && (left.representation.equals(symbol.left.toString()))
        //&& (right.representation.equals(((Symbol)obj).right.representation))
        ); // switched check to look at left symbol rather than right
    }
}
