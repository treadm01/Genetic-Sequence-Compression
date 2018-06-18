public class Digram {

    Symbol first;
    Symbol second;

    public Digram(Symbol f, Symbol s) {
        first = f;
        second = s;
    }
//
//    @Override
//    public int compareTo(Object o) {
//        Digram b;
//        if (!(o instanceof Digram)) {
//            throw new ClassCastException("Must be bigram. Received " + o.getClass());
//        }
//        else {
//            b = (Digram) o;
//        }
//
//        if (b.first.toString().equals(this.first.toString())
//                && b.second.toString().equals(this.second.toString())) {
//            return 0;
//        }
//        return 1;
//    }

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

        if (b.first == null || this.first == null ||
                this.second == null || b.second == null) {
            return false;
        }

        return  (b.first.toString().equals(this.first.toString())
                && b.second.toString().equals(this.second.toString()));
    }

    @Override
    public String toString() {
        return (first + " " + second);
    }

    @Override
    public int hashCode() {
        return 0;//(int) ((int) this.first.toString().charAt(0) + (int) this.second.toString().charAt(0));
    }
}
