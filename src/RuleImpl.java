import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleImpl implements RuleInterface {
    private Integer ruleUsage = 0;
    Map<Digram, List<Integer>> symbolHashMap = new HashMap<>();
    Map<NonTerminalTwo, List<Integer>> nonTerminalHashMap = new HashMap<>();
    Digram tail, head;
    List<Symbol> values = new ArrayList<>(); // might be able to eventually remove and just use indexes

    @Override
    public Integer getSize() {
        return values.size();
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
        Digram d = getTail();//new Digram(values.get(values.size()-2), values.get(values.size()-1)); // current digram

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
            // add values/remove values from value list
            values.remove(values.get(index - offset));
            values.add(index - offset, nonTerminal);
            values.remove((index-1) - offset);

            // update indexes for from values after removing an element
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
            // create new digrams
            // left
            if (((index-1) - offset) >= 0) {
                Digram q = new Digram(values.get((index - 1) - offset), nonTerminal);
                addDigram(q, (index) - offset);
                addNonTerminal(nonTerminal, (index) - offset);
            }

            // right - have to check the index + 2 to stop adding a new rule,
            // that will subsequently be written over......
            if ((index + 1) - offset <= values.size() - 1  && !symbolHashMap.get(d).contains(index + 2)) {
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

    public void addNonTerminal(NonTerminalTwo nonTerminal, Integer index) {
        if (!nonTerminalHashMap.containsKey(nonTerminal)) {
            List<Integer> i = new ArrayList<>();
            i.add(index);
            nonTerminalHashMap.putIfAbsent(nonTerminal, i);
        }
        else {
            nonTerminalHashMap.get(nonTerminal).add(index);
        }
    }

//    public void updateDigramMap() {
//
//    }

    /**
     * take out nonTerminal symbol and replace with what it represented
     */
    @Override
    public void replaceNonTerminal(NonTerminalTwo nonTerminal) {
        // add new digrams from the rule with correct indexes - both digrams in rule and new digrams where new digrams occur
        // update digrams for this rule with correct indexes
        // and remove nonterminal from nonterminal map

        // remove unused bigram containing nonterminal
        for (Integer i : nonTerminalHashMap.get(nonTerminal)) {
            if ((i - 1) >= 0) {
                Digram dLeft = new Digram(values.get(i - 1), nonTerminal);
                symbolHashMap.remove(dLeft);
            }
            if ((i + 1) <= values.size() - 1) {
                Digram dRight = new Digram(nonTerminal, values.get(i + 1));
                symbolHashMap.remove(dRight);
            }
        }


        int offset = 0; // have to use an offset when removing from list....
        for (Integer i : nonTerminalHashMap.get(nonTerminal)) {
            // add values of nonterminal rule to this rules values
            values.addAll(i + offset, nonTerminal.getRule().values);
            values.remove(nonTerminal);
            offset = nonTerminal.getRule().values.size();

            // update indexes for from values after removing an element
            for (List<Integer> in : symbolHashMap.values()) {
                if (! in.equals(symbolHashMap.get(nonTerminal))) {
                    for (Integer num : in) {
                        if (num > i) {
                            int copy = num;
                            copy++;
                            in.set(in.indexOf(num), copy);
                        }
                    }
                }
            }
        }

        // add digrams from nonterminal to this digram map
        offset = 0;
        Digram lDigram, hDigram;
        int highestIndex = 0;

        // TODO ldigram hdigram not used at all, if longer values it wont work
        for (Integer i : nonTerminalHashMap.get(nonTerminal)) {
            for (Digram digram : nonTerminal.getRule().symbolHashMap.keySet()) {
                addDigram(digram, (i + 1) + offset); //TODO don't think this will work for longer rules
                // order of above might be out, not always after...... if index higher add offset.... mess

                List<Integer> li = nonTerminal.getRule().symbolHashMap.get(digram);
                if (li.get(0) == 1) {
                    lDigram = digram;
                }

                if (li.get(li.size()-1) > highestIndex) {
                    highestIndex = li.get(li.size()-1);
                    hDigram = digram;
                }
                // if only one terminal add digrams from either side, if more add either side of first and last
                // want the highest and the lowest index and corresponding digrams, then do for left and right
                // create new digrams
                // left
                if (((i-1) + offset) >= 0) {
                    Digram q = new Digram(values.get((i - 1) + offset), digram.first);
                    addDigram(q, i  + offset);
                }

                // right - have to check the index + 2 to stop adding a new rule,
                // that will subsequently be written over......
                if ((i + 1) + offset <= values.size() - 1  && !symbolHashMap.get(digram).contains(i + 2)) {
                    Digram q = new Digram(digram.second, values.get((i + 2) + offset));
                    addDigram(q, ((i + 2) + offset));
                }

                offset += 1;
            }
        }

        nonTerminalHashMap.remove(nonTerminal);


    }

    @Override
    public Boolean checkDigram() {
        return symbolHashMap.get(getTail()).size() > 1;
    }

    /**
     * adds terminal to hashmap, currently just keeping an index for key
     * if its the first then add it, if not then set the left symbol to the previous
     * symbol and set last to that one
     * @param terminal
     */
    @Override
    public void addTerminal(Symbol terminal) {
        if (values.size() > 0) {
            Digram digram = new Digram(values.get(values.size()-1), terminal);
            addDigram(digram, values.size());
            tail = new Digram(values.get(values.size() - 1), terminal);
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
    public Digram getTail() {
        return tail;
    }

    public Map<Digram, List<Integer>> getSymbolHashMap() {
        return symbolHashMap;
    }

    public Map<NonTerminalTwo, List<Integer>> getNonTerminalHashMap() {
        return nonTerminalHashMap;
    }
}
