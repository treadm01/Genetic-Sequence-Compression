package GrammarCoder;

import java.util.*;
import java.util.stream.Collectors;

//todo check digram map adding and removing is accurate, could be losing a lot there

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    public Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol
    private Rule firstRule; // main rule
    public Set<Rule> rules; // rules used for output and encoding
    String mainInput; // string of the input, used for edit rule indexes
    int streamIndex = 0;
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
        Symbol nextSymbol = new Terminal(input.charAt(0));
        getFirstRule().addNextSymbol(nextSymbol);
        // have to do this to ensure first symbol added... todo improve
        for (int i = 1; i < input.length(); i++) {
            //System.out.println(getFirstRule().getRuleString());
            nextSymbol = new Terminal(input.charAt(i));
            nextSymbol.symbolIndex = i; // keeping index for edits
            nextSymbol = checkApproxRepeat(nextSymbol); // if next lot of symbols is approx match add a nonterminal next
            i = nextSymbol.symbolIndex; // update the index for if there is a nonterminal added including a bunch of symbols

            //System.out.println(getFirstRule().getRuleString());
            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(nextSymbol);
            checkDigram(getFirstRule().getLast());

//            System.out.println();
//            System.out.println(getFirstRule().getRuleString());
//            System.out.println("MAP");
//            System.out.println(printDigrams());
//            System.out.println();
//
//            rules.add(getFirstRule()); //todo get with getter and setter
//            generateRules(getFirstRule().getFirst());
//            System.out.println(printRules());
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

            //todo find the best nonterminal to use, or a selection
            List<NonTerminal> orderedList = matchingNonTerminal.getRule().nonTerminalList.stream()
                    .filter(x -> x.getRight() instanceof NonTerminal)
                    .collect(Collectors.toList());

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

            // two nonterminals are the ones worth checking
            if (previousNonTerminal.getRight() instanceof NonTerminal
                    && !previousNonTerminal.getRight().equals(matchingNonTerminal)) { // check no overlap
                NonTerminal nextNonTerminal = (NonTerminal) previousNonTerminal.getRight();
//                System.out.println("RULE " + nextNonTerminal.getRule());
//                System.out.println(nextNonTerminal.getRule().getRuleString());
                String lastSequence = nextNonTerminal.getRule().getSymbolString(nextNonTerminal.getRule(), nextNonTerminal.isComplement);
                String nextSequence;

                // check that comparing the string wont go over length
                if (symbol.symbolIndex + lastSequence.length() <= mainInput.length()) {
                    nextSequence = mainInput.substring(symbol.symbolIndex, symbol.symbolIndex + lastSequence.length());

                    List<Edit> edits = new ArrayList<>();

                    int editNumber = 0;
                    for (int j = 0; j < lastSequence.length(); j++) {
                        if (lastSequence.charAt(j) != nextSequence.charAt(j)) {
                            editNumber++;
                            edits.add(new Edit(symbol.symbolIndex + j, String.valueOf(nextSequence.charAt(j))));
                        }
                    }

                    if (editNumber > 0 && editNumber <= lastSequence.length() * 0.1) {
                        nextSymbol = new NonTerminal(nextNonTerminal.getRule());
                        nextSymbol.setIsEdit(edits);
                        //todo will have to add to nonterminal rule list
                        newIndex += lastSequence.length()-1;
                        nextSymbol.symbolIndex = newIndex;
                        nextSymbol.isComplement = nextNonTerminal.isComplement;
                    }
                }
                // if a complete match update nonterminal here
                // else edit and update here
            }
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
            if (digramMap.containsKey(symbol)) {
                // retrieve existing digram, if complement return original
                Symbol existingDigram = getOriginalDigram(symbol);
                // if the matching digram is an overlap either side (see bug tests) do nothing
                if (existingDigram.getRight() != symbol
                        && existingDigram.getLeft() != symbol) { // todo find a better way to place this
                    // if the existing digram is guard either side, it must be a complete digram rule/ an existing rule
                    if (existingDigram.getLeft().getLeft().isGuard()
                            && existingDigram.getRight().isGuard()) {
                        existingRule(symbol, existingDigram);
                    } else { // if digram has been seen but only once, no rule, then create new rule
                        createRule(symbol, existingDigram);
                    }
                }
            } else { // digram not been seen before, add to digram map
                addToDigramMap(symbol);
                addToDigramMap(symbol.getReverseComplement());
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

    public void removeDigrams(Symbol digram) {
//        System.out.println("REMOVING");
//        System.out.println(digram.getLeft() + " " + digram);
        digramMap.remove(digram);
        //System.out.println(printDigrams());
        //todo creating via getReveseComplement to remove, if created with the objects could add that way too
        //todo removing complement, even if reverse still exists....
        // if tt is there will it have been given the next right
        digramMap.remove(digram.getReverseComplement());
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
        // if the symbols are not equal then one is a noncomplement and the rule is set for this
        // todo given current location of complement set, dupelicate code here for new and old terminals
        // can be refactored, replace digram needs the correct complements
        newTerminal.isComplement = !symbol.equals(oldSymbol);

//        newTerminal.symbolIndex = symbol.getLeft().symbolIndex;
//        oldTerminal.symbolIndex = oldSymbol.getLeft().symbolIndex;
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

//        nonTerminal.symbolIndex = symbol.getLeft().symbolIndex;
        //todo OLD SYMBOL NEED PULLING UP?
        nonTerminal.updateEdits(symbol);

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
              //  System.out.println("before " + printDigrams());
                removeDigramsFromMap(symbol);
                removeDigrams(symbol); // when being removed have to remove the actual digram too not just left and right digrams

               // System.out.println("after " + printDigrams());
                //todo this order seems less guaranteed to crash, other more consistent error... hard to solve though
                nonTerminal.removeRule(); // uses the rule method to reassign elements of rule
                //order of these two... in relation to removing rules used only once, noncomplement
                //System.out.println("checking new digrams : ");
                //System.out.println(nonTerminal.getLeft().getRight().getLeft() + " " + nonTerminal.getLeft().getRight());
                //System.out.println(nonTerminal.getRight().getLeft() + " " +  nonTerminal.getRight());
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
//        System.out.println("removing");
//        System.out.println("centre " + symbol.getLeft() + " " + symbol);
//        System.out.println("centre " + symbol.getLeft() + " " + symbol);

        // don't remove digram if of an overlapping digram
        if (digramMap.containsKey(symbol.getLeft())){ // if it's in there and its not overlapping with a rule that you would want to keep, then remove it
            Symbol existing = digramMap.get(symbol.getLeft());
            if (existing == symbol.getLeft()) {
              //  System.out.println("left " + symbol.getLeft().getLeft() + " " + symbol.getLeft());
                //System.out.println("digrams from map " + symbol.getLeft() + " " + symbol);
                removeDigrams(symbol.getLeft());
            }
        }

        // not so much that its removing the wrong one, but it is editing that which remains
        if (!symbol.getRight().equals(symbol)) { // should this be getright.getrigh? both
            //System.out.println("right " + symbol + " " + symbol.getRight());
            // as if symbol get right would not equal symbol, because if preceded by a, checking one side for overlap
            // but not the other...
            //System.out.println("digrams from map right " + symbol.getLeft() + " " + symbol + " " + symbol.getRight() + " " + symbol.getRight().getRight());
            removeDigrams(symbol.getRight());

            // if removed a digram that was overlapping with itself, need to re-add//todo this needs to be done properly and for both directions
            if (symbol.getRight().equals(symbol.getRight().getRight())) {
                addToDigramMap(symbol.getRight().getRight());
                // whenever adding, add reverse complement
                Symbol reverse = symbol.getRight().getRight().getReverseComplement();
                addToDigramMap(reverse);
            }

        }

        //System.out.println(digramMap);
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

    //noncomplement followed by complement, get first of rule which is nonterminal
    // add rule of that, which is already there... no wait
    // you get the rule of the nonterminal not the containing rule
    public void generateRules(Symbol current) {
        while (!current.isGuard()) {
            //System.out.print(current + " ?");
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
                // keep an index of symbols seen which can then be subtracted from the edit index
                // to give one relative to the symbols..... relative position also used when getting
                // symbol rule string...
                streamIndex++;
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

