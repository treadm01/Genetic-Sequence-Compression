import java.util.List;
import java.util.Map;

public interface RuleInterface {
    Integer getSize();
    Integer getUseAmount();
    void incrementUse();
    void decrementUse();
    void replaceDigram(NonTerminalTwo nonTerminal);
    void replaceNonTerminal(NonTerminalTwo nonTerminal);
    Boolean checkDigram();
    void addTerminal(Symbol terminal);
    Symbol getSymbol(Terminal symbol); //TODO for testing
    Digram getTail();//TODO for testing
    Map<Digram, List<Integer>> getSymbolHashMap(); // TODO for testing
    Map<NonTerminalTwo, List<Integer>> getNonTerminalHashMap(); // TODO for testing
}
