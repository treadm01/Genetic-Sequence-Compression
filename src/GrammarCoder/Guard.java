package GrammarCoder;

public class Guard extends Symbol {
    private Rule guardRule;

    Guard(Rule rule) {
        representation = 0;
        guardRule = rule;
    }

    /**
     * access to rule object via the guard
     * @return
     */
    public Rule getGuardRule() {return guardRule;}
}