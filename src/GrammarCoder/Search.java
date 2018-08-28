package GrammarCoder;

import java.util.*;

public class Search {
    Map<Character, Set<NonTerminal>> rulesByStartSymbols = new HashMap<>();
    Map<Character, Set<NonTerminal>> rulesByEndSymbols = new HashMap<>();
    Map<Symbol, Symbol> digramMap; // - digram points to digram via right hand symbol
    Set<Rule> rules;
    Map<String, String> ruleStringMap = new HashMap<>();

    public Search(Map<Symbol, Symbol> digramMap, Set<Rule> rules) {
        this.digramMap = digramMap;
        this.rules = rules;
    }

    public void initRuleBySymbols(String searchString) {
        Set<Character> uniqueSymbols = new HashSet<>();
        for (int i = 0; i < searchString.length(); i++) {
            uniqueSymbols.add(searchString.charAt(i));
        }

        for (Character c : uniqueSymbols) {
            rulesByStartSymbols.putIfAbsent(c, new HashSet());
            rulesByEndSymbols.putIfAbsent(c, new HashSet());
            rulesByStartSymbols.putIfAbsent(Terminal.reverseSymbol(c), new HashSet<>());
            rulesByEndSymbols.putIfAbsent(Terminal.reverseSymbol(c), new HashSet<>());
        }
    }

