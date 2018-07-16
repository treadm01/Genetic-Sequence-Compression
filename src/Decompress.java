import java.util.ArrayList;
import java.util.List;

public class Decompress {

    //TODO just from string, keep a hash of rules to access from there but the pointers refer to
    // todo uncompressed string
    //TODO or else try to rebuild the entire grammar with links

    //build the complete grammar up from the string and return the first rule
    // but need to maintain the string as well to use the indexes...
    // when rules are created add to list, add nonterminals
    // then string of first rule is gained at the end and can be ran through into a rule
    public Rule buildGrammar(String input) {
        Compress c = new Compress();
        String decompressString = "";
        int position = 1;
        String index = "", width = "";
        c.getFirstRule().addNextSymbol(new Terminal(input.charAt(0)));
        decompressString = c.getFirstRule().getRuleString();
        while (position < input.length()) {
            if (Character.isAlphabetic(input.charAt(position))) {
                c.getFirstRule().addNextSymbol(new Terminal(input.charAt(position)));
                c.checkDigram(c.getFirstRule().getLast());
                decompressString = c.getFirstRule().getRuleString();
            }
            else if (input.charAt(position) == '(') {
                position++;
                while (input.charAt(position) != ',') {
                    index += input.charAt(position);
                    position++;
                }

                if (input.charAt(position) == ',') {
                    position++;
                    while (input.charAt(position) != ')') {
                        width += input.charAt(position);
                        position++;
                    }
                }

                for (int j = 0; j < Integer.valueOf(width); j++) { //TODO only works for width of one... single number
                    char ch = decompressString.charAt(Integer.parseInt(index) + j); // has to be decompress to get right symbols
                    if (Character.isAlphabetic(ch)) {
                        c.getFirstRule().addNextSymbol(new Terminal(ch));
                    }
                    else {
                        c.getFirstRule().addNextSymbol(
                                new NonTerminal(c.ruleList.get((Character.getNumericValue(ch)/2)-1)));
                    }
                    c.checkDigram(c.getFirstRule().getLast());
                    // what if not a terminal?
                }

                decompressString = c.getFirstRule().getRuleString();
                index = "";
                width = "";
            }
            // meeting another nonterminal, first comes width then nonterminal number
            else {
                width += input.charAt(position);// todo what if width is more than one??
                position++;
                for (int z = 0; z < Integer.valueOf(width); z++) { //TODO only works for width of one... single number
                    char ch = input.charAt(position + z); // has to be input to get the nonterminal
                    c.getFirstRule().addNextSymbol(
                            // can't get like this as they change
                            new NonTerminal(c.ruleList.get((Character.getNumericValue(ch)/2)-1)));
                    c.checkDigram(c.getFirstRule().getLast());
                }
                decompressString = c.getFirstRule().getRuleString();
                width = "";
            }
            position++;
            System.out.println(decompressString);
        }

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
