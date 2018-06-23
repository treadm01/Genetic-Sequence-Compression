import java.util.*;
import java.util.stream.Collectors;

/**
 * intend to do most of the work here, not sure if will be possible
 * keeps a list of the rules, but then how to search that by rule number?
 * and how to access the values stored in the rules?
 */
public class Compress {
    Map<String, Terminal> terminals = new HashMap<>();
    Map<Integer, NonTerminal> nonTerminals = new HashMap<>();
    Map<Digram, List<Integer>> digramIndex = new HashMap<>();

    /**
     * Main method to take string of the input, and run through the symbol,
     * currently holds re-order and prints out the rules at the end, returns the
     * list too
     * @param input
     * @return
     */
    public List<NonTerminal> processInput(String input) {
        //TODO decide where reorder will go, manage where things are created
        NonTerminal firstNTRule = new NonTerminal(); // the 0 rule initial rule
        String ch;
        nonTerminals.put(0, firstNTRule);
        for (int i = 0; i < input.length(); i++) {
            System.out.println("working through symbol " + i + " of " + input.length());

            // add the element to string to first rule
            ch = input.substring(i, i+1);

            terminals.putIfAbsent(ch, new Terminal(ch)); // how to do???

            firstNTRule.addValues(terminals.get(ch)); // created a new terminal,... shouldn't be hidden like that

            threeRule(firstNTRule);
        }

        // reorder rules here after they've been formed... improves compression
        //reorderRules(firstNTRule);

        // print out the final values - // just for debugging
        List<NonTerminal> finalRules = new ArrayList<>();
        for (NonTerminal r : nonTerminals.values()) {
            finalRules.add(r);
        }
        printRules(finalRules);

        return finalRules;
    }

    /**
     * main three rules that repeatedly run over the symbol stream
     * to find hierarchical rules
     * @param firstRule
     */
    public void threeRule(NonTerminal firstRule) {
        //TODO implement this without String
        String checkValues = "";
        String currentValues = firstRule.getValues();
        // repeat the methods until nothing changes/needs changing
        while (!currentValues.equals(checkValues)) {
            currentValues = firstRule.getValues();
            checkRepeat(firstRule); // check for pattern in first rule
            // if bigram in another rule, update first rule again
            existingBigram(firstRule);
            ruleUtility(firstRule);// remove any rules that are only used once
            checkValues = firstRule.getValues(); // assign new first rule to the check
        }
    }

    /**
     * using the most recent digram of main input, check for
     * repeats through out the grammar
     * @param fr
     */
    public void checkRepeat(NonTerminal fr) {
        if (fr.checkDigram()) {
            NonTerminal newRule = new NonTerminal();
            newRule.addValues(fr.getCurrentDigram()); //TODO would want to update this after
            nonTerminals.put(newRule.number, newRule);
           // System.out.println("RULES HERE " + nonTerminals);
            if (!digramIndex.containsKey(fr.currentDigram)) {
                List<Integer> index = new ArrayList<>();
                index.add(newRule.number);
                digramIndex.put(fr.currentDigram, index);
            }
            else {
                digramIndex.get(fr.currentDigram).add(digramIndex.get(fr.currentDigram).size(),
                        newRule.number);
            }
            fr.updateRule(newRule); //TODO update the left and right of elements here
        }
    }


