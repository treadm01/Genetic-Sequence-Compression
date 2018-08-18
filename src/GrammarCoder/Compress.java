package GrammarCoder;

import java.util.*;

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    public Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol
    private Rule firstRule; // main rule
    Set<Rule> rules; // rules used for output and encoding
    String mainInput; // string of the input, used for edit rule indexes
    NonTerminal lastNonTerminal; // used as indicator for edit rules

    /**
     * main constructor for compress, just initialises, maps and first rules etc
     */
    public Compress() {
        Rule.ruleNumber = 0;
        digramMap = new HashMap<>();
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
        getFirstRule().addNextSymbol(new Terminal(input.charAt(0)));
        for (int i = 1; i < input.length(); i++) {
            Symbol nextSymbol = new Terminal(input.charAt(i));
            nextSymbol.symbolIndex = i; // keeping index for edits
          //  nextSymbol = checkApproxRepeat(nextSymbol); // if next lot of symbols is approx match add a nonterminal next
            i = nextSymbol.symbolIndex; // update the index for if there is a nonterminal added including a bunch of symbols
            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(nextSymbol);

            checkDigram(getFirstRule().getLast());
        }

        rules.add(getFirstRule()); //todo get with getter and setter
        generateRules(getFirstRule().getFirst());

        // debugging output
        System.out.println(printRules());
        System.out.println("Length of grammar rule: " + getFirstRule().getRuleString().length());
        System.out.println();
    }

    public void addToDigramMap(Symbol symbol) {
        digramMap.putIfAbsent(symbol, symbol);
    }

    //todo this is all one method?????
    public Symbol checkApproxRepeat(Symbol symbol) {
        int editCount = 0;
        Symbol editSymbol = null;
        Symbol lastEdit = null;
        Symbol currentLast = getFirstRule().getLast();
        int newIndex = symbol.symbolIndex; // to jump over the seen symbols
//
        // check nonterminals for approx repeats todo need to be able to find a decent match
        if (lastNonTerminal != null && currentLast.getRepresentation() == lastNonTerminal.getRepresentation()) {
            if (lastNonTerminal.getRight() instanceof NonTerminal) {
                //todo this will get edits too
                NonTerminal nextNonterminal = ((NonTerminal) lastNonTerminal.getRight());
                String lastSequence = nextNonterminal.getRule().getSymbolString(nextNonterminal.getRule(), nextNonterminal.isComplement);
                String nextSequence = "";
                //System.out.println(lastSequence);
                if (symbol.symbolIndex + lastSequence.length() <= mainInput.length()) {
//                    System.out.println("index of last thing " + symbol.symbolIndex);
//                    System.out.println("new index should be " + (symbol.symbolIndex + lastSequence.length()));
                    nextSequence = mainInput.substring(symbol.symbolIndex, symbol.symbolIndex + lastSequence.length());
                    //System.out.println(nextSequence);
                }

                if (lastSequence.length() == nextSequence.length()) {
                    String edits = "";

                    //TODO COULD BE ISSUES WITH SUBRULES AND RULE UTILITY , MULTIPLE SYMBOLS BEING CHECKED???
                    //todo edits not being added in certain instances, remove the edits != "" check
                    // possibly exact matches???
                    int editNumber = 0;
                    for (int j = 0; j < lastSequence.length(); j++) {
                        if (lastSequence.charAt(j) != nextSequence.charAt(j)) {
                            editNumber++;
                            edits += ("*" + symbol.symbolIndex + j) + String.valueOf(nextSequence.charAt(j));
                        }
                    }

                    if (editNumber < lastSequence.length() * 0.1 && edits != "") {
                        // won't have edit indexes.... hmm
                        symbol = new NonTerminal(nextNonterminal.getRule());
                        symbol.setIsEdit(edits);
                        newIndex += lastSequence.length();
                        symbol.symbolIndex = newIndex;
      //                  System.out.println(symbol);
                    }
                }
            }
        }

        if (lastNonTerminal != null && lastNonTerminal.getRight() instanceof Terminal
                && currentLast instanceof Terminal) { // nonterminal has been added
            Symbol nextLeft = lastNonTerminal.getRight();
            //todo need to account for reverse complement again
            Symbol nextRight = nextLeft.getNextTerminal(nextLeft.getRight(), false);
            // if the last nonterminal is actually the last one (might not be needed later with existing rule incorporated)
            if (currentLast.getLeft().getRepresentation() == lastNonTerminal.getRepresentation()
                    && currentLast.getLeft().isComplement == lastNonTerminal.isComplement
                    && !currentLast.getLeft().equals(lastNonTerminal.getRight().getRight())
            //        && !currentLast.getLeft().equals(lastNonTerminal)
            ) {
                //get the following terminal digram
                //if next right matches then that SHOULD be it...
                // as if the last terminal had matched it would have been added
                if (nextRight.getRepresentation() == symbol.getRepresentation()
                        && currentLast.getRepresentation() != nextLeft.getRepresentation()) {
                    editCount++;
                    editSymbol = currentLast; // the one to be edited
                    lastEdit = nextLeft; // the one it is changed to
                }
            }
        }

        if (editCount == 1) {
                Symbol newSymbol = new Terminal(lastEdit.toString().charAt(0));
                newSymbol.assignRight(editSymbol.getRight()); // the new one
                newSymbol.assignLeft(editSymbol.getLeft());
                editSymbol.getRight().assignLeft(newSymbol); // the one being edited
                editSymbol.getLeft().assignRight(newSymbol);
                //todo this bit not relevan as always last... but the edit will be in a different place...
                int offset = editSymbol.symbolIndex;
                newSymbol.setIsEdit("*" + offset + String.valueOf(editSymbol.toString().charAt(0)));
                removeDigrams(editSymbol);
                checkDigram(newSymbol);
        }

        return symbol;
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
                // retrieve existing digram, if complement return original
                Symbol existingDigram = getOriginalDigram(symbol);
                // if the matching digram is an overlap do nothing
                if (existingDigram.getRight() != symbol) { // todo find a better way to place this
                    // if the existing digram is guard either side, it must be a complete digram rule/ an existing rule
                    //todo this can't be working for longer rules??? not chekcing longer rules just digrams
                    if (existingDigram.getLeft().getLeft().isGuard()
                            && existingDigram.getRight().isGuard()) {
                        existingRule(symbol, existingDigram);
                    } else { // if digram has been seen but only once, no rule, then create new rule
                        createRule(symbol, existingDigram);
                    }
                }
            } else { // digram not been seen before, add to digram map
                addToDigramMap(symbol);
                addToDigramMap(getReverseComplement(symbol));
            }
    }

    /**
     * when a digram has occured but was first entered as a reverse complement, ie never seen
     * the original digram it was created from needs to be used for the correct links and location
     * @return
     */
    public Symbol getOriginalDigram(Symbol digram) {
        Symbol symbol = digramMap.get(digram);
        // if digram was created as complement, should have no right hand symbol
        if (symbol.getRight() == null) {//symbol.isComplement) { // can't really use is complement in this way as nonterminals might be part of a complement digram, but not a complement
            symbol = digramMap.get(symbol.getLeft().complement);
        }
        return symbol;
    }

    //todo for terminal and nonterminal
    //get new symbols and assign them together... could be in the class? its creating a digram but could return, just the right
    public Symbol getReverseComplement(Symbol digram) {
        Symbol left = createReverseComplement(digram);
        Symbol right = createReverseComplement(digram.getLeft()); // left and right symbols of reverse digram as it will be entered into the map

        right.assignLeft(left);
        left.assignRight(right);
        left.assignLeft(new Terminal('!')); //todo for comparisons in hashmap, complement requires a left.left

        return right;
    }

    //todo should be in symbol? - used by getreverse, creates the actual instance
    public Symbol createReverseComplement(Symbol currentSymbol) {
        Symbol reverse = new Symbol(); // could it ever be guard? todo yes seems to be, setting guards needlessly
        if (currentSymbol instanceof Terminal) { // right hand side of digram
            reverse = new Terminal(Terminal.reverseSymbol(currentSymbol.toString().charAt(0))); //todo a better way to get char
        }
        else if (currentSymbol instanceof NonTerminal) {
            reverse = new NonTerminal(((NonTerminal) currentSymbol).getRule());
            ((NonTerminal) currentSymbol).getRule().decrementCount();
        }

        // if nonterminal is complement, it's complement wont be, same for terminals,
        // shouldn't be an issue as all are unique and values aren't altered elsewhere
        //currentSymbol.complement = left;
        reverse.isComplement = !currentSymbol.isComplement;
        reverse.complement = currentSymbol;

        return reverse;
    }

    public void removeDigrams(Symbol digram) {
        digramMap.remove(digram);
        //todo creating via getReveseComplement to remove, if created with the objects could add that way too
        digramMap.remove(getReverseComplement(digram));
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
        // if the symbols are not equal then one is a noncomplement and the rule is set for this
        // todo given current location of complement set, dupelicate code here for new and old terminals
        // can be refactored, replace digram needs the correct complements
        newTerminal.isComplement = !symbol.equals(oldSymbol);

        // pass on edits to nonterminals from symbols
        newTerminal.updateEdits(symbol);
        oldTerminal.updateEdits(oldSymbol);

        replaceDigram(oldTerminal, oldSymbol); // update rule for first instance of digram
        replaceDigram(newTerminal, symbol);// update rule for last instance of digram

        // add the repeating digram to the new rule, which in turn is linked to the nonterminal
        newRule.addSymbols(oldSymbol.getLeft(), oldSymbol); // add symbols to the new rule/terminal

        //check the symbols removed and deal with if they are rules
        // reduce rule count if being replaced or remove if 1
        replaceRule(oldSymbol.getLeft());
        replaceRule(oldSymbol);

        // set the last terminal to check next symbols for approx repeat to
        lastNonTerminal = oldTerminal;
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
        //todo can this use get first?
        Guard g = (Guard) oldSymbol.getRight(); // have to get guard and then rule from there
        Rule rule = g.getGuardRule(); // get rule using pointer to it in the guard
        NonTerminal nonTerminal = new NonTerminal(rule);
        rule.nonTerminalList.add(nonTerminal); //TODO ADDING EXSITING RULE NONTERMINALS - WHAT ABOUT REMOVING THEM???????
        nonTerminal.isComplement = !symbol.equals(oldSymbol); //true or alternate value, would have to alternate the nonterminal???

        //todo OLD SYMBOL NEED PULLING UP?
        nonTerminal.updateEdits(symbol);

        // second might not exist
        lastNonTerminal = rule.nonTerminalList.get(1); //unlikely to be different as first would already have been don

        replaceDigram(nonTerminal, symbol);// replace the repeated digram wtih rule
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
                removeDigramsFromMap(symbol);
                removeDigrams(symbol); // when being removed have to remove the actual digram too not just left and right digrams

                //todo this order seems less guaranteed to crash, other more consistent error
                nonTerminal.removeRule(); // uses the rule method to reassign elements of rule
                //order of these two... in relation to removing rules used only once, noncomplement
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
    private void replaceDigram(NonTerminal nonTerminal, Symbol symbol) {
        removeDigramsFromMap(symbol);

        nonTerminal.assignRight(symbol.getRight());
        nonTerminal.assignLeft(symbol.getLeft().getLeft());

        symbol.getLeft().getLeft().assignRight(nonTerminal);
        symbol.getRight().assignLeft(nonTerminal);

        checkNewDigrams(nonTerminal, nonTerminal.getRight(), nonTerminal);
    }

    /**
     * when nonterminals are added or removed the old digrams must be removed from the map
     * currently requires some extra checks for ensuring that the digrams being removed do not
     * correspond with the same digram that is overlapping
     * @param symbol
     */
    private void removeDigramsFromMap(Symbol symbol) {
        // don't remove digram if of an overlapping digram
        //TODO better way to do this
        if (digramMap.containsKey(symbol.getLeft())){ // if it's in there and its not overlapping with a rule that you would want to keep, then remove it
            Symbol existing = digramMap.get(symbol.getLeft());
            if (existing == symbol.getLeft()) {
                removeDigrams(symbol.getLeft());
            }
        }

        if (!symbol.getRight().equals(symbol)) {
            removeDigrams(symbol.getRight());
        }
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
                generateRules(rule.getFirst());
            }
            current = current.getRight();
        }
    }

    //for debugging only
    /**
     * prints out all the digrams added to the digram map
     */
    public String printDigrams() {
        String output = "";
        for (Symbol s : digramMap.values()) {
            output += s.getLeft() + " " + s + ", ";
        }
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

