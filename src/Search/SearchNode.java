package Search;

import GrammarCoder.Rule;

class SearchNode {
    Rule rule;
    SearchNode digramNodeLeft;
    SearchNode digramNodeRight;
    SearchNode symbolNode;
    SearchNode completeRule;
    String searchString;
    int matchingDigrams;

    SearchNode(Rule rule, String searchString) {
        this.searchString = searchString;
        this.rule = new Rule();
        this.rule.addAllSymbols(rule.getFirst());
    }

    int numberOfMatchingDigrams() {
        return matchingDigrams;
    }
}
