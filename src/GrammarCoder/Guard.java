package GrammarCoder;

public class Guard extends Symbol {
    private Rule guardRule;

    public Guard(Rule rule) {
        representation = 0;
        guardRule = rule;
    }

    public Rule getGuardRule() {return guardRule;}
}