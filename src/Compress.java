import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    private Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol
    private Integer ruleNumber; // count for created rules
    private Rule firstRule; // main base 'nonterminal'
    private NonTerminal mainRule; // rule for holding base nonterminal
    private HashSet<String> rules; // used for debugging, printing out rules

    // nonterminal points to head/guard of rule, that points to first, last points back to head
    // any nonterminals point to rule
    // TODO is it better without nonterminal map?? how to print rules properly?
    // TODO reorder rules to rule usage
    // TODO access guard positions better, containing rule etc, use numbers rather than strings
    //TODO keep digrams from left to right

    /**
     * main constructor for compress, just initialises, maps and first rules etc
     */
    public Compress() {
        digramMap = new HashMap<>();
        rules = new HashSet<>();
        ruleNumber = 0;
        firstRule = new Rule(ruleNumber); // create first rule;
        mainRule = new NonTerminal(getFirstRule()); //TODO 0 or -1 not contained by any rule
    }

    /**
     * main method loops through the input and adds the latest symbol to the main rule
     * calls check digram on for last two symbols
     * some output calls
     * @param input
     */
    public void processInput(String input) {
        getFirstRule().addNextSymbol(new Terminal(input.substring(0, 0 + 1)));
        for (int i = 1; i < input.length(); i++) {
            //System.out.println(i + " of " + input.length());
            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(new Terminal(input.substring(i, i + 1)));
            checkDigram(getFirstRule().getLast());
            //printDigrams();
            //printRules();
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
    public void checkDigram(Symbol symbol) {
        // as checkDigram is called recursively when digrams change,
        // this first check is to ensure that the digram is not at the edge TODO better way to do this
        if (!symbol.isGuard()
                && !symbol.left.isGuard()) {
            // check existing digrams for last digram, update them with new rule
            if (digramMap.containsKey(symbol)) {
                //TODO a better way to check for existing rule
                // if the existing digram has ? either side, it must be a complete digram rule/ an existing rule
                Symbol existingDigram = digramMap.get(symbol); // existing digram
                //TODO maintain a length of nonterminal? then just check if it is 2??
                if (existingDigram.left.left.isGuard()
                        && existingDigram.right.isGuard()) {
                    //TODO just need a get rule from nonterminal, from symbol, rather than containing rule
                    //TODO the actual rule, then send rule as reference for new terminal...
                    //TODO the rule needs to be an object you can link to
                    //System.out.println("RULE " + existingDigram.right.right);
                    existingRule(symbol, existingDigram);//ruleMap.get(existingDigram.right.containingRule));
                } else { // if digram has been seen but only once, no rule, then create new rule
                    createRule(symbol, existingDigram);
                }
            }
            // digram not been seen before, add to digram map
            else {
                digramMap.putIfAbsent(symbol, symbol);
            }
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
        if (symbol instanceof NonTerminal) { // if the symbol is a rule reduce usage
            NonTerminal nonTerminal = (NonTerminal) symbol;
            nonTerminal.rule.count--; // TODO getter setter
            if (nonTerminal.rule.count == USED_ONCE) { // if rule is down to one, remove completely
                if (!symbol.left.isGuard()) {
                    digramMap.remove(symbol.left);
                }
                if (!symbol.right.isGuard()) {
                    digramMap.remove(symbol.right);
                }
                nonTerminal.removeRule(); // uses the rule method to reassign elements of rule
                checkDigram(nonTerminal.right);
                checkDigram(nonTerminal.left.right);
        //        ruleMap.remove(Integer.valueOf(nonTerminal.representation)); // remove that rule from the map
            }
        }
    }

    /**
     * might not be needed you know... just work the links of the symbols??
     * replace an instance of a digram with a nonterminal
     * @param ruleWithDigram
     * @param rule
     * @param symbol - the position of the digram to be replaced
     */
    public void replaceDigram(NonTerminal ruleWithDigram, Rule rule, Symbol symbol) {
        digramMap.remove(symbol.left);
        if (!symbol.right.isGuard()) {
            digramMap.remove(symbol.right);
        }
        NonTerminal nonTerminal = new NonTerminal(rule);
        ruleWithDigram.rule.updateNonTerminal(nonTerminal, symbol); // update for second
        checkDigram(nonTerminal);
        checkDigram(nonTerminal.right);
    }


    /**
     * already a rule for the digram found, replace it with that rule
     * this needs looking into - TODO recursive here? consolidate with other method?
     * takes the symbol being the latest digram of the main rule and
     * the already exsiting rule/nonterminal for that digram
     * @param symbol
     */
    public void existingRule(Symbol symbol, Symbol oldSymbol) {
//        System.out.println("SYMBOL " + symbol.left + " " + symbol);
//        System.out.println(nonTerminal);
        Rule rule =  (Rule) oldSymbol.right.right;
        Symbol first = rule.last.left; // first symbol of digram
        Symbol second = rule.last; // second symbol
        replaceDigram(mainRule, rule, symbol);
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
        Rule newRule = new Rule(ruleNumber); // create new rule to hold new Nonterminal

        //TODO what is this doing if the rule isn't the mainrule????
        replaceDigram(mainRule, newRule, firstDigram); // update rule for first instance of digram
        replaceDigram(mainRule, newRule, secondDigram);// update rule for last instance of digram

        // TODO this below can't be done before the digrams are dealt with in a rule, as it wipes out the references of left and right. check if ok
        // update containing rule here... TODO or not for now, just use head and tail
        newRule.addSymbols(firstDigram.left, firstDigram); // add symbols to the new rule/terminal

        // put the new rule/nonTerminal into the map
        //ruleMap.putIfAbsent(ruleNumber, newRule);

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
        while (!current.isGuard()) {
            output += current + " ";
            if (current instanceof NonTerminal) {
                generateRules(((NonTerminal) current).rule.guard.left.right);
            }
            current = current.right;
        }
        rules.add(output);
    }

    /**
     * prints rules by looping through all the nonterminals generated
     */
    public void printRules() {
//        System.out.println();
//        for (Rule rule : ruleMap.values()) {
//            Symbol s = rule.guard.left.right;
//            String output = "";
//            do {
//                output += s.toString() + " ";
//                s = s.right;
//            } while (!s.representation.equals("?"));
//
//            System.out.print("#" + rule + " > ");
//            System.out.print(output);
//            //System.out.print(" use number " + nt.count);
//            System.out.println();
//        }
//        System.out.println();
    }

    /**
     * prints out all the digrams added to the digram map
     * //TODO check which digrams are kept or can be removed, clean up digram map
     */
    public void printDigrams() {
        for (Symbol s : digramMap.values()) {
            System.out.print(s.left + " " + s + ", ");
        }
        System.out.println();
    }

    /**
     * for debugging, creates the string back from the cfg generated
     * @param rule
     * @return
     */
    public String decompress(NonTerminal rule) {
        Symbol s = rule.rule.guard.left.right;
        String output = "";
        do {
            if (s instanceof Terminal) {
                output += s.toString();
                s = s.right;
            }
            else {
                output += decompress((NonTerminal) s);
                s = s.right;
            }
        } while (!s.isGuard());
        return output;
    }

    /**
     * returns the encapsulating rule of the base rule
     * for use with decompress test, could probably use nonterminal
     * //TODO decide whether nonterminal or rule will be the standard acess
     * @return
     */
    public NonTerminal getActualFirstRule() {
        return this.mainRule;
    }


    /**
     * getter for the main rule nonterminal
     * @return
     */
    public Rule getFirstRule() {
        return this.firstRule;
    }
}