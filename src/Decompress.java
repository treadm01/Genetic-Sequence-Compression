import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Decompress {
    List<NonTerminal> markers = new ArrayList<>();
    HashMap<Long, NonTerminal> marker = new HashMap<>();

    //TODO just from string, keep a hash of rules to access from there but the pointers refer to
    // todo uncompressed string
    //TODO or else try to rebuild the entire grammar with links

    //build the complete grammar up from the string and return the first rule
    // but need to maintain the string as well to use the indexes...
    // when rules are created add to list, add nonterminals
    // then string of first rule is gained at the end and can be ran through into a rule
    public Rule buildGrammar(String input) {
        Compress c = new Compress();
        int position = 0;
        while (position < input.length()) {
            if (input.charAt(position) == 'M') { // if a marker create rule for it and position it there
                position++; // move again to get length
                Rule r = new Rule();
                r.length = Character.getNumericValue(input.charAt(position));
                NonTerminal nonTerminal = new NonTerminal(r);
                c.getFirstRule().addNextSymbol(nonTerminal);
                markers.add(nonTerminal);
                marker.put(nonTerminal.representation, nonTerminal);
            }
            else if (Character.isAlphabetic(input.charAt(position))) { // if terminal add it
                c.getFirstRule().addNextSymbol(new Terminal(input.charAt(position)));
            }
            else if (input.charAt(position) == '(') { // if a pointer deal with it and rule
                //first number will be index in list - 1
                String index = "";
                position++;
                while (input.charAt(position) != ')') {
                    index += input.charAt(position);
                    position++;
                }

                NonTerminal nonTerminal = markers.get(Integer.parseInt(index)-1);

                // add the symbols to the rule
                Symbol symbol = nonTerminal.getRight();

                for (int z = 0; z < nonTerminal.rule.length; z++) {
                    //TODO specifically needs to be an uncompressed rule....
                    if (nonTerminal.getRight() instanceof NonTerminal) { // if next symbol is an uncompressed rule
                        NonTerminal nt = (NonTerminal) nonTerminal.getRight();
                        if (!nt.getRule().compressed) {
                            for (int x = 0; x < nt.rule.length; x++) { // add symbols for that rule length
                                nonTerminal.assignRight(nonTerminal.getRight().getRight());
                                nonTerminal.getRule().addNextSymbol(nonTerminal.getRight().getLeft());
                            }
                        }
                    }
                    nonTerminal.assignRight(nonTerminal.getRight().getRight()); // assign next symbol in order
                    nonTerminal.getRule().addNextSymbol(nonTerminal.getRight().getLeft());


                }
                //System.out.println(nonTerminal.getRule().getRuleString());
                nonTerminal.getRight().assignLeft(nonTerminal); // assign new right of terminals left to the nonterminal

                nonTerminal.getRule().compressed = true; //tOdo not sure this is in  the right place
                // add the second instance of nonterminal
                NonTerminal nt = new NonTerminal(nonTerminal.getRule());
                c.getFirstRule().addNextSymbol(nt);
            }
            else { // repeated terminal
                char cb = input.charAt(position);
                Long i = Long.valueOf(Character.getNumericValue(cb));
                NonTerminal nonTerminal = new NonTerminal(marker.get(i).getRule());
                c.getFirstRule().addNextSymbol(nonTerminal);
            }
            position++;
        }
        c.rules.add(c.getFirstRule());
        c.generateRules(c.getFirstRule().getGuard().getRight());

//        TODO this slows things down on larger files a great deal when printint
        // printRules();// needed to compute length of rule at the moment
        System.out.println(c.printRules());
        return c.getFirstRule();
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
