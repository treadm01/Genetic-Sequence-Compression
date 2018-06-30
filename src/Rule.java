public class Rule extends Symbol {
    // keep reference to nonTerminal this occurs in??
    NonTerminal nonTerminal; // the nonTerminal the rule points to

    public Rule(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
        this.nonTerminal.count++;
        representation = nonTerminal.representation;
    }

    public void removeRule() {
//        System.out.println(nonTerminal.values);
//        System.out.println(right.left);
//
//        nonTerminal.values.get(1).left = left;
//        nonTerminal.values.get(nonTerminal.values.size() - 2).right = right;
//
//        right.left = nonTerminal.values.get(nonTerminal.values.size() - 2);
//
//        System.out.println(right.left);

    }
}
