public class Rule extends Symbol {
    // keep reference to nonTerminal this occurs in??
    NonTerminal nonTerminal; // the nonTerminal the rule points to
    NonTerminal locationTerminal;

    public Rule(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
        this.nonTerminal.count++;
        representation = nonTerminal.representation;
    }

    public void removeRule() {
        //TODO figure out how to access rule to be removed and replace with the rule it points to
        //TODO how to work for longer symbols??
        nonTerminal.last.right = right;
        nonTerminal.guard.left.right.left = left;

        right.left = nonTerminal.last;
        left.right = nonTerminal.guard.left.right;
    }
}
