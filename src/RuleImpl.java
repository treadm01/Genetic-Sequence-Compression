import java.util.HashMap;
import java.util.Map;

public class RuleImpl implements RuleInterface {
    private Integer ruleSize = 0;
    private Integer ruleUsage = 0;
    Map<Integer, Symbol> symbolHashMap = new HashMap<>();
    Symbol tail;

    @Override
    public Integer getSize() {
        return ruleSize;
    }

    @Override
    public Integer getUseAmount() {
        return ruleUsage;
    }

    @Override
    public void incrementUse() {
        ruleUsage++;
    }

    @Override
    public void decrementUse() {
        ruleUsage--;
    }

    @Override
    public void replaceDigram() {

    }

    @Override
    public void replaceNonTerminal() {

    }

    @Override
    public Boolean checkDigram() {
        return null;
    }

    @Override
    public void addTerminal(Terminal terminal) {
        if (symbolHashMap.size() == 0) {
            tail = terminal;
        }
        else {
            terminal.setLeftSymbol(tail);
            tail = terminal;
        }
        symbolHashMap.putIfAbsent(symbolHashMap.size(), terminal);
    }

    @Override
    public String toString() {
        String rule = "";
        for (Symbol s : symbolHashMap.values()) {
            rule += s.toString();
        }
        return rule;
    }
}
