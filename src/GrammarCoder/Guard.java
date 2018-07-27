package GrammarCoder;

public class Guard extends Symbol {
    Rule guardRule;

    public Guard(Rule rule) {
        representation = 0; // TODO needed to check equality...
        guardRule = rule; // easy access to rule from guard
    }

    public Rule getGuardRule() {return guardRule;}
}