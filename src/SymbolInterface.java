public interface SymbolInterface {
    void setSymbol(String representation);
    String getSymbol();
    void setLeftSymbol(Symbol leftSymbol);
    void setRightSymbol(Symbol rightSymbol);
    Symbol getLeftSymbol();
    Symbol getRightSymbol();
}
