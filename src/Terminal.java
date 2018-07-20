public class Terminal extends Symbol {
    long originalValue;

    public Terminal(int representation) { //TODO make long??
        originalValue = representation;
        this.representation = (representation * 2) + 1;
        left = new Symbol(); //todo having to add for equality check, not needed otherwise
    }

    @Override
    public String toString() {
        return String.valueOf((char) originalValue);
    }

    public long getOriginalValue() {
        return originalValue;
    }
}
