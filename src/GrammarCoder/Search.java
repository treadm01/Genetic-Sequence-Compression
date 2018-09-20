package GrammarCoder;

import java.util.*;

public class Search {
    private DigramMap digramMap; // - digram points to digram via right hand symbol
    private Set<Rule> rulesFromGrammar = new HashSet<>();
    Set<String> uniqueSymbols;
    Rule mainRule;
    Set<Symbol> matchingDigrams = new HashSet<>();
    Set<String> uniqueTerminals = new HashSet<>();
    Set<Rule> possibleNonTerminal = new HashSet<>();

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

    public void checkDigramsToRule(Rule newRule, Boolean isLast) {
        if (digramMap.containsDigram(newRule.getLast())) { //todo need a mehod to determine if a rule
            Symbol oldSymbol = digramMap.getOriginalDigram(newRule.getLast());
            if (digramMap.digramIsARule(oldSymbol)
             || (newRule.getLast().getLeft().getLeft().isGuard() // is first
                    && oldSymbol.getRight().isGuard())
            || (isLast && !newRule.getLast().equals(oldSymbol) && oldSymbol.getRight().isGuard())) {
                Guard g = (Guard) oldSymbol.getRight(); // have to get guard and then rule from there
                Rule rule = g.getGuardRule(); // get rule using pointer to it in the guard
                assignDigrams(rule, newRule, oldSymbol, isLast);
            } else if (isLast && oldSymbol.getLeft().getLeft().isGuard()
                    || (newRule.getLast().getLeft().getLeft().isGuard() && !newRule.getLast().equals(oldSymbol) && oldSymbol.getLeft().getLeft().isGuard())
            ) {
                Guard g = (Guard) oldSymbol.getLeft().getLeft(); // have to get guard and then rule from there
                Rule rule = g.getGuardRule(); // get rule using pointer to it in the guard
                assignDigrams(rule, newRule, oldSymbol, true);
            }
            //else if (isLast && )
        }
    }


    public void assignDigrams(Rule rule, Rule newRule, Symbol oldSymbol, Boolean isLast) {
        if (rule.representation != 0) {
            NonTerminal nonTerminal = new NonTerminal(rule);
            // needs to be from complement variable, but then need to assign each new symbol. maybe
            nonTerminal.isComplement = !newRule.getLast().equals(oldSymbol); //true or alternate value, would have to alternate the nonterminal???

            nonTerminal.assignRight(newRule.getLast().getRight());
            nonTerminal.assignLeft(newRule.getLast().getLeft().getLeft());

            newRule.getLast().getLeft().getLeft().assignRight(nonTerminal);
            newRule.getLast().getRight().assignLeft(nonTerminal);

            // todo not checking digrams works on some detracts on others
            checkDigramsToRule(newRule, isLast);
        }
    }

