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

    // used to pass number of relevant digrams in a rule to the rules next check
    int numberOfMatchingDigrams() {
        return matchingDigrams;
    }
}
