public class Rule extends Symbol {
    // keep reference to nonTerminal this occurs in??
    NonTerminal nonTerminal; // the nonTerminal the rule points to

    public Rule(NonTerminal nonTerminal) {
        this.nonTerminal = nonTerminal;
        this.nonTerminal.count++;
        representation = nonTerminal.representation;
    }

    public void removeRule() {
        left.right = nonTerminal.guard.left.right;
        nonTerminal.last.right = right;
        //right.left = nonTerminal.last;
//        left.right = nonTerminal.guard.left.right;
//        nonTerminal.last.right = right;
//        nonTerminal.guard.left.right.left = left;
//        right.left = nonTerminal.last;
//        //nonTerminal.last.right = right;
        //nonTerminal.guard.left.right.left = left;

        //right.left = nonTerminal.last;
        //left.right = nonTerminal.guard.left.right;
        System.out.println(right.left);
        System.out.println("right of this rule is " + right.left);
        System.out.println("left of this rule is " + left + left.right + left.right.right);
        System.out.println("last symbol the rule points to " + nonTerminal.last.right);
        System.out.println("first symbol the rule points to " + nonTerminal.guard.left.right);
        //System.out.println(right.left);
        //right.left = nonTerminal.last;
//        nonTerminal.last.right = right;
//        nonTerminal.guard.left.right.left = left;
    }
}
