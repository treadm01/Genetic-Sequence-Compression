package GrammarCoder;

import java.util.*;

public class Compress {
    private final static int USED_ONCE = 1; // rule used once
    private DigramMap digramMap;
    private Rule firstRule; // main rule
    public Set<Rule> rules; // rules used for output and encoding
    private String mainInput; // string of the input, used for edit rule indexes
    private int streamIndex = 0;
    public int numberOfEdits = 0;

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
         //   System.out.println(nextSymbol);
          //  System.out.println("before " + i);
            for (Symbol s : checkApproxRepeat(nextSymbol)) {
                i = s.symbolIndex; // update the index for if there is a nonterminal added including a bunch of symbols
                getFirstRule().addNextSymbol(s);
                checkDigram(getFirstRule().getLast());
            //    System.out.println(getFirstRule().getRuleString());
            //    System.out.println("during " + i);
             //   System.out.println(s);
            }

           // System.out.println(getFirstRule().getLast());
           // System.out.println("after " + i);

            //nextSymbol = checkApproxRepeat(nextSymbol); // if next lot of symbols is approx match add a nonterminal next
        }
        // seperate method
        rules.add(getFirstRule()); //todo get with getter and setter
        generateRules(getFirstRule().getFirst());
        deubugGrammarOutput();
    }

    public void deubugGrammarOutput() {
        // debugging output
        System.out.println(printRules());
        System.out.println("Length of grammar rule: " + getFirstRule().getRuleString().length());
        System.out.println("Rule number: " + rules.size());
        System.out.println("Number of edits " + numberOfEdits);
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
    public List<Symbol> checkApproxRepeat(Symbol symbol) {
        // will either be the same symbol sent, an edited one (maybe) or one at the end of a nonterminal sequence
        Symbol nextSymbol = symbol;
        NonTerminal matchingNonTerminal;
        int newIndex = symbol.symbolIndex; // to jump over the seen symbols
        // at this point the next potential terminal will not have been added
        int bestEdit = 0;

        List<Symbol> symbols = new ArrayList<>();
        symbols.add(nextSymbol);

        // get the last nonterminal, if it wasn't one then not checking
        if (getFirstRule().getLast() instanceof NonTerminal) {
            matchingNonTerminal = (NonTerminal) getFirstRule().getLast();
            int matchingNonTerminalLength = matchingNonTerminal.getRule().getSymbolString(matchingNonTerminal.getRule(), false).length();
            for (NonTerminal nonTerminalSet : matchingNonTerminal.getRule().nonTerminalList) {
                if (matchingNonTerminal.isComplement == nonTerminalSet.isComplement) {
                    //todo neexs to not check itself, nonterminals lost when links change?
                    // cache of strings?
                    List<Symbol> next = new ArrayList<>();
                    Symbol nt = nonTerminalSet.getRight();
                    String lastSequence = "";

                    //try string only terminal method??? benefits might be quicker?
//                System.out.println("right of match is " + nt);
//                System.out.println("match is " + nt.getLeft());
//                System.out.println("left of match is " + nt.getLeft().getLeft());
                    //todo need a way to check benefit here and roll back if one too many
                    while (!nt.isGuard() && nt != matchingNonTerminal
                            && symbol.symbolIndex + lastSequence.length() <= mainInput.length() - 1
                            && lastSequence.length() < 30) {
                        next.add(nt);
                        if (nt instanceof NonTerminal) {
                            lastSequence += ((NonTerminal) nt).getRule().getSymbolString(((NonTerminal) nt).getRule(), nt.isComplement);
                        } else if (nt instanceof Terminal) {
                            lastSequence += nt;
                        }
                        nt = nt.getRight();
                    }

                    String nextSequence;
                    if (symbol.symbolIndex + lastSequence.length() <= mainInput.length()) {
                        nextSequence = mainInput.substring(symbol.symbolIndex, symbol.symbolIndex + lastSequence.length());
                        int editNumber = 0;
                        for (int j = 0; j < lastSequence.length(); j++) {
                            if (lastSequence.charAt(j) != nextSequence.charAt(j)) {
                                editNumber++;
                            }
                        }

                        int combinedLength = (lastSequence.length() + matchingNonTerminalLength);

                        if (editNumber > 0 && editNumber <= combinedLength * 0.1 && next.size() >= 3) {
                            //if (next.size() > 2) {
                            if (combinedLength - editNumber > bestEdit) {
//                                System.out.println(lastSequence);
//                                System.out.println(nextSequence);
                                symbols.clear();
                                bestEdit = combinedLength - editNumber;
                                int indexInString = 0;
                                for (Symbol s : next) {
                              //      System.out.println("matching symbol " + s);
                                    //todo should be an edit on t, but as subrules need to go through them
                                    if (s instanceof NonTerminal) {
                                        NonTerminal nonTerminalClone = new NonTerminal(((NonTerminal) s).getRule()); //todo symbol sindex?
                                        String nonterminalString = ((NonTerminal) s).getRule().getSymbolString(((NonTerminal) s).getRule(), s.isComplement);
                                        List<Edit> edits = new ArrayList<>();
                                //        System.out.println(nonterminalString);
                                        for (int x = 0; x < nonterminalString.length(); x++) {
                                //            System.out.println("well");
                                            int pos = indexInString + x;
                                            if (lastSequence.charAt(pos) != nextSequence.charAt(pos)) {
                                                Boolean isComplement = lastSequence.charAt(pos) == Terminal.reverseSymbol(nextSequence.charAt(pos));
                                                edits.add(new Edit(symbol.symbolIndex + pos, String.valueOf(nextSequence.charAt(pos)), isComplement));
                                       //         System.out.println("edit nonterminal");
                                            }
                                        }
                                        nonTerminalClone.setIsEdit(edits);
                                        nonTerminalClone.isComplement = s.isComplement;
                                        symbols.add(nonTerminalClone);
                                        indexInString += nonterminalString.length();
                                        //System.out.println(indexInString);
                                        nonTerminalClone.symbolIndex = symbol.symbolIndex + indexInString - 1;
                                    } else if (s instanceof Terminal) {
                                        List<Edit> edits = new ArrayList<>();
                                        Symbol terminal = new Terminal(lastSequence.charAt(indexInString));
                                        if (lastSequence.charAt(indexInString) != nextSequence.charAt(indexInString)) {
                                            Boolean isComplement = lastSequence.charAt(indexInString) == Terminal.reverseSymbol(nextSequence.charAt(indexInString));
                                            edits.add(new Edit(symbol.symbolIndex + indexInString, String.valueOf(nextSequence.charAt(indexInString)), isComplement));
                                            terminal.setIsEdit(edits);
                                         //   System.out.println("edit terminal");
                                        }
                                        terminal.symbolIndex = symbol.symbolIndex + indexInString;
                                        indexInString++;

                                        //  System.out.println("INDEX " + terminal.symbolIndex);
                                        symbols.add(terminal);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // might have to be done to terminal level just so any changes to digram map are maintained
        // i mean theoretically they should match, you add the digrams and it creates new rule
        // then as new ones are added the new rules should be matching anyway
        // might be better terminal level to match indexes, although more likely to mess up digrams

        if (!symbols.get(0).equals(symbol)) {
            System.out.println(symbols);
        }
        return symbols;
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

        oldTerminal.symbolIndex = oldSymbol.getLeft().symbolIndex;
        newTerminal.symbolIndex = symbol.getLeft().symbolIndex;

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

        //System.out.println("checking " + nonTerminal);
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
                        numberOfEdits++;
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

