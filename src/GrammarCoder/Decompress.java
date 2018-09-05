package GrammarCoder;

import java.util.*;

public class Decompress {
    HashMap<Integer, NonTerminal> marker = new HashMap<>();
    int position = 0; // for walking through the actual string, todo see if replaceable with for loop
    String input;
    Compress c;
    List<Integer> adjustedMarkers = new ArrayList<>();
    Set<Character> pointerMarker = new HashSet<>();
    Set<Character> symbolMarker = new HashSet<>();

    //todo if this works can remove the 2 marker thing, adaptive doesn't need to be as explicit
    //todo rather than times 2 could start at 128, would still need * 2, but wouldn't have to sutract 128
    public Rule buildGrammar(String ruleString) {
        pointerMarker.add('{');
        pointerMarker.add('?');
        symbolMarker.add('\'');
        symbolMarker.add('!');
        input = ruleString; //todo use by getter and setter
        c = new Compress();// todo split out the methods used by both
        while (position < input.length()) {
            if (input.charAt(position) == '#') { // if a marker create rule for it and position it there
                Rule r = new Rule();
                r.length = retrieveStringSegment(); // read in the length todo rename method, not just nonterminal
                addNonTerminal(r, false, false); // add nonterminal to rule
                adjustedMarkers.add(marker.size() * 2); // add position of rule created to list which can then be used in place of the rule number iteself
                marker.put(marker.size() * 2, (NonTerminal) c.getFirstRule().getLast()); // add rule to hashmap
            }
            else if (pointerMarker.contains(input.charAt(position))) { // if a pointer deal with it and its rule
                Boolean isComplement = false;
                Boolean isReverse = false;

                if (input.charAt(position) == '{') {

                    isComplement = true;
                    isReverse = true;
                }
                else if (input.charAt(position) == '}') {

                    isReverse = true;
                }
                else if (input.charAt(position) == '~') {

                    isComplement = true;
                }
                // if next symbol is a number, its a pointer so deal with that...
                int pos = retrieveStringSegment(); // get nonterminal to retrieve from hashmap
                NonTerminal nonTerminal = marker.get(adjustedMarkers.get(pos)); //get rule corresponding to the index of the marker
                adjustedMarkers.remove(pos); // remove from the list, getting the actual nonterminal as it has the links?
                evaluateRule(nonTerminal); // recursively go through any rules that might be within a rule
                addNonTerminal(nonTerminal.getRule(), isComplement, isReverse);
            }
            else if (symbolMarker.contains(input.charAt(position))) { // nonterminal symbol

                Boolean isComplement = false;
                Boolean isReverse = false;
                // if not even then reverse complement, and referring to the rule below
                if (input.charAt(position) == '\'') {

                    isComplement = true;
                    isReverse = true;
                }
                else if (input.charAt(position) == '^') {

                    isReverse = true;
                }
                else if (input.charAt(position) == '`') {

                    isComplement = true;
                }
                int pos = retrieveStringSegment(); // get nonterminal to retrieve from hashmap
                //pos -= 128;
                addNonTerminal(marker.get(pos).getRule(), isComplement, isReverse);
            }
//            else if (input.charAt(position) == '*') {
//                //todo needs to handle markernumber edits... no, should already be doing that
//                // get the symbols and then add to the nonterimal edits
//                List<Edit> edits = new ArrayList<>();
//                int index = retrieveStringSegment();
//                Edit e = new Edit(index, String.valueOf(input.charAt(position + 1)));
//                edits.add(e);
//                // if part of a sub rule need to add to head rule
//                c.getFirstRule().getLast().setIsEdit(edits);
//                position++; // have to move past the symbol being edited
//            }
            else {
                if (input.charAt(position) < 128) { // if terminal add it to first rule
                    c.getFirstRule().addNextSymbol(new Terminal(input.charAt(position)));
                }
            }
            position++; // increase position in string
        }
        System.out.println(c.getFirstRule().getSymbolString(c.getFirstRule(), false));
        return c.getFirstRule(); // todo can't me using compress.....
    }

    /**
     * loop through the string where a number occurs until no longer a digit
     * retrieve the int value of all those digits
     * @return
     */
    public int retrieveStringSegment() {
        String symbol = "";
        position++;
        return (int) input.charAt(position);
//        if (input.charAt(position + 1) > 128) {
//            return (int) input.charAt(position + 1);
//        }
//        while (input.charAt(position + 1) < 58 && input.charAt(position + 1) > 47) {
//            symbol += input.charAt(position + 1);
//            position++;
//            if (position + 1 >= input.length()) { //todo if it reaches the end of the string then break
//                //todo try to remove need to do this
//                break;
//            }
//        }
//        return Integer.valueOf(symbol);
    }

    //todo or here to add if an edit...
    public void addNonTerminal(Rule rule, Boolean isComplement, Boolean isReverse) {
        NonTerminal nonTerminal = new NonTerminal(rule); // get rule from hashmap
        nonTerminal.isComplement = isComplement && isReverse;
        c.getFirstRule().addNextSymbol(nonTerminal); // add to main rule
    }

    /**
     * recursively loop through rules and their lengths
     * @param nonTerminal
     */
    //TODO clean up
    public void evaluateRule(NonTerminal nonTerminal) {
        if (!nonTerminal.getRule().compressed) {
            nonTerminal.getRule().compressed = true;
            // for the length of the rule add it's neighbours (what the rule refers to) to the rule
            for (int z = 0; z < nonTerminal.rule.length; z++) {
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
                //System.out.println("GrammarCoder.Rule " + nonTerminal + " > " + nonTerminal.getRule().getRuleString());
            }
            // assign new right of terminals left to the nonterminal
            nonTerminal.getRight().assignLeft(nonTerminal);
        }
    }


    //TODO NOT USED!!! RULE HAS ITS OWN DECOMPRESS... NEEDS LOOKING INTO
    /**
     * for debugging, creates the string back from the cfg generated, but does not work for reverse comp
     * @param rule
     * @return
     */
    public String decompress(Rule rule) {
        Symbol s = rule.getGuard().getRight();
        String output = "";
        do {
            if (s instanceof Terminal) {
                output += s.toString();
                s = s.getRight();
            }
            else {
                output += decompress(((NonTerminal) s).getRule());
                s = s.getRight();
            }

        } while (!s.isGuard());
        return output;
    }

}