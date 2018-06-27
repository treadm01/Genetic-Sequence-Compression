import java.util.HashMap;
import java.util.Map;

public class Compress {
    Map<Symbol, Symbol> digramMap = new HashMap<>();
    Map<NonTerminal, NonTerminal> nonTerminalMap = new HashMap<>(); // map for all nonterminals not sure if needed

    // TODO remove old digrams after adding nonTerminal
    // TODO update new digrams where nonTerminal was added
    // TODO recursively check new digram from where nonTerminals are added...
    // TODO rule numbers accurate

    /**
     * main method, doing too much, need to break up
     * @param input
     */
    public void processInput(String input) {
        NonTerminal firstRule = new NonTerminal(); // create first rule
        nonTerminalMap.put(firstRule, firstRule); // put in map
        for (int i = 0; i < input.length(); i++) {
            // add next symbol from input to the first rule
            firstRule.addNextSymbol(new Terminal(input.substring(i, i + 1)));

            // check whether the last(and one before if through left) has already been seen
            if (digramMap.containsKey(firstRule.getLast())) {
                Symbol first = digramMap.get(firstRule.getLast()); // the matching digram added earlier
                Symbol second = firstRule.getLast(); // new digram from last added terminal

                // print out current digram being checked
                System.out.println("DIGRAM = " + firstRule.getLast().left + " " + firstRule.getLast());

                firstRule.updateNonTerminal(first); // update rule for first digram
                firstRule.updateNonTerminal(second); // update for second
            }
            else {
                // add digram of last added symbols
                digramMap.put(firstRule.getLast(), firstRule.getLast());
            }
        }

        // output for debugging
        System.out.println(digramMap);
        System.out.println(firstRule.values);

        // print out from nodes for debugging
        Symbol s = firstRule.values.get(1);
        do {
            System.out.println(s);
            s = s.right;
        } while (!s.representation.equals("!"));
    }
}
