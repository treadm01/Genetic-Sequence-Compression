package GrammarCoder;

public class Edit {
    String symbol;
    int index; //to compare to each other use longest encoding first
    Boolean isComplement;

    public Edit(int index, String symbol, Boolean isComplement) {
        this.index = index;
        this.symbol = symbol;
        this.isComplement = isComplement;
    }

    @Override
    public String toString() {
        return index + " " + symbol;
    }
}
