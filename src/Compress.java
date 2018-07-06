import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    private Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol
    private Map<Integer, NonTerminal> nonTerminalMap; // nonterminal map created, key being the rule number.
    private Integer ruleNumber; // count for created rules
    private NonTerminal firstRule; // main base 'nonterminal'
    private Rule mainRule; // rule for holding base nonterminal
    private HashSet<String> rules; // used for debugging, printing out rules

    //TODO organise digrams remove correctly
    // TODO clean up and add documentation
    //TODO sort out the writefile test bug
    /**
     * main constructor for compress, just initialises, maps and first rules etc
     */
    public Compress() {
        digramMap = new HashMap<>();
        nonTerminalMap = new HashMap<>();
        rules = new HashSet<>();
        ruleNumber = 0;
        mainRule = new Rule(0); //TODO 0 or -1 not contained by any rule
        firstRule = new NonTerminal(mainRule); // create first rule;
    }

    /**
     * main method loops through the input and adds the latest symbol to the main rule
     * calls check digram on for last two symbols
     * some output calls
     * @param input
     */
    public void processInput(String input) {
        // starting from 1 to avoid digram of guard
        getActualFirstRule().addNextSymbol(new Terminal(input.substring(0, 0 + 1)));
        for (int i = 1; i < input.length(); i++) {
            System.out.println(i + " of " + input.length());
            // add next symbol from input to the first rule
            getActualFirstRule().addNextSymbol(new Terminal(input.substring(i, i + 1)));
            checkDigram(getActualFirstRule().getLast().left); //TODO have to send link of left...

            // debugging
//            rules.clear();
//            generateRules(getActualFirstRule().guard.right);
//            System.out.println(rules);

            //printDigrams();
            //printRules();
        }
        generateRules(getActualFirstRule().guard.right);
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
    public void checkDigram(Symbol newDigram) {
        if (digramMap.containsKey(newDigram)) {
            Symbol existingDigram = digramMap.get(newDigram); // existing digram

            if (existingDigram.left.representation.equals("?")
                    && existingDigram.right.right.representation.equals("?")) {
                existingRule(newDigram, existingDigram);
            }
            else {
                createRule(newDigram, existingDigram);
            }
        }
        else {
            digramMap.putIfAbsent(newDigram, newDigram);
        }
    }

    /**
     * create the rule for a repeated digram, requires being done twice for both instances
     * of the digram, rule has two instances, the nonterminal it represents only one
     * takes the latest digram and the digram that occured earlier from the digram map
     *
     */
    //TODO make generic for rule being updated?? not first rule
    public void createRule(Symbol lastDigram, Symbol existingDigram) {
        //TODO update specific rule by getting containing rule - don't add containing for every terminal or rule
        //TODO just access last? you might not know where last is
        ruleNumber++; // increase rule number
        Rule newRule = new Rule(ruleNumber);
        replaceDigram(mainRule, newRule, existingDigram);
        replaceDigram(mainRule, newRule, lastDigram);

        newRule.addSymbols(existingDigram, existingDigram.right);

        replaceRule(existingDigram);
        replaceRule(existingDigram.right);
    }


    /**
     * might not be needed you know... just work the links of the symbols??
     * replace an instance of a digram with a nonterminal
     * @param ruleWithDigram
     * @param
     * @param symbol - the position of the digram to be replaced
     */
    public void replaceDigram(Rule ruleWithDigram, Rule newRule, Symbol symbol) {
        //TODO how to access nonterminal? put method in rule or shift around?
        digramMap.remove(symbol.right);
        digramMap.remove(symbol.left);

        NonTerminal nonTerminal = new NonTerminal(newRule);

        ruleWithDigram.updateNonTerminal(nonTerminal, symbol);


        digramMap.putIfAbsent(nonTerminal.left, nonTerminal.left);
//        //TODO only add right if inbetween two symbols ie not the end
//
        if (!nonTerminal.right.representation.equals("?")) {
            digramMap.putIfAbsent(nonTerminal, nonTerminal); // not really necessary if last symbol in rule
        }
//        System.out.println("here");
//        printDigrams();
//        System.out.println("end");
    }


    /**
     * if a digram is being replaced with a new nonterminal and either symbols of that
     * digram are a rule, their count/usage must be reduced
     * if the count has reached one, then the rule is removed and it's occurence replaced with
     * the symbols it stood for
     * @param symbol
     */
    public void replaceRule(Symbol symbol) {
        if (symbol instanceof NonTerminal) { // if the symbol is a rule reduce usage
            NonTerminal nonTerminal = ((NonTerminal) symbol);
            nonTerminal.rule.count--; // TODO getter setter
            if (nonTerminal.rule.count == USED_ONCE) { // if rule is down to one, remove completely
                nonTerminal.removeRule(); // uses the rule method to reassign elements of rule
            }
        }
    }



    /**
     * already a rule for the digram found, replace it with that rule
     * this needs looking into - TODO recursive here? consolidate with other method?
     * takes the symbol being the latest digram of the main rule and
     * the already exsiting rule/nonterminal for that digram
     * @param
     */
    public void existingRule(Symbol newDigram, Symbol existingDigram) {
        //TODO did have a remove symbol.left
        // TODO think digrams aren't being removed properly
//        System.out.println("exisitng : " + existingDigram);
//        System.out.println("should be a rule " + existingDigram.left.left);
        NonTerminal nonTerminal = new NonTerminal((Rule) existingDigram.left.left);
        //replaceDigram(mainRule, (Rule) existingDigram.left.left, newDigram);
        mainRule.updateNonTerminal(nonTerminal, newDigram);
        checkDigram(nonTerminal.left);
        replaceRule(existingDigram);
        replaceRule(existingDigram.right);
    }


    /**
     * just returns a list of the rules generated in generate rules
     * used for debugging at the moment
     * @return
     */
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
            if (current instanceof NonTerminal) {
                generateRules(((NonTerminal) current).rule.guard.right);
            }
            current = current.right;
        }
        rules.add(output);
    }

    /**
     * prints rules by looping through all the nonterminals generated
     */
    public void printRules() {
        System.out.println();
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
        System.out.println();
    }

    /**
     * prints out all the digrams added to the digram map
     * //TODO check which digrams are kept or can be removed, clean up digram map
     */
    public void printDigrams() {
        for (Symbol s : digramMap.values()) {
            System.out.print(s + " " + s.right + ", ");
        }

        System.out.println();
    }

    /**
     * for debugging, creates the string back from the cfg generated
     * @param rule
     * @return
     */
    public String decompress(Rule rule) {
        Symbol s = rule.guard.left.right;
        String output = "";
        do {
            if (s instanceof Terminal) {
                output += s.toString();
                s = s.right;
            }
            else {
                output += decompress((Rule) s);
                s = s.right;
            }
        } while (!s.representation.equals("?"));
        return output;
    }

    /**
     * returns the encapsulating rule of the base rule
     * for use with decompress test, could probably use nonterminal
     * //TODO decide whether nonterminal or rule will be the standard acess
     * @return
     */
    public Rule getActualFirstRule() {
        return this.mainRule;
    }


    /**
     * getter for the main rule nonterminal
     * @return
     */
    public NonTerminal getFirstRule() {
        return this.firstRule;
    }
}
