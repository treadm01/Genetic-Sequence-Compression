package GrammarCoder;

import java.util.*;

public class Decompress {
    private HashMap<Integer, NonTerminal> marker = new HashMap<>();
    private int position = 0;
    private String input;
    private Rule grammar;
    private List<Integer> adjustedMarkers = new ArrayList<>();
    private static Set<Character> POINTER_SET = Set.of('{', '?', '%', '[');
    private static Set<Character> SYMBOL_SET = Set.of('\'', '!');
    private static Set<Character> MARKER_SET = Set.of('#', '^');
    private static int COMMON_RULE_LENGTH = 2;

    public Decompress() {
        grammar = new Rule();
    }

    /**
     * processes the creation of a nonterminal corresponding to a marker symbol
     * seen in implicit symbol stream
     */
    private void decompressRuleMarker() {
        Rule r = new Rule();
        if (input.charAt(position) == '#') { //marker symbol designating nonterminal seen
            r.length = retrieveStringSegment(); // read in the length of rule
        }
        else {r.length = COMMON_RULE_LENGTH;}
        addNonTerminal(r, false); // add nonterminal to rule
        adjustedMarkers.add(marker.size() * COMMON_RULE_LENGTH); // add position of rule created to list which can then be used in place of the rule number itself
        marker.put(marker.size() * COMMON_RULE_LENGTH, (NonTerminal) grammar.getLast()); // add rule to hashmap
    }

    /**
     * processes the creation of a nonterminal corresponding to the second time
     * it is seen, relative to the position it had been inserted in a list
     */
    private void decompressPointer() {
        Boolean isComplement = input.charAt(position) == '{' || input.charAt(position) == '%';
        int pos;

        if (input.charAt(position) == '{' || input.charAt(position) == '?') {
            pos = retrieveStringSegment(); // get nonterminal to retrieve from hashmap
        }
        else {
            pos = 1;
        }

        NonTerminal nonTerminal = marker.get(adjustedMarkers.get(pos)); //get rule corresponding to the index of the marker
        adjustedMarkers.remove(pos); // remove from the list, getting the actual nonterminal as it has the links?
        evaluateRule(nonTerminal); // recursively go through any rules that might be within a rule
        addNonTerminal(nonTerminal.getRule(), isComplement);
    }

    /**
     * process nonterminal from implicit symbol given to it
     */
    private void decompressNonTerminal() {
        Boolean isComplement = input.charAt(position) == '\'';
        int pos = retrieveStringSegment(); // get nonterminal to retrieve from hashmap
        addNonTerminal(marker.get(pos).getRule(), isComplement);
    }

    /**
     * process any edits for a nonterminal given in the implicit stream
     * and apply them back to the nonterminal object
     */
    private void decompressEdit() {
        // get the symbols and then add to the nonterimal edits
        List<Edit> edits = new ArrayList<>();
        int index = retrieveStringSegment();
        Edit e = new Edit(index, String.valueOf(input.charAt(position + 1)));
        edits.add(e);
        // if part of a sub rule need to add to head rule
        grammar.getLast().setIsEdit(edits);
        position++; // have to move past the symbol being edited
    }

    /**
     * assess the implicit symbol string for each unique symbol, nonterminal and terminal
     * to rebuild the grammar and return the corresponding main rule
     * @param ruleString
     * @return
     */
    public Rule buildGrammar(String ruleString) {
        input = ruleString;
        while (position < input.length()) {
            if (MARKER_SET.contains(input.charAt(position))) { // if a marker create rule for it and position it there
                decompressRuleMarker();
            }
            else if (POINTER_SET.contains(input.charAt(position))) { // if a pointer deal with it and its rule
                decompressPointer();
            }
            else if (SYMBOL_SET.contains(input.charAt(position))) { // nonterminal symbol
                decompressNonTerminal();
            }
            else if (input.charAt(position) == '*') { // deal with edits of a nonterminal
                decompressEdit();
            }
            else if (input.charAt(position) == 0) {
                //todo added to begin dealing with sequences with additional symbols beyond 4 letter alphabet
                // where char 0 appears indicates that the next symbol is taken as is and not a unique symbol
                // not fully implemented or working
                position++;
                grammar.addNextSymbol(new Terminal(input.charAt(position)));
            }
            else {
                // else deal with terminal symbol
                if (input.charAt(position) < 128) { // if terminal add it to first rule
                    grammar.addNextSymbol(new Terminal(input.charAt(position)));
                }
            }
            position++; // increase position in string
        }
        return grammar;
    }

    /**
     * loop through the string where a number occurs until no longer a digit
     * retrieve the int value of all those digits
     * @return
     */
    private int retrieveStringSegment() {
        position++;
        return (int) input.charAt(position);
    }

    /**
     * add created nonterminal to the main rule being constructed
     * @param rule
     * @param isComplement
     */
    private void addNonTerminal(Rule rule, Boolean isComplement) {
        NonTerminal nonTerminal = new NonTerminal(rule); // get rule from hashmap
        nonTerminal.setIsComplement(isComplement);
        grammar.addNextSymbol(nonTerminal); // add to main rule
    }

    /**
     * recursively loop through rules and their lengths dealing with each symbol
     * a rule has to be fully processed before moving on, to handle sub-rules
     * for instance #2#3 - both rules and their symbols are iterated over here
     * @param nonTerminal
     */
    private void evaluateRule(NonTerminal nonTerminal) {
        if (!nonTerminal.getRule().compressed) {
            nonTerminal.getRule().compressed = true;
            // for the length of the rule, add it's neighbours (what the rule refers to) to the rule
            for (int i = 0; i < nonTerminal.rule.length; i++) {
                //sometimes a rule is not yet evaluated and this needs to be gone over
                if (nonTerminal.getRight() instanceof NonTerminal) { // if next symbol is an uncompressed rule
                    NonTerminal nt = (NonTerminal) nonTerminal.getRight();
                    if (!nt.getRule().compressed) {
                        evaluateRule(nt);
                        // rule has been evaluated so needs to be marked as so to not check again
                    }
                }
                // move links around as symbols are added to rule
                nonTerminal.assignRight(nonTerminal.getRight().getRight()); // assign next symbol in order
                nonTerminal.getRule().addNextSymbol(nonTerminal.getRight().getLeft());
            }
            // assign new right of terminals left to the nonterminal
            nonTerminal.getRight().assignLeft(nonTerminal);
        }
    }

    /**
     * for debugging, creates the string back from the cfg generated, but does not work for reverse comp
     * @param rule
     * @return
     */
    public String decompress(Rule rule) {
        return rule.getSymbolString(rule, false);
    }
}