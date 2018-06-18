import java.util.HashMap;
import java.util.Map;

public interface RuleInterface {
    Map<Integer, Symbol> symbolHashMap = new HashMap<>();
    Integer getSize();
    Integer getUseAmount();
    void incrementUse();
    void decrementUse();
    void replaceDigram();
    void replaceNonTerminal();
}