    /**
     * check through the rules created for patterns that have already occurred.
     * Updates those rules with new non terminals and updates the original rule
     * from existing rules (rather than create a new one)
     * @param fr
     */
    public void existingBigram(NonTerminal fr) {
        //TODO need to get hashset index for this
        List<NonTerminal> newRuleLst = new ArrayList<>();
//        System.out.println("current bigram " + fr.getCurrentDigram());
//        System.out.println("digram index " + digramIndex);
//        System.out.println("NON TERMINALS " + nonTerminals);

        //printout
//        List<NonTerminal> finalRules = new ArrayList<>();
//        for (NonTerminal r : nonTerminals.values()) {
//            finalRules.add(r);
//        }
//        printRules(finalRules);

        // TODO not doing enough loops.... there's an a 15 that should be found as repeat in
        // TODO 17, create a new rule and update
        if (digramIndex.containsKey(fr.getCurrentDigram())) {
            //System.out.println("current bigram " + fr.getCurrentDigram());
            //System.out.println("CONTAINED IN DIGRAMAP " + digramIndex);
            for (Integer i : digramIndex.get(fr.getCurrentDigram())) {
                if (nonTerminals.get(i) != null) {
          //          System.out.println("GO TO BED " + nonTerminals.get(i).values);
                    if (nonTerminals.get(i).values.size() == 2) { // what is this 2??
                        fr.updateRule(nonTerminals.get(i));
                    }
                    else {
                        NonTerminal newRule = new NonTerminal();
                        newRule.addValues(fr.getCurrentDigram()); // how to get bigram from first rule??
                        newRuleLst.add(newRule);
                        fr.updateRule(newRule);
                        nonTerminals.get(i).updateRule(newRule);
                    }
                }
            }
        }

        //System.out.println("DONE A LOOP");
//
//        for (NonTerminal r : nonTerminals.values()) {
////            System.out.println("R " + r);
////            System.out.println("R - bigrams " + r.getAllBigrams());
////            System.out.println(fr.getCurrentDigram());
//            if (r.getAllBigrams().contains(fr.getCurrentDigram())) {
//                if (r.values.size() == 2) {
//                    fr.updateRule(r);
//                }
//                else {
//                    System.out.println("hello");
//                    NonTerminal newRule = new NonTerminal();
//                    newRule.addValues(fr.getCurrentDigram()); // how to get bigram from first rule??
//                    newRuleLst.add(newRule);
//                    fr.updateRule(newRule);
//                    r.updateRule(newRule);
//                }
//            }
//        }

        // add the newly created rules to main list
        for (NonTerminal r : newRuleLst) {
            nonTerminals.put(r.number, r);
            //System.out.println("r numbers " + r.number);
//            if (!digramIndex.containsKey(fr.currentDigram.second)) {
//                List<Integer> index = new ArrayList<>();
//                index.add(r.number);
//                digramIndex.put(fr.currentDigram.second, index);
//                digramIndex.put(fr.currentDigram.first, index);
//            }
//            else  {
//                digramIndex.get(fr.currentDigram.first).add(digramIndex.get(fr.currentDigram.first).size(),
//                        r.number);
//                digramIndex.get(fr.currentDigram.second).add(digramIndex.get(fr.currentDigram.second).size(),
//                        r.number);
//            }
        }
    }


    /**
     * if a rule is only used once then remove it and... replace
     * with what is linked to.
     * @param fr
     */
    public void ruleUtility(NonTerminal fr){
        List<NonTerminal> ntList = nonTerminals.values().stream()
                .filter(x -> x.useNumber == 1)
                .collect(Collectors.toList());

        for (NonTerminal s : ntList) {
            NonTerminal hold = nonTerminals.get(s.usedByList.get(0));
            // when a rule is replaced with digram try to add that to the new rule in digram map
            // gonna have to remove too
            for (Digram d : s.getAllBigrams()) {
                if (!digramIndex.containsKey(d)) {
                    List<Integer> index = new ArrayList<>();
                    index.add(hold.number);
                    digramIndex.put(d, index);
                }
                else {
                    digramIndex.get(d).add(digramIndex.get(d).size(),
                            hold.number);
                }
            }
            hold.replaceNonTerminal(s);
            nonTerminals.remove(s.number); // remove key??
        }
    }


    // returns a reordered list
    public void reorderRules(NonTerminal fr) {
        //TODO need to reorder rules at the end so more often used rules have lower numbers
    }


    /**
     * small method to print out rules
     * @param r
     */
    public void printRules(List<NonTerminal> r) {
        for (NonTerminal nt : r) {
            System.out.println(nt.getValues());
        }
    }

}
