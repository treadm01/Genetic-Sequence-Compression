import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleImpl implements RuleInterface {
    private Integer ruleSize = 0;
    private Integer ruleUsage = 0;
    Map<Digram, List<Integer>> symbolHashMap = new HashMap<>();
    Symbol tail, head;
    List<Symbol> values = new ArrayList<>(); // might be able to eventually remove and just use indexes

    Map<Digram, List<Integer>> ts = new HashMap<>();

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
    public void replaceDigram(NonTerminalTwo nonTerminal) {
        Digram d = new Digram(values.get(values.size()-2), values.get(values.size()-1)); // current digram

        // remove all unused digrams
        for (Integer index : symbolHashMap.get(d)) {
            // digrams to remove
            // left
            if ((index - 2) >= 0) {
                Digram digramLeft = new Digram(values.get(index - 2), values.get(index - 1));
                if (symbolHashMap.containsKey(digramLeft)) {
                    symbolHashMap.get(digramLeft).remove(Integer.valueOf(index - 1));
                    if (symbolHashMap.get(digramLeft).size() == 0) {
                        symbolHashMap.remove(digramLeft);
                    }
                }
            }
            // right
            if ((index + 1) <= values.size() - 1) {
                Digram digramRight = new Digram(values.get(index), values.get(index + 1));
                if (symbolHashMap.containsKey(digramRight)) {
                    symbolHashMap.get(digramRight).remove(Integer.valueOf(index + 1));
                    if (symbolHashMap.get(digramRight).size() == 0) {
                        symbolHashMap.remove(digramRight);
                    }
                }
            }
        }

        // add new values to rule
        int offset = 0; // have to use an offset when removing from list....
        for (Integer index : symbolHashMap.get(d)) {
            values.remove(values.get(index - offset));
            values.add(index - offset, nonTerminal);
            values.remove((index-1) - offset);

            for (List<Integer> in : symbolHashMap.values()) {
                if (! in.equals(symbolHashMap.get(d))) {
                    for (Integer num : in) {
                        if (num > index) {
                            int copy = num;
                            copy--;
                            in.set(in.indexOf(num), copy);
                        }
                    }
                }
            }

            offset += 1;

            if (((index-1) - offset) >= 0) {
                Digram q = new Digram(values.get((index - 1) - offset), nonTerminal);
                addDigram(q, (index) - offset);
            }

            if ((index + 1) - offset <= values.size() - 1) {
                Digram q = new Digram(nonTerminal, values.get((index + 1) - offset));
                addDigram(q, ((index) - offset) + 1);
            }

        }

        symbolHashMap.remove(d);

    }

    public void addDigram(Digram digram, Integer index) {
        if (!symbolHashMap.containsKey(digram)) {
            List<Integer> i = new ArrayList<>();
            i.add(index);
            symbolHashMap.putIfAbsent(digram, i);
        }
        else {
            symbolHashMap.get(digram).add(index); // add new index to list of terminal...
        }
    }

//    public void updateDigramMap() {
//
//    }

    /**
     * take out nonTerminal symbol and replace with what it represented
     */
    @Override
    public void replaceNonTerminal() {

    }

    @Override
    public Boolean checkDigram() {
        Digram d = new Digram(values.get(values.size()-2), values.get(values.size()-1));
        return symbolHashMap.get(d).size() > 1;
    }

    /**
     * adds terminal to hashmap, currently just keeping an index for key
     * if its the first then add it, if not then set the left symbol to the previous
     * symbol and set last to that one
     * @param terminal
     */
    @Override
    public void addTerminal(Terminal terminal) {
        if (values.size() > 0) {
            Digram digram = new Digram(values.get(values.size()-1), terminal);
            addDigram(digram, values.size());
        }
        values.add(terminal);
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
        String result = "";
        for (Symbol s : values) {
            result += s.toString();
        }
        return result;
    }

    /**
     * using for debugging checking digrams
     * @return
     */
    public Symbol getTail() {
        return tail;
    }

    public Map<Digram, List<Integer>> getSymbolHashMap() {
        return symbolHashMap;
    }
}
