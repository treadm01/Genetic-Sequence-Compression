public class bigram implements Comparable {

    symbol first;
    symbol second;

    public bigram(symbol f, symbol s) {
        first = f;
        second = s;
    }

    @Override
    public int compareTo(Object o) {
        bigram b = null;
        if (!(o instanceof bigram)) {
            throw new ClassCastException("Must be bigram. Received " + o.getClass());
        }
        else {
            b = (bigram) o;
        }

        if (b.first.getRepresentation().equals(this.first.getRepresentation())
                && b.second.getRepresentation().equals(this.second.getRepresentation())) {
            return 0;
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        bigram b = null;
        if (this == o) {
            return true;
        }
        if (!(o instanceof bigram)) {
            throw new ClassCastException("Must be bigram. Received " + o.getClass());
        }
        else {
            b = (bigram) o;
        }

        return  (b.first.getRepresentation().equals(this.first.getRepresentation())
                && b.second.getRepresentation().equals(this.second.getRepresentation()));
    }
}
