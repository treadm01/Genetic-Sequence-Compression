package GrammarCoder;

public class SearchNode {
    Rule rule;
    SearchNode digramNodeLeft;
    SearchNode digramNodeRight;
    SearchNode symbolNode;
    SearchNode parent;
    SearchNode completeRule;
    String searchString;
    int matchingDigrams;

    public SearchNode(Rule rule, String searchString) {
        this.searchString = searchString;
        this.rule = new Rule();
        this.rule.addAllSymbols(rule.getFirst());
    }

    public int numberOfMatchingDigrams() {
        return matchingDigrams;
    }
}
