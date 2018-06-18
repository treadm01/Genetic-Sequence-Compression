/**
 * terminal
 */
public class Terminal extends Symbol {
    public Terminal(String representation) {
        setSymbol(representation); //TODO not sure about this, rather that using own methods
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

        if (t.getLeftSymbol() == null || this.getLeftSymbol() == null) {
            return false;
        }

        return  (t.toString().equals(this.toString()) && t.getLeftSymbol().toString().equals(this.getLeftSymbol().toString()));
    }

    @Override
    public int hashCode() {
        return (int) this.toString().charAt(0);
    }
}
