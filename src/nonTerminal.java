/**
 * non terminal regular symbols that don't go anywhere
 */

public class nonTerminal extends symbol {
    /**
     * the actual symbol
     * @param s
     */
    public nonTerminal(Character s) {
        this.representation = s;
    }

    /**
     * quick way to get the symbol
     * @return
     */
    public Character getRepresentation() {
        return this.representation;
    }


    @Override
    public boolean equals(Object o) {
        nonTerminal t = null;
        if (!(o instanceof nonTerminal)) {
            throw new ClassCastException("Must be bigram. Received " + o.getClass());
        }
        else {
            t = (nonTerminal) o;
        }

        return  (t.getRepresentation().equals(this.getRepresentation()));
    }

}
