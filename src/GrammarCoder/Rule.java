package GrammarCoder;

import java.util.*;

public class Rule extends Symbol{
    int count; // number of times this rule is used
    private Guard guard;
    static Integer ruleNumber = 0; // representation for all rules
    Set<NonTerminal> nonTerminalList; // for edits to check all linked nonterminals representing this rule
    //todo "removed" only used in sequences with more than four symbol alphabet
    Rule removed; // an additional rule used when creating rules that will be subsequently be removed
    Boolean compressed = false; // used in decompressing implicit stream to indicate whether more subrules need decoding
    // variables for implicit encoding...
    int timeSeen = 0; // number of times the nonterminal for a rule has been seen
    int position; // relative position for the nonterminal in implicit encoding
    int length; // length of compressed rule

    public Rule() {
        this.representation = ruleNumber; // representation increased by the number of rules already created
        ruleNumber += 2; // number will be even as opposed to symbols for terminals
        guard = new Guard(this);
        assignRight(guard);
        assignLeft(guard);
        guard.assignRight(guard);
        guard.assignLeft(guard);
        nonTerminalList = new LinkedHashSet<>();
    }

    /**
     * adds a symbol to the last symbol of the rule
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        symbol.assignLeft(guard.left); // left of symbol is current last, actualguard.left
        symbol.assignRight(guard); // symbol right should be actual guard
        guard.left.assignRight(symbol); // assign current last right to this symbol
        guard.assignLeft(symbol); // assign last to symbol
    }

    /**
     * add the two symbols from a digram to a nonTerminal
     * @param left
     * @param right
     */
    public void addSymbols(Symbol left, Symbol right) {
        // set the edits to false so subrule is generic
        left.setIsEdited(false);
        right.setIsEdited(false);
//        System.out.println("s " + left.getLeft().getLeft() + left.getLeft() + left + right);
//        Symbol s = left.getLeft();
        this.addNextSymbol(left);
        this.addNextSymbol(right);
//        System.out.println("s " + left.getLeft().getLeft() + left.getLeft() + left + right);
//        System.out.println("well " + s.getLeft() + s);
    }

    /**
     * get string of the rule at nonterminal and terminal level, used in printing rules
     * @return
     */
    public String getRuleString() {
        StringBuilder symbols = new StringBuilder();
        Symbol first = guard.getRight();
        while (!first.isGuard()) {
            symbols.append(" ").append(first.toString());
            first = first.getRight();
        }
        return symbols.toString();
    }

    /**
     * retrieves length of rule for the symbols it represents
     * both terminals and nonterminals
     */
    public int getRuleLength() {
        int ruleLength = 0;
        Symbol current = this.getGuard().getRight();
        while (!current.isGuard()) {
            ruleLength++;
            current = current.getRight();
        }
        return ruleLength;
    }

    /**
     * returns a string of the symbols at the terminal level for a rule
     * sending the main rule as the first argument returns the original
     * input sequence todo should be refactored more
     * @param rule
     * @param complement
     * @return
     */
    public String getSymbolString(Rule rule, Boolean complement) {
        Symbol s;
        StringBuilder output = new StringBuilder();
        s = getNextSymbol(complement, rule.getGuard());
        do {
            if (s instanceof Terminal) {
                output.append(getNextTerminal(complement, s));
                s = getNextSymbol(complement, s);
            }
            else if (s instanceof NonTerminal) {
                int currentLength = output.length(); // length before start of edited rule
                // recursively loop back through nonterminals todo should work in its own method..
                if (complement) {
                    output.append(getSymbolString(((NonTerminal) s).getRule(), !s.getIsComplement()));
                }
                else {
                    output.append(getSymbolString(((NonTerminal) s).getRule(), s.getIsComplement()));
                }

                // apply any edits from the nonterminal to the string
                if (s.getIsEdited()) {
                    for (Edit e : (((NonTerminal)s).editList)) {
                        output.replace(currentLength + e.index, currentLength + e.index + 1, e.symbol);
                    }
                }
                // move to next symbol
                s = getNextSymbol(complement, s);
            }
        } while (!s.isGuard());
        return output.toString();
    }

    /**
     * get the next symbol of a rule depending on whether the current
     * nonterminal representing it is a reverse complement or not
     * @param complement
     * @param symbol
     * @return
     */
    private Symbol getNextSymbol(Boolean complement, Symbol symbol) {
        Symbol nextSymbol;
        if (complement) {
            nextSymbol = symbol.getLeft();
        }
        else {
            nextSymbol = symbol.getRight();
        }
        return nextSymbol;
    }

    /**
     * get the next terminal string output depending on whether
     * reverse complement or not
     * @param complement
     * @param s
     * @return
     */
    private String getNextTerminal(Boolean complement, Symbol s) {
        StringBuilder output = new StringBuilder();
        if (complement) {
            output.append(Terminal.reverseSymbol(s.toString().charAt(0)));
        }
        else {
            output.append(s.toString());
        }
        return output.toString();
    }


    /**
     * method to duplicate the symbols of a rule to be added to a new rule
     * used in searching, needs improving
     * @param left
     */
    public void addAllSymbols(Symbol left) {
        if (left == null) {
            left = left.getRight();
        }
        while (left != null && !left.isGuard()) {
            if (left instanceof Terminal) {
                Terminal t = new Terminal(left.toString().charAt(0));
                addNextSymbol(t);
                left = left.getRight();
            }
            else if (left instanceof NonTerminal) {
                NonTerminal nt = new NonTerminal(((NonTerminal) left).getRule());
                nt.setIsComplement(left.getIsComplement());
                addNextSymbol(nt);
                left = left.getRight();
            }
        }
    }

    /**
     * return the last element
     * @return
     */
    public Symbol getLast() {
        return guard.left;
    }

    public Symbol getFirst() {return getGuard().getRight();}

    public Guard getGuard() { return guard; }

    public int getCount() { return count; }

    void incrementCount() { count++;}

    void decrementCount() { count--;}
}