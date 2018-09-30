package GrammarCoder;

import java.util.*;

public class Compress {
    private static final int USED_ONCE = 1; // rule used once
    private DigramMap digramMap;
    private Rule firstRule;
    private Set<Rule> rules; // rules used for output and encoding
    private String mainInput; // string of the input, used for edit rule indexes
    private int streamIndex = 0; // indexes for edits

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
     * method that calls main loop depending on use of edits or not
     * @param input
     */
    public void processInput(String input, Boolean withEdits) {
        mainInput = input;
        Symbol nextSymbol = new Terminal(input.charAt(0));
        getFirstRule().addNextSymbol(nextSymbol);

        if (withEdits) {
            processWithEdits();
        }
        else {
            processWithOutEdits();
        }

        //debugGrammarOutput();
    }

    /**
     * loops through the input and adds the latest symbol to the main rule
     * calls check digram on the last two symbols
     */
    private void processWithEdits() {
        String sequence = getMainInput();
        ApproxRepeat approxRepeat = new ApproxRepeat(getFirstRule(), getMainInput());
        Symbol nextSymbol;
        for (int i = 1; i < sequence.length(); i++) {
            nextSymbol = new Terminal(sequence.charAt(i));
            nextSymbol.symbolIndex = i; // keeping index for edits
            for (Symbol s : approxRepeat.checkApproxRepeat(nextSymbol)) {
                i = s.symbolIndex; // update the index for if there is a nonterminal added including a bunch of symbols
                getFirstRule().addNextSymbol(s);
                checkDigram(getFirstRule().getLast());
            }
        }
        alterEditIndex(getFirstRule().getFirst()); // indexes of edits changed relative to the nonterminal they are in
    }

    private void processWithOutEdits() {
        String sequence = getMainInput();
        Symbol nextSymbol;
        for (int i = 1; i < sequence.length(); i++) {
            nextSymbol = new Terminal(sequence.charAt(i));
            getFirstRule().addNextSymbol(nextSymbol);
            checkDigram(getFirstRule().getLast());
        }
    }

    /**
     * method that checks through the main options of latest two symbols
     * if new digram not seen before, add to map
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
     * of the digram, rule has two instances, the nonterminal it represents only one.
     * takes the latest digram and the digram that occurred earlier from the digram map
     * @param symbol
     */
    private void createRule(Symbol symbol, Symbol oldSymbol) {
        Rule newRule = new Rule(); // create new rule to hold new Nonterminal
        NonTerminal oldTerminal = new NonTerminal(newRule);
        NonTerminal newTerminal = new NonTerminal(newRule);

        // update indexes of digrams, for use with edits only
        oldTerminal.symbolIndex = oldSymbol.getLeft().symbolIndex;
        newTerminal.symbolIndex = symbol.getLeft().symbolIndex;

        newTerminal.setIsComplement(!symbol.equals(oldSymbol));

        // pass on edits to nonterminals from symbols - not needed if not using edits...
        newTerminal.updateEdits(symbol);
        oldTerminal.updateEdits(oldSymbol);

        addNonTerminal(oldTerminal, oldSymbol); // update rule for first instance of digram
        addNonTerminal(newTerminal, symbol);// update rule for last instance of digram

        Objects.requireNonNullElse(newRule.removed, newRule).addSymbols(oldSymbol.getLeft(), oldSymbol); // add symbols to the new rule/terminal

        //check the symbols removed and deal with if they are rules
        //reduce rule count if being replaced, or remove if 1
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

        // check removed digrams for nonterminal removal, rule utility
        replaceRule(oldSymbol.getLeft());
        replaceRule(oldSymbol);
    }

    /**
     * already a rule for the digram found, replace it with that rule
     * takes the symbol being the latest digram of the main rule and
     * the already exsiting rule/nonterminal for that digram
     * @param symbol
     */
    private void existingRule(Symbol symbol, Symbol oldSymbol) {
        Guard g = (Guard) oldSymbol.getRight(); // have to get guard and then rule from there
        Rule rule = g.getGuardRule(); // get rule using pointer to it in the guard
        NonTerminal nonTerminal = new NonTerminal(rule);
        nonTerminal.symbolIndex = symbol.getLeft().symbolIndex;
        nonTerminal.setIsComplement(!symbol.equals(oldSymbol));

        nonTerminal.updateEdits(symbol);

        addNonTerminal(nonTerminal, symbol);// replace the repeated digram with rule
        replaceRule(rule.getLast().getLeft()); // check each removed symbol for rule usage
        replaceRule(rule.getLast());
    }

    /**
     * if a digram is being replaced with a new nonterminal and either symbols of that
     * digram are a rule, their count/usage must be reduced
     * if the count has reached one, then the rule is removed and it's occurrence replaced with
     * the symbols it stood for
     * @param symbol
     */
    private void replaceRule(Symbol symbol) {
        if (symbol instanceof NonTerminal) { // if the symbol is a rule reduce usage
            NonTerminal nonTerminal = (NonTerminal) symbol;
            nonTerminal.getRule().decrementCount();
            nonTerminal.getRule().nonTerminalList.remove(nonTerminal);
            if (nonTerminal.getRule().getCount() == USED_ONCE) { // if rule is down to one, remove completely
                digramMap.removeDigramsFromMap(symbol);
                digramMap.removeDigrams(symbol); // when being removed have to remove the actual digram too not just left and right digrams
                nonTerminal.removeRule(); // uses the rule method to reassign elements of rule
                checkNewDigrams(nonTerminal.getLeft().getRight(), nonTerminal.getRight(), nonTerminal);
            }
        }
    }

    /**
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
     * iterate over the grammar for nonterminals with edits and
     * update their index positions relative to the nonterminal they occur in.
     * most efficient way of storing indexes
     * @param current
     */
    private void alterEditIndex(Symbol current) {
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                Rule rule = ((NonTerminal) current).getRule();
                if (current.getIsEdited()) {
                    for (Edit e : current.editList) {
                        e.index -= streamIndex;
                    }
                }
                alterEditIndex(rule.getFirst());
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

    public Rule getFirstRule() {
        return this.firstRule;
    }

    private String getMainInput() {return this.mainInput;}

    public Set<Rule> getRules() {
        return rules;
    }

    /**
     * debug output method, prints most of the results used to compare
     * grammar construction, rules, etc
     */
    private void debugGrammarOutput() {
        rules.add(getFirstRule());
        generateRules(getFirstRule().getFirst());
        System.out.println(printRules());
        System.out.println("Length of grammar rule: " + getFirstRule().getRuleString().length());
        System.out.println("Rule number: " + rules.size());
//        int numberOfEdits = 0; //
//        System.out.println("Number of edits " + numberOfEdits);
        int lengthRHS = 0;
        int longestRule = 0;
        int maxRepeat = 0;
        int lengthOfRulesOnly = 0;
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

    /**
     * works through the symbols and collects all the rules in a set
     * only used for creating grammar output
     * @param current
     */
    public void generateRules(Symbol current) {
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                Rule rule = ((NonTerminal) current).getRule();
                rules.add(rule);
                generateRules(rule.getFirst());
            }
            else {
                streamIndex++; // keeps position for edit indexes
            }
            current = current.getRight();
        }
    }

    /**
     * prints out the symbols corresponding to the generated rules
     * used for debugging
     * @return
     */
    public String printRules() {
        String output = "";
        for (Rule r : rules) {
            output += r + " >" + r.getRuleString() + "\n";
        }
        return output;
    }
}
