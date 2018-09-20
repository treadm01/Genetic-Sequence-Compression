package GrammarCoder;

import java.util.*;

// approximate repeats with sep mini grammar? problem is they would have to be new objects
// todo seperate decode for each level rule string put in decode from grammar
// todo currently decompress is more like an implicit decompress
// todo sep approx repeat too
// search in its own package?
//todo are the nonterminals being kept properly in the set?
public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    private DigramMap digramMap;
    private Rule firstRule; // main rule
    public Set<Rule> rules; // rules used for output and encoding
    private String mainInput; // string of the input, used for edit rule indexes
    private int streamIndex = 0;
    public int numberOfEdits = 0;

    /**
     * main constructor for compress, initialises, hash map and first rules.
     * rule number counter
     */
    public Compress() {
        Rule.ruleNumber = 0;
        digramMap = new DigramMap();
        rules = new HashSet<>();
        firstRule = new Rule(); // create first rule;
    }

    /**
     * main method loops through the input and adds the latest symbol to the main rule
     * calls check digram on for last two symbols
     * method called depending on using edits or not
     * @param input
     */
    public void processInput(String input, Boolean withEdits) {
        mainInput = input; //todo assign and set properly, getter and setter
        Symbol nextSymbol = new Terminal(input.charAt(0));
        getFirstRule().addNextSymbol(nextSymbol);

        if (withEdits) {
            processWithEdits();
        }
        else {
            processWithOutEdits();
        }

        // seperate method
        rules.add(getFirstRule()); //todo get with getter and setter
        generateRules(getFirstRule().getFirst());
        deubugGrammarOutput();
    }

    private void processWithEdits() {
        String sequence = getMainInput();
        ApproxRepeat approxRepeat = new ApproxRepeat(getFirstRule(), getMainInput());
        for (int i = 1; i < sequence.length(); i++) {
            Symbol nextSymbol = new Terminal(sequence.charAt(i));
            nextSymbol.symbolIndex = i; // keeping index for edits
            for (Symbol s : approxRepeat.checkApproxRepeat(nextSymbol)) {
                i = s.symbolIndex; // update the index for if there is a nonterminal added including a bunch of symbols
                getFirstRule().addNextSymbol(s);
                checkDigram(getFirstRule().getLast());
            }
        }
    }

    private void processWithOutEdits() {
        String sequence = getMainInput();
        for (int i = 1; i < sequence.length(); i++) {
            Symbol nextSymbol = new Terminal(sequence.charAt(i));
            nextSymbol.symbolIndex = i; // keeping index for edits
            getFirstRule().addNextSymbol(nextSymbol);
            checkDigram(getFirstRule().getLast());
            // seperate method
//            rules.add(getFirstRule()); //todo get with getter and setter
//            generateRules(getFirstRule().getFirst());
//            System.out.println(printRules());

        }
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
        if (digramMap.containsDigram(symbol)) {
            // retrieve existing digram, if complement return original
            Symbol existingDigram = digramMap.getOriginalDigram(symbol);
            // if the matching digram is an overlap either side do nothing
            if (existingDigram.isNotOverlapping(symbol)) {
                // if the existing digram is guard either side, it must be a complete digram rule/ an existing rule
                if (existingDigram.isARule()) {
                    existingRule(symbol, existingDigram);
                }
                else { // if digram has been seen but only once, no rule, then create new rule
                    createRule(symbol, existingDigram);
                }
            }
        }
        else { // digram not been seen before, add to digram map
            digramMap.addNewDigrams(symbol);
        }
    }

    /**
     * create the rule for a repeated digram, requires being done twice for both instances
     * of the digram, rule has two instances, the nonterminal it represents only one
     * takes the latest digram and the digram that occured earlier from the digram map
     * @param symbol
     */
    private void createRule(Symbol symbol, Symbol oldSymbol) {
        Rule newRule = new Rule(); // create new rule to hold new Nonterminal
        NonTerminal oldTerminal = new NonTerminal(newRule);
        NonTerminal newTerminal = new NonTerminal(newRule);

        oldTerminal.symbolIndex = oldSymbol.getLeft().symbolIndex;
        newTerminal.symbolIndex = symbol.getLeft().symbolIndex;

        // can be refactored, replace digram needs the correct complements
        newTerminal.isComplement = !symbol.equals(oldSymbol);

        // pass on edits to nonterminals from symbols - not needed if not using edits...
        newTerminal.updateEdits(symbol);
        oldTerminal.updateEdits(oldSymbol);

        addNonTerminal(oldTerminal, oldSymbol); // update rule for first instance of digram
        addNonTerminal(newTerminal, symbol);// update rule for last instance of digram
//
//        System.out.println(newRule);
//        System.out.println(oldSymbol.getLeft() + " " + oldSymbol);
//        System.out.println(oldSymbol.getLeft().getLeft().getLeft());
//        if (oldSymbol instanceof NonTerminal) {
//            System.out.println(((NonTerminal) oldSymbol).getRule().getFirst());
//        }
//        // add the repeating digram to the new rule, which in turn is linked to the nonterminal
//        System.out.println(newRule.count);
        if (newRule.removed == null) {
            newRule.addSymbols(oldSymbol.getLeft(), oldSymbol); // add symbols to the new rule/terminal
        }
        else {
            newRule.removed.addSymbols(oldSymbol.getLeft(), oldSymbol); // add symbols to the new rule/terminal
        }

        //check the symbols removed and deal with if they are rules
        //reduce rule count if being replaced or remove if 1

        if (oldSymbol instanceof NonTerminal) {
            if (((NonTerminal) oldSymbol).getRule().getRuleLength() == 0) {
                ((NonTerminal) oldSymbol).getRule().removed = newRule;
            }
        }

        if (oldSymbol.getLeft() instanceof NonTerminal) {
            if (((NonTerminal) oldSymbol.getLeft()).getRule().getRuleLength() == 0) {
                ((NonTerminal) oldSymbol.getLeft()).getRule().removed = newRule;
            }
        }

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
    private void existingRule(Symbol symbol, Symbol oldSymbol) {
        //TODO could this be done more directly? - digram to nonterminal???
        Guard g = (Guard) oldSymbol.getRight(); // have to get guard and then rule from there
        Rule rule = g.getGuardRule(); // get rule using pointer to it in the guard
        NonTerminal nonTerminal = new NonTerminal(rule);
        nonTerminal.symbolIndex = symbol.getLeft().symbolIndex;
        // needs to be from complement variable, but then need to assign each new symbol. maybe
        nonTerminal.isComplement = !symbol.equals(oldSymbol); //true or alternate value, would have to alternate the nonterminal???

        nonTerminal.updateEdits(symbol);

        addNonTerminal(nonTerminal, symbol);// replace the repeated digram wtih rule
        replaceRule(rule.getLast().getLeft()); // check each removed symbol for rule usage
        replaceRule(rule.getLast());
    }

    /**
     * if a digram is being replaced with a new nonterminal and either symbols of that
     * digram are a rule, their count/usage must be reduced
     * if the count has reached one, then the rule is removed and it's occurence replaced with
     * the symbols it stood for
     * @param symbol
     */
    private void replaceRule(Symbol symbol) {
        if (symbol instanceof NonTerminal) { // if the symbol is a rule reduce usage
            NonTerminal nonTerminal = (NonTerminal) symbol;
                nonTerminal.getRule().decrementCount();
                nonTerminal.getRule().nonTerminalList.remove(nonTerminal);
                if (nonTerminal.getRule().getCount() == USED_ONCE) { // if rule is down to one, remove completely
                   // System.out.println("removing " + nonTerminal.getRule());
                    digramMap.removeDigramsFromMap(symbol);
                    digramMap.removeDigrams(symbol); // when being removed have to remove the actual digram too not just left and right digrams
                    nonTerminal.removeRule(); // uses the rule method to reassign elements of rule
                    checkNewDigrams(nonTerminal.getLeft().getRight(), nonTerminal.getRight(), nonTerminal);
                }
        }
    }

    /**
     * might not be needed you know... just work the links of the symbols??
     * replace an instance of a digram with a nonterminal
     * @param nonTerminal
     * @param symbol - the position of the digram to be replaced
     */
    private void addNonTerminal(NonTerminal nonTerminal, Symbol symbol) {
        digramMap.removeDigramsFromMap(symbol);

        nonTerminal.assignRight(symbol.getRight());
        nonTerminal.assignLeft(symbol.getLeft().getLeft());

        symbol.getLeft().getLeft().assignRight(nonTerminal);
        symbol.getRight().assignLeft(nonTerminal);

        nonTerminal.getRule().nonTerminalList.add(nonTerminal);
//        if (symbol.getRight() instanceof NonTerminal) {
//            System.out.println(symbol.getRight());
//            ((NonTerminal) symbol.getRight()).getRule().nonTerminalList.add((NonTerminal) symbol.getRight());
//        }
//        if (symbol.getLeft().getLeft() instanceof NonTerminal) {
//            System.out.println(symbol.getLeft().getLeft());
//            ((NonTerminal) symbol.getLeft().getLeft()).getRule().nonTerminalList.add((NonTerminal) symbol.getLeft().getLeft());
//        }

        checkNewDigrams(nonTerminal, nonTerminal.getRight(), nonTerminal);
    }

    /**
     * when a nonterminal is added or removed the corresponding changes in the surrounding digrams
     * must be checked to maintain digram uniqueness and rule utility
     * @param left
     * @param right
     * @param nonTerminal
     */
    private void checkNewDigrams(Symbol left, Symbol right, NonTerminal nonTerminal) {
        if (!nonTerminal.getRight().isGuard()) {
            checkDigram(right);
        }
        if (!nonTerminal.getLeft().isGuard()) {
            checkDigram(left);
        }
    }

    /**
     *
     * prints out the symbols corresponding to the generated rules
     * @return
     */
    //todo this can use get symbol string from rule right?
    public String printRules() {
        String output = "";
        for (Rule r : rules) {
            output += r + " >" + r.getRuleString() + "\n";
        }
        return output;
    }

    /**
     * works through the symbols and collects all the rules in a set
     * @param current
     */
    public void generateRules(Symbol current) {
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                Rule rule = ((NonTerminal) current).getRule();
                rules.add(rule);
                if (current.isEdited) {
                    for (Edit e : current.editList) {
                        e.index -= streamIndex;
                        numberOfEdits++; // for debug output
                    }
                }
                generateRules(rule.getFirst());
            }
            else {
                streamIndex++; // keeps position for edit indexes
            }
            current = current.getRight();
        }
    }

    public DigramMap getDigramMap() {
        return digramMap;
    }

    /**
     * getter for the main rule nonterminal
     * @return
     */
    public Rule getFirstRule() {
        return this.firstRule;
    }

    private String getMainInput() {return this.mainInput;}


    public void deubugGrammarOutput() {
        // debugging output
        System.out.println(printRules());
        System.out.println("Length of grammar rule: " + getFirstRule().getRuleString().length());
        System.out.println("Rule number: " + rules.size());
        System.out.println("Number of edits " + numberOfEdits);
        int lengthRHS = 0;
        int longestRule = 0;
        int maxRepeat = 0;
        int lengthOfRulesOnly = 0;
        //todo unclear if should include first rule
        for (Rule r : rules) {
            int ruleLength = r.getRuleLength();
            int symbolLength = r.getSymbolString(r, false).length();
            lengthRHS += ruleLength;
            if (r.getRepresentation() != 0) {
                lengthOfRulesOnly += symbolLength;
                if (symbolLength > longestRule) {
                    longestRule = symbolLength;
                }
                if (r.count > maxRepeat) {
                    maxRepeat = r.count;
                }
            }
        }
        System.out.println("length of RHS " + lengthRHS);
        System.out.println("longest rule " + longestRule);
        System.out.println("max repeat " + maxRepeat);
        System.out.println("length of just rules " + lengthOfRulesOnly);
        System.out.println();
    }
}
