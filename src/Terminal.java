public class Terminal extends Symbol {
    long originalValue;

    public Terminal(int representation) {
        originalValue = representation;
        this.representation = (representation * 2) + 1;
    }

    @Override
    public String toString() {
        return String.valueOf((char) originalValue);
    }
}
