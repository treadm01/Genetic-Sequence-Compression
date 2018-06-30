import java.util.HashMap;
import java.util.Map;

public class Compress {
    Map<Symbol, Symbol> digramMap = new HashMap<>();
    Map<Symbol, NonTerminal> nonTerminalMap = new HashMap<>(); // map for all nonterminals not sure if needed
    NonTerminal firstRule = new NonTerminal(0); // create first rule;

    // TODO remove old digrams after adding nonTerminal
    // TODO make a shell rule that keeps count of usage, holds a nonterminal, remove when reaches 1
    // TODO use either left right or the list in nonTerminals

    // keep list of nonterminals to their first symbol check that way for repeats
    // keep a shell to instantiate for nonTerminals then send same instance of a rule to it

    /**
     * main method, doing too much, need to break up
     * @param input
     */
    public void processInput(String input) {
        nonTerminalMap.put(firstRule, firstRule); // put in map
        for (int i = 0; i < input.length(); i++) {
            // add next symbol from input to the first rule
            firstRule.addNextSymbol(new Terminal(input.substring(i, i + 1)));
            printRules();
            System.out.println();
            checkDigram();
            printRules();
            System.out.println();
        }



        // print out for debugging
        // printDigrams();
        printRules();
    }

    public void printRules() {
        for (NonTerminal nt : nonTerminalMap.values()) {
            System.out.print("RULE " + nt + " ");
            System.out.print(nt.values + " ");
            System.out.print(nt.count);
            System.out.println();
        }
    }

    public void printDigrams() {
        for (Symbol s : digramMap.values()) {
            System.out.println(s.left + " " + s);
        }
    }

    //TODO have method for boolean check of digram
    //TODO have method for updating firstRule
    public void checkDigram() {
        // TODO update new digrams where nonTerminal was added
        // TODO recursively check new digram from where nonTerminals are added...
        // check whether new digram has already been seen

        if (digramMap.containsKey(firstRule.getLast())) {
            // if digram seen and rule already exists, get rule and update first rule with it
            if (nonTerminalMap.containsKey(firstRule.getLast())) {
                Rule rule = new Rule(nonTerminalMap.get(firstRule.getLast()));
                Symbol second = firstRule.getLast(); // new digram from last added terminal
                firstRule.updateNonTerminal(rule, second); // update rule for first digram

                checkDigram(); // adding a re check here for new terminal added, should probably be somewhere else as well
                digramMap.putIfAbsent(rule, rule);
            }
            else { // create new rule for digram and update rule with them in all instances
                Symbol first = digramMap.get(firstRule.getLast()); // the matching digram added earlier
                Symbol second = firstRule.getLast(); // new digram from last added terminal

                int ruleNumber = nonTerminalMap.size();

                NonTerminal newRule = new NonTerminal(ruleNumber);
                newRule.addSymbols(first.left, first);

                // reduce rule count if being replaced.... if either of digram a nonterminal then rmeove
                if (first.left instanceof Rule) {
                    ((Rule) first.left).nonTerminal.count--;
                }
                if (first instanceof Rule) {
                    ((Rule) first).nonTerminal.count--;
                }

                Rule rule = new Rule(newRule);
                firstRule.updateNonTerminal(rule, first); // update rule for first digram

                digramMap.putIfAbsent(rule, rule);
                digramMap.putIfAbsent(rule.right, rule.right);

                rule = new Rule(newRule);
                firstRule.updateNonTerminal(rule, second); // update for second
                nonTerminalMap.putIfAbsent(rule.nonTerminal.values.get(2), rule.nonTerminal);

                digramMap.putIfAbsent(rule, rule);
            }
            //checkDigram();
        }
        else { // digram not been seen before, add to digram map
            digramMap.putIfAbsent(firstRule.getLast(), firstRule.getLast());
        }
    }

    //GETTER FOR FIRST RULE
}
