package GrammarCoder;

public class Edit {
    String symbol;
    int index;

    Edit(int index, String symbol) {
        this.index = index;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return index + " " + symbol;
    }
}
