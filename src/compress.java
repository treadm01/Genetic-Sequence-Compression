import java.util.*;
import java.util.stream.Collectors;

/**
 * intend to do most of the work here, not sure if will be possible
 * keeps a list of the rules, but then how to search that by rule number?
 * and how to access the values stored in the rules?
 */
public class compress {
    List<nonTerminal> NTrules = new ArrayList<>();
    Set<nonTerminal> nonTerminalSet = new HashSet<>();
    Set<Terminal> terminals = new HashSet<>();
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
            terminals.add(new Terminal(ch));

            firstNTRule.addValues(ch); // created a new terminal,... shouldn't be hidden like that

            threeRule(firstNTRule);
        }

        terminals.forEach(x -> x.toString());

        // reorder rules here after they've been formed... improves compression
        reorderRules(firstNTRule);

        // print out the final values
        List<nonTerminal> finalRules = new ArrayList<>();
        finalRules.add(firstNTRule);
        for (nonTerminal r : NTrules) {
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
            NTrules.add(newRule);
            fr.updateRule(newRule);
        }
    }

    /**
     * if a rule is only used once then remove it and... replace
     * with what is linked to.
     * @param fr
     */
    public void ruleUtility(nonTerminal fr){
        //TODO too many loops, needs cleaning

        // get all symbols that are nonTerminal and are only used once
        List<symbol> ntList = NTrules.stream()
                .filter(x -> x instanceof nonTerminal)
                .filter(x -> x.useNumber == 1)
                .collect(Collectors.toList());

        // TODO this is a list of rules that occur only once and you're checking everything to find them
        // TODO you should be able ti find out where they are
        for (symbol s : ntList) { // for every nonterminal used only once
            //TODO this should cover the first rule, ok at the moment but... if a rule that is used only once is in the first one it will be missed
            for (nonTerminal r : NTrules) { // for other rules check only occuring once
                if (r.values.contains(s)) { // if the value contains the rule
                    int index = 0; //TODO not sure how you're getting this index or why, cant use rule number?
                    for (nonTerminal rx : NTrules) {
                        if (rx.getRuleNumber() == Integer.parseInt(String.valueOf(s.getRepresentation()))) {
                            index = NTrules.indexOf(rx);
                        }
                    }
                    r.values.addAll(r.values.indexOf(s),
                            NTrules.get(index).values);
                    r.values.remove(s);

                }
            }
        }

        // remove the rules that had only one instance
        for (symbol r : ntList) {
            NTrules.remove(r);
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
        for (nonTerminal r : NTrules) {
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
            NTrules.add(r);
        }
    }

    // WHAT DOES THIS DO TOBY?
    public List<nonTerminal> reorderRules(nonTerminal fr) {

        List<nonTerminal> completeRules = new ArrayList<>();
        completeRules.add(fr);
        completeRules.addAll(NTrules);

        // return instead of mutate etc

        // causing issues somewhere with decompression
        // can't see how to sort by size of rule as well as frequency....

//        System.out.println("unsorted");
//        for (nonTerminal r : completeRules) {
//            System.out.println(r.getValues());
//        }

        NTrules = NTrules.stream()
                .sorted((x, y) -> y.useNumber.compareTo(x.useNumber))
                //.sorted((x, y) -> y.ruleSize.compareTo(x.ruleSize)) // sort by size messes up
                .collect(Collectors.toList());


//        System.out.println("sorted");
//        System.out.println(fr.getValues());
//        for (rule r : rules) {
//            System.out.println(r.getValues());
//        }

        for (nonTerminal r : NTrules) {
            int newNumber = NTrules.indexOf(r) + 1;
            for (nonTerminal r2 : completeRules) {
                for (symbol s : r2.values) {
                    if (s.equals(r)) {
                        s.representation = String.valueOf(newNumber);
                    }
                }
            }
            r.setRuleNumber(NTrules.indexOf(r) + 1);
        }

        return completeRules;
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
