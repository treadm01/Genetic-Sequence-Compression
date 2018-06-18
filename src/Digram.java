public class Digram implements Comparable {

    Symbol first;
    Symbol second;

    public Digram(Symbol f, Symbol s) {
        first = f;
        second = s;
    }

    @Override
    public int compareTo(Object o) {
        Digram b;
        if (!(o instanceof Digram)) {
            throw new ClassCastException("Must be bigram. Received " + o.getClass());
        }
        else {
            b = (Digram) o;
        }

        if (b.first.toString().equals(this.first.toString())
                && b.second.toString().equals(this.second.toString())) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        Digram b = null;
        if (this == o) {
            return true;
        }
        if (!(o instanceof Digram)) {
            throw new ClassCastException("Must be bigram. Received " + o.getClass());
        }
        else {
            b = (Digram) o;
        }

        return  (b.first.toString().equals(this.first.toString())
                && b.second.toString().equals(this.second.toString()));
    }
}
