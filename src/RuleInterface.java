import java.util.Map;

public interface RuleInterface {
    Integer getSize();
    Integer getUseAmount();
    void incrementUse();
    void decrementUse();
    void replaceDigram(Symbol symbol, NonTerminalTwo nonTerminal);
    void replaceNonTerminal();
    Boolean checkDigram();
    void addTerminal(Terminal terminal);
    Symbol getSymbol(Terminal symbol); //TODO for testing
    Symbol getTail();//TODO for testing
    Map<Digram, Integer> getSymbolHashMap(); // TODO for testing
}
