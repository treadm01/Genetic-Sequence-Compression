import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Compress {
    Map<Symbol, Symbol> digramMap = new HashMap<>();
    Map<Symbol, NonTerminal> nonTerminalMap = new HashMap<>(); // map for all nonterminals not sure if needed
    NonTerminal firstRule = new NonTerminal(0); // create first rule;
    HashSet<String> rules = new HashSet<>();

    // TODO remove old digrams after adding nonTerminal
    // TODO make sure the way symbols are created and stored isn't going to cause issues later on
    // keep list of nonterminals to their first symbol check that way for repeats

    /**
     * main method, doing too much, need to break up
     * @param input
     */
    public void processInput(String input) {
        nonTerminalMap.put(firstRule, firstRule); // put in map
        for (int i = 0; i < input.length(); i++) {
            //System.out.println(i + " of " + input.length());
            // add next symbol from input to the first rule
            firstRule.addNextSymbol(new Terminal(input.substring(i, i + 1)));
            checkDigram();
            printRules();
            printDigrams();
        }
        generateRules(firstRule.guard.left.right);
        System.out.println(rules);
        printRules();
    }

    public String getRules() {
        return rules.toString();
    }

    public void generateRules(Symbol current) {
        String output = "";
        while (!current.representation.equals("?")) {
            output += current;
            if (current instanceof Rule) {
                generateRules(((Rule) current).nonTerminal.guard.left.right);

            }
            current = current.right;
        }
        rules.add(output);
    }

    public void printRules() {
        for (NonTerminal nt : nonTerminalMap.values()) {

            Symbol s = nt.guard.left.right;
            String output = "";
            do {
                output += s.toString();
                s = s.right;
            } while (!s.representation.equals("?"));

            System.out.print("#" + nt + " > ");
            System.out.print(output);
            //System.out.print(" use number " + nt.count);
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
    //TODO CLEAN AND BREAK UP WITH METHOD
    public void checkDigram() {
        // check whether new digram has already been seen
        Symbol first;
        if (digramMap.containsKey(firstRule.getLast())) {
            // if digram seen and rule already exists, get rule and update first rule with it
            if (nonTerminalMap.containsKey(firstRule.getLast())) {
                //THREE??
                first = firstRule.getLast(); // new digram from last added terminal
                Rule rule = new Rule(nonTerminalMap.get(firstRule.getLast())); // create new rule and send through nonTerminal
                firstRule.updateNonTerminal(rule, first); // update rule for first digram

                checkDigram(); // adding a re check here for new terminal added, should probably be somewhere else as well

                // TODO possible to put this before recurisve checkDigram? would then match same process further down
                digramMap.putIfAbsent(rule, rule); // add potential new digram with added nonTerminal
            }
            else { // create new rule for digram and update rule with them in all instances
                first = digramMap.get(firstRule.getLast()); // the matching digram added earlier
                Symbol second = firstRule.getLast(); // new digram from last added terminal
                int ruleNumber = nonTerminalMap.size(); // TODO get in a better way
                NonTerminal newRule = new NonTerminal(ruleNumber); // create new rule to hold new Nonterminal


                // ONE
                Rule rule = new Rule(newRule); // create a new rule but with the same nonTerminal
                firstRule.updateNonTerminal(rule, first); // update rule for first digram
                // add new digrams from the use of Nonterminal

                // TWO
                // create new rule for second instance with the same nonterminal
                Rule ruleTwo = new Rule(newRule);
                firstRule.updateNonTerminal(ruleTwo, second); // update for second
                // add potential digram of adding new nonterminal to end of rule
                digramMap.putIfAbsent(ruleTwo, ruleTwo);

                //ADDED TWO SEPARATE RULES CREATED THEN ADDED FIRST RULE TO DIGRAM MAP AFTER EDITING MAIN
                // RULE TO ENSURE THEY ARE RELEVANT
                digramMap.putIfAbsent(rule, rule);
                digramMap.putIfAbsent(rule.right, rule.right);

                // TODO this below can't be done before the digrams are dealt with in a rule, as it wipes out the references of left and right. check if ok
                newRule.addSymbols(first.left, first); // add symbols to the new rule/terminal
                // put the new rule/nonTerminal into the map
                nonTerminalMap.putIfAbsent(rule.nonTerminal.guard.left.right.right, rule.nonTerminal);
            }

            // reduce rule count if being replaced.... if either of digram a nonterminal then rmeove
            //TODO clean up, make separate method
            if (first.left instanceof Rule) {
                ((Rule) first.left).nonTerminal.count--;
                if (((Rule) first.left).nonTerminal.count == 1) {
                    ((Rule) first.left).removeRule();
                }
            }
            if (first instanceof Rule) {
                ((Rule) first).nonTerminal.count--;
                if (((Rule) first).nonTerminal.count == 1) {
                    ((Rule) first).removeRule();
                }
            }

            //checkDigram();
        }
        else { // digram not been seen before, add to digram map
            digramMap.putIfAbsent(firstRule.getLast(), firstRule.getLast());
        }
    }

    //GETTER FOR FIRST RULE
}
