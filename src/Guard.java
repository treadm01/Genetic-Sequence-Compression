public class Guard extends Symbol {
    Rule guardRule;

    public Guard(Rule rule) {
        representation = "?"; // TODO needed to check equality...
        guardRule = rule; // easy access to rule from guard
    }


    @Override
    public Boolean isGuard() {
        return true;
    }

    public Rule getGuardRule() {return guardRule;}
}