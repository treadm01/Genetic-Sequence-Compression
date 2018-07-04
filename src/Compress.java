import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Compress {
    Map<Symbol, Symbol> digramMap = new HashMap<>();
    Map<Integer, NonTerminal> nonTerminalMap = new HashMap<>(); // map for all nonterminals not sure if needed
    Integer ruleNumber = 0;
    NonTerminal firstRule = new NonTerminal(ruleNumber); // create first rule;
    HashSet<String> rules = new HashSet<>();
    final static int USED_ONCE = 1;
    Rule mainRule;

    //TODO reordering of rule numbers, most frequently used are lower
    // TODO nonterminals are not being removed


    // TODO remove old digrams after adding nonTerminal
    // TODO make sure the way symbols are created and stored isn't going to cause issues later on
    // keep list of nonterminals to their first symbol check that way for repeats
    // TODO these rule manipulations set as methods in the classes themselves?

    /**
     * main method loops through the input and adds the latest symbol to the main rule
     * calls check digram on for last two symbols
     * some output calls
     * @param input
     */
    public void processInput(String input) {
        mainRule = new Rule(getFirstRule(), 0); //TODO 0 or -1 not contained by any rule
        nonTerminalMap.put(0, getFirstRule()); // put in map
        for (int i = 0; i < input.length(); i++) {
            //System.out.println(i + " of " + input.length());
            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(new Terminal(input.substring(i, i + 1), 0));
            checkDigram();
            //printRules();
            //printDigrams();
        }
        generateRules(getFirstRule().guard.left.right);
        System.out.println(rules);
        printRules();
    }

    //TODO have method for boolean check of digram
    //TODO needs to update the rule.nonTerminal with matching digram
    // TODO need to check whether the digram in rule is entire rule or not
    //TODO won't work if digram occurs somewhere in the middle of a rule

    //TODO NEED TO CLEAN UP SO CAN BE APPLICABLE TO ANY RULE NOT JUST FIRST - do i? or possible to update via rule some how, like remove rule

    //TODO would it work with keeping the original digrams for location?
    /**
     * metod that checks through the main options of latest two symbols
     * if new digram not seen beofre, add to map
     * if seen beofre and already a rule, use that rule instead
     * if seen and not a rule, make a new rule
     * each new digram with the use of a rule must be checked also
     */
    public void checkDigram() {
        Symbol lastDigram = firstRule.getLast();
        // check existing digrams for last digram, update them with new rule
        if (digramMap.containsKey(lastDigram)) {
            //TODO a better way to check for existing rule
            if (digramMap.get(lastDigram).left.left.representation.equals("?")
                    && digramMap.get(lastDigram).right.representation.equals("?")
                    && nonTerminalMap.containsKey(digramMap.get(lastDigram).left.left.containingRule)
                    ) {
                existingRule(lastDigram);
            }
            else { // if digram has been seen but only once, no rule, then create new rule
                createRule(lastDigram);
            }
        }
        // digram not been seen before, add to digram map
        else {
            digramMap.putIfAbsent(lastDigram, lastDigram);
        }
    }

    /**
     * if a digram is being replaced with a new nonterminal and either symbols of that
     * digram are a rule, their count/usage must be reduced
     * if the count has reached one, then the rule is removed and it's occurence replaced with
     * the symbols it stood for
     * @param symbol
     */
    public void replaceRule(Symbol symbol) {
        if (symbol instanceof Rule) { // if the symbol is a rule reduce usage
            Rule rule = (Rule) symbol;
            rule.nonTerminal.count--;
            if (rule.nonTerminal.count == USED_ONCE) { // if rule is down to one, remove completely
                rule.removeRule();
                nonTerminalMap.remove(Integer.valueOf(rule.representation));
            }
        }
    }

    /**
     * might not be needed you know... just work the links of the symbols??
     * replace an instance of a digram with a nonterminal
     * @param ruleWithDigram
     * @param nonTerminal
     * @param symbol - the position of the digram to be replaced
     */
    public void replaceDigram(Rule ruleWithDigram, NonTerminal nonTerminal, Symbol symbol) {
        Rule rule = new Rule(nonTerminal, Integer.valueOf(ruleWithDigram.representation));

        //TODO how to access nonterminal? put method in rule or shift around?
        ruleWithDigram.nonTerminal.updateNonTerminal(rule, symbol); // update for second
        // add potential digram of adding new nonterminal to end of rule

        //TODO - are new digrams created when adding to sub rules???
        digramMap.remove(symbol.left);
        //digramMap.remove(symbol); //TODO removed to keep digrams that are placed in a new rule
        digramMap.putIfAbsent(rule, rule);
        digramMap.putIfAbsent(rule.right, rule.right); // not really necessary if last symbol in rule
    }


    /**
     * already a rule for the digram found, replace it with that rule
     * this needs looking into - TODO recursive here? consolidate with other method?
     * @param symbol
     */
    public void existingRule(Symbol symbol) {
        // getting last and last.left should be ok as exisitng rule should be matching a digram
        NonTerminal nonTerminal = nonTerminalMap.get(digramMap.get(symbol).left.left.containingRule);
        Symbol first = nonTerminal.last.left;
        Symbol second = nonTerminal.last;

        Rule rule = new Rule(nonTerminal,
                symbol.containingRule); // create new rule and send through nonTerminal
        firstRule.updateNonTerminal(rule, symbol); // update rule for first digram
        digramMap.remove(symbol.left); // TODO hmmm what's this doing and is it bad?

        //TODO why only recurse from here? consolidate methods
        checkDigram(); // adding a re check here for new terminal added, should probably be somewhere else as well
        digramMap.putIfAbsent(rule, rule); // add potential new digram with added nonTerminal

        // reduce rule count if being replaced.... if either symbol of digram a nonterminal then rmeove
        // references retrieved earlier as updating first rule can lose the second
        replaceRule(first);
        replaceRule(second);
    }

    /**
     * create the rule for a repeated digram, requires being done twice for both instances
     * of the digram, rule has two instances, the nonterminal it represents only one
     * @param symbol
     */
    public void createRule(Symbol symbol) {
        //TODO update specific rule by getting containing rule - don't add containing for every terminal or rule
        //TODO just access last? you might not know where last is
        Symbol secondDigram = symbol; // get new digram from last symbol added
        Symbol firstDigram = digramMap.get(secondDigram); // matching digram in the rule
        ruleNumber++;
        NonTerminal newRule = new NonTerminal(ruleNumber); // create new rule to hold new Nonterminal

        //TODO what is this doing if the rule isn't the mainrule????
        // update rule for first instance of digram
        replaceDigram(mainRule, newRule, firstDigram);
        // update rule for last instance of digram
        replaceDigram(mainRule, newRule, secondDigram);

        // TODO this below can't be done before the digrams are dealt with in a rule, as it wipes out the references of left and right. check if ok
        // update containing rule here... TODO or not for now, just use head and tail
        newRule.addSymbols(firstDigram.left, firstDigram); // add symbols to the new rule/terminal

        // put the new rule/nonTerminal into the map
        nonTerminalMap.putIfAbsent(ruleNumber, newRule);

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

    /**
     * creates strings of the symbols for each nonterminal
     * @param current
     */
    public void generateRules(Symbol current) {
        String output = "";
        while (!current.representation.equals("?")) {
            output += current + " ";
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
                output += s.toString() + " ";
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
