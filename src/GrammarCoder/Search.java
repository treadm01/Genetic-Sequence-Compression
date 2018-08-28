package GrammarCoder;

import java.util.*;

public class Search {
    Map<Character, Set<NonTerminal>> rulesByStartSymbols = new HashMap<>();
    Map<Character, Set<NonTerminal>> rulesByEndSymbols = new HashMap<>();
    Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol

//    if (!rulesByStartSymbols.containsKey(input.charAt(i))) {
//        rulesByStartSymbols.putIfAbsent(input.charAt(i), new HashSet());
//        rulesByEndSymbols.putIfAbsent(input.charAt(i), new HashSet());
//        rulesByStartSymbols.putIfAbsent(Terminal.reverseSymbol(input.charAt(i)), new HashSet<>());
//        rulesByEndSymbols.putIfAbsent(Terminal.reverseSymbol(input.charAt(i)), new HashSet<>());
//    }

    public Search(Map<Symbol, Symbol> digramMap) {
        this.digramMap = digramMap;
    }

    public void initRuleBySymbols(Set<String> digrams) {
        String uniqueSymbols = "";
        for (String s : digrams) {
            uniqueSymbols += s.substring(0,1);
        }

        System.out.println(uniqueSymbols);

        for (int i = 0; i < uniqueSymbols.length(); i++) {
            if (!rulesByStartSymbols.containsKey(uniqueSymbols.charAt(i))) { // only if not in start???
                rulesByStartSymbols.putIfAbsent(uniqueSymbols.charAt(i), new HashSet());
                rulesByEndSymbols.putIfAbsent(uniqueSymbols.charAt(i), new HashSet());
                rulesByStartSymbols.putIfAbsent(Terminal.reverseSymbol(uniqueSymbols.charAt(i)), new HashSet<>());
                rulesByEndSymbols.putIfAbsent(Terminal.reverseSymbol(uniqueSymbols.charAt(i)), new HashSet<>());
            }
        }
    }

    public void createRuleBySymbols(Set<Rule> rules) {
        for (Rule r : rules) {
            if (r.representation != 0) {
                // regular
                String s = r.getSymbolString(r, r.isComplement);

                NonTerminal regular = new NonTerminal(r); // todo what about rule count etc?
                regular.assignLeft(new Terminal('!'));
                rulesByStartSymbols.get(s.charAt(0)).add(regular);
                rulesByEndSymbols.get(s.charAt(s.length() - 1)).add(regular);


                NonTerminal isComplement = new NonTerminal(r);
                isComplement.assignLeft(new Terminal('!'));
                isComplement.isComplement = true;
                rulesByEndSymbols.get(Terminal.reverseSymbol(s.charAt(0))).add(isComplement);
                // missing a symbol
                rulesByStartSymbols.get(Terminal.reverseSymbol(s.charAt(s.length() - 1))).add(isComplement);
            }
        }
    }

    public Set<String> createSearchStringDigrams(String searchString) {
        Set<String> digramStrings = new LinkedHashSet<>();
        // get all unique digrams of the search string
        for (int i = 0; i < searchString.length() - 1; i++) {
            String s = searchString.substring(i, i + 2);
            digramStrings.add(s);
        }
        return digramStrings;
    }

    public Map<String, List<Rule>> getPossibleDigrams(Set<String> digramStrings) {
        // find the digrams in the grammar that correspond to those in the search string
        // all possible combinations of terminals and nonterminals todo though not reverse quite...
        List<Rule> foundRule;
        Map<String, List<Rule>> stringRules = new LinkedHashMap<>();
        for (String s : digramStrings) {
            foundRule = createSearchDigram(s.charAt(0), s.charAt(1));
            stringRules.put(s, foundRule);
        }
        return stringRules;
    }


