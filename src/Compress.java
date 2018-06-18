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

        for (int i = 0; i < input.length(); i++) {
            System.out.println("working through symbol " + i + " of " + input.length());

            // add the element to string to first rule
            String ch = input.substring(i, i+1);

            Terminal currentTerminal = new Terminal(ch);
            if (firstNTRule.values.size() != 0) {
                currentTerminal.setLeft(firstNTRule.values.get(firstNTRule.values.size()-1));
            }

            terminals.putIfAbsent(ch, new Terminal(ch)); // how to do???

            firstNTRule.addValues(currentTerminal); // created a new terminal,... shouldn't be hidden like that

            threeRule(firstNTRule);
        }

        // reorder rules here after they've been formed... improves compression
        //reorderRules(firstNTRule);

        // print out the final values - // just for debugging
        List<NonTerminal> finalRules = new ArrayList<>();
        finalRules.add(firstNTRule);
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
            fr.updateRule(newRule); //TODO update the left and right of elements here
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
    public void existingBigram(NonTerminal fr) {
        //TODO clean up // took firstrule out of rules to make this run easier but could have other issues
        //TODO remove for loops use hash
        List<NonTerminal> newRuleLst = new ArrayList<>();
        for (NonTerminal r : nonTerminals.values()) {
            Digram actualB = new Digram(r.values.get(r.values.size() - 2), r.values.get(r.values.size() - 1));
            r.setCurrentDigram(actualB); // clean up - when is a good consistent way to set this?
            if (r.getAllBigrams(). contains(fr.getCurrentDigram())) {
                if (r.values.size() == 2) {
                    fr.updateRule(r);
                }
                else {
                    NonTerminal newRule = new NonTerminal();
                    newRule.addValues(fr.getCurrentDigram()); // how to get bigram from first rule??
                    newRuleLst.add(newRule);
                    fr.updateRule(newRule);
                    r.updateRule(newRule);
                }
            }
        }

        // add the newly created rules to main list
        for (NonTerminal r : newRuleLst) {
            nonTerminals.put(r.number, r);
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
