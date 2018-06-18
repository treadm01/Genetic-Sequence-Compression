import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class RuleImpl implements RuleInterface {
    private Integer ruleSize = 0;
    private Integer ruleUsage = 0;
    Map<Digram, Integer> symbolHashMap = new HashMap<>();
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

        System.out.println(symbol.getLeftSymbol().getLeftSymbol());
    }

    /**
     * take out nonTerminal symbol and replace with what it represented
     */
    @Override
    public void replaceNonTerminal() {

    }

    @Override
    public Boolean checkDigram() {
        Digram p = new Digram(tail.getLeftSymbol(), tail);
        return symbolHashMap.get(p) > 1;
    }

    /**
     * adds terminal to hashmap, currently just keeping an index for key
     * if its the first then add it, if not then set the left symbol to the previous
     * symbol and set last to that one
     * @param terminal
     */
    @Override
    public void addTerminal(Terminal terminal) {
        Digram digram;
        if (symbolHashMap.size() == 0) {
            head = terminal;
            tail = terminal;
            digram = new Digram(null, terminal); // TODO don't do this
        }
        else {
            terminal.setLeftSymbol(tail);
            tail.setRightSymbol(terminal);
            digram = new Digram(tail, terminal);
            tail = terminal;
        }
        if (!symbolHashMap.containsKey(digram)) {
            symbolHashMap.putIfAbsent(digram, 1);
        }
        else {
            symbolHashMap.put(digram, symbolHashMap.get(digram) + 1);
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

    public Map<Digram, Integer> getSymbolHashMap() {
        return symbolHashMap;
    }
}
