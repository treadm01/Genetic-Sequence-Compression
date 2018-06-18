public interface RuleInterface {
    Integer getSize();
    Integer getUseAmount();
    void incrementUse();
    void decrementUse();
    void replaceDigram();
    void replaceNonTerminal();
    Boolean checkDigram();
    void addTerminal(String terminal);
    Symbol getSymbol(Terminal symbol);
    Symbol getTail();//TODO for testing
}
