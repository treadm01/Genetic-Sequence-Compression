package Search;

import GrammarCoder.*;

import java.util.*;

public class Search {
    private DigramMap digramMap; // - digram points to digram via right hand symbol
    private Set<Rule> rulesFromGrammar = new HashSet<>();
    private Set<String> uniqueTerminals = new HashSet<>();
    private Set<String> checkedEncodings = new HashSet<>();
    private Boolean found = false;
    private PriorityQueue<SearchNode> searchNodePriorityQueue;

    public Search(Rule firstRule) {
        rulesFromGrammar.add(firstRule);
        generateRules(firstRule.getFirst());
        this.digramMap = createDigramMap(firstRule);
    }

    //todo deduplicate - slightly different to compress as doesn't have edits - give edits, in a sense will be finding approximate repeats
    // are edits retrieved?? should be if string is decompressedd in the same way in final check
    private void generateRules(Symbol current) {
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                Rule rule = ((NonTerminal) current).getRule();
                rulesFromGrammar.add(rule);
                generateRules(rule.getFirst());
            }
            current = current.getRight();
        }
    }

    private DigramMap createDigramMap(Rule firstRule) {
        DigramMap dm = new DigramMap();
        generateRules(firstRule.getFirst()); //todo needs unduplicating
        for (Rule r : rulesFromGrammar) {
            Symbol firstRight = r.getFirst().getRight();
            do {
                dm.addNewDigrams(firstRight);
                if (firstRight.getLeft() instanceof Terminal) {
                    uniqueTerminals.add(firstRight.getLeft().toString());
                    uniqueTerminals.add(String.valueOf(Terminal.reverseSymbol(firstRight.getLeft().toString().charAt(0))));
                }
                if (firstRight instanceof Terminal) {
                    uniqueTerminals.add(firstRight.toString());
                    uniqueTerminals.add(String.valueOf(Terminal.reverseSymbol(firstRight.toString().charAt(0))));
                }
                firstRight = firstRight.getRight();
            } while (!firstRight.isGuard());
        }
        return dm;
    }

    public Boolean search(String searchString) {
        searchNodePriorityQueue = new PriorityQueue<>(Comparator.comparingInt(SearchNode::numberOfMatchingDigrams));
        uniqueTerminals.add("");
        //single letter search
        if (searchString.length() == 1) {
            found = uniqueTerminals.contains(searchString);
        }
        else {
            Rule baseRule = new Rule();
            baseRule.addNextSymbol(new Terminal(searchString.charAt(0)));
            baseRule.addNextSymbol(new Terminal(searchString.charAt(1)));
            SearchNode base = new SearchNode(baseRule, searchString.substring(2));
            base.matchingDigrams = countMatchingDigrams(baseRule);
            buildSearchTree(base, searchString);
        }

        return found;
    }

    private void buildSearchTree(SearchNode base, String searchString) {
        searchNodePriorityQueue.add(base);
        SearchNode searchNode;
        while (!searchNodePriorityQueue.isEmpty()) {
            searchNode = searchNodePriorityQueue.poll();
            buildLeftNode(searchNode);
            buildSymbolNode(searchNode);
            buildCompleteRuleNode(searchNode);
            buildRightNode(searchNode);

            if (searchNode.searchString.length() == 0) {
                if (checkDigramsAreConsecutive(searchNode.rule)) {
                    if (searchNode.rule.getSymbolString(searchNode.rule, false).contains(searchString)) {
                        found = true;
                        break;
                    }
                }
            }
        }
    }

    private void addNode(SearchNode searchNode, SearchNode newNode) {
        if (!checkedEncodings.contains(newNode.rule.getRuleString())) {
            newNode.matchingDigrams = countMatchingDigrams(newNode.rule);
            newNode.matchingDigrams += searchNode.matchingDigrams;
            searchNodePriorityQueue.add(newNode);
            checkedEncodings.add(newNode.rule.getRuleString());
        }
    }

    // will have to take rest of search string too to create new terminals
    private void buildLeftNode(SearchNode searchNode) {
        if (searchNode.digramNodeLeft == null && searchNode.searchString.length() >= 1) {
            searchNode.digramNodeLeft = new SearchNode(searchNode.rule, searchNode.searchString);
            checkdigram(searchNode.digramNodeLeft.rule.getFirst().getRight());
            addNode(searchNode, searchNode.digramNodeLeft);
        }
    }

    private void buildCompleteRuleNode(SearchNode searchNode) {
        if (searchNode.completeRule == null && searchNode.searchString.length() >= 1) {
            searchNode.completeRule = new SearchNode(searchNode.rule, searchNode.searchString);
            checkIfCompleteRule(searchNode.completeRule.rule.getLast());
            addNode(searchNode, searchNode.completeRule);
        }
    }

    private void buildSymbolNode(SearchNode searchNode) {
        if (searchNode.symbolNode == null && searchNode.searchString.length() >= 1) {
            searchNode.symbolNode = new SearchNode(searchNode.rule, searchNode.searchString.substring(1));
            searchNode.symbolNode.rule.addNextSymbol(new Terminal(searchNode.searchString.charAt(0)));
            addNode(searchNode, searchNode.symbolNode);
        }
    }

    private void buildRightNode(SearchNode searchNode) {
        if (searchNode.digramNodeRight == null) {
            SearchNode digram;
            if (searchNode.searchString.length() >= 1) {
                digram = new SearchNode(searchNode.rule, searchNode.searchString);
            } else {
                digram = new SearchNode(searchNode.rule, "");
            }
            searchNode.digramNodeRight = digram;
            checkdigram(digram.rule.getLast());
            addNode(searchNode, searchNode.digramNodeRight);
        }
    }

    private void replaceSequence(Rule rule, Symbol start, Symbol stop, Boolean isComplement) {
        NonTerminal nonTerminal = new NonTerminal(rule);
        nonTerminal.isComplement = isComplement;
        start.assignRight(nonTerminal);
        stop.getRight().assignLeft(nonTerminal);
        nonTerminal.assignLeft(start);
        nonTerminal.assignRight(stop.getRight());
    }

    private void checkIfCompleteRule(Symbol last) {
        if (digramMap.containsDigram(last)) {
            //loop through the rule, check not main rule etc
            Symbol check = digramMap.getOriginalDigram(last);
            //some reverse complements are the same forwards and backwards so has to be checked for both
            if (last.equals(check)
                    || digramMap.getOriginalDigram(last).equals(digramMap.getExistingDigram(last))) {
                checkNormalRule(last, check);
            }
            if (digramMap.getOriginalDigram(last).equals(digramMap.getExistingDigram(last))
                    || !last.equals(check) ) {
                checkReverseComplementRule(last, check);
            }
        }
    }

    private void checkNormalRule(Symbol last, Symbol check) {
        Symbol stop = last;
        if (check.getRight().isGuard()) { // has to be rule
            Guard g = (Guard) check.getRight(); // get rule number with this
            Rule r = g.getGuardRule();
            if (r.getRepresentation() != 0) {
                while (!last.getLeft().getLeft().isGuard()) {
                    last = last.getLeft();
                    check = check.getLeft();
                    if (!last.equals(check)) {
                        break;
                    } else if (check.getLeft().getLeft().isGuard()) {
                        replaceSequence(r, last.getLeft().getLeft(), stop, false);
                    }
                }
            }
        }
    }

    private void checkReverseComplementRule(Symbol last, Symbol check) {
        Symbol stop = last; // stop position taken from intitial last position
        if (check.getLeft().getLeft().isGuard()) { // has to be rule
            Guard g = (Guard) check.getLeft().getLeft(); // get rule number with this
            Rule r = g.getGuardRule();
            if (r.getRepresentation() != 0) {
                while (!check.getRight().isGuard()) {
                    last = last.getLeft();
                    check = check.getRight();
                    if (digramMap.containsDigram(last)) {
                        if (!digramMap.getOriginalDigram(last).equals(check)) {
                            break;
                        }
                        else if (check.getRight().isGuard()) {
                            replaceSequence(r, last.getLeft().getLeft(), stop, true);
                        }
                    }
                }
            }
        }
    }

    private void checkdigram(Symbol digram) {
        if (digramMap.containsDigram(digram)) {
            Symbol oldSymbol = digramMap.getOriginalDigram(digram);
            if (oldSymbol.isARule()) {
                Guard g = (Guard) oldSymbol.getRight(); // have to get guard and then rule from there
                Rule rule = g.getGuardRule(); // get rule using pointer to it in the guard
                assignDigrams(rule, digram);
            }
        }
    }

    private void assignDigrams(Rule rule, Symbol digram) {
            if (rule.getRepresentation() != 0) {
                NonTerminal nonTerminal = new NonTerminal(rule);
                // needs to be from complement variable, but then need to assign each new symbol. maybe
                nonTerminal.isComplement = !digram.equals(rule.getLast()); //true or alternate value, would have to alternate the nonterminal???

                nonTerminal.assignRight(digram.getRight());
                nonTerminal.assignLeft(digram.getLeft().getLeft());

                digram.getLeft().getLeft().assignRight(nonTerminal);
                digram.getRight().assignLeft(nonTerminal);
            }
        }

    private Boolean checkDigramsAreConsecutive(Rule rule) {
        if (rule.getRuleLength() == 1) {
            rule = ((NonTerminal)rule.getLast()).getRule();
        }
        Symbol first = rule.getLast();
        Symbol check;

        // check that last digram is correct
        if (rule.getRuleLength() == 0) {
            return false;
        }
        else if (digramMap.containsDigram(first)) {
            if (rule.getRuleLength() == 2) {
                return true;
            }
            check = digramMap.getOriginalDigram(first);
        }
        else {
            return false;
        }

        boolean found;
        found = false;

        // checking that haven't reached the first digram
        while (!first.getLeft().isGuard()) {
            // if at any point digram does not exist then false
            if (!digramMap.containsDigram(first)) {
                found = false;
                break;
            }

            if (check.equals(first)) {
                found = digramMap.getOriginalDigram(first).equals(first);
            }
            else {
                found = false;
            }

            if (!found) {
                break;
            }

            first = first.getLeft();
            check = check.getLeft();
        }
        return found;
    }

    private int countMatchingDigrams(Rule rule) {
        Symbol first = rule.getFirst().getRight();
        int count = 0;
        int consecutiveCount = 0;
        while (!first.isGuard()) {
            if (digramMap.containsDigram(first)) {
                count++;
                count += consecutiveCount;
                consecutiveCount++;
                if (digramMap.getRuleNumber(first) == 0) {
                    count *= 2;
                }
            }
            else {
                consecutiveCount = 0;
            }
            first = first.getRight();
        }
        return count;
    }
}
