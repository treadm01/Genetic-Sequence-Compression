public class Symbol {
    Symbol left, right;
    String representation;
    static final int prime = 2265539; // from sequitur
    int containingRule;

    @Override
    public String toString() {
        return this.representation;
    }

    @Override
    public int hashCode() {
        long code;
        //from sequitur
        int a = Integer.valueOf(this.toString().charAt(0));
        int b = Integer.valueOf(left.toString().charAt(0)); // switched check to look at left symbol rather than right
        code = ((21599 * (long)a) + (20507 * (long)b));
        code = code % (long)prime;
        return (int)code;
    }


    @Override
    public boolean equals(Object obj) {
        //TODO add all checks

        // if this symbol is also the right hand side of a digram, then they are not equal
        // as they're dependent on each other
        if (this == ((Symbol) obj).right) { return false; }

        return ((representation.equals((obj).toString()))
                && (left.representation.equals(((Symbol)obj).left.representation))
        //&& (right.representation.equals(((Symbol)obj).right.representation))
        ); // switched check to look at left symbol rather than right
    }
}
