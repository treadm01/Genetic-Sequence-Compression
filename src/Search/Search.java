package Search;

import GrammarCoder.*;

import java.util.*;

public class Search {
    private DigramMap digramMap;
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


    /**
     * rules created individually for grammar to more easily
     * reconstruct digram map for each
     * @param current
     */
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

    /**
     * create digram map from all rules in grammar
     * also fills a set of the unique symbols that occur
     * to search single values
     * @param firstRule
     * @return
     */
    private DigramMap createDigramMap(Rule firstRule) {
        DigramMap dm = new DigramMap();
        generateRules(firstRule.getFirst());
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

    /**
     * main search method returns boolean value if search string
     * is found within the grammar, works for single characters
     * and currently only certain length search strings that correspond
     * to a retrievable encoding
     * @param searchString
     * @return
     */
    public Boolean search(String searchString) {
        uniqueTerminals.add("");
        //single letter search
        if (searchString.length() == 1) {
            found = uniqueTerminals.contains(searchString);
        }
        else {
            searchNodePriorityQueue = new PriorityQueue<>(Comparator.comparingInt(SearchNode::numberOfMatchingDigrams));
            Rule baseRule = new Rule(); // a rule containing the initial digram of the search string
            baseRule.addNextSymbol(new Terminal(searchString.charAt(0)));
            baseRule.addNextSymbol(new Terminal(searchString.charAt(1)));
            SearchNode base = new SearchNode(baseRule, searchString.substring(2));
            // count of how many of the rules digrams occur within the grammar - used for priority
            base.matchingDigrams = countMatchingDigrams(baseRule);
            buildSearchTree(base, searchString);
        }
        return found;
    }

    /**
     * build search tree of possible options for how the search string would
     * be encoded within the grammar
     * @param base
     * @param searchString
     */
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

    /**
     * add node to the tree if hasn't been seen already, update
     * the priority and add to the queue
     * @param searchNode
     * @param newNode
     */
    private void addNode(SearchNode searchNode, SearchNode newNode) {
        if (!checkedEncodings.contains(newNode.rule.getRuleString())) {
            newNode.matchingDigrams = countMatchingDigrams(newNode.rule);
            newNode.matchingDigrams += searchNode.matchingDigrams;
            searchNodePriorityQueue.add(newNode);
            checkedEncodings.add(newNode.rule.getRuleString());
        }
    }

    // checks the first digram of the rule for if it is a nonterminal
    private void buildLeftNode(SearchNode searchNode) {
        if (searchNode.digramNodeLeft == null && searchNode.searchString.length() >= 1) {
            searchNode.digramNodeLeft = new SearchNode(searchNode.rule, searchNode.searchString);
            checkdigram(searchNode.digramNodeLeft.rule.getFirst().getRight());
            addNode(searchNode, searchNode.digramNodeLeft);
        }
    }

    // checks whether the entire rule of the node is a rule itself
    private void buildCompleteRuleNode(SearchNode searchNode) {
        if (searchNode.completeRule == null && searchNode.searchString.length() >= 1) {
            searchNode.completeRule = new SearchNode(searchNode.rule, searchNode.searchString);
            checkIfCompleteRule(searchNode.completeRule.rule.getLast());
            addNode(searchNode, searchNode.completeRule);
        }
    }

    // adds the next symbol from the search string
    private void buildSymbolNode(SearchNode searchNode) {
        if (searchNode.symbolNode == null && searchNode.searchString.length() >= 1) {
            searchNode.symbolNode = new SearchNode(searchNode.rule, searchNode.searchString.substring(1));
            searchNode.symbolNode.rule.addNextSymbol(new Terminal(searchNode.searchString.charAt(0)));
            addNode(searchNode, searchNode.symbolNode);
        }
    }

    // checks the last digram of the rule to see if it is itself an entire rule
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

    /**
     * when a number of symbols have been matched as rule, replace with a nonterminal
     * @param rule
     * @param start
     * @param stop
     * @param isComplement
     */
    private void replaceSequence(Rule rule, Symbol start, Symbol stop, Boolean isComplement) {
        NonTerminal nonTerminal = new NonTerminal(rule);
        nonTerminal.setIsComplement(isComplement);
        start.assignRight(nonTerminal);
        stop.getRight().assignLeft(nonTerminal);
        nonTerminal.assignLeft(start);
        nonTerminal.assignRight(stop.getRight());
    }

    /**
     * cycle through the symbols of a rule and compares them
     * to the corresponding symbols of the grammar. If they all match
     * then it matches the corresponding rule
     * @param last
     */
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

    // whether a reverse complement or not affects the order the digrams must be checked in
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

    // attempting to match a reverse complement rule todo needs refactoring
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

    /**
     * check whether the digram occurs already in the digram map
     * and process accordingly
     * @param digram
     */
    private void checkdigram(Symbol digram) {
        if (digramMap.containsDigram(digram)) {
            Symbol oldSymbol = digramMap.getOriginalDigram(digram);
            if (oldSymbol.isARule()) {
                Guard g = (Guard) oldSymbol.getRight();
                Rule rule = g.getGuardRule();
                assignDigrams(rule, digram);
            }
        }
    }

    /**
     * creates new nonterminal for the found digram and reapplies the links
     * within the rule
     * @param rule
     * @param digram
     */
    private void assignDigrams(Rule rule, Symbol digram) {
        if (rule.getRepresentation() != 0) {
            NonTerminal nonTerminal = new NonTerminal(rule);
            nonTerminal.setIsComplement(!digram.equals(rule.getLast()));

            nonTerminal.assignRight(digram.getRight());
            nonTerminal.assignLeft(digram.getLeft().getLeft());

            digram.getLeft().getLeft().assignRight(nonTerminal);
            digram.getRight().assignLeft(nonTerminal);
        }
    }


    /**
     * once rule has been formed that encodes the search string
     * have to check that the digrams occur consecutively within
     * the grammar, that is a sequence that does actually occur
     * todo needs refactoring
     * @param rule
     * @return
     */
    private Boolean checkDigramsAreConsecutive(Rule rule) {
        // if rule is fully encoded within one nonterminal, check the sequence it represents
        if (rule.getRuleLength() == 1) {
            rule = ((NonTerminal)rule.getLast()).getRule();
        }

        Symbol first = rule.getLast(); // start from the end of rule and work backwards - avoids overlap
        Symbol check;

        if (rule.getRuleLength() == 0) { // todo shouldn't be getting 0 length rules now
            return false;
        }
        else if (digramMap.containsDigram(first)) {
            if (rule.getRuleLength() == 2) { // if found and entire length, then found
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

    /**
     * iterate over digrams in rule and cound all those that occur and
     * are consecutive to increase priority for those more likely to
     * be a part of the grammar
     * @param rule
     * @return
     */
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
