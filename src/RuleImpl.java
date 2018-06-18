import java.util.HashMap;
import java.util.Map;

public class RuleImpl implements RuleInterface {
    private Integer ruleSize = 0;
    private Integer ruleUsage = 0;
    Map<Symbol, Integer> symbolHashMap = new HashMap<>();
    Symbol tail, head;

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

    /**
     * take out matching digram and replace with a nonTerminal
     */
    @Override
    public void replaceDigram(Symbol symbol, NonTerminalTwo nonTerminal) {

    }

    /**
     * take out nonTerminal symbol and replace with what it represented
     */
    @Override
    public void replaceNonTerminal() {

    }

    @Override
    public Boolean checkDigram() {
       return symbolHashMap.get(tail) > 1;
    }

    /**
     * adds terminal to hashmap, currently just keeping an index for key
     * if its the first then add it, if not then set the left symbol to the previous
     * symbol and set last to that one
     * @param terminal
     */
    @Override
    public void addTerminal(Terminal terminal) {
        if (symbolHashMap.size() == 0) {
            head = terminal;
            tail = terminal;
        }
        else {
            terminal.setLeftSymbol(tail);
            tail.setRightSymbol(terminal);
            tail = terminal;
        }
        if (!symbolHashMap.containsKey(terminal)) {
            symbolHashMap.putIfAbsent(terminal, 1);
        }
        else {
            symbolHashMap.put(terminal, symbolHashMap.get(terminal) + 1);
        }
    }

    /**
     * not sure if this will ever work was using for debugging
     * @param symbol
     * @return
     */
    @Override
    public Symbol getSymbol(Terminal symbol) {
        return null;//symbolHashMap.values();
    }

    /**
     * starts from head and cycles through symbols until it is null,
     * the end of rule
     * @return
     */
    @Override
    public String toString() {
        String rule = "";
        Symbol s = head;

        do {
            rule += s.toString();
            s = s.getRightSymbol();
        } while (s != null);

        return rule;
    }

    /**
     * using for debugging checking digrams
     * @return
     */
    public Symbol getTail() {
        return tail;
    }

    public Map<Symbol, Integer> getSymbolHashMap() {
        return symbolHashMap;
    }
}
