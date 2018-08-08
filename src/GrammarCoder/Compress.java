package GrammarCoder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    public Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol
    private Rule firstRule; // main rule
    Set<Rule> rules;
    int markerNum = 128; // todo set to 0 check if issue later
    List<Integer> adjustedMarkers = new ArrayList<>();
    Set<Character> alphabet = new HashSet();
    String mainInput;

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
            // add next symbol from input to the first rule
            getFirstRule().addNextSymbol(new Terminal(input.charAt(i)));
            checkDigram(getFirstRule().getLast());
            //System.out.println(getFirstRule().getRuleString());
//            System.out.println(printDigrams());
        }
        rules.add(getFirstRule());
        generateRules(getFirstRule().getGuard().getRight());

//       //TODO make a method just to get length... or get length better
        printRules();// needed to compute length of rule at the moment
//        System.out.println(printRules());
//        String encoded = encode(getFirstRule().getGuard().getRight(), "");
//        System.out.println("ENCODED: " + encoded + "\nLENGTH: " + encoded.length());
//        System.out.println("Length of grammar rule: " + getFirstRule().getRuleString().length());
//        System.out.println();

        checkApproximateRepeat();
    }

    public void addToDigramMap(Symbol symbol) {
        digramMap.putIfAbsent(symbol, symbol);
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
            Symbol existingDigram = getOriginalDigram(symbol); // retrieves existing digram, if complement returns original
            // if the matching digram is an overlap do nothing
            if (existingDigram.getRight() != symbol) { // todo find a better way to place this
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

        // if the symbols are not equal then one is a noncomplement and the rule is set for this
        if (!symbol.equals(oldSymbol)) {
            newTerminal.isComplement = true;
        }

        replaceDigram(oldTerminal, oldSymbol); // update rule for first instance of digram
        replaceDigram(newTerminal, symbol);// update rule for last instance of digram

        // add the repeating digram to the new rule
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
        Guard g = (Guard) oldSymbol.getRight(); // have to get guard and then rule from there
        Rule rule = g.getGuardRule(); // get rule using pointer to it in the guard
        NonTerminal nonTerminal = new NonTerminal(rule);

        //todo is it possible for a value... no, if complement is looked for the noncomplement is returned
        if (!symbol.equals(oldSymbol)) {
            nonTerminal.isComplement = true; //true or alternate value, would have to alternate the nonterminal???
        }

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
            output += "\n";
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

                    // length is often 2 so only add if not
                    if (nt.getRule().length != 2) {
                        output += nt.getRule().length;
                    }
                    markerNum++;
                    output = encode(nt.getRule().getGuard().getRight(), output); // if nonterminal need to recurse back
                }
                else if (nt.rule.timeSeen == 1) {
                    //TODO use even odd distinction of rules??
                    nt.rule.timeSeen++;
                    String complementIndicator = "!"; // non complement
                    if (nt.isComplement) {
                        complementIndicator = "?"; // complement
                    }
                    int index = adjustedMarkers.indexOf(nt.rule.position); // get index of current list that is used by both
                    output += complementIndicator + index; // the index of the rule position can be used instead but corresponds to the correct value
                    adjustedMarkers.remove(index);// remove when used
                }
                else {
                    String complementIndicator; // non complement
                    if (nt.isComplement) {
                        complementIndicator = "?"; // complement
                        output += complementIndicator + (char) nt.rule.position; //+ rules.size();
                        //this METHOD STORING DIFFERENT SYMBOLS FOR REVERSE COMPLEMENT MIGHT WORK, IF YOU CAN COUNT THE AMOUNT
                        // OF RULES IN DECODER AND SUBTRACT FROM THE COMPLEMENT CHARACTER
                        //output += (char) (nt.rule.position + rules.size());
                    }
                    else {
                        output += (char) nt.rule.position;
                    }
                }
            }
            else {
                output += current; // add regular symbols to it
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

    public List<Rule> orderRulesByLength() {
        // initialise length of symbol string the rule represents to reprder
        // the rules by their lengths
        for (Rule r : rules) {
            r.symbolRule = r.getSymbolString(r, false);
        }

        // reorder the rules by their symbol length
        List<Rule> orderedRules = rules.stream()
                .sorted(Rule::compareTo)
                //.filter(x -> x.symbolRule.length() > 5) // todo this needs to be as low as possible
                .collect(Collectors.toList());
        return orderedRules;
    }

    public Map<Long, List<NonTerminal>> getNonTerminals() {
        // build map of nonterminals that occur in the mainrule
        Map<Long, List<NonTerminal>> nonterminalMap = new HashMap<>();
        Symbol s = getFirstRule().getGuard().getRight();
        while (!s.isGuard()) {
            if (nonterminalMap.containsKey(s.getRepresentation())) {
                nonterminalMap.get(s.getRepresentation()).add((NonTerminal) s);
            }
            else if (s instanceof NonTerminal){
                List<NonTerminal> nonTerminalList = new ArrayList<>();
                nonTerminalList.add((NonTerminal) s);
                nonterminalMap.put(s.getRepresentation(), nonTerminalList);
            }
            s = s.getRight();
        }
        return nonterminalMap;
    }


    //TODO JUST DO RULES AT THE TOP RULE
    //todo get the following string until too many edits etc,
    //todo keep track of necessary edits
    //todo make the edits on the grammar and ensure encoded,
    //todo ensure the nonterminal indicates it requires an edit op
    // which will be where?? would have to be at top level
    // top level where two repeats differ... how to get there?
    // how to deal with the symbols replacing
    // i think uncondense what is necessary then edit
    // might uncondense first or second, but only edit second
    // so 10 12 > 10 gc edit, check digrams

    //TODO not just substitute but insert delete
    //todo check by whole nonterminal to save time
    //todo ensure each nonterminal is checked, seems like 1582 in humprt should have two pair checks
    //nope, there is another match with very similar following symbol, but it is in a subrule, or encoded some other way
    public void checkApproximateRepeat() {
        List<Rule> orderedRules = orderRulesByLength(); // todo necessary or just store those that are best and try them all
        Map<Long, List<NonTerminal>> nonterminalMap = getNonTerminals();

        for (Rule r : orderedRules) {
            // not every rule in the map
            if (nonterminalMap.containsKey(r.representation)) {
                List<NonTerminal> nonTerminalList = nonterminalMap.get(r.representation);
                for (int i = 0; i < nonTerminalList.size(); i++) {
                    for (int j = i + 1; j < nonTerminalList.size(); j++) {
                        //todo return bool? indicator of what to do?
                        nonTerminalRepeats(nonTerminalList.get(i), nonTerminalList.get(j));
                    }
                }
            }
        }
    }


    public void nonTerminalRepeats(NonTerminal first, NonTerminal second) {
        String firstSubString = "";
        String secondSubString = "";
        int editNumber = 0;
        int index = 0;
        Symbol firstNext = first.getRight();
        Symbol secondNext = second.getRight();

        while (editNumber < 20
                && !firstNext.equals(second) // ensure not overlapping second
                && !secondNext.isGuard()
                && !(firstNext.equals(second) && secondNext.isGuard())) {

            // if not overlapping string being checked, get string and move right
            firstSubString += getNextSubString(firstNext);
            firstNext = firstNext.getRight();

            // if second string has not reached the end, do the same for it
            secondSubString += getNextSubString(secondNext);
            secondNext = secondNext.getRight();

            while (index < firstSubString.length() && index < secondSubString.length()) {
                if (firstSubString.charAt(index) != secondSubString.charAt(index)) {
                    editNumber++;
                }
                index++;
            }
        }

        if (firstSubString.length() > 4 && editNumber < firstSubString.length() * 0.3) {
            System.out.println(first + " = " + first.getRule().getSymbolString(first.getRule(), first.isComplement));
            System.out.println("f " + firstSubString);
            System.out.println("s " + secondSubString);
            System.out.println();
        }
    }

    public String getNextSubString(Symbol currentSymbol) {
        String subString = "";
        if (currentSymbol instanceof Terminal) {
            subString = currentSymbol.toString();
        }
        else {
            Rule r = ((NonTerminal) currentSymbol).getRule();
            subString = r.getSymbolString(r, currentSymbol.isComplement);
           // System.out.println("rule is " + r);
            //System.out.println("string is " + subString);
        }
        return subString;
    }
}

