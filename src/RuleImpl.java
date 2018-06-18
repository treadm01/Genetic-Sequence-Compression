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
        Long count = symbolHashMap.values()
                .stream()
                .filter(x -> x.digramEquals(tail))
                .count();
        return count > 1;
    }

    /**
     * adds terminal to hashmap, currently just keeping an index for key
     * if its the first then add it, if not then set the left symbol to the previous
     * symbol and set last to that one
     * @param terminal
     */
    @Override
    public void addTerminal(String terminal) {
        Terminal t = new Terminal(terminal);
        if (symbolHashMap.size() == 0) {
            tail = t;
        }
        else {
            t.setLeftSymbol(tail);
            tail = t;
        }
        symbolHashMap.putIfAbsent(symbolHashMap.size(), t);
    }

    @Override
    public Symbol getSymbol(Terminal symbol) {
        return null;//symbolHashMap.values();
    }

    @Override
    public String toString() {
        String rule = "";
        for (Symbol s : symbolHashMap.values()) {
            rule += s.toString();
        }
        return rule;
    }

    public Symbol getTail() {
        return tail;
    }
}
