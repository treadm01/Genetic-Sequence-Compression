public class Rule extends Symbol {
    NonTerminal nonTerminal;

    public Rule(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
        this.nonTerminal.count++;
        representation = nonTerminal.representation;
    }
}
