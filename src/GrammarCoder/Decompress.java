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

    private void decompressRuleMarker() {
        Rule r = new Rule();
        if (input.charAt(position) == '#') {
            r.length = retrieveStringSegment(); // read in the length
        }
        else {r.length = COMMON_RULE_LENGTH;}
        addNonTerminal(r, false); // add nonterminal to rule
        adjustedMarkers.add(marker.size() * COMMON_RULE_LENGTH); // add position of rule created to list which can then be used in place of the rule number iteself
        marker.put(marker.size() * COMMON_RULE_LENGTH, (NonTerminal) grammar.getLast()); // add rule to hashmap
    }

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

    private void decompressNonTerminal() {
        Boolean isComplement = input.charAt(position) == '\'';
        int pos = retrieveStringSegment(); // get nonterminal to retrieve from hashmap
        addNonTerminal(marker.get(pos).getRule(), isComplement);
    }

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
            else if (input.charAt(position) == '*') {
                decompressEdit();
            }
            else if (input.charAt(position) == 0) {
                position++;
                grammar.addNextSymbol(new Terminal(input.charAt(position)));
            }
            else {
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

    private void addNonTerminal(Rule rule, Boolean isComplement) {
        NonTerminal nonTerminal = new NonTerminal(rule); // get rule from hashmap
        nonTerminal.setIsComplement(isComplement);
        grammar.addNextSymbol(nonTerminal); // add to main rule
    }

    /**
     * recursively loop through rules and their lengths
     * @param nonTerminal
     */
    private void evaluateRule(NonTerminal nonTerminal) {
        if (!nonTerminal.getRule().compressed) {
            nonTerminal.getRule().compressed = true;
            // for the length of the rule add it's neighbours (what the rule refers to) to the rule
            for (int i = 0; i < nonTerminal.rule.length; i++) {
                //sometimes a rule is not yet evaluated and this needs to be gone over
                if (nonTerminal.getRight() instanceof NonTerminal) { // if next symbol is an uncompressed rule
                    NonTerminal nt = (NonTerminal) nonTerminal.getRight();
                    if (!nt.getRule().compressed) {
                        evaluateRule(nt);
                        // rule has been evaluated so needs to be marked as to not check
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