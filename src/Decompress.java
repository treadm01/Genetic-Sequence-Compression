import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Decompress {
    HashMap<Integer, NonTerminal> marker = new HashMap<>();
    int position = 0; // for walking through the actual string, todo see if replaceable with for loop
    String input;
    Compress c;
    List<Integer> adjustedMarkers = new ArrayList<>();

    //TODO CLEAN UP!!!
    //TODO A LOT DEPENDS ON NEXT STEP OF ENCODING
    public Rule buildGrammar(String ruleString) {
        input = ruleString; //todo use by getter and setter
        c = new Compress();// todo split out the methods used by both
        while (position < input.length()) {
            //TODO seems to be adding extra item to list, so might not be iterating properly
            if (input.charAt(position) == '#') { // if a marker create rule for it and position it there
                int length = 2; // most often the length will be 2
                if (Character.isDigit(input.charAt(position + 1))) { // if the next position is a length not 2
                    length = retrieveStringSegment(); // read in the length todo rename method, not just nonterminal
                }
                Rule r = new Rule();
                r.length = length; // rule length known from next symbol
                addNonTerminal(r); // add nonterminal to rule
                adjustedMarkers.add(marker.size());
                marker.put(marker.size(), (NonTerminal) c.getFirstRule().getLast()); // add rule to hashmap
            }
            else if (Character.isAlphabetic(input.charAt(position))) { // if terminal add it to first rule
                c.getFirstRule().addNextSymbol(new Terminal(input.charAt(position)));
            }
            else if (input.charAt(position) == '(') { // if a pointer deal with it and its rule
                int pos = retrieveStringSegment(); // get nonterminal to retrieve from hashmap
                NonTerminal nonTerminal = marker.get(adjustedMarkers.get(pos));
                adjustedMarkers.remove(pos); //TODO what about evaluate rules..
                evaluateRule(nonTerminal); // recursively go through any rules that might be within a rule
                addNonTerminal(nonTerminal.getRule());
            }
            else if (input.charAt(position) == '[') { // if a pointer deal with it and its rule
                int pos = retrieveStringSegment();
                addNonTerminal(marker.get(pos).getRule()); // starting from 0 todo chekc if an issue, was 1
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
        while (Character.isDigit(input.charAt(position + 1))) {
            symbol += input.charAt(position + 1);
            position++;
            if (position + 1 >= input.length()) { //todo if it reaches the end of the string then break
                //todo try to remove need to do this
                break;
            }
        }
        return Integer.valueOf(symbol);
    }

    public void addNonTerminal(Rule rule) {
        NonTerminal nonTerminal = new NonTerminal(rule); // get rule from hashmap
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
                //System.out.println("Rule " + nonTerminal + " > " + nonTerminal.getRule().getRuleString());
            }
            // assign new right of terminals left to the nonterminal
            nonTerminal.getRight().assignLeft(nonTerminal);
        }
    }


    /**
     * for debugging, creates the string back from the cfg generated
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
