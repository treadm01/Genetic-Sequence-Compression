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

    //TODO reordering of rule numbers, most frequently used are lower? read
    //TODO can createRule/existingRule methods be comined more - process improved
    // TODO can methods be put in their respective classes
    // TODO use string or int consistently for representation of nonterminals etc
    // TODO remove old digrams after adding nonTerminal
    // TODO make sure the way symbols are created and stored isn't going to cause issues later on
    // TODO these rule manipulations set as methods in the classes themselves?
    // TODO can the nonterminal map be removed completely?
    // TODO implementation of containingRule indicator needs checking - use head and tail to get direct access?
    // TODO same for checking if digram is a length two nonterminal?
    // tODO containing rules are not maintained for most symbols, only used via heads and tails of rules... remove if possible

    /**
     * main constructor for compress, just initialises, maps and first rules etc
     */
    public Compress() {
        digramMap = new HashMap<>();
        nonTerminalMap = new HashMap<>();
        rules = new HashSet<>();
        ruleNumber = 0;
        firstRule = new NonTerminal(ruleNumber); // create first rule;
    }

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
        //TODO can probably clean up use of digrams here
        Symbol lastDigram = firstRule.getLast();
        // check existing digrams for last digram, update them with new rule
        if (digramMap.containsKey(lastDigram)) {
            //TODO a better way to check for existing rule
            // if the existing digram has ? either side, it must be a complete digram rule/ an existing rule
            Symbol existingDigram = digramMap.get(lastDigram); // existing digram
            //TODO maintain a length of nonterminal? then just check if it is 2??
            if (existingDigram.left.left.representation.equals("?")
                    && existingDigram.right.representation.equals("?")
                    //TODO create a method to check the containing rule...
                    //TODO below is superfluous check i think, if the digram exists,
                    // TODO is two then it should be in the map anyway...
                    //&& nonTerminalMap.containsKey(existingDigram.left.left.containingRule)
                    ) {

                existingRule(lastDigram, nonTerminalMap.get(existingDigram.right.containingRule));
            }
            else { // if digram has been seen but only once, no rule, then create new rule
                createRule(lastDigram, existingDigram);
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
            rule.nonTerminal.count--; // TODO getter setter
            if (rule.nonTerminal.count == USED_ONCE) { // if rule is down to one, remove completely
                rule.removeRule(); // uses the rule method to reassign elements of rule
                //TODO check out remove rule, decide on representation use
                nonTerminalMap.remove(Integer.valueOf(rule.representation)); // remove that rule from the map
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
     * takes the symbol being the latest digram of the main rule and
     * the already exsiting rule/nonterminal for that digram
     * @param symbol
     */
    public void existingRule(Symbol symbol, NonTerminal nonTerminal) {
        // getting last and last.left should be ok as exisitng rule should be matching a digram
        // get the existing rule from the nonterminal map, by checking the digram and where that digram
        // currently resides, using the containing rule of the left hand head/buffer
        Symbol first = nonTerminal.last.left; // first symbol of digram
        Symbol second = nonTerminal.last; // second symbol

        // create new rule and send through nonTerminal and containing rule of where it will be PROBABLY ALWAYS 0 HERE AS LASTDIGRAM
        Rule rule = new Rule(nonTerminal, symbol.containingRule);

        firstRule.updateNonTerminal(rule, symbol); // update rule for first digram
        digramMap.remove(symbol.left); // removing digram of previous two symbols as should no longer occur

        //TODO why only recurse from here? consolidate methods
        checkDigram(); // adding a re check here for new terminal added, should probably be somewhere else as well

        //TODO below does not seem to be necessary
        //digramMap.putIfAbsent(rule, rule); // add potential new digram with added nonTerminal after recursion

        // reduce rule count if being replaced.... if either symbol of digram a nonterminal then rmeove
        // references retrieved earlier as updating first rule can lose the second
        replaceRule(first);
        replaceRule(second);
    }

    /**
     * create the rule for a repeated digram, requires being done twice for both instances
     * of the digram, rule has two instances, the nonterminal it represents only one
     * takes the latest digram and the digram that occured earlier from the digram map
     * @param symbol
     */
    //TODO make generic for rule being updated?? not first rule
    public void createRule(Symbol symbol, Symbol oldSymbol) {
        //TODO update specific rule by getting containing rule - don't add containing for every terminal or rule
        //TODO just access last? you might not know where last is
        Symbol secondDigram = symbol; // get new digram from last symbol added
        Symbol firstDigram = oldSymbol; // matching digram in the rule
        ruleNumber++; // increase rule number
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
            if (current instanceof Rule) {
                generateRules(((Rule) current).nonTerminal.guard.left.right);
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
            System.out.println(s.left + " " + s);
        }
    }

    /**
     * for debugging, creates the string back from the cfg generated
     * @param rule
     * @return
     */
    public String decompress(Rule rule) {
        Symbol s = rule.nonTerminal.guard.left.right;
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
