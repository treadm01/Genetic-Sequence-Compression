package GrammarCoder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    public Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol
    private Rule firstRule; // main rule
    Set<Rule> rules;
    int markerNum = 128; // todo set to 0 check if issue later
    List<Integer> adjustedMarkers = new ArrayList<>();
    Set<Character> alphabet = new HashSet();
    String mainInput;
    NonTerminal lastNonTerminal;
    public Map<String, Integer> allSymbols = new HashMap<>();
    List<Rule> orderedRules;

    // go back to numbers rather than symbols - get frequency of each individual symbol again
    //TODO EDIT INDEXES FROM BEGINNING OF RULE, RATHER THAN BEGINNGIN OF STRING, THEN DIFFERENCE BETWEEN?
    //todo how to set lastnonterminal
    //todo would it be better to register edited rules as distinct?
    //todo existing rules, which ones to check to?
    //todo edit digrams, then try existing rules, or nonterminal checks
    // TODO CHECK WHICH PREVIOUS NONTERMINAL TO MATCH TO - JUST THE FIRST THAT MATCHES? might not work if checking whole nonterminals
    // TRY IF NEXT SYMBOL IS A NONTERMINAL START LOOPING THROUGH THE SAME NUMBER OF NEXT SYMBOLS
    // WITH EDITS, A PERCENTAGE OF THE LENGTH.... YEH MIGHT WORK, SUB RULES SHOULD MATCH... WOULD HAVE TO EDIT
    // TO MATCH THE ENTIRE LENGTH
    // TODO BUG 1 either generate rules overflow or missing subrule/guard conversion
    //TODO //order of calls in replacerule... in relation to removing rules used only once, noncomplement
    // todo BUG 2disappearing subrule under wrong match test

    //TODO INDEXES NOW RELATIVE OT THE ENTIRE INPUT, SUBTRACT THE OFFSET SOMEHOW
    // TODO ENSURE DECODING FROM ENCODED STREAM works

    //TODO WHAT HAPPENS WHEN A EDIT RULE IS REMOVED?
    // work with 'second' symbol of a digram
    // try insert or delete, reverser matches, symbols before a rule... length of the nonterminal

    //todo maybe go back to a huffman type code assignment for all individual ints and symbols
    //todo a gammar code for each based on frequency of the symbols, use differences again
    //todo consider trying single complement changes, so when tc is seen tg is stored also

    /**
     * main constructor for compress, just initialises, maps and first rules etc
     */
    public Compress() {
        Rule.ruleNumber = 0;
        digramMap = new HashMap<>();
        rules = new HashSet<>();
        firstRule = new Rule(); // create first rule;
        //lastNonTerminal = new NonTerminal(firstRule);

        alphabet.add('a');
        alphabet.add('c');
        alphabet.add('g');
        alphabet.add('t');
        alphabet.add('#');
        alphabet.add('(');
        alphabet.add(')');
    }

    /**
     * main method loops through the input and adds the latest symbol to the main rule
     * calls check digram on for last two symbols
     * some output calls
     * @param input
     */
    public void processInput(String input) {
        mainInput = input; //todo assign and set properly
        getFirstRule().addNextSymbol(new Terminal(input.charAt(0)));
        // would need to set first symbols index
        for (int i = 1; i < input.length(); i++) {
            //System.out.println(i + " of " + input.length());
            Symbol nextSymbol = new Terminal(input.charAt(i));
            nextSymbol.symbolIndex = i; // keeping index for edits
            nextSymbol = checkApproxRepeat(nextSymbol); // if next lot of symbols is a[pprox match add a nonterminal next
            i = nextSymbol.symbolIndex; // update the index for if there is a nonterminal added including a bunch of symbols

//            rules.add(getFirstRule());
//            generateRules(getFirstRule().getGuard().getRight());
//            System.out.println(printRules());// needed to compute length of rule at the moment

            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(nextSymbol);
////
//            rules.add(getFirstRule());
//            generateRules(getFirstRule().getGuard().getRight());
//            System.out.println(printRules());// needed to compute length of rule at the moment
//

            checkDigram(getFirstRule().getLast());
        }

        rules.add(getFirstRule());
        generateRules(getFirstRule().getGuard().getRight());
//
//        firstRule.count = rules.size()+2; // just to make sure 0 is first...
//        orderedRules = rules.stream() // order rules by use so more common has a lower representation
//                .sorted(Rule::compareTo)
//                .collect(Collectors.toList());

//
////        // TODO shuffle the actual values round?? depends on what will eventually be saved
////        // TODO odd or even can be checked in binary by last digit
////
////        // readding index
//        for (Rule r : orderedRules) {
//            r.index = orderedRules.indexOf(r) * 2;
//        }
//
//        System.out.println(printRules());

//
//        printRules();
////       //TODO make a method just to get length... or get length better
        System.out.println(printRules());// needed to compute length of rule at the moment
        String encoded = encode(getFirstRule().getGuard().getRight(), "");
        System.out.println("ENCODED: " + encoded + "\nLENGTH: " + encoded.length());
        System.out.println("Length of grammar rule: " + getFirstRule().getRuleString().length());
        System.out.println();
//
        System.out.println(allSymbols);
        System.out.println(allSymbols.size());
    }

    public void addToDigramMap(Symbol symbol) {
        digramMap.putIfAbsent(symbol, symbol);
    }

    public Symbol getNextTerminal(Symbol currentSymbol, Boolean isComplement) {
        //todo is guard an ok check, does this really always get the next terminal
        while (!(currentSymbol instanceof Terminal) && !currentSymbol.isGuard()) {
            if (currentSymbol instanceof Terminal) {
                if (isComplement) {
                    currentSymbol = currentSymbol.getLeft();
                }
                else {
                    currentSymbol = currentSymbol.getRight();
                }
            }
            else {
                if (isComplement) {
                    currentSymbol = getNextTerminal(((NonTerminal) currentSymbol).getRule().getLast(),
                            currentSymbol.isComplement);
                } else {
                    currentSymbol = getNextTerminal(((NonTerminal) currentSymbol).getRule().getGuard().getRight(),
                            currentSymbol.isComplement);
                }
            }
        }
        if (currentSymbol instanceof Terminal && isComplement) {
            Symbol complementSymbol = new Terminal(Terminal.reverseSymbol(currentSymbol.toString().charAt(0)));
            //todo ordering of links ok here for complement?
            complementSymbol.assignRight(currentSymbol.getRight());
            complementSymbol.assignLeft(currentSymbol.getLeft());
            currentSymbol = complementSymbol;
        }
        return currentSymbol;
    }

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
                            edits += (symbol.symbolIndex + j) + String.valueOf(nextSequence.charAt(j));
                        }
                    }

                    if (editNumber < lastSequence.length() * 0.1 && edits != "") {
//                        System.out.println("well");
//                        System.out.println(edits);
//                        System.out.println(nextNonterminal);
//                        System.out.println(lastSequence);
//                        System.out.println(nextSequence);
                        //System.out.println(mainInput.substring(symbol.symbolIndex, symbol.symbolIndex + lastSequence.length()));
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
            Symbol nextRight = getNextTerminal(nextLeft.getRight(), false);
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
                newSymbol.setIsEdit(offset + String.valueOf(editSymbol.toString().charAt(0)));
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
    public Symbol getReverseComplement(Symbol digram) {
        Symbol left = new Symbol();
        Symbol right = new Symbol(); // left and right symbols of reverse digram as it will be entered into the map

        if (digram instanceof Terminal) { // right hand side of digram
            left = new Terminal(Terminal.reverseSymbol(digram.toString().charAt(0))); //todo a better way to get char
        }
        else if (digram instanceof NonTerminal) {
            left = new NonTerminal(((NonTerminal) digram).getRule());
            ((NonTerminal) digram).getRule().decrementCount();
        }

        if (digram.getLeft() instanceof Terminal) { // get left hand side of reverse digram
            right = new Terminal(Terminal.reverseSymbol(digram.getLeft().toString().charAt(0))); //todo a better way to get char
        }
        else if (digram.getLeft() instanceof NonTerminal) {
            right = new NonTerminal(((NonTerminal) digram.getLeft()).getRule());
            ((NonTerminal) digram.getLeft()).getRule().decrementCount();
        }

        // if nonterminal is complement, it's complement wont be, same for terminals,
        // shouldn't be an issue as all are unique and values aren't altered elsewhere
        left.isComplement = !digram.isComplement;
        left.complement = digram;
        //digram.complement = left;

        right.isComplement = !digram.getLeft().isComplement;
        right.complement = digram.getLeft();
        //digram.getLeft().complement = right;

        right.assignLeft(left);
        left.assignRight(right);
        left.assignLeft(new Terminal('!')); //todo for comparisons in hashmap, complement requires a left.left

        return right;
    }

    public void removeDigrams(Symbol digram) {
        // the digrams complements left is being reassigned
        //System.out.println("removing " + digram.getLeft() + " " + digram);
        digramMap.remove(digram);
        // creating to remove, if created with the objects could add that way too
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

        // add nonterminals to list, when using exsting rule check each instance for possible repeat
        newRule.nonTerminalList.add(oldTerminal);
        newRule.nonTerminalList.add(newTerminal);

        // if the symbols are not equal then one is a noncomplement and the rule is set for this
        if (!symbol.equals(oldSymbol)) {
            newTerminal.isComplement = true;
        }

//        System.out.println("symbol " + symbol.getLeft() + symbol.getLeft().isEdited);
//        System.out.println("symbol " + symbol + symbol.isEdited);

        //todo needs to be for old terminals too, when relevant
        if (symbol.isEdited) {
            newTerminal.setIsEdit(symbol.edits);
        }
        if (symbol.getLeft().isEdited) {
            newTerminal.setIsEdit(symbol.getLeft().edits);
        }
        if (oldSymbol.isEdited) {
            oldTerminal.setIsEdit(oldSymbol.edits);
        }
        if (oldSymbol.getLeft().isEdited) {
            oldTerminal.setIsEdit(oldSymbol.getLeft().edits);
        }


        replaceDigram(oldTerminal, oldSymbol); // update rule for first instance of digram
        replaceDigram(newTerminal, symbol);// update rule for last instance of digram

        // add the repeating digram to the new rule
        newRule.addSymbols(oldSymbol.getLeft(), oldSymbol); // add symbols to the new rule/terminal

        //check the symbols removed and deal with if they are rules
        // reduce rule count if being replaced or remove if 1
        replaceRule(oldSymbol.getLeft());
        replaceRule(oldSymbol);
        // set the last terminal to check next symbols for approx repeat to
        lastNonTerminal = oldTerminal;
        // done by digram so only really need to check two
        // if only one edit required? if two? could end up changing everything?
        // if you get two symbols after a nonterminal and no direct match, check for edit first
        // rather than taking a digram
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

        //todo is it possible for a value... no, if complement is looked for the noncomplement is returned
        if (!symbol.equals(oldSymbol)) {
            nonTerminal.isComplement = true; //true or alternate value, would have to alternate the nonterminal???
        }


        //todo make sep method - OLD SYMBOL NEED PULLING UP?
        if (symbol.isEdited) {
            nonTerminal.setIsEdit(symbol.edits);
        }
        if (symbol.getLeft().isEdited) {
            nonTerminal.setIsEdit(symbol.getLeft().edits);
        }

        // problem is when you have an existing rule that is a complete rule
        // there is nothing to check it to... nonterminal here is the new nonterminal
        // you need to be able to set from one of the instances...
        //send a list through and rather than one nonterminal, then check all in the list

        // will be difficult to pick a better instance, without knowing what is coming
        // can only be done once on current, todo possible to add edits to previous nonterminals?
        //check lengths of following

        //todo remember each one will be different or potentially an edit (shou;dn't matter)
        // and each one will be different, dont have to match to the same each time
        // if you get the checking ahead working might work ok or be able to use more efficiently

        lastNonTerminal = rule.nonTerminalList.get(1); //unlikely to be different as first would already have been done

        // second might not exist

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
     * prints out the symbols corresponding to the generated rules
     * @return
     */
    public String printRules() {
        String output = "";
        for (Rule r : rules) {
            r.length = 0; //TODO really need to separate getting length from printing
            output += r + " > ";
            Symbol current = r.getGuard().getRight();
            while (!current.isGuard()) {
                if (current instanceof NonTerminal) {
                    output +=  current.toString() + " ";
                }
                else {
                    output += current + " ";
                }
                r.length++; // TODO updating length of rule here.... better place to do it.
                current = current.getRight();
            }
            output += " ";
        }
        return output;
    }



    //TODO clean up
    //TODO decide how to store rules, just numbered as they are seen or try to keep 2,4,6 etc
    public String encode(Symbol symbol, String output) {
        Symbol current = symbol;
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                NonTerminal nt = (NonTerminal) current;
                if (nt.rule.timeSeen == 0) {
                    nt.rule.timeSeen++; // count for number of times rule has been seen
                    nt.rule.position = markerNum; // 'position' really an indicator of the marker assigne to it
                    adjustedMarkers.add(markerNum); // add number for index of list, when removed, corresponds with list
                    output += "#";
                    addSymbols("#");

                    // length is often 2 so only add if not
                    if (nt.getRule().length != 2) {
                        output += nt.getRule().length;
                        addSymbols(String.valueOf(nt.getRule().length));
                    }

                    markerNum++;
                    output = encode(nt.getRule().getGuard().getRight(), output); // if nonterminal need to recurse back
                }
                else if (nt.rule.timeSeen == 1) {
                    //TODO use even odd distinction of rules??
                    nt.rule.timeSeen++;
                    String complementIndicator = "!"; // non complement //todo why two? if not there then noncomplement??
                    if (nt.isComplement) {
                        complementIndicator = "?"; // complement
                    }

                    //todo NEED TO SPLIT UP EDIT SYMBOLS *, INDEX, SYMBOL
                    String isEdit = "";
                    if (nt.isEdited) {
                        isEdit += "*" + nt.edits;
                    }

                    int index = adjustedMarkers.indexOf(nt.rule.position); // get index of current list that is used by both
                    output += complementIndicator + index + isEdit; // the index of the rule position can be used instead but corresponds to the correct value
                    addSymbols(complementIndicator);
                    addSymbols(String.valueOf(index) + isEdit);
                    adjustedMarkers.remove(index);// remove when used
                }
                else {

                    String isEdit = "";
                    if (nt.isEdited) {
                        isEdit += "*" + nt.edits;
                    }

                    String complementIndicator = "!"; // non complement
                    if (nt.isComplement) {
                        complementIndicator = "?"; // complement
                        output += complementIndicator + nt.rule.position + isEdit; //+ rules.size();
                        //this METHOD STORING DIFFERENT SYMBOLS FOR REVERSE COMPLEMENT MIGHT WORK, IF YOU CAN COUNT THE AMOUNT
                        // OF RULES IN DECODER AND SUBTRACT FROM THE COMPLEMENT CHARACTER
                        //output += (char) (nt.rule.position + rules.size());
                        addSymbols(complementIndicator);
                        addSymbols(String.valueOf(nt.rule.position) + isEdit);
                    }
                    else {
                        output += complementIndicator + nt.rule.position + isEdit;
                        addSymbols(complementIndicator);
                        addSymbols(String.valueOf(nt.rule.position) + isEdit);
                    }
                }
            }
            else {
                output += current; // add regular symbols to it
                addSymbols(current.toString());
            }
            current = current.getRight(); // move to next symbol
        }


        //todo implement properly
        try (PrintWriter out = new PrintWriter("/home/tread/IdeaProjects/projectGC/textFiles/compressTest")) {
            out.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }


    public void addSymbols(String c) {
        if (allSymbols.containsKey(c)) {
            Integer count = allSymbols.get(c);
            allSymbols.put(c, count + 1);
        }
        else {
            allSymbols.put(c, 1);
        }
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
                generateRules(rule.getGuard().getRight());
            }
            current = current.getRight();
        }
    }

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