    public void createRuleBySymbols() {
        for (Rule r : rules) {
            if (r.representation != 0) {
                // regular
                String s = r.getSymbolString(r, r.isComplement);
                Character start = s.charAt(0);
                Character end = s.charAt(s.length() - 1);

                // need to check that the symbols have been added
                NonTerminal regular = new NonTerminal(r); // todo what about rule count etc?
                regular.assignLeft(new Terminal('!'));
                if (rulesByStartSymbols.containsKey(start)) {
                    rulesByStartSymbols.get(start).add(regular);
                }
                if (rulesByEndSymbols.containsKey(end)) {
                    rulesByEndSymbols.get(end).add(regular);
                }

                start = Terminal.reverseSymbol(start);
                end = Terminal.reverseSymbol(end);
                NonTerminal isComplement = new NonTerminal(r);
                isComplement.assignLeft(new Terminal('!'));
                isComplement.isComplement = true;

                if (rulesByEndSymbols.containsKey(start)) {
                    rulesByEndSymbols.get(start).add(isComplement);
                }
                if (rulesByStartSymbols.containsKey(end)) {
                    rulesByStartSymbols.get(end).add(isComplement);
                }
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
        List<Rule> foundRule; // make a set???
        Map<String, List<Rule>> stringRules = new LinkedHashMap<>();
        for (String s : digramStrings) {
            foundRule = createSearchDigram(s.charAt(0), s.charAt(1));
            stringRules.put(s, foundRule);
        }
        return stringRules;
    }

    public Map<Integer, List<Rule>> possibleDigramsForSearchString(String searchString, Map<String, List<Rule>> stringRules) {
        // adding possible options for each digram in search string
        Map<Integer, List<Rule>> searchRules = new HashMap<>();
        for (int i = 0; i < searchString.length() - 1; i++) {
            String s = searchString.substring(i, i + 2);
            List<Rule> ruleList = new ArrayList<>(); // so creating a map of possible options for each digram
            ruleList.addAll(stringRules.get(s));
            searchRules.put(i, ruleList);
        }
        return searchRules;
    }


    // sending searchRule as argument means wont be updated....
    // removes all digrams that cannot contain the rule
//    public void checkEachDigramForPossibleString(String searchString, Map<Integer, List<Rule>> searchRules) {
//        for (int i = 0; i < searchRules.size(); i++) {
//            String s = searchString.substring(i + 1);
//            searchRules.get(i).removeAll(checkPossibility(searchRules.get(i), searchString, s));
//        }
//    }

    //get every instance of first digram
    public Boolean search(String searchString) {
        Boolean found = false;
        Set<String> digramStrings = createSearchStringDigrams(searchString);
        // then create rules by start symbols
        initRuleBySymbols(searchString);
        createRuleBySymbols();

        Map<String, List<Rule>> stringRules = getPossibleDigrams(digramStrings);
        Map<Integer, List<Rule>> searchRules = possibleDigramsForSearchString(searchString, stringRules);

        // search for one symbol .... can be checked after createrulebysymbols
        if (searchString.length() == 1) {
            System.out.println(rulesByStartSymbols.keySet());
            return rulesByStartSymbols.containsKey(searchString.charAt(0));
        }

//        //reomve non linking ones here?
//        Set<String> leftHand = new HashSet<>();
//        Set<String> rightHand = new HashSet<>();
//        for (List<Rule> ruleList : searchRules.values()) {
//            for (Rule r : ruleList) {
//                leftHand.add(r.getFirst().toString());
//                rightHand.add(r.getLast().toString());
//            }
//        }
//
//        List<Rule> remove = new ArrayList<>();
//        for (List<Rule> ruleList : searchRules.values()) {
//            for (Rule r : ruleList) {
//                if (!leftHand.contains(r.getLast().toString()) && !rightHand.contains(r.getFirst().toString())) {
//                    remove.add(r);
//                }
//            }
//            ruleList.removeAll(remove);
//        }

//        checkEachDigramForPossibleString(searchString, searchRules);

        //todo edit grammars...... might not get returned... no, edits, should be dealt with in certain instances...

    // get digram from main rule, then move right from there, check if digrams occur in those found (create hash of htem)
    // build rule as you go, check string
        Map<Symbol, Symbol> searchDigramMap = new HashMap<>();
        for (List<Rule> ruleList : searchRules.values()) {
            for (Rule r : ruleList) {
                searchDigramMap.put(r.getLast(), r.getLast());
//            searchDigramMap.putIfAbsent(getReverseComplement(r.getLast()), getReverseComplement(r.getLast()));
            }
        }

        List<Rule> morePossible = new ArrayList<>();
        for (List<Rule> ruleList : searchRules.values()) {
            for (Rule r : ruleList) {
                Symbol currentSymbol = digramMap.get(r.getLast()); //todo - make a more accessible methodgetOriginalDigram(r.getLast());
                Boolean complement = currentSymbol.getRepresentation() != r.getLast().getRepresentation();
                //System.out.println(complement);
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
        }

        // add to a list, set etc then keep each occurence... then would have to check all rather than break...
        // get index from digram map
        for (Rule r : morePossible) {
            if (r.getSymbolString(r, false).contains(searchString))
            {
                found = true;
                System.out.println(r.getRuleString());
                //break;
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
//    public List<Rule> checkPossibility(List<Rule> rules, String searchString, String remainingString) {
//        List<Rule> removeRules = new ArrayList<>();
//        int cut = searchString.length() - remainingString.length();
//        String firstHalf = searchString.substring(0, cut);
//        // todo what about if both are nonterminal...
//        for (Rule r : rules) {
//            if (r.getFirst() instanceof NonTerminal && firstHalf.length() > 1) {
//                Rule ntRule = ((NonTerminal) r.getFirst()).getRule();
//                String ruleString;
//                if (ruleStringMap.containsKey(r.getFirst().toString())) {
//                    ruleString = ruleStringMap.get(r.getFirst().toString());
//                } else {
//                    ruleString = ntRule.getSymbolString(ntRule, r.getFirst().isComplement);
//                    ruleStringMap.putIfAbsent(r.getFirst().toString(), ruleString);
//                }
//                int lowest = firstHalf.length();
//                if (ruleString.length() < lowest) {
//                    lowest = ruleString.length();
//                }
//                //todo clean, reverse strings to more accurately check
//                StringBuilder sb = new StringBuilder();
//                sb.append(ruleString);
//                sb.reverse();
//                StringBuilder sbf = new StringBuilder();
//                sbf.append(firstHalf);
//                sbf.reverse();
//                // need to check from the end of string backwards
//                for (int j = 0; j < lowest; j++) { //lowest - 1 for string length... why not for last?
//                    if (sb.charAt(j) != sbf.charAt(j)) {
//                        removeRules.add(r);
//                        break;
//                    }
//                }
//            }
//            if (r.getLast() instanceof NonTerminal && remainingString.length() > 1
//                    && !removeRules.contains(r)) {
//                //todo need to account for reverse complement too
//                Rule ntRule = ((NonTerminal) r.getLast()).getRule();
//                String ruleString;
//                if (ruleStringMap.containsKey(r.getLast().toString())) {
//                    ruleString = ruleStringMap.get(r.getLast().toString());
//                }
//                else {
//                    ruleString = ntRule.getSymbolString(ntRule, r.getLast().isComplement);
//                    ruleStringMap.putIfAbsent(r.getLast().toString(), ruleString);
//                }
//                int lowest = ruleString.length();
//                if (remainingString.length() < lowest) {
//                    lowest = remainingString.length();
//                }
//                // best to check rule string length? when checking the last digram wont be
//                for (int j = 0; j < lowest; j++) {
//                    // might be getting to end of searchstring length???
//                    if (ruleString.charAt(j) != remainingString.charAt(j)) {
//                        removeRules.add(r);
//                        break;
//                    }
//                }
//            }
//        }
////        System.out.println("seen " + seen);
////        System.out.println("count " + count);
////        System.out.println("rules given " + rules);
////        System.out.println("remove rules " + removeRules.toString());
//     return removeRules;
//    }
}
