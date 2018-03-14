import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * intend to do most of the work here, not sure if will be possible
 * keeps a list of the rules, but then how to search that by rule number?
 * and how to access the values stored in the rules?
 */
public class compress {
    List<rule> rules = new ArrayList<>();

    // receives a string of whatever and works on it char by char
    public void processInput(String input) {
        rule.ruleNumber = 0;
        rule firstRule = new rule();
        for (int i = 0; i < input.length(); i++) {
            // add the element to string to first rule
            firstRule.addValues(input.charAt(i));

            checkRepeat(firstRule); // check for pattern in first rule

            // if bigram in another rule update first rule again
            existingBigram(firstRule);

            checkRepeat(firstRule);

            ruleUtility(firstRule);
        }

        // print out the final values
        firstRule.getValues();
        for (rule r : rules) {
            r.getValues();
        }
    }

    public void checkRepeat(rule fr) {
        if (fr.checkBigram()) {
            rule newRule = new rule();
            newRule.addValues(fr.getCurrentBigram()); // how to get bigram from first rule??
            rules.add(newRule);
            fr.updateRule(newRule);
        }
    }

    public void ruleUtility(rule fr){
        List<symbol> allSymbols = new ArrayList<>();
        allSymbols.addAll(fr.values);
        for (rule r : rules) {
            allSymbols.addAll(r.values);
        }

        List<symbol> ntList = allSymbols.stream()
                .filter(x -> x instanceof nonTerminal)
                .collect(Collectors.toList());

        List<symbol> once = new ArrayList<>();
        List<symbol> onceNoMore = new ArrayList<>();
        for (symbol s : ntList) {
            if (once.contains(s)){ // not the same objects....
                onceNoMore.add(s);
                once.remove(s);
            }

            if (!onceNoMore.contains(s)) {
                once.add(s);
            }
        }

        // remove the single rules
        List<rule> removalList = new ArrayList<>();
        for (symbol s : once) {
            for (rule r : rules) {
                if (r.values.contains(s)) {
                    r.values.addAll(r.values.indexOf(s),
                            rules.get(Integer.parseInt(String.valueOf(s.getRepresentation()))-1).values);
                    //checkRepeat(r); check for repeat in rule????
                    r.values.remove(s);
                    removalList.add(rules.get(Integer.parseInt(String.valueOf(s.getRepresentation()))-1));
                }
            }
        }

        for (rule r : removalList) {
            rules.remove(r);
        }
    }

    // took firstrule out of rules to make this run easier but could have other issues
    public void existingBigram(rule fr) {
        for (rule r : rules) {
            if (fr.getCurrentBigram().equals(r.getCurrentBigram())) {
                fr.updateRule(r);
            }
        }

    }

}
