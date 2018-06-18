public interface SymbolInterface {
    void setSymbol(String representation);
    Symbol getSymbol();
    void setLeftSymbol(Symbol leftSymbol);
    void setRightSymbol(Symbol rightSymbol);
    Symbol getLeftSymbol();
    Symbol getRightSymbol();
}
