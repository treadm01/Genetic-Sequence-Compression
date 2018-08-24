package GrammarCoder;

import java.util.*;
import java.util.stream.Collectors;

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    public Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol
    private Rule firstRule; // main rule
    Set<Rule> rules; // rules used for output and encoding
    String mainInput; // string of the input, used for edit rule indexes
    int streamIndex = 0;
    Map<Character, Set<NonTerminal>> rulesByStartSymbols = new HashMap<>();
    Map<Character, Set<NonTerminal>> rulesByEndSymbols = new HashMap<>();

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
        for (int i = 1; i < input.length(); i++) {
            if (!rulesByStartSymbols.containsKey(input.charAt(i))) {
                rulesByStartSymbols.putIfAbsent(input.charAt(i), new HashSet());
                rulesByEndSymbols.putIfAbsent(input.charAt(i), new HashSet());
            }
            nextSymbol = new Terminal(input.charAt(i));
            nextSymbol.symbolIndex = i; // keeping index for edits
            nextSymbol = checkApproxRepeat(nextSymbol); // if next lot of symbols is approx match add a nonterminal next
            i = nextSymbol.symbolIndex; // update the index for if there is a nonterminal added including a bunch of symbols

            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(nextSymbol);
            checkDigram(getFirstRule().getLast());
        }


        rules.add(getFirstRule()); //todo get with getter and setter
        generateRules(getFirstRule().getFirst());

        for (Rule r : rules) {
            if (r.representation != 0) {
                // regular
                String s = r.getSymbolString(r, r.isComplement);

                NonTerminal regular = new NonTerminal(r); // todo what about rule count etc?
                regular.assignLeft(new Terminal('!'));
                rulesByStartSymbols.get(s.charAt(0)).add(regular);
                rulesByEndSymbols.get(s.charAt(s.length() - 1)).add(regular);


                NonTerminal isComplement = new NonTerminal(r);
                isComplement.assignLeft(new Terminal('!'));
                isComplement.isComplement = true;
                rulesByEndSymbols.get(Terminal.reverseSymbol(s.charAt(0))).add(isComplement);
                rulesByStartSymbols.get(Terminal.reverseSymbol(s.charAt(s.length() - 1))).add(isComplement);
            }
        }

        System.out.println(rulesByStartSymbols);
        System.out.println(rulesByEndSymbols);

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
                String lastSequence = nextNonTerminal.getRule().getSymbolString(nextNonTerminal.getRule(), nextNonTerminal.isComplement);
                String nextSequence = "";

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
                // if the matching digram is an overlap do nothing
                if (existingDigram.getRight() != symbol) { // todo find a better way to place this
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

    public Boolean search(String searchString) {
        Boolean found = false;
        List<Rule> foundRules = new ArrayList<>();
        List<List<Rule>> allRule = new ArrayList<>();
        for (int i = 0; i < searchString.length() - 1; i++) {
            allRule.add(createSeachDigram(searchString.charAt(i), searchString.charAt(i + 1)));
            System.out.println("LOOKING FOR " + searchString.charAt(i) + " " + searchString.charAt(i + 1));
                for (Rule r : allRule.get(allRule.size() - 1)) {
                    System.out.println(r.getRuleString());
                    System.out.println(r.getSymbolString(r, r.isComplement));
                    System.out.println();
                }
        }



        return found;
    }


    // you want to have multiple instances of the same links, but be able to check to unique digrams
    public List<Rule> createSeachDigram(Character leftSymbol, Character rightSymbol) {
        List<Rule> newRules = new ArrayList<>();

        // joint standard terminals
        //todo reverse complements??
        Terminal left = new Terminal(leftSymbol);
        Terminal right = new Terminal(rightSymbol);
        right.assignLeft(left);
        left.assignRight(right);

        //todo need the exisiting rule check
        if (digramMap.containsKey(right)) {
            Symbol real = digramMap.get(right);
            Rule nRule = new Rule();
            if (real.getLeft().getLeft().isGuard()
                    && real.getRight().isGuard()) {
                Guard g = (Guard) real.getRight();
                Rule subRule = g.getGuardRule();
                NonTerminal nonTerminal = new NonTerminal(subRule);
                nonTerminal.isComplement = !right.equals(real); //true or alternate value, would have to alternate the nonterminal???
                nRule.addNextSymbol(nonTerminal);
                newRules.add(nRule);
            }
            else {
                nRule.addAllSymbols(left);
                newRules.add(nRule);
            }
        }

        // join nonterminals ending in left with terminal next
        for (NonTerminal nt : rulesByEndSymbols.get(leftSymbol)) {
            NonTerminal n = new NonTerminal(nt.getRule());
            n.isComplement = nt.isComplement;
            Terminal ntright = new Terminal(rightSymbol);
            ntright.assignLeft(n);
            n.assignRight(ntright);

            if (digramMap.containsKey(ntright)) {
                Rule nRule = new Rule();
                nRule.addAllSymbols(n);
                newRules.add(nRule);
            }
        }

        //join nonterminal right with standard terminal left
        for (NonTerminal nt : rulesByStartSymbols.get(rightSymbol)) {
            NonTerminal n = new NonTerminal(nt.getRule());
            n.isComplement = nt.isComplement;
            Terminal ntleft = new Terminal(leftSymbol);
            n.assignLeft(ntleft);
            ntleft.assignRight(n);

            if (digramMap.containsKey(n)) {
                Rule nRule = new Rule();
                nRule.addAllSymbols(ntleft);
                newRules.add(nRule);
            }
        }

        for (NonTerminal ntl : rulesByStartSymbols.get(leftSymbol)) {
            for (NonTerminal ntr : rulesByEndSymbols.get(rightSymbol)) {
                NonTerminal nl = new NonTerminal(ntl.getRule());
                nl.isComplement = ntl.isComplement;
                NonTerminal nr = new NonTerminal(ntl.getRule());
                nr.isComplement = ntr.isComplement;
                nr.assignLeft(nl);
                nl.assignRight(nr);
                if (digramMap.containsKey(nr)) {
                    Rule nRule = new Rule();
                    nRule.addAllSymbols(nl);
                    newRules.add(nRule);
                }
            }
        }
        // join two nonterminals
        return newRules;
    }
}

