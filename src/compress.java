import java.util.*;
import java.util.stream.Collectors;

/**
 * intend to do most of the work here, not sure if will be possible
 * keeps a list of the rules, but then how to search that by rule number?
 * and how to access the values stored in the rules?
 */
public class compress {
    Map<String, Terminal> terminals = new HashMap<>();
    Map<Integer, nonTerminal> nonTerminals = new HashMap<>();

    /**
     * Main method to take string of the input, and run through the symbol,
     * currently holds re-order and prints out the rules at the end, returns the
     * list too
     * @param input
     * @return
     */
    public List<nonTerminal> processInput(String input) {
        //TODO decide where reorder will go, manage where things are created
        nonTerminal.ruleNumber = 0; // ehh set with a setter
        nonTerminal firstNTRule = new nonTerminal();

        for (int i = 0; i < input.length(); i++) {
            System.out.println("working through symbol " + i + " of " + input.length());

            // add the element to string to first rule
            String ch = input.substring(i, i+1);
            terminals.putIfAbsent(ch, new Terminal(ch));

            firstNTRule.addValues(terminals.get(ch)); // created a new terminal,... shouldn't be hidden like that

            threeRule(firstNTRule);
        }

        // reorder rules here after they've been formed... improves compression
        //reorderRules(firstNTRule);

        // print out the final values - // just for debugging
        List<nonTerminal> finalRules = new ArrayList<>();
        finalRules.add(firstNTRule);
        for (nonTerminal r : nonTerminals.values()) {
            finalRules.add(r);
        }

        // print out rules
        printRules(finalRules);

        return finalRules;
    }

    /**
     * main three rules that repeatedly run over the symbol stream
     * to find hierarchical rules
     * @param firstRule
     */
    public void threeRule(nonTerminal firstRule) {
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
    public void checkRepeat(nonTerminal fr) {
        if (fr.checkBigram()) {
            nonTerminal newRule = new nonTerminal();
            newRule.addValues(fr.getCurrentBigram()); // how to get bigram from first rule??
            nonTerminals.put(newRule.number, newRule);
            fr.updateRule(newRule);
        }
    }


    /**
     * check through the rules created for patterns that have already occurred.
     * Updates those rules with new non terminals and updates the original rule
     * from existing rules (rather than create a new one)
     * @param fr
     */

    //TODO keep a hasset of bigrams, or map with bigram to the rule number they appear in, find the
    //TODO hash get all the rules where they occur, but that keeps a map of bigrams and list of
    //TODO everything... Alternative, don't store symbols individually in nonTerminal but as bigrams
    //TODO that wouldn't be direct look up though
    public void existingBigram(nonTerminal fr) {
        //TODO clean up // took firstrule out of rules to make this run easier but could have other issues
        List<nonTerminal> newRuleLst = new ArrayList<>();
        for (nonTerminal r : nonTerminals.values()) {
        //for (nonTerminal r : nonTerminals.values()) {
            bigram actualB = new bigram(r.values.get(r.values.size() - 2), r.values.get(r.values.size() - 1));
            r.setCurrentBigram(actualB); // clean up - when is a good consistent way to set this?

            //THIS WILL UPDATE THE FIRST RULE EVEN IF THE RULE IS LARGE THAN TWO... DOESN'T MATTER RIGHT?
            // IF THEY EQUAL SHOULD UPDATE AND MAKE A NEW RULE...
            // had to specify size of values to two, whole rule to work the second half, not whole
            // rule with the get all bigrams method.....

//            if (fr.getCurrentBigram().equals(r.getCurrentBigram()) && r.values.size() == 2) {
//                fr.updateRule(r);
//            }
//            else
            if (r.getAllBigrams().contains(fr.getCurrentBigram())) {
                if (r.values.size() == 2) {
                    fr.updateRule(r);
                }
                else {
                    nonTerminal newRule = new nonTerminal();
                    newRule.addValues(fr.getCurrentBigram()); // how to get bigram from first rule??
                    newRuleLst.add(newRule);
                    fr.updateRule(newRule);
                    r.updateRule(newRule);
                }
            }
        }

        // add the newly created rules to main list
        for (nonTerminal r : newRuleLst) {
            nonTerminals.put(r.number, r);
        }
    }


    /**
     * if a rule is only used once then remove it and... replace
     * with what is linked to.
     * @param fr
     */
    public void ruleUtility(nonTerminal fr){
        List<nonTerminal> ntList = nonTerminals.values().stream()
                .filter(x -> x.useNumber == 1)
                .collect(Collectors.toList());

        for (nonTerminal s : ntList) {
            nonTerminal hold = nonTerminals.get(s.usedByList.get(0));
            hold.values.addAll(hold.values.indexOf(s), s.values);
            hold.values.remove(s);
            nonTerminals.remove(s.number); // remove key??
        }
    }


    // returns a reordered list
    public void reorderRules(nonTerminal fr) {
        //TODO need to reorder rules at the end so more often used rules have lower numbers
    }


    /**
     * small method to print out rules
     * @param r
     */
    public void printRules(List<nonTerminal> r) {
        for (nonTerminal nt : r) {
            System.out.println(nt.getValues());
        }
    }

}
