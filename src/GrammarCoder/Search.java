package GrammarCoder;

import java.util.*;

public class Search {
    private DigramMap digramMap; // - digram points to digram via right hand symbol
    private Set<Rule> rulesFromGrammar = new HashSet<>();
    private Set<String> uniqueSymbols;
    private Rule mainRule;
    private Set<String> uniqueTerminals = new HashSet<>();
    private Set<String> checkedEncodings = new HashSet<>();
    Boolean found = false;

    public Search(Rule firstRule) {
        mainRule = firstRule;
        uniqueSymbols = new HashSet<>();
        rulesFromGrammar.add(firstRule);
        generateRules(firstRule.getFirst());
        this.digramMap = createDigramMap(firstRule);
    }

    //todo deduplicate - slightly different to compress as doesn't have edits - give edits, in a sense will be finding approximate repeats
    // are edits retrieved?? should be if string is decompressedd in the same way in final check
    public void generateRules(Symbol current) {
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                Rule rule = ((NonTerminal) current).getRule();
                rulesFromGrammar.add(rule);
                generateRules(rule.getFirst());
            }
            current = current.getRight();
        }
    }


    //todo search by rule, can do by length, ones too short don't bother
    // create digram map too, search for reverse complement at the same time
    // keep a cache
    public DigramMap createDigramMap(Rule firstRule) {
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


     //get every instance of first digram
    public Boolean search(String searchString) {
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
            //two things search digram or add symbol new node for each
            List<Rule> possibleRules = buildSearchTree(base, searchString);

            for (Rule r : possibleRules) {
                for (String s : uniqueTerminals) {
                    base = new SearchNode(r, s);
                    buildSearchTree(base, "");
                }
            }
        }

        return found;
    }

    public List<Rule> buildSearchTree(SearchNode base, String searchString) {
        PriorityQueue<SearchNode> searchNodePriorityQueue
                = new PriorityQueue<>(Comparator.comparingInt(SearchNode::numberOfMatchingDigrams));
        searchNodePriorityQueue.add(base);
        List<Rule> possibleRules = new ArrayList<>();
        while (!searchNodePriorityQueue.isEmpty()) {
            SearchNode searchNode = searchNodePriorityQueue.poll();

            if (searchNode.digramNodeLeft == null) {
                //System.out.println("left getting " + searchNode.rule.getRuleString());
                // create rule add to queue
                buildLeftNode(searchNode);
                if (!checkedEncodings.contains(searchNode.digramNodeLeft.rule.getRuleString())) {
                    searchNode.digramNodeLeft.matchingDigrams = countMatchingDigrams(searchNode.digramNodeLeft.rule);
                    searchNodePriorityQueue.add(searchNode.digramNodeLeft);
                    checkedEncodings.add(searchNode.digramNodeLeft.rule.getRuleString());
//                    System.out.println(" left returning " + searchNode.digramNodeLeft.rule.getRuleString());
                }
            }
            if (searchNode.symbolNode == null) {
                //System.out.println("add gettinf " + searchNode.rule.getRuleString());
                buildSymbolNode(searchNode);
                if (!checkedEncodings.contains(searchNode.symbolNode.rule.getRuleString())) {
                    searchNode.symbolNode.matchingDigrams = countMatchingDigrams(searchNode.symbolNode.rule);
                    searchNodePriorityQueue.add(searchNode.symbolNode);
                    checkedEncodings.add(searchNode.symbolNode.rule.getRuleString());
  //                  System.out.println("add returning " + searchNode.symbolNode.rule.getRuleString());
                }

            }
            if (searchNode.digramNodeRight == null) {
                //System.out.println("right getting " + searchNode.rule.getRuleString());
                buildRightNode(searchNode);
                if (!checkedEncodings.contains(searchNode.digramNodeRight.rule.getRuleString())) {
                    searchNode.digramNodeRight.matchingDigrams = countMatchingDigrams(searchNode.digramNodeRight.rule);
                    searchNodePriorityQueue.add(searchNode.digramNodeRight);
                    checkedEncodings.add(searchNode.digramNodeRight.rule.getRuleString());
    //                System.out.println("right returning " + searchNode.digramNodeRight.rule.getRuleString());
                }
            }
            //todo added check for complete rules, need to do separate check and adds
            if (searchNode.completeRule == null) {
                buildCompleteRuleNode(searchNode);
                if (!checkedEncodings.contains(searchNode.completeRule.rule.getRuleString())) {
                    searchNode.completeRule.matchingDigrams = countMatchingDigrams(searchNode.completeRule.rule);
                    searchNodePriorityQueue.add(searchNode.completeRule);
                    checkedEncodings.add(searchNode.completeRule.rule.getRuleString());
                }
            }


            if (searchNode.searchString.length() == 0) {
                possibleRules.add(searchNode.rule);
                if (checkDigramsAreConsecutive(searchNode.rule)) {
                    if (searchNode.rule.getSymbolString(searchNode.rule, false).contains(searchString)) {
                        found = true;
                        break;
                    }
                }
            }

        }
        return possibleRules;
    }

    // will have to take rest of search string too to create new terminals
    public void buildLeftNode(SearchNode searchNode) {
        SearchNode digram;
        if (searchNode.searchString.length() >= 1) {
            digram = new SearchNode(searchNode.rule, searchNode.searchString);
        }
        else {
            digram = new SearchNode(searchNode.rule, "");
        }
        checkdigram(digram.rule.getFirst().getRight());
        searchNode.digramNodeLeft = digram;
    }

    public void buildSymbolNode(SearchNode searchNode) {
        //System.out.println(searchNode.rule.getRuleString());
        SearchNode symbol;
        if (searchNode.searchString.length() >= 1) {
            symbol = new SearchNode(searchNode.rule, searchNode.searchString.substring(1));
            symbol.rule.addNextSymbol(new Terminal(searchNode.searchString.charAt(0)));
        }
        else {
            symbol = new SearchNode(searchNode.rule, "");
        }

        searchNode.symbolNode = symbol;
    }

    public void buildRightNode(SearchNode searchNode) {
        SearchNode digram;
        if (searchNode.searchString.length() >= 1) {
            digram = new SearchNode(searchNode.rule, searchNode.searchString);
        }
        else {
            digram = new SearchNode(searchNode.rule, "");
        }
        checkdigram(digram.rule.getLast());
        searchNode.digramNodeRight = digram;
    }

    public void buildCompleteRuleNode(SearchNode searchNode) {
        SearchNode digram;
        //todo this check still needed?
        if (searchNode.searchString.length() >= 1) {
            digram = new SearchNode(searchNode.rule, searchNode.searchString);
        }
        else {
            digram = new SearchNode(searchNode.rule, "");
        }
        checkIfCompleteRule(digram.rule.getLast());
        searchNode.completeRule = digram;
    }

    public void replaceSequence(Rule rule, Symbol start, Symbol stop, Boolean isComplement) {
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
                    if (!last.equals(check)) { //todo reverse complements?
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
            if (digramMap.digramIsARule(oldSymbol)) {
                Guard g = (Guard) oldSymbol.getRight(); // have to get guard and then rule from there
                Rule rule = g.getGuardRule(); // get rule using pointer to it in the guard
                assignDigrams(rule, digram);
            }
        }
    }

    public void assignDigrams(Rule rule, Symbol digram) {
            if (rule.representation != 0) {
                NonTerminal nonTerminal = new NonTerminal(rule);
                // needs to be from complement variable, but then need to assign each new symbol. maybe
                nonTerminal.isComplement = !digram.equals(rule.getLast()); //true or alternate value, would have to alternate the nonterminal???

                nonTerminal.assignRight(digram.getRight());
                nonTerminal.assignLeft(digram.getLeft().getLeft());

                digram.getLeft().getLeft().assignRight(nonTerminal);
                digram.getRight().assignLeft(nonTerminal);

                // todo not checking digrams works on some detracts on others
                //checkdigram(nonTerminal);
                //checkdigram(nonTerminal.getRight());
            }
        }

    public Boolean checkDigramsAreConsecutive(Rule rule) {
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
         //   System.out.println("should be ");
            if (rule.getRuleLength() == 2) {
           //     System.out.println("here");
                return true;
            }
            // get matching
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

    public Set<String> createSearchString(String searchString) {
        Set<String> searchStrings = new HashSet<>();
        //System.out.println(uniqueTerminals);
        for (String s : uniqueTerminals) {
            for (String s1 : uniqueTerminals) {
                String s5 = searchString + s + s1;
                String s6 = s + s1 + searchString;
                String s4 = s + s1 + searchString + s1 + s;
                String s7 = s1 + s + searchString + s + s1;
                String s8 = s + searchString + s1;
                searchStrings.add(s4);
                searchStrings.add(s5);
                searchStrings.add(s6);
                searchStrings.add(s7);
                searchStrings.add(s8);
            }
        }

//        for (String s : searchStrings) {
//            System.out.println(s);
//        }
        return searchStrings;
    }

    public int countMatchingDigrams(Rule rule) {
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
