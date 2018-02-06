
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindPattern {
    private Map<Integer, String> grammars = new HashMap<>();
    private static final Integer FIRST_RULE = 0;
    Integer nextRule = 1;
    int charIndex = 65;
    String sequence = "";


    public Map<Integer, String> getGrammars() {
        return this.grammars;
    }

    /**
     * add new symbol from input
     *
     * @param NonTerminal
     * @param input
     */
    public void addSymbol(Integer NonTerminal, String input) {
        if (grammars.containsKey(NonTerminal)) {
            grammars.put(NonTerminal, grammars.get(NonTerminal) + input);
        } else {
            grammars.putIfAbsent(NonTerminal, input);
        }
    }

    /**
     * get the last two symbols of input
     *
     * @return
     */
    public String getDigram() {
        return grammars.get(FIRST_RULE).substring(grammars.get(FIRST_RULE).length() - 2);
    }

    public Integer checkForPattern(String digram) {
        Integer count = 0;
        for (int i = 0; i < grammars.get(FIRST_RULE).length() - 1; i++) {
            if (grammars.get(FIRST_RULE).substring(i, i + 2).equals(digram)) {
                count++;
            }
        }

        return count;
    }

    public Integer checkForFoundPattern(String digram) {
        // because S is part of the same map have to check al lbut one... replace S as a string on its own??
        Integer symbol = FIRST_RULE;
        for (Integer s : grammars.keySet()) {
            if (s != FIRST_RULE) {
                if (grammars.get(s).equals(digram)) {
                    symbol = s;
                }
            }
        }
        return symbol;
    }

    public void updateSequenceWithFoundPattern() {
        String digram = getDigram();
        // check for pattern in previous found patterns
        Integer s = checkForFoundPattern(digram);
        if (s != FIRST_RULE) {
            grammars.put(FIRST_RULE, grammars.get(FIRST_RULE).replaceAll(digram, s.toString()));
        }
    }

    public void updateSequence() {
        String digram = getDigram();
        // check for pattern within itself and update if necessary
        if (checkForPattern(digram) > 1) {
            grammars.put(FIRST_RULE, grammars.get(FIRST_RULE).replaceAll(digram, nextRule.toString()));
            grammars.put(nextRule, digram); // made a new rule
            nextRule++; //update next grammar symbol
        }

    }

    public void updateGrammar(String input) {
        addSymbol(FIRST_RULE, input.substring(0, 1));
        for (int i = 1; i < input.length(); i++) {
            addSymbol(FIRST_RULE, input.substring(i, i + 1));

            updateSequenceWithFoundPattern();
            updateSequence();

            // for all rules only used once where they are in the value replace with their terminal (should only happen once... so need for for loops?)
            for (Integer s : ruleUtility()) { // for every item in list of only occuring once terminals
                for (Integer k : grammars.keySet()) { // check to all other entries
                    if (grammars.get(k).contains(s.toString())) {
                        grammars.put(k, grammars.get(k).replaceAll(s.toString(), grammars.get(s))); // replace with the nonterminal
                    }
                }
                grammars.remove(s); // safe to have this here? or have a separate removal aspect.
            }

            // need a real way to check for duplicates and remove them, need to do a loop on
            // new symbol whether from input or new digram...
            for (Integer k : grammars.keySet()) {
                for (Integer sk : grammars.keySet()) {
                    if (grammars.get(k).equals(grammars.get(sk))
                            && k != sk) {
                        grammars.put(FIRST_RULE, grammars.get(FIRST_RULE).replaceAll(sk.toString(), k.toString()));
                    }
                }
            }

            updateSequenceWithFoundPattern();
            updateSequence();

            System.out.println(grammars.toString());
        }

    }

    private List<Integer> ruleUtility() {
        int count = 0;
        List<Integer> one = new ArrayList<>();

        // for every key check how many times they occur in other locations
        for (Integer s : grammars.keySet()) { // key
            for (String v : grammars.values()) { // every value
                for (int i = 0; i < v.length(); i++) { // length of value
                    if (v.substring(i, i + 1).equals(s.toString())) {
                        count++; // how many times occur
                    }
                }
            }
            if (count < 2 && s != FIRST_RULE) { // if only happens once
                one.add(s); // add to the list
            }
            count = 0; // reset for next terminal check
        }

        return one; // return list
    }
}
