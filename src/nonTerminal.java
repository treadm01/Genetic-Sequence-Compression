/**
 * non terminal regular symbols that don't go anywhere
 */

public class nonTerminal extends symbol {
    /**
     * the actual symbol
     * @param s
     */
    public nonTerminal(String s) {
        this.representation = s;
    }

    /**
     * quick way to get the symbol
     * @return
     */
    public String getRepresentation() {
        return this.representation;
    }


    @Override
    public boolean equals(Object o) {
        nonTerminal t = null;
        if (!(o instanceof nonTerminal) && !(o instanceof terminal)) {
            throw new ClassCastException("Must be nontermin. Received " + o.getClass());
        }
        else if (o instanceof terminal) {
            return false;
        }
        else {
            t = (nonTerminal) o;
        }

        return  (t.getRepresentation().equals(this.getRepresentation()));
    }

}
