import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleImpl implements RuleInterface {
    private Integer ruleSize = 0;
    private Integer ruleUsage = 0;
    Map<Integer, Symbol> symbolHashMap = new HashMap<>();
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
        //nonterminal left set as first.leftsymbol of digram,
        // nonterminal  right set as second.rightsymbol of digram

        //TODO same problem, have to be able to change the values, how to store them???

        List<Symbol> symbolList = symbolHashMap.values()
                .stream()
                .filter(x -> x.equals(symbol))
                .collect(Collectors.toList());

        for (Symbol s : symbolList) {
            s.setLeftSymbol(s.getLeftSymbol().getLeftSymbol()); // if head??
            s = nonTerminal;
        }

        // need to add and remove digrams and nonTerminals from map


        /**
         * as it is, keeps a list of digrams that occurs and their frequencies
         * problems arise when you want to replace a digram with something else
         * there's a link between each individual symbol, and there's the generic
         * digrams, replacing digrams can't be done like this
         *
         * when replacing a digram you have the digram itself the last two symbols
         * or tail of current rule
         *
         * have to replace every instance of that with the nonTerminal
         *
         * keep a separate hashmap for altering??
         *
         * switch it to have a digram for each instance, have to recheck way of counting digrams
         */
    }

    /**
     * take out nonTerminal symbol and replace with what it represented
     */
    @Override
    public void replaceNonTerminal() {

    }

    @Override
    public Boolean checkDigram() {
//        Digram p = new Digram(tail.getLeftSymbol(), tail);
//        return symbolHashMap.get(p) > 1;
        Long count = symbolHashMap.values()
                .stream()
                .filter(x -> x.equals(tail))
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
            symbolHashMap.putIfAbsent(symbolHashMap.size(), terminal);
        }
        else {
            symbolHashMap.put(symbolHashMap.size(), terminal);
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

    public Map<Integer, Symbol> getSymbolHashMap() {
        return symbolHashMap;
    }
}
