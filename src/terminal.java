/**
 * terminal just a regular symbol that doesn't go anywhere
 */

public class terminal extends symbol {

    public terminal(Character s) {
        this.representation = s;
    }

    public Character getRepresentation() {
        return this.representation;
    }

    @Override
    public boolean equals(Object o) {
        terminal t = null;
        if (!(o instanceof terminal) && !(o instanceof nonTerminal)) {
            throw new ClassCastException("Must be terminal. Received " + o.getClass());
        }
        else if (o instanceof nonTerminal) {
            return false;
        }
        else {
            t = (terminal) o;
        }

        return  (t.getRepresentation().equals(this.getRepresentation()));
    }
}
