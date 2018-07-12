public class Guard extends Symbol {
    Rule guardRule;

    @Override
    public Boolean isGuard() {
        return true;
    }


    public Guard(Rule rule) {
        representation = "?"; // TODO needed to check equality...
        guardRule = rule; // easy access to rule from guard
    }
}