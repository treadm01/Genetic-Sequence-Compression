package GrammarCoder;

import java.util.*;

public class Search {
    private Map<Character, Set<NonTerminal>> rulesByStartSymbols = new HashMap<>();
    private Map<Character, Set<NonTerminal>> rulesByEndSymbols = new HashMap<>();
    private DigramMap digramMap; // - digram points to digram via right hand symbol
    private Set<Rule> rules;
    private Set<Rule> rulesFromGrammar = new HashSet<>();
    Set<String> uniqueSymbols;

    public Search(Rule firstRule, Set<Rule> rules) {
        uniqueSymbols = new HashSet<>();
        rulesFromGrammar.add(firstRule);
        generateRules(firstRule.getFirst());
        this.rules = rulesFromGrammar;
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

    public DigramMap createDigramMap(Rule firstRule) {
        DigramMap dm = new DigramMap();
        generateRules(firstRule.getFirst()); //todo needs unduplicating
        for (Rule r : rulesFromGrammar) {
            Symbol firstRight = r.getFirst().getRight();
            while (!firstRight.isGuard()) {
                dm.addNewDigrams(firstRight);
                uniqueSymbols.add(firstRight.getLeft().toString());
                uniqueSymbols.add(String.valueOf(Terminal.reverseSymbol(firstRight.getLeft().toString().charAt(0))));
                firstRight = firstRight.getRight();
            }
        }
        return dm;
    }

    public void initRuleBySymbols(String searchString) {
        Set<Character> uniqueSymbols = new HashSet<>();
        for (int i = 0; i < searchString.length(); i++) {
            uniqueSymbols.add(searchString.charAt(i));
        }

        System.out.println(uniqueSymbols);

        for (Character c : uniqueSymbols) {
            rulesByStartSymbols.putIfAbsent(c, new HashSet());
            rulesByEndSymbols.putIfAbsent(c, new HashSet());
            rulesByStartSymbols.putIfAbsent(Terminal.reverseSymbol(c), new HashSet<>());
            rulesByEndSymbols.putIfAbsent(Terminal.reverseSymbol(c), new HashSet<>());
        }
    }

    public void createRuleBySymbols() {
        for (Rule r : rules) {
            //if (r.representation != 0) {
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
            return uniqueSymbols.contains(searchString)
                    || !rulesByStartSymbols.get(searchString.charAt(0)).isEmpty()
                    || !rulesByEndSymbols.get(searchString.charAt(0)).isEmpty();
        }

        //todo edit grammars...... might not get returned... no, edits, should be dealt with in certain instances...

    // get digram from main rule, then move right from there, check if digrams occur in those found (create hash of htem)
    // build rule as you go, check string
        Map<Symbol, Symbol> searchDigramMap = new HashMap<>();
        for (List<Rule> ruleList : searchRules.values()) {
            for (Rule r : ruleList) {
                searchDigramMap.put(r.getLast(), r.getLast());
                searchDigramMap.putIfAbsent(r.getLast().getReverseComplement(), r.getLast().getReverseComplement());
            }
        }

//        for (Symbol s : digramMap.values()) {
//            System.out.print(s.getLeft() + " " + s + " / ");
//        }
//
//        System.out.println();
//        for (Symbol s : searchDigramMap.values()) {
//            System.out.print(s.getLeft() + " " + s + " / ");
//        }

        // hav to link trhgouh and maintain both, if next does not match the thing looking for then complement
        // if it does revert to normal
        List<Rule> morePossible = new ArrayList<>();
        for (List<Rule> ruleList : searchRules.values()) {
            for (Rule r : ruleList) {
                Symbol currentSymbol = digramMap.getOriginalDigram(r.getLast()); //todo - make a more accessible methodgetOriginalDigram(r.getLast());
                Boolean complement = currentSymbol.getRepresentation() != r.getLast().getRepresentation();
                Rule newRule = new Rule();
                newRule.addAllSymbols(r.getFirst()); // but what to add??? add whatever and set complement???

                if (!complement) {
                    currentSymbol = currentSymbol.getRight();
                }
                else {
                    currentSymbol = currentSymbol.getLeft();
                }

//                System.out.println(r.getFirst() + " " + r.getLast());
//                System.out.println(complement);
//                System.out.println(currentSymbol);
//                System.out.println();

                while (searchDigramMap.containsKey(currentSymbol)) {
                    Symbol getNonModifiable = searchDigramMap.get(currentSymbol); // would need to flip complement back??
                    // if complement need a way to add just the left symbol, no just add last of reverse
                    if (!complement) { // just this for standard noncomplement
                        newRule.addAllSymbols(getNonModifiable);
            //            System.out.println(newRule.getRuleString());
                        currentSymbol = currentSymbol.getRight();
                    }
                    else { // trying to get reverse complement checks..... todo seems to work mostly.... but not sure for the right reasons
                        newRule.addAllSymbols(getNonModifiable.getReverseComplement()); // wont work as add all will add all
                        currentSymbol = currentSymbol.getLeft();
                        //complement = currentSymbol.getRepresentation() != newRule.getLast().getRepresentation();
                    }
                }
                morePossible.add(newRule); // same problem this will only check one route
            }
        }

        List<Rule> doubleCheck = new ArrayList<>(); // having to remove some more not in list, todo better way
        for (Rule r : morePossible) {
            Symbol s = r.getFirst().getRight();
            while (!s.isGuard()) {
                if (digramMap.existingDigram(s)) {
                    s = s.getRight();
                }
                else {
                    System.out.println("no");
                    doubleCheck.add(r);
                    break;
                }
            }
        }

        morePossible.removeAll(doubleCheck);

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


    //todo clean up
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

        if (digramMap.existingDigram(right)) {
            //  System.out.println("WELL ? " + left + " " + right);
            Rule nRule = new Rule();
            Symbol real = digramMap.getExistingDigram(right);
            nRule.isComplement = real.getRight() == null; // indicated whether the existing is a complement
            nRule.addAllSymbols(left);
            ruleList.add(nRule);
        }
        return ruleList;
    }

}
