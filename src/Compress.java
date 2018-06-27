import java.util.HashMap;
import java.util.Map;

public class Compress {
    Map<Symbol, Symbol> digramMap = new HashMap<>();
    Map<NonTerminal, NonTerminal> nonTerminalMap = new HashMap<>(); // map for all nonterminals not sure if needed

    public void processInput(String input) {
        // create first rule
        NonTerminal firstRule = new NonTerminal("0");
        nonTerminalMap.put(firstRule, firstRule); // put in map
        for (int i = 0; i < input.length(); i++) {
            // add next symbol from input to the first rule
            firstRule.addNextSymbol(new Terminal(input.substring(i, i + 1)));

            // check whether the last(and one before if through left) has already been seen
            if (digramMap.containsKey(firstRule.getLast())) {

                Symbol first = digramMap.get(firstRule.getLast());
                Symbol second = firstRule.getLast();

//                firstRule.values.remove(first);
//                firstRule.values.remove(second);


                System.out.println(firstRule.getLast().left + " " + firstRule.getLast());
                // create new rule for found digram
                NonTerminal newRule = new NonTerminal(String.valueOf(nonTerminalMap.size()));
                newRule.addSymbols(firstRule.getLast()); // add symbols of digram to rule
                digramMap.put(newRule, newRule); // put rule in the map
                // TODO remove old digrams??
                // TODO update rule with new nonTerminal -
                // WHICH IS? reorder left and right of the ones found

                firstRule.updateNonTerminal(newRule, first);
                firstRule.updateNonTerminal(newRule, second);
            }
            else
                {
                    // add digram of last added symbols
                digramMap.put(firstRule.getLast(), firstRule.getLast());
                }
        }
        System.out.println(digramMap);
        System.out.println(firstRule.values);
    }
}
