import java.util.HashMap;

public class Decompress {
    HashMap<Integer, NonTerminal> marker = new HashMap<>();

    //TODO clean up
    //TODO make sure working for diferent widths, longer that n
    public Rule buildGrammar(String input) {
        Compress c = new Compress();
        int position = 0; // for walking through the actual string, todo see if replaceable with for loop
        int makerNum = 0; // to keep implicit index of rules made
        while (position < input.length()) {
            if (input.charAt(position) == 'M') { // if a marker create rule for it and position it there
                position++; // move again to get length
                Rule r = new Rule();
                r.length = Character.getNumericValue(input.charAt(position)); // rule length known from next symbol
                NonTerminal nonTerminal = new NonTerminal(r);
                c.getFirstRule().addNextSymbol(nonTerminal); // creating rule
                makerNum++; // new rule made
                marker.put(makerNum, nonTerminal); // add rule to hashmap
            }
            else if (Character.isAlphabetic(input.charAt(position))) { // if terminal add it to first rule
                c.getFirstRule().addNextSymbol(new Terminal(input.charAt(position)));
            }
            else if (input.charAt(position) == '(') { // if a pointer deal with it and its rule
                // get index from within brackets
                String index = "";
                position++;
                while (input.charAt(position) != ')') {
                    index += input.charAt(position);
                    position++;
                }

                // convert index to int to get corresponding rule from nonterminal
                Integer i = Integer.valueOf(index);
                NonTerminal nonTerminal = marker.get(i);

                // for the length of the rule add it's neighbours (what the rule refers to) to the rule
                for (int z = 0; z < nonTerminal.rule.length; z++) {
                    //sometimes a rule is not yet evaluated and this needs to be gone over
                    //TODO specifically needs to be an uncompressed rule....
                    //TODO this will have to be done recursively...
                    if (nonTerminal.getRight() instanceof NonTerminal) { // if next symbol is an uncompressed rule
                        NonTerminal nt = (NonTerminal) nonTerminal.getRight();
                        if (!nt.getRule().compressed) {
                            for (int x = 0; x < nt.rule.length; x++) { // add symbols for that rule length
                                nonTerminal.assignRight(nonTerminal.getRight().getRight());
                                nonTerminal.getRule().addNextSymbol(nonTerminal.getRight().getLeft());
                            }
                        }
                    }
                    // move links around as symbols are added to rule
                    nonTerminal.assignRight(nonTerminal.getRight().getRight()); // assign next symbol in order
                    nonTerminal.getRule().addNextSymbol(nonTerminal.getRight().getLeft());
                }

                // assign new right of terminals left to the nonterminal
                nonTerminal.getRight().assignLeft(nonTerminal);

                // rule has been evaluated so needs to be marked as to not check
                nonTerminal.getRule().compressed = true;

                // add the second instance of nonterminal where the pointer was
                NonTerminal nt = new NonTerminal(nonTerminal.getRule());
                c.getFirstRule().addNextSymbol(nt);
            }
            else { // repeated nonterminal that has already been seen twice
                char cb = input.charAt(position);
                Integer i = Character.getNumericValue(cb); // get marker indicator from string
                NonTerminal nonTerminal = new NonTerminal(marker.get(i).getRule()); // get rule from hashmap
                c.getFirstRule().addNextSymbol(nonTerminal); // add to main rule
            }
            position++; // increase position in string
        }

        // these three lines just for debugging, looking at the rules
        c.rules.add(c.getFirstRule());
        c.generateRules(c.getFirstRule().getGuard().getRight());
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