    //get every instance of first digram
    public Boolean search(String searchString) {
        Boolean found = false;
        Set<String> digramStrings = createSearchStringDigrams(searchString);
        Map<Integer, List<Rule>> searchRules = new LinkedHashMap<>();
        Map<String, List<Rule>> stringRules = getPossibleDigrams(digramStrings);

        if (searchString.length() == 1) {
            return rulesByStartSymbols.containsKey(searchString.charAt(0));
        }

        // adding possible options for each digram in search string
        List<String> digramS = new ArrayList<>();
        for (int i = 0; i < searchString.length() - 1; i++) {
            String s = searchString.substring(i, i + 2);
            digramS.add(s);
            List<Rule> ruleList = new ArrayList<>();
            ruleList.addAll(stringRules.get(s));
            searchRules.put(i, ruleList);
        }

        for (int i = 0; i < searchRules.size(); i++) {
            String s = searchString.substring(i + 1);
            searchRules.get(i).removeAll(checkPossibility(searchRules.get(i), searchString, s));
            for (Rule r : searchRules.get(i)) {
                if (r.getSymbolString(r, false).contains(searchString)) {
                    found = r.getSymbolString(r, false).contains(searchString);
                    break;
                }
            }
        }

//        System.out.println("REMOVE SUB - still remaining: ");
//        for (List<Rule> ruleList : searchRules.values()) {
//            for (Rule r : ruleList) {
//                System.out.print(r.getRuleString());
//                System.out.print( " " + r.getSymbolString(r, false));
//            }
//            System.out.println();
//        }

        // check first and last, if digram has no further links either side then remove

        Set<String> leftHand = new HashSet<>();
        Set<String> rightHand = new HashSet<>();
        for (List<Rule> ruleList : searchRules.values()) {
            for (Rule r : ruleList) {
                leftHand.add(r.getFirst().toString());
                rightHand.add(r.getLast().toString());
            }
        }

//        System.out.println("REMOVE UNCONNECTING");
//        for (List<Rule> ruleList : searchRules.values()) {
//            for (Rule r : ruleList) {
//                System.out.print(r.getRuleString());
//                System.out.print( " " + r.getSymbolString(r, false));
//            }
//            System.out.println();
//        }

        List<Rule> removeRule = new ArrayList<>();
        List<Rule> possibleRule = new ArrayList<>();
        for (List<Rule> ruleList : searchRules.values()) {
            for (Rule r : ruleList) {
                if (!leftHand.contains(r.getLast().toString())
                        && !rightHand.contains(r.getFirst().toString())) {
                    removeRule.add(r);
                }
            }
            ruleList.removeAll(removeRule);
            possibleRule.addAll(ruleList);
        }

//        System.out.println("REMOVED DIGRAMS");
//        for (List<Rule> ruleList : searchRules.values()) {
//            System.out.println("different digram");
//            for (Rule r : ruleList) {
//                System.out.print(r.getRuleString());
//                System.out.print( " " + r.getSymbolString(r, false));
//            }
//            System.out.println();
//        }


// get digram from main rule, then move right from there, check if digrams occur in those found (create hash of htem)
// build rule as you go, check string
        Map<Symbol, Symbol> searchDigramMap = new HashMap<>();
        for (Rule r : possibleRule) {
            searchDigramMap.put(r.getLast(), r.getLast());
//            searchDigramMap.putIfAbsent(getReverseComplement(r.getLast()), getReverseComplement(r.getLast()));
        }

        List<Rule> morePossible = new ArrayList<>();
        for (Rule r : possibleRule) {
            Symbol currentSymbol = digramMap.get(r.getLast()); //todo - make a more accessible methodgetOriginalDigram(r.getLast());
            Boolean complement = currentSymbol.getRepresentation() != r.getLast().getRepresentation();
            System.out.println(complement);
            currentSymbol = currentSymbol.getRight();
            //todo - reverse complement issues other issues... needs cleaning and making sure it works with just a grammar
//            if (!complement) {
//                currentSymbol = currentSymbol.getRight();
//            }
//            else {
//                currentSymbol = currentSymbol.getLeft();
//                currentSymbol = getReverseComplement(currentSymbol); // use an actual get method, rather than create new each time
//            }
//
//            System.out.println("actual digram " + r.getFirst() + " " + r.getLast());
//            System.out.println("from map " + currentSymbol.getLeft() + " " + currentSymbol);
            Rule newRule = new Rule();
            newRule.addAllSymbols(r.getFirst());
            while (searchDigramMap.containsKey(currentSymbol)) {
                Symbol getNonModifiable = searchDigramMap.get(currentSymbol);
                newRule.addAllSymbols(getNonModifiable);
                currentSymbol = currentSymbol.getRight();
            }
            morePossible.add(newRule); // same problem this will only check one route
        }

        // finding components, registered as in the grammar because of reverse complements etc
        // but need to check reverse tooo....
        // better to check earlier....
        // if 6 was > cag would be found first
        // im not sure you do find them... just looking for two symbols
        // because if it exists it will be found elsewhere
        for (Rule r : morePossible) {
            System.out.print(r.getRuleString());
            System.out.print(" " + r.getSymbolString(r, false));
        }

        for (Rule r : morePossible) {
            if (r.getSymbolString(r, false).contains(searchString))
            {
                found = true;
                break;
            }
        }

        return found;
    }


    // you want to have multiple instances of the same links, but be able to check to unique digrams
    public List<Rule> createSearchDigram(Character leftSymbol, Character rightSymbol) {
        List<Rule> newRules = new ArrayList<>();
        // joint standard terminals
        //todo reverse complements??
        Terminal left = new Terminal(leftSymbol);
        Terminal right = new Terminal(rightSymbol);
        right.assignLeft(left);
        left.assignRight(right);

        //todo need the exisiting rule check
        newRules.addAll(checkIfExisting(left, right));

        // join nonterminals ending in left with terminal next
        for (NonTerminal nt : rulesByEndSymbols.get(leftSymbol)) {
            NonTerminal n = new NonTerminal(nt.getRule());
            n.isComplement = nt.isComplement;
            Terminal ntright = new Terminal(rightSymbol);
            ntright.assignLeft(n);
            n.assignRight(ntright);
            newRules.addAll(checkIfExisting(n, ntright));
        }

        //join nonterminal right with standard terminal left
        for (NonTerminal nt : rulesByStartSymbols.get(rightSymbol)) {
            NonTerminal n = new NonTerminal(nt.getRule());
            n.isComplement = nt.isComplement;
            Terminal ntleft = new Terminal(leftSymbol);
            n.assignLeft(ntleft);
            ntleft.assignRight(n);
            newRules.addAll(checkIfExisting(ntleft, n));
        }
//
        for (NonTerminal ntl : rulesByEndSymbols.get(leftSymbol)) {
            for (NonTerminal ntr : rulesByStartSymbols.get(rightSymbol)) {
                NonTerminal nl = new NonTerminal(ntl.getRule());
                nl.isComplement = ntl.isComplement;
                NonTerminal nr = new NonTerminal(ntr.getRule());
                nr.isComplement = ntr.isComplement;
                nr.assignLeft(nl);
                nl.assignRight(nr);
                newRules.addAll(checkIfExisting(nl, nr));
            }
        }
        // join two nonterminals
        return newRules;
    }

    public List<Rule> checkIfExisting(Symbol left, Symbol right) {
        //todo need the exisiting rule check
        List<Rule> ruleList = new ArrayList<>();
        //System.out.println("SYMBOL " + left + " " + right);

        if (digramMap.containsKey(right)) {
            //  System.out.println("WELL ? " + left + " " + right);
            Rule nRule = new Rule();
            Symbol real = digramMap.get(right);
            nRule.isComplement = real.getRight() == null; // indicated whether the existing is a complement
            nRule.addAllSymbols(left);
            ruleList.add(nRule);
        }
        return ruleList;
    }

    // might not be that you get it in hte first digram so don't create existing rule
    // get digrams and checl if they contain the digram and following oncontain the rule
    public List<Rule> checkPossibility(List<Rule> rules, String searchString, String remainingString) {
        List<Rule> removeRules = new ArrayList<>();
        int cut = searchString.length() - remainingString.length();
        String firstHalf = searchString.substring(0, cut);
        // todo what about if both are nonterminal...
        for (Rule r : rules) {
            if (r.getRuleLength() == 1) {
                // this code is same as that used in nonterminal check needs refactoring
                String ruleString = r.getSymbolString(r, r.isComplement);
                ruleString = ruleString.substring(2);
                int lowest = ruleString.length();
                if (remainingString.length() < lowest) {
                    lowest = remainingString.length();
                }

                for (int j = 0; j < lowest; j++) {
                    // might be getting to end of searchstring length???
                    if (ruleString.charAt(j) != remainingString.charAt(j)) {
                        removeRules.add(r);
                        break;
                    }
                }

            }
            if (r.getFirst() instanceof NonTerminal && firstHalf.length() > 1) {
                // same code as below again
                Rule ntRule = ((NonTerminal) r.getFirst()).getRule();
                String ruleString = ntRule.getSymbolString(ntRule, r.getFirst().isComplement);

                int lowest = firstHalf.length();
                if (ruleString.length() < lowest) {
                    lowest = ruleString.length();
                }

                //todo clean, reverse strings to more accurately check
                StringBuilder sb = new StringBuilder();
                sb.append(ruleString);
                sb.reverse();
                StringBuilder sbf = new StringBuilder();
                sbf.append(firstHalf);
                sbf.reverse();
                // need to check from the end of string backwards
                for (int j = 0; j < lowest; j++) { //lowest - 1 for string length... why not for last?
                    if (sb.charAt(j) != sbf.charAt(j)) {
                        removeRules.add(r);
                        break;
                    }
                }

            }
            if (r.getLast() instanceof NonTerminal && remainingString.length() > 1) {
                //todo need to account for reverse complement too
                Rule ntRule = ((NonTerminal) r.getLast()).getRule();
                String ruleString = ntRule.getSymbolString(ntRule, r.getLast().isComplement);
                int lowest = ruleString.length();
                if (remainingString.length() < lowest) {
                    lowest = remainingString.length();
                }
                // best to check rule string length? when checking the last digram wont be
                for (int j = 0; j < lowest; j++) {
                    // might be getting to end of searchstring length???
                    if (ruleString.charAt(j) != remainingString.charAt(j)) {
                        removeRules.add(r);
                        break;
                    }
                }
            }
        }
        return removeRules;
    }

}
