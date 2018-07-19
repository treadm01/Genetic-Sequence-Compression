import java.util.HashMap;
import java.util.HashSet;

public class Decompress {
    HashMap<Integer, NonTerminal> marker = new HashMap<>();
    HashSet<Character> stops = new HashSet<>();
    int position = 0; // for walking through the actual string, todo see if replaceable with for loop
    String input;

    //TODO CLEAN UP!!!
    //TODO make sure working for diferent widths, longer that n
    //TODO using compression methods....
    //TODO multiple rule symbols after each other, how to differentiate if just numbers
    //TODO if indicators around nonterminal symbols are used, could remove marker M symbol....would need to specify the length of the length

    //TODO A LOT DEPENDS ON NEXT STEP OF ENCODING
    public Rule buildGrammar(String ruleString) {
        input = ruleString;
        stops.add('(');
        stops.add('[');
        stops.add('#');
        Compress c = new Compress();
        int makerNum = 0; // to keep implicit index of rules made
        while (position < input.length()) {
            if (input.charAt(position) == '#') { // if a marker create rule for it and position it there
                position++; // move again to get length
                Rule r = new Rule();
                String length = "";

                // if no length, means it is two
                if (!Character.isDigit(input.charAt(position))) {
                    length = "2";
                }
                else {
                    length = retrieveStringSegment();
                }
                position--; // TODO have to pull back one as final place will be next check.....

                Integer i = Integer.valueOf(length); // get marker indicator from string
                r.length = i; // rule length known from next symbol
                NonTerminal nonTerminal = new NonTerminal(r);
                c.getFirstRule().addNextSymbol(nonTerminal); // creating rule
                makerNum++; // new rule made
                marker.put(makerNum, nonTerminal); // add rule to hashmap
            }
            else if (Character.isAlphabetic(input.charAt(position))) { // if terminal add it to first rule
                c.getFirstRule().addNextSymbol(new Terminal(input.charAt(position)));
            }
            else if (input.charAt(position) == '(') { // if a pointer deal with it and its rule
                int pos = retrieveNonTerminalSymbol();
                NonTerminal nonTerminal = marker.get(pos);
                evaluateRule(nonTerminal);
                // add the second instance of nonterminal where the pointer was
                NonTerminal nt = new NonTerminal(nonTerminal.getRule());
                c.getFirstRule().addNextSymbol(nt);
            }
            else if (input.charAt(position) == '[') { // if a pointer deal with it and its rule
                // get index from within brackets
                int pos = retrieveNonTerminalSymbol();
                NonTerminal nonTerminal = new NonTerminal(marker.get(pos).getRule()); // get rule from hashmap
                c.getFirstRule().addNextSymbol(nonTerminal); // add to main rule
            }
            position++; // increase position in string
        }
        return c.getFirstRule();
    }

    public int retrieveNonTerminalSymbol() {
        position++;
        String symbol = retrieveStringSegment();
        position--;
        Integer i = Integer.valueOf(symbol); // get marker indicator from string
        return i;
    }

    public String retrieveStringSegment() {
        String symbol = "";
        while (Character.isDigit(input.charAt(position))) {
            symbol += input.charAt(position);
            position++;
            if (position >= input.length()) {
                break;
            }
        }
        return symbol;
    }


    /**
     * recursively loop through rules and their lengths
     * @param nonTerminal
     */
    public void evaluateRule(NonTerminal nonTerminal) {
        if (!nonTerminal.getRule().compressed) {
            nonTerminal.getRule().compressed = true;
            // for the length of the rule add it's neighbours (what the rule refers to) to the rule
            for (int z = 0; z < nonTerminal.rule.length; z++) {
                //sometimes a rule is not yet evaluated and this needs to be gone over
                //TODO specifically needs to be an uncompressed rule....
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
