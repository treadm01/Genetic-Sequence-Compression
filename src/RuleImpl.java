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
        // get list of indexes of the digram from map
        // replace all instances in values with terminal



        // how to update digram map
        // can remove those updated easily (i hope)

        // but same problem of how to update other digrams
        Digram d = new Digram(values.get(values.size()-2), values.get(values.size()-1));


        for (Integer index : symbolHashMap.get(d)) {
            // digrams to remove
            // left
            if ((index - 2) >= 0) {
                Digram digramLeft = new Digram(values.get(index - 2), values.get(index - 1));
                symbolHashMap.get(digramLeft).remove(Integer.valueOf(index - 1));
            }
            // right
            if ((index + 1) <= values.size() - 1) {
                Digram digramRight = new Digram(values.get(index), values.get(index + 1));
                symbolHashMap.get(digramRight).remove(Integer.valueOf(index + 1));
            }


        }



        int offset = 0; // have to use an offset when removing from list....
        for (Integer index : symbolHashMap.get(d)) {
            values.remove(values.get(index - offset));
            values.add(index - offset, nonTerminal);
            values.remove((index-1) - offset);
            offset += 1;
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
            if (!symbolHashMap.containsKey(digram)) {
                List<Integer> index = new ArrayList<>();
                index.add(values.size());
                symbolHashMap.putIfAbsent(digram, index);
            } else {
                symbolHashMap.get(digram).add(values.size()); // add new index to list of terminal...
            }
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
