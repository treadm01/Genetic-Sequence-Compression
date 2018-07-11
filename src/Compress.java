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
        for (int i = 0; i < input.length(); i++) {
            //System.out.println(i + " of " + input.length());
            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(new Terminal(input.substring(i, i + 1)));
            checkDigram(getFirstRule().getLast());
//
//            if (i > 340000) {
//                rules.clear();
//                rules.add(getFirstRule());
//                generateRules(getFirstRule().actualGuard.right);
//                System.out.println(printRules());
//            }

        }

        rules.add(getFirstRule());
        generateRules(getFirstRule().actualGuard.right);
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
            // check existing digrams for last digram, update them with new rule
            if (digramMap.containsKey(symbol)) {
                // if the existing digram has ? either side, it must be a complete digram rule/ an existing rule
                Symbol existingDigram = digramMap.get(symbol); // existing digram
                if (existingDigram.left.left.isGuard() && existingDigram.right.isGuard()) {
                    existingRule(symbol, existingDigram);
                }
                else { // if digram has been seen but only once, no rule, then create new rule
                    createRule(symbol, existingDigram);
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
        Symbol secondDigram = symbol; // get new digram from last symbol added
        Symbol firstDigram = oldSymbol; // matching digram in the rule
        ruleNumber++; // increase rule number
        Rule newRule = new Rule(ruleNumber); // create new rule to hold new Nonterminal

        replaceDigram(newRule, firstDigram); // update rule for first instance of digram
        replaceDigram(newRule, secondDigram);// update rule for last instance of digram

        newRule.addSymbols(firstDigram.left, firstDigram); // add symbols to the new rule/terminal

        if (newRule.representation.equals("4053")) {
            System.out.println("before replace");
            System.out.println("left " + newRule.actualGuard.right);
            System.out.println("right " + newRule.actualGuard.right.right);
            System.out.println(firstDigram.left);
        }

        // reduce rule count if being replaced.... if either symbol of digram a nonterminal then rmeove
        replaceRule(firstDigram.left);
        if (newRule.representation.equals("4053")) {
            System.out.println("first replace");
            System.out.println("left " + newRule.actualGuard.right);
            System.out.println("right " + newRule.actualGuard.right.right);
        }
        replaceRule(firstDigram);
        if (newRule.representation.equals("4053")) {
            System.out.println("second replace");
            System.out.println("left " + newRule.actualGuard.right);
            System.out.println("right " + newRule.actualGuard.right.right);
        }
    }

    /**
     * already a rule for the digram found, replace it with that rule
     * this needs looking into - TODO recursive here? consolidate with other method?
     * takes the symbol being the latest digram of the main rule and
     * the already exsiting rule/nonterminal for that digram
     * @param symbol
     */
    public void existingRule(Symbol symbol, Symbol oldSymbol) {
        Guard g = (Guard) oldSymbol.right; // have to get guard and then rule from there
        Rule rule = g.guardRule; // get rule using pointer to it in the guard// right.right will be guard
        Symbol first = rule.getLast().left; // first symbol of digram
        Symbol second = rule.getLast(); // second symbol
        replaceDigram(rule, symbol);
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
            if (nonTerminal.representation.equals("4052")) {
                System.out.println("RULE HERE IS " + nonTerminal.rule.count);
            }

            nonTerminal.rule.count--; // TODO getter setter
            if (nonTerminal.rule.count == USED_ONCE) { // if rule is down to one, remove completely
                removeDigramsFromMap(symbol);
                nonTerminal.removeRule(); // uses the rule method to reassign elements of rule
                checkNewDigrams(nonTerminal.left.right, nonTerminal.right, nonTerminal);
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
        nonTerminal.assignRight(symbol.right);
        nonTerminal.assignLeft(symbol.left.left);
        symbol.left.left.assignRight(nonTerminal);
        symbol.right.assignLeft(nonTerminal);

        checkNewDigrams(nonTerminal, nonTerminal.right, nonTerminal);
    }

    //TODO could be digrams, as hash if two with same values exist just gets the first?
    public void removeDigramsFromMap(Symbol symbol) {
        digramMap.remove(symbol.left);
        digramMap.remove(symbol.right);
    }

    public void checkNewDigrams(Symbol left, Symbol right, NonTerminal nonTerminal) {
        if (!nonTerminal.right.isGuard()) {
            checkDigram(right);
        }
        if (!nonTerminal.left.isGuard()) {
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
                generateRules(rule.actualGuard.right);
            }

//            System.out.println("The next symbol is " + current.right);
//            System.out.println("IS GUARD " + current.right.isGuard());
            current = current.right;
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