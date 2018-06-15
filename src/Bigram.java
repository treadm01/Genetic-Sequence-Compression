public class Bigram implements Comparable {

    Symbol first;
    Symbol second;

    public Bigram(Symbol f, Symbol s) {
        first = f;
        second = s;
    }

    @Override
    public int compareTo(Object o) {
        Bigram b;
        if (!(o instanceof Bigram)) {
            throw new ClassCastException("Must be bigram. Received " + o.getClass());
        }
        else {
            b = (Bigram) o;
        }

        if (b.first.toString().equals(this.first.toString())
                && b.second.toString().equals(this.second.toString())) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        Bigram b = null;
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bigram)) {
            throw new ClassCastException("Must be bigram. Received " + o.getClass());
        }
        else {
            b = (Bigram) o;
        }

        return  (b.first.toString().equals(this.first.toString())
                && b.second.toString().equals(this.second.toString()));
    }
}
