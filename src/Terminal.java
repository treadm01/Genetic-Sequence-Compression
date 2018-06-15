/**
 * terminal just a regular symbol that doesn't go anywhere
 */

public class Terminal extends Symbol {

    public Terminal(String s) {
        this.representation = s;
    }

    @Override
    public String toString() {
        return this.representation;
    }

    @Override
    public boolean equals(Object o) {
        Terminal t = null;
        if (!(o instanceof Terminal) && !(o instanceof NonTerminal)) {
            throw new ClassCastException("Must be terminal. Received " + o.getClass());
        }
        else if (o instanceof NonTerminal) {
            return false;
        }
        else {
            t = (Terminal) o;
        }

        return  (t.toString().equals(this.toString()));
    }

    @Override
    public int hashCode() {
        return (int) this.toString().charAt(0);
    }
}