     //get every instance of first digram
    public Boolean search(String searchString) {
        Boolean found = false;
        uniqueTerminals.add("");
        //single letter search
        if (searchString.length() == 1) {
            found = uniqueTerminals.contains(searchString);
        }
        else { // todo needs to be generic
//            Rule newRule = createSearchRule(searchString);
//            // if an existing digram found
//          //  System.out.println(newRule.getRuleString());
//            if (newRule.getRuleLength() == 1
//                    || newRule.getRuleLength() == 1 && digramMap.containsDigram(newRule.getLast())) {
//                found = true;
//            }
//            else if (newRule.getRuleLength() == 2 && checkDigramsAreConsecutive(newRule)) {
//                found = true;
//                System.out.println("found digram");
//            }
//            else { // create rule for digrams from search string
//                StringBuilder reverseComplement = new StringBuilder();
//                for (int j = 0; j < searchString.length(); j++) {
//                    reverseComplement.append(Terminal.reverseSymbol(searchString.charAt(j)));
//                }
//                reverseComplement.reverse();
//                List<String> allSearch = new ArrayList<>();
//                allSearch.addAll(createSearchString(searchString));
//                allSearch.addAll(createSearchString(reverseComplement.toString()));
//
//                //
//                for (String s : allSearch) {
//                    newRule = createSearchRule(s);
//                    //System.out.println(newRule.getRuleString());
//                    //System.out.println(newRule.getRuleString());
//                    if (checkDigramsAreConsecutive(newRule)) {
//                      //  System.out.println(s);
//                        found = newRule.getSymbolString(newRule, false).contains(s); //todo safe to check for s ??? or do searchstring and reverse comple of searchstring
//                    }
//                }
//            }
//            // and if not, search by creating larger rules
//        }
//
//        if (!found) {
//            Rule newRule = new Rule();
//            for (int i = 0; i < searchString.length(); i++) {
//                newRule.addNextSymbol(new Terminal(searchString.charAt(i)));
//            }
//            checkByDigram(newRule);
//            System.out.println(newRule.getRuleString());
//            System.out.println(newRule.getRuleString());
//            if (checkDigramsAreConsecutive(newRule)) {
//                found = newRule.getSymbolString(newRule, false).contains(searchString); //todo safe to check for s ??? or do searchstring and reverse comple of searchstring
//            }
//        }

            StringBuilder reverseComplement = new StringBuilder();
            for (int j = 0; j < searchString.length(); j++) {
                reverseComplement.append(Terminal.reverseSymbol(searchString.charAt(j)));
            }
            reverseComplement.reverse();
            List<String> allSearch = new ArrayList<>();
            allSearch.add(searchString);
            allSearch.add(reverseComplement.toString());
            allSearch.addAll(createSearchString(searchString));
            allSearch.addAll(createSearchString(reverseComplement.toString()));

            //
            for (String s : allSearch) {
                Rule newRule = new Rule();
                for (int i = 0; i < s.length(); i++) {
                    newRule.addNextSymbol(new Terminal(s.charAt(i)));
                }

                checkByDigramRule(newRule);
         //       System.out.println(newRule.getRuleString());
                if (checkDigramsAreConsecutive(newRule)) {
                    System.out.println(newRule.getSymbolString(newRule, false));
                    found = newRule.getSymbolString(newRule, false).contains(searchString);
                }
                if (found) {
                    break;
                }
            }
        }
        return found;
    }

    //todo need to be able to do by digrams too not each symbol
    public void checkByDigramRule(Rule rule) {
        Symbol first = rule.getFirst().getRight();
        while (!first.isGuard()) {
            if (digramMap.containsDigram(first)) {
                Symbol check = digramMap.getOriginalDigram(first);
                if (digramMap.digramIsARule(check)) { // if a rule update digrams
                    createNonTerminal(((Guard)check.getRight()).getGuardRule(), first, check);
                }
                else if (first.equals(rule.getFirst().getRight())) {
                    if (check.getRight().isGuard()) {
                        createNonTerminal(((Guard)check.getRight()).getGuardRule(), first, check);
                    }
                    else {
                        Symbol start = first;

                        //todo needs to be implemented for reverse complements too, notfindingreversecomplementtest
                        // if a match but not an entire rule, check through the rest
                        while (first.equals(check) && !first.isGuard() && !check.isGuard()) {
                            first = first.getRight();
                            check = check.getRight();
                            // if reached the end of the rule and all equal
                            //todo need to be able to stop if reaching end of rule being checked, and get rule from the remaining sequence
                            if ((check.getRight().isGuard()) && first.equals(check)) {
                                replaceSequence(((Guard)check.getRight()).getGuardRule(), start, first);
                            }

                        }
                    }
                    //check next digrams until - todo if nonterminal then what have to make that nonterminal..
                }
                else if (first.equals(rule.getLast())) { // if last
                    if (!first.equals(check)) { // if reverse complement take rest of rule as if first
                        if (check.getRight().isGuard()) {
                            createNonTerminal(((Guard)check.getRight()).getGuardRule(), first, check);
                        }
                    }
                    else { // else if normal take rest of rule as if first
                        if (check.getLeft().getLeft().isGuard()) {
                            createNonTerminal(((Guard) check.getLeft().getLeft()).getGuardRule(), first, check);
                        }
                    }
                }
            }

            // if a match move two right, if not one at a time
            if (!first.isGuard()) {
                first = first.getRight();
            }

            // all with reverse and not reverse
            //if first is first do as above, take all rule if at the end

            // if not first and found keep checking

            // if last and found take rest of rule
        }
    }

    public void replaceSequence(Rule rule, Symbol start, Symbol stop) {
        NonTerminal nonTerminal = new NonTerminal(rule);
        start.getLeft().getLeft().assignRight(nonTerminal);
        stop.getRight().assignLeft(nonTerminal);
        nonTerminal.assignLeft(start.getLeft());
        nonTerminal.assignRight(stop.getRight());
    }

