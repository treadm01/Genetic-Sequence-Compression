package GrammarCoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Decompress {
    HashMap<Integer, NonTerminal> marker = new HashMap<>();
    int position = 0; // for walking through the actual string, todo see if replaceable with for loop
    String input;
    Compress c;
    List<Integer> adjustedMarkers = new ArrayList<>();
    int previousMarker = 0;

    //TODO CLEAN UP!!!
    //TODO A LOT DEPENDS ON NEXT STEP OF ENCODING
    //TODO remember most rules are two....
    public Rule buildGrammar(String ruleString) {
        input = ruleString; //todo use by getter and setter
        c = new Compress();// todo split out the methods used by both
        while (position < input.length()) {
            if (input.charAt(position) == '#') { // if a marker create rule for it and position it there
                int length = 2; // most often the length will be 2
                if (input.charAt(position + 1) < 58 && input.charAt(position + 1) > 47) { // if the next position is a length not 2
                    length = retrieveStringSegment(); // read in the length todo rename method, not just nonterminal
                }
                Rule r = new Rule();
                r.length = length; // rule length known from next symbol
                addNonTerminal(r, false); // add nonterminal to rule
                adjustedMarkers.add(marker.size()); // add position of rule created to list which can then be used in place of the rule number iteself
                marker.put(marker.size(), (NonTerminal) c.getFirstRule().getLast()); // add rule to hashmap
            }
            else if (input.charAt(position) == '!' || input.charAt(position) == '?') { // if a pointer deal with it and its rule
                Boolean isComplement = false;
                if (input.charAt(position) == '?' ){
                    isComplement = true;
                }
                // if next symbol is a number, its a pointer so deal with that...
                if (input.charAt(position + 1) < 58 && input.charAt(position + 1) > 47) {
                    int pos = retrieveStringSegment(); // get nonterminal to retrieve from hashmap
                    //todo would think after this section would check for * and add edit string to nonterminal
                    //todo maybe, plus below if necessary (will third time seen ever be edit?). need to go through again
                   // System.out.println(position);
                    NonTerminal nonTerminal = marker.get(adjustedMarkers.get(pos)); //get rule corresponding to the index of the marker
                    adjustedMarkers.remove(pos); // remove from the list, getting the actual nonterminal as it has the links?
                    evaluateRule(nonTerminal); // recursively go through any rules that might be within a rule
                    addNonTerminal(nonTerminal.getRule(), isComplement);
                }
                else { // if it isn't then its a reverse complement rule
                    position++; //move to get the symbol
                    int pos = input.charAt(position) - 128; //added as seems so have to remove 128 initial marker number
                    addNonTerminal(marker.get(pos).getRule(), isComplement);
                }
            }
            else {
                if (input.charAt(position) < 128) { // if terminal add it to first rule
                    c.getFirstRule().addNextSymbol(new Terminal(input.charAt(position)));
                }
                else { //if a rule that is not a reverse complement
                    int pos = input.charAt(position) - 128;//retrieveStringSegment();
                    addNonTerminal(marker.get(pos).getRule(), false);
                }
            }
            position++; // increase position in string
        }
        return c.getFirstRule();
    }

    /**
     * loop through the string where a number occurs until no longer a digit
     * retrieve the int value of all those digits
     * @return
     */
    public int retrieveStringSegment() {
        String symbol = "";
        //System.out.println(input.charAt(position+1));
        while (input.charAt(position + 1) < 58 && input.charAt(position + 1) > 47) {
            symbol += input.charAt(position + 1);
            position++;
            if (position + 1 >= input.length()) { //todo if it reaches the end of the string then break
                //todo try to remove need to do this
                break;
            }
        }
        return Integer.valueOf(symbol);
    }

    //todo or here to add if an edit...
    public void addNonTerminal(Rule rule, Boolean isComplement) {
        NonTerminal nonTerminal = new NonTerminal(rule); // get rule from hashmap
        nonTerminal.isComplement = isComplement;
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
     * for debugging, creates the string back from the cfg generated,
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
