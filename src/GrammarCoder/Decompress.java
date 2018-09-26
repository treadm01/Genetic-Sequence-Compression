package GrammarCoder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Decompress {
    private String PATH = System.getProperty("user.dir");
    private String COMPRESSED_PATH = PATH + "/compressedFiles";
    private HashMap<Integer, NonTerminal> marker = new HashMap<>();
    private int position = 0; // for walking through the actual string, todo see if replaceable with for loop
    private String input;
    private Rule grammar;
    private List<Integer> adjustedMarkers = new ArrayList<>();
    private Set<Character> pointerMarker = new HashSet<>();
    private Set<Character> symbolMarker = new HashSet<>();
    private Set<Character> markerMarker = new HashSet<>();
    private static int COMMON_RULE_LENGTH = 2;
    private static int EVEN_RULE_NUMBER = 2;
    private static char EDIT_SYMBOL = '*';
    private static char STANDARD_MARKER = '#';
    private static int UNIQUE_SYMBOL_INDICATOR = 0;
    private static int HIGHEST_CHARACTER_SYMBOL = 128;

    public Decompress() {
        createSymbolSets();
        grammar = new Rule();
    }

    private void decompressRuleMarker() {
        Rule r = new Rule();
        if (input.charAt(position) == STANDARD_MARKER) {
            r.length = retrieveStringSegment(); // read in the length todo rename method, not just nonterminal
        }
        else {r.length = COMMON_RULE_LENGTH;}
        addNonTerminal(r, false); // add nonterminal to rule
        adjustedMarkers.add(marker.size() * EVEN_RULE_NUMBER); // add position of rule created to list which can then be used in place of the rule number iteself
        marker.put(marker.size() * EVEN_RULE_NUMBER, (NonTerminal) grammar.getLast()); // add rule to hashmap
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
        Edit e = new Edit(index, String.valueOf(input.charAt(position + 1)), false);
        edits.add(e);
        // if part of a sub rule need to add to head rule
        grammar.getLast().setIsEdit(edits);
        position++; // have to move past the symbol being edited
    }

    public Rule buildGrammar(String ruleString) {
        input = ruleString;
        while (position < input.length()) {
            if (markerMarker.contains(input.charAt(position))) { // if a marker create rule for it and position it there
                decompressRuleMarker();
            }
            else if (pointerMarker.contains(input.charAt(position))) { // if a pointer deal with it and its rule
                decompressPointer();
            }
            else if (symbolMarker.contains(input.charAt(position))) { // nonterminal symbol
                decompressNonTerminal();
            }
            else if (input.charAt(position) == EDIT_SYMBOL) {
                decompressEdit();
            }
            else if (input.charAt(position) == UNIQUE_SYMBOL_INDICATOR) {
                position++;
                grammar.addNextSymbol(new Terminal(input.charAt(position)));
            }
            else {
                if (input.charAt(position) < HIGHEST_CHARACTER_SYMBOL) { // if terminal add it to first rule
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
    public int retrieveStringSegment() {
        position++;
        return (int) input.charAt(position);
    }

    public void addNonTerminal(Rule rule, Boolean isComplement) {
        NonTerminal nonTerminal = new NonTerminal(rule); // get rule from hashmap
        nonTerminal.isComplement = isComplement;
        grammar.addNextSymbol(nonTerminal); // add to main rule
    }

    /**
     * recursively loop through rules and their lengths
     * @param nonTerminal
     */
    public void evaluateRule(NonTerminal nonTerminal) {
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

    public void writeToFile(String output) {
        try (PrintWriter out = new PrintWriter(COMPRESSED_PATH + "/compressTest.txt")) {
            out.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createSymbolSets() {
        pointerMarker.add('{');
        pointerMarker.add('?');
        pointerMarker.add('%');
        pointerMarker.add('[');
        symbolMarker.add('\'');
        symbolMarker.add('!');
        markerMarker.add('#');
        markerMarker.add('^');
    }

}