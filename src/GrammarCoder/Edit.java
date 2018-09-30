package GrammarCoder;

public class Edit {
    String symbol; //todo use character
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
