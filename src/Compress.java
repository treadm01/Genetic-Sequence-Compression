import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    private Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol
    private Integer ruleNumber; // count for created rules
    private Rule firstRule; // main base 'nonterminal'
    private HashSet<Rule> rules; // used for debugging, printing out rules //TODO make of actual rules
    String mainInput;

    //TODO think bug may be to do with 'matching' digrams that overlap, then replacing them with which ever is found first in the map
    // two digrams of overlap aa will be matched to another aa, but the first should be retrieved
    // also somehow linking rule back to itself...
    // TODO make sure all left and right assigned and handled properly!!!!!!!
    // TODO reorder rules to rule usage
    // TODO keep digrams from left to right
    // TODO need to check whether the digram in rule is entire rule or not

    /**
     * main constructor for compress, just initialises, maps and first rules etc
     */
    public Compress() {
        digramMap = new HashMap<>();
        rules = new HashSet<>();
        ruleNumber = 0;
        firstRule = new Rule(ruleNumber); // create first rule;
    }

    /**
     * main method loops through the input and adds the latest symbol to the main rule
     * calls check digram on for last two symbols
     * some output calls
     * @param input
     */
    public void processInput(String input) {
        //TODO probably creating a digram fir first guard and first symbol that isn't needed
        getFirstRule().addNextSymbol(new Terminal(input.substring(0, 0 + 1)));
        mainInput = input;
        for (int i = 1; i < input.length(); i++) {
            //System.out.println(i + " of " + input.length());
            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(new Terminal(input.substring(i, i + 1)));
            checkDigram(getFirstRule().getLast());
           // printDigrams();


//
//            if (i > 340000) {
//                rules.clear();
//                rules.add(getFirstRule());
//                generateRules(getFirstRule().actualGuard.right);
//                System.out.println(printRules());
//            }

        }

        rules.add(getFirstRule());
        generateRules(getFirstRule().actualGuard.getRight());
        System.out.println(printRules());
        // below for reodering ruls
//
//        List<Rule> orderedRules = rules.stream()
//                .sorted(Rule::compareTo)
//                .collect(Collectors.toList());
//
//        //System.out.println(orderedRules);
//
//        for (Rule r : orderedRules) {
//            if (!r.representation.equals("0")) {
//                r.representation = String.valueOf(orderedRules.indexOf(r)+1);
//            }
//        }
//
//        System.out.println(orderedRules);
    }

    /**
     * method that checks through the main options of latest two symbols
     * if new digram not seen beofre, add to map
     * if seen before and already a rule, use that rule instead
     * if seen and not a rule, make a new rule
     * each new digram with the use of a rule must be checked also
     */
    public void checkDigram(Symbol symbol) {

        //TODO seems to be added twice without a nonterminal being created, possibly because they are overlapping
        //TODO also seems to be removed twice yet then registers as being in the digram map...
        //TODO replaces it with the rule it is in, thus creating the loop
            // check existing digrams for last digram, update them with new rule
            if (digramMap.containsKey(symbol)) {
                // if the existing digram has ? either side, it must be a complete digram rule/ an existing rule
                Symbol existingDigram = digramMap.get(symbol); // existing digram


                // if the matching digram is an overlap do nothing
                if (existingDigram.right != symbol) { // todo added in replacement of check in symol equals method

                    if (existingDigram.getLeft().getLeft().isGuard() && existingDigram.getRight().isGuard()) {

                        existingRule(symbol, existingDigram);
                    } else { // if digram has been seen but only once, no rule, then create new rule

                        createRule(symbol, existingDigram);
                    }
                }
            }
            else {
                // digram not been seen before, add to digram map
                digramMap.putIfAbsent(symbol, symbol);
            }
    }


    /**
     * create the rule for a repeated digram, requires being done twice for both instances
     * of the digram, rule has two instances, the nonterminal it represents only one
     * takes the latest digram and the digram that occured earlier from the digram map
     * @param symbol
     */
    public void createRule(Symbol symbol, Symbol oldSymbol) {
        ruleNumber++; // increase rule number
        Rule newRule = new Rule(ruleNumber); // create new rule to hold new Nonterminal

        replaceDigram(newRule, oldSymbol); // update rule for first instance of digram
        replaceDigram(newRule, symbol);// update rule for last instance of digram

        newRule.addSymbols(oldSymbol.getLeft(), oldSymbol); // add symbols to the new rule/terminal

        //TODO because of the way digrams are only kept for one instance - have to put it back in
       // digramMap.putIfAbsent(oldSymbol, oldSymbol); // adding back in the digram when placed in new rule

        // reduce rule count if being replaced.... if either symbol of digram a nonterminal then rmeove
        replaceRule(oldSymbol.getLeft());
        replaceRule(oldSymbol);
    }

    /**
     * already a rule for the digram found, replace it with that rule
     * this needs looking into - TODO recursive here? consolidate with other method?
     * takes the symbol being the latest digram of the main rule and
     * the already exsiting rule/nonterminal for that digram
     * @param symbol
     */
    public void existingRule(Symbol symbol, Symbol oldSymbol) {
        Guard g = (Guard) oldSymbol.getRight(); // have to get guard and then rule from there
        Rule rule = g.guardRule; // get rule using pointer to it in the guard// right.right will be guard
        Symbol first = rule.getLast().left; // first symbol of digram
        Symbol second = rule.getLast(); // second symbol
        replaceDigram(rule, symbol);

        //TODO because of the way digrams are only kept for one instance - have to put it back in
        //digramMap.putIfAbsent(oldSymbol, oldSymbol); // adding back in the digram when placed in new rule

        replaceRule(first);
        replaceRule(second);
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
            //TODO the rule is down to two when here, will be removed,
            //TODO not sure if a problem with updating rule count (don't think so added an extra)
            // TODO or the removal of the rule when just assigned to a new rule
            //TODO so the link reassinged is the new rule itself


            nonTerminal.rule.count--; // TODO getter setter
            if (nonTerminal.rule.count == USED_ONCE) { // if rule is down to one, remove completely
                removeDigramsFromMap(symbol);
                nonTerminal.removeRule(); // uses the rule method to reassign elements of rule
                checkNewDigrams(nonTerminal.getLeft().getRight(), nonTerminal.getRight(), nonTerminal);
            }
        }
    }

    /**
     * might not be needed you know... just work the links of the symbols??
     * replace an instance of a digram with a nonterminal
     * @param newRule
     * @param symbol - the position of the digram to be replaced
     */
    public void replaceDigram(Rule newRule, Symbol symbol) {
        removeDigramsFromMap(symbol);
        NonTerminal nonTerminal = new NonTerminal(newRule);

        // join the links
        nonTerminal.assignRight(symbol.getRight());
        nonTerminal.assignLeft(symbol.getLeft().getLeft());
        symbol.getLeft().getLeft().assignRight(nonTerminal);
        symbol.getRight().assignLeft(nonTerminal);

        checkNewDigrams(nonTerminal, nonTerminal.getRight(), nonTerminal);
    }

    //TODO could be digrams, as hash if two with same values exist just gets the first?
    public void removeDigramsFromMap(Symbol symbol) {
        if (digramMap.containsKey(symbol.getLeft())){
            Symbol existing = digramMap.get(symbol.getLeft());
            if (existing == symbol.getLeft()) {
                digramMap.remove(symbol.getLeft());
            }
        }


        // don't remove digram if of an overlapping digram
        if (!symbol.getRight().equals(symbol)) {
            digramMap.remove(symbol.getRight());
        }
    }

    public void checkNewDigrams(Symbol left, Symbol right, NonTerminal nonTerminal) {
        if (!nonTerminal.getRight().isGuard()) {
            checkDigram(right);
        }
        if (!nonTerminal.getLeft().isGuard()) {
            checkDigram(left);
        }
    }

    /**
     * just returns a list of the rules generated in generate rules
     * used for debugging at the moment
     * @return
     */
    public String printRules() {
        String output = "";
        for (Rule r : rules) {
            output += r + " > ";
            Symbol current = r.actualGuard.right;
            while (!current.isGuard()) {
                if (current instanceof NonTerminal) {
                    output += ((NonTerminal) current).rule.representation + " ";
                }
                else {
                    output += current + " ";
                }
                current = current.right;
            }
            output += "| ";
        }
        return output;
    }

    /**
     * creates strings of the symbols for each nonterminal
     * @param current
     */
    public void generateRules(Symbol current) {
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
   //             System.out.println("CURRENT IS " + current);
     //           System.out.println("right of nonterminal is " + current.right);
//                System.out.println("LEFT OF IT IS " + current.left);
                Rule rule = ((NonTerminal) current).rule;
                rules.add(rule);
       //         System.out.println("AND THE FIRST IS " + rule.actualGuard.right);
                generateRules(rule.actualGuard.getRight());
            }

//            System.out.println("The next symbol is " + current.right);
//            System.out.println("IS GUARD " + current.right.isGuard());
            current = current.getRight();
        }
        //System.out.println("RETURNING");
    }

    /**
     * prints out all the digrams added to the digram map
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
    public String decompress(Rule rule) {
        Symbol s = rule.actualGuard.right;
        String output = "";
        do {
            if (s instanceof Terminal) {
                output += s.toString();
                s = s.right;
            }
            else {
                output += decompress(((NonTerminal) s).getRule());
                s = s.right;
            }

        } while (!s.isGuard());
        return output;
    }

    /**
     * getter for the main rule nonterminal
     * @return
     */
    public Rule getFirstRule() {
        return this.firstRule;
    }
}