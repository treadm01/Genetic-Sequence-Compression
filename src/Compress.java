import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Compress {
    Map<Symbol, Symbol> digramMap = new HashMap<>();
    Map<Symbol, NonTerminal> nonTerminalMap = new HashMap<>(); // map for all nonterminals not sure if needed
    Integer ruleNumber = 0;
    NonTerminal firstRule = new NonTerminal(ruleNumber); // create first rule;
    HashSet<String> rules = new HashSet<>();
    final static int USED_ONCE = 1;
    Rule mainRule;


    // TODO remove old digrams after adding nonTerminal
    // TODO make sure the way symbols are created and stored isn't going to cause issues later on
    // keep list of nonterminals to their first symbol check that way for repeats
    // TODO these rule manipulations set as methods in the classes themselves?

    /**
     * main method, doing too much, need to break up
     * @param input
     */
    public void processInput(String input) {
        mainRule = new Rule(getFirstRule());
        nonTerminalMap.put(getFirstRule(), getFirstRule()); // put in map
        for (int i = 0; i < input.length(); i++) {
            //System.out.println(i + " of " + input.length());
            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(new Terminal(input.substring(i, i + 1)));
            checkDigram();
            printRules();
            printDigrams();
        }
        generateRules(getFirstRule().guard.left.right);
        System.out.println(rules);
        printRules();
    }

    //TODO have method for boolean check of digram
    // getting the thing it occurs in but not updating the digram, just taking
    //TODO needs to update the rule.nonTerminal with matching digram
    // TODO need to check whether the digram in rule is entire rule or not
    //TODO won't work if digram occurs somewhere in the middle of a rule
    //TODO NEED TO CLEAN UP SO CAN BE APPLICABLE TO ANY RULE NOT JUST FIRST - do i? or possible to update via rule some how, like remove rule
    //TODO would it work with keeping the original digrams for location?

    public void checkDigram() {
        Symbol lastDigram = firstRule.getLast();
        // check existing digrams for last digram, update them with new rule
        if (digramMap.containsKey(lastDigram)) {
            //TODO need to check if already a rule, if there is one then use that
            // TODO else need to create a new rule and update the digram WHEREVER IT IS

            /**
             * a digram that is already a rule is not being registered as so, not sure why
             * new separation of methods allows for rethinking and change flow of execution
             */

            // if contains digram
            // check if its already a rule, if so use that
            // else create new rule and update
            // TODO create a proper check for if a digram matches an existing rule
            System.out.println("BEFORE " + lastDigram.left + " " + lastDigram);
            System.out.println("left of rule " + digramMap.get(lastDigram).left.representation);
            System.out.println("RIGHT " + digramMap.get(lastDigram).representation);
            System.out.println("IS IT IN HERE? " + nonTerminalMap.containsKey(lastDigram));
            System.out.println("BRACE");
            printRules();
            System.out.println(nonTerminalMap.keySet());
            System.out.println(nonTerminalMap.values());
            System.out.println("BRACE");

            if (digramMap.get(lastDigram).left.left.representation.equals("?")
                    && digramMap.get(lastDigram).right.representation.equals("?")
                    && nonTerminalMap.containsKey(lastDigram)) {
                System.out.println("HERE " + lastDigram.left + " " + lastDigram);
                existingRule(lastDigram);
            }
            else {
                System.out.println("OR HERE " + lastDigram.left + " " + lastDigram);
                createRule(lastDigram);
            }
        }
        // digram not been seen before, add to digram map
        else {
            digramMap.putIfAbsent(lastDigram, lastDigram);
        }
    }

    public void replaceRule(Symbol symbol) {
        if (symbol instanceof Rule) {
            Rule rule = (Rule) symbol;
            rule.nonTerminal.count--;
            if (rule.nonTerminal.count == USED_ONCE) {
                nonTerminalMap.remove(rule.nonTerminal.guard.left.right.right);
                rule.removeRule();
            }
        }
    }

    public void updateRule(Rule oldRule, NonTerminal nonTerminal, Symbol symbol) {
        Rule rule = new Rule(nonTerminal);
        //TODO how to access nonterminal? put method in rule or shift around?
        oldRule.nonTerminal.updateNonTerminal(rule, symbol); // update for second
        // add potential digram of adding new nonterminal to end of rule

        //TODO - are new digrams created when adding to sub rules???
        digramMap.remove(symbol.left);
        //digramMap.remove(symbol); //TODO removed to keep digrams that are placed in a new rule
        digramMap.putIfAbsent(rule, rule);
        digramMap.putIfAbsent(rule.right, rule.right); // not really necessary if last symbol in rule
    }

    public void existingRule(Symbol symbol) {
        // TODO this doesn't really check that a new exact rule has been seen, length of rule must be two
        Rule rule = new Rule(nonTerminalMap.get(symbol)); // create new rule and send through nonTerminal
        firstRule.updateNonTerminal(rule, symbol); // update rule for first digram
        digramMap.remove(symbol.left); // TODO hmmm what's this doing and is it bad?
        checkDigram(); // adding a re check here for new terminal added, should probably be somewhere else as well
        digramMap.putIfAbsent(rule, rule); // add potential new digram with added nonTerminal

        // reduce rule count if being replaced.... if either symbol of digram a nonterminal then rmeove
        replaceRule(symbol.left);
        replaceRule(symbol);
    }

    public void createRule(Symbol symbol) {
        Symbol secondDigram = symbol; // get new digram from last symbol added
        Symbol firstDigram = digramMap.get(secondDigram); // matching digram in the rule
        ruleNumber++;
        NonTerminal newRule = new NonTerminal(ruleNumber); // create new rule to hold new Nonterminal

        // update rule for first instance of digram
        updateRule(mainRule, newRule, firstDigram);
        // update rule for last instance of digram
        updateRule(mainRule, newRule, secondDigram);

        // TODO this below can't be done before the digrams are dealt with in a rule, as it wipes out the references of left and right. check if ok
        newRule.addSymbols(firstDigram.left, firstDigram); // add symbols to the new rule/terminal

        // put the new rule/nonTerminal into the map
        nonTerminalMap.putIfAbsent(newRule.guard.left.right.right, newRule);

        // reduce rule count if being replaced.... if either symbol of digram a nonterminal then rmeove
        replaceRule(firstDigram.left);
        replaceRule(firstDigram);
    }

    public NonTerminal getFirstRule() {
        return this.firstRule;
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
        System.out.println(output);
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
            System.out.println(s.left + " " + s + s.right);
        }
    }
}