    public void createNonTerminal(Rule rule, Symbol digram, Symbol previousDigram) {
        if (rule.representation != 0) {
            NonTerminal nonTerminal = new NonTerminal(rule);
            // needs to be from complement variable, but then need to assign each new symbol. maybe
            nonTerminal.isComplement = !digram.equals(previousDigram); //true or alternate value, would have to alternate the nonterminal???

            nonTerminal.assignRight(digram.getRight());
            nonTerminal.assignLeft(digram.getLeft().getLeft());

            digram.getLeft().getLeft().assignRight(nonTerminal);
            digram.getRight().assignLeft(nonTerminal);

            //todo check new digrams here?
            //System.out.println("FINDING " + nonTerminal.getLeft() + " " + nonTerminal);
            if (digramMap.containsDigram(nonTerminal)) {
                Symbol check = digramMap.getOriginalDigram(nonTerminal);
                if (digramMap.digramIsARule(check)) {
                    createNonTerminal(((Guard)check.getRight()).getGuardRule(), nonTerminal, digramMap.getOriginalDigram(nonTerminal));
                }
            }
            if (digramMap.containsDigram(nonTerminal.getRight())) {
                Symbol check = digramMap.getOriginalDigram(nonTerminal.getRight());
                if (digramMap.digramIsARule(check)) {
                    createNonTerminal(((Guard)check.getRight()).getGuardRule(), nonTerminal.getRight(), digramMap.getOriginalDigram(nonTerminal.getRight()));
                }
            }
        }

    }

//    public void checkByDigram(Rule rule) {
//        Symbol first = rule.getFirst().getRight();
//        for (int i = 2; i < rule.getRuleLength(); i++) {
//            // if found replace with nonterminal - don't check new digrams till the end
//            System.out.println(first.getLeft() + " " + first);
//            if (digramMap.containsDigram(first) && digramMap.digramIsARule(digramMap.getOriginalDigram(first))) {
//                // create new nonterminal with rule and assign the links
//                System.out.println("why");
//                NonTerminal nonTerminal = new NonTerminal(((Guard) digramMap.getOriginalDigram(first).getRight()).getGuardRule());
//                nonTerminal.isComplement = !first.equals(digramMap.getOriginalDigram(first));
//                first.getLeft().getLeft().assignRight(nonTerminal);
//                first.getRight().assignLeft(nonTerminal);
//                nonTerminal.assignLeft(first.getLeft().getLeft());
//                nonTerminal.assignRight(first.getRight());
//            }
//            if (!digramMap.containsDigram(first)) {
//                first = first.getRight();
//                System.out.println("should ");
//            }
//            else {
//                first = first.getRight().getRight();
//            }
//        }
//    }
//
//    public Rule createSearchRule(String searchString) {
//        Rule newRule = new Rule();
//        newRule.addNextSymbol(new Terminal(searchString.charAt(0)));
//      //  System.out.println("search " + searchString);
//        for (int i = 1; i < searchString.length(); i++) {
//            newRule.addNextSymbol(new Terminal(searchString.charAt(i)));
//           // System.out.println(newRule.getRuleString());
//            checkDigramsToRule(newRule, i == searchString.length()-1);
//        }
//        return newRule;
//    }

    // check they all occur then check they're consecutively separately
    //todo problem is they might not all occur
//    public Boolean checkDigramsOccur(Rule rule) {
//        Symbol first = rule.getFirst().getRight();
//        Boolean found = false;
//        while (!first.isGuard()) {
//            if (digramMap.containsDigram(first)) {
//                found = true;
//            }
//            else {
//                found = false;
//            }
//            if (!found) {break;}
//            first = first.getRight();
//        }
//        return found;
//    }

    public Boolean checkDigramsAreConsecutive(Rule rule) {
        Symbol first = rule.getLast();
        Symbol check;

        //System.out.println(rule.getRuleString());

        // check that last digram is correct
        if (digramMap.containsDigram(first)) {
            if (rule.getRuleLength() == 2) {
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

        if (found) {
            System.out.println("FOUND");
            System.out.println(rule.getRuleString());
        }
        return found;
    }

    public Set<String> createSearchString(String searchString) {
        Set<String> searchStrings = new HashSet<>();
        //System.out.println(uniqueTerminals);
        for (String s : uniqueTerminals) {
            for (String s1 : uniqueTerminals) {
                String s4 = s + searchString + s1;
                searchStrings.add(s4);
                for (String s2 : uniqueTerminals) {
                    for (String s3 : uniqueTerminals) {
                        s4 = s + s1 + searchString + s2 + s3;
                        searchStrings.add(s4);
                    }
                }
            }
        }
        return searchStrings;
    }

}
