package GrammarCoder;

import java.util.*;

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    private DigramMap digramMap;
    private Rule firstRule; // main rule
    public Set<Rule> rules; // rules used for output and encoding
    private String mainInput; // string of the input, used for edit rule indexes
    private int streamIndex = 0;

    /**
     * main constructor for compress, just initialises, maps and first rules etc
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
     * some output calls
     * @param input
     */
    public void processInput(String input) {
        mainInput = input; //todo assign and set properly, getter and setter
        Symbol nextSymbol = new Terminal(input.charAt(0));
        getFirstRule().addNextSymbol(nextSymbol);
        for (int i = 1; i < input.length(); i++) {
            nextSymbol = new Terminal(input.charAt(i));
            nextSymbol.symbolIndex = i; // keeping index for edits
//            nextSymbol = checkApproxRepeat(nextSymbol); // if next lot of symbols is approx match add a nonterminal next
            i = nextSymbol.symbolIndex; // update the index for if there is a nonterminal added including a bunch of symbols
            getFirstRule().addNextSymbol(nextSymbol);
            checkDigram(getFirstRule().getLast());
        }
        rules.add(getFirstRule()); //todo get with getter and setter
        generateRules(getFirstRule().getFirst());
    }

    public void deubugGrammarOutput() {
        // debugging output
        System.out.println(printRules());
        System.out.println("Length of grammar rule: " + getFirstRule().getRuleString().length());
        System.out.println("Rule number: " + rules.size());
        int lengthRHS = 0;
        int longestRule = 0;
        int maxRepeat = 0;
        //todo unclear if should include first rule
        for (Rule r : rules) {
            int ruleLength = r.getRuleLength();
            int symbolLength = r.getSymbolString(r, false).length();
            lengthRHS += ruleLength;
            if (r.getRepresentation() != 0) {
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
        System.out.println();
    }

    //todo existing rules can't take advantage of?
    public Symbol checkApproxRepeat(Symbol symbol) {
        // will either be the same symbol sent, an edited one (maybe) or one at the end of a nonterminal sequence
        Symbol nextSymbol = symbol;
        NonTerminal matchingNonTerminal;
        NonTerminal previousNonTerminal;
        int newIndex = symbol.symbolIndex; // to jump over the seen symbols
        // at this point the next potential terminal will not have been added

        // get the last nonterminal, if it wasn't one then not checking
        if (getFirstRule().getLast() instanceof NonTerminal) {
            matchingNonTerminal = (NonTerminal) getFirstRule().getLast();

            NonTerminal longestNext = matchingNonTerminal.getRule().nonTerminalList.get(0);

            // need to try and find the best match to try and edit
            // greatest match for the fewest amount of edits
            //int count = 0;
//            for (NonTerminal nt : orderedList) {
//                if (((NonTerminal)nt.getRight()).getRule().getRuleLength() > count) {
//                    longestNext = nt;
//                    count = ((NonTerminal)nt.getRight()).getRule().getRuleLength();
//                }
//            }

            // longest length for the fewest amounts of edits only works across two terminals
            // when there may be a small next terminal with a small edit that encodes a lot more
            // is there anyway to tell?
            previousNonTerminal = longestNext;
            // need to compare the next things length... but what about edits.... would have to compare to underlying rule

            List<Symbol> next = new ArrayList<>();
            Symbol nt = previousNonTerminal.getRight();
            String lastSequence = "";
            while (nt instanceof NonTerminal && nt != matchingNonTerminal && lastSequence.length() < 15) {
                lastSequence += ((NonTerminal) nt).getRule().getSymbolString(((NonTerminal) nt).getRule(), nt.isComplement);
                next.add(nt);
                nt = nt.getRight();
            }

//            for (Symbol n : next) {
//                //System.out.print(" " + n + " ");
//            }

            String nextSequence = "";
            if (symbol.symbolIndex + lastSequence.length() <= mainInput.length()) {
                nextSequence = mainInput.substring(symbol.symbolIndex, symbol.symbolIndex + lastSequence.length());
                int editNumber = 0;
                List<Edit> edits = new ArrayList<>();
                for (int j = 0; j < lastSequence.length(); j++) {
                    if (lastSequence.charAt(j) != nextSequence.charAt(j)) {
                        editNumber++;
                        edits.add(new Edit(symbol.symbolIndex + j, String.valueOf(nextSequence.charAt(j))));
                    }
                }
                if (editNumber > 0 && editNumber <= lastSequence.length() * 0.3) {
//                    System.out.print("MATCH -");
//                    for (Symbol s : next) {
//                        System.out.print(" " + s + " ");
//                    }
//                    System.out.println();
//                    System.out.println(lastSequence);
//                    System.out.println(nextSequence);
//                    System.out.println();

                    if (next.size() == 1) {
                        NonTerminal nextNonTerminal = (NonTerminal) next.get(0);
                        nextSymbol = new NonTerminal(nextNonTerminal.getRule());
                        nextSymbol.setIsEdit(edits);
                        //todo will have to add to nonterminal rule list
                        newIndex += lastSequence.length() - 1;
                        nextSymbol.symbolIndex = newIndex;
                        nextSymbol.isComplement = nextNonTerminal.isComplement;
                    }

                }


            }

            // will be difficult to maintain digrams....
            // when adding new one might alter next enough to not make sense anymore
            // which edits refer to which digrams and how to add????
            // do it one nonterminal at a time
        }

        //System.out.println(getFirstRule().getRuleString());

        return nextSymbol;
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
        if (digramMap.existingDigram(symbol)) {
            // retrieve existing digram, if complement return original
            Symbol existingDigram = digramMap.getOriginalDigram(symbol);
            // if the matching digram is an overlap either side (see bug tests) do nothing
            if (existingDigram.getRight() != symbol
                    && existingDigram.getLeft() != symbol) { // todo find a better way to place this
                // if the existing digram is guard either side, it must be a complete digram rule/ an existing rule
                if (existingDigram.getLeft().getLeft().isGuard()
                        && existingDigram.getRight().isGuard()) {
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
        newRule.nonTerminalList.add(oldTerminal);
        newRule.nonTerminalList.add(newTerminal);

        // can be refactored, replace digram needs the correct complements
        newTerminal.isComplement = !symbol.equals(oldSymbol);

        // pass on edits to nonterminals from symbols
        newTerminal.updateEdits(symbol);
        oldTerminal.updateEdits(oldSymbol);

        addNonTerminal(oldTerminal, oldSymbol); // update rule for first instance of digram
        addNonTerminal(newTerminal, symbol);// update rule for last instance of digram

        // add the repeating digram to the new rule, which in turn is linked to the nonterminal
        newRule.addSymbols(oldSymbol.getLeft(), oldSymbol); // add symbols to the new rule/terminal

        //check the symbols removed and deal with if they are rules
        //reduce rule count if being replaced or remove if 1
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
        rule.nonTerminalList.add(nonTerminal); //TODO ADDING EXSITING RULE NONTERMINALS - WHAT ABOUT REMOVING THEM???????
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
            if (nonTerminal.getRule().getCount() == USED_ONCE) { // if rule is down to one, remove completely
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
}

