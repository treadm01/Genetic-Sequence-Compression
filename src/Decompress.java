public class Decompress {


    public Rule buildGrammar(String input) {
        Rule r = new Rule();
        for (int i = 0; i < input.length(); i++) {
            if (Character.isAlphabetic(input.charAt(i))) {
                Terminal t = new Terminal(input.charAt(i));
                r.addNextSymbol(t);
            }
            else if (input.charAt(i) == '(') {
                Rule rule = new Rule();
                for (int j = 0; j < Character.getNumericValue(input.charAt(i+3)); j++) { // if width is more than one digit?
                    rule.addNextSymbol(new Terminal(input.charAt(Character.getNumericValue(input.charAt((i+1)+j)))));
                }
                r.addNextSymbol(new NonTerminal(rule));
            }
        }
        return r;
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
