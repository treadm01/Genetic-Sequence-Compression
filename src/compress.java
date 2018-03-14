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
        //rules.add(firstRule);
        for (int i = 0; i < input.length(); i++) {
            firstRule.addValues(input.charAt(i));

            checkRepeat(firstRule);

            // took firstrule out of rules to make this run easier but could have other issues
            // if bigram in another rule update first rule again
            for (rule r : rules) {
                if (firstRule.getCurrentBigram().equals(r.getCurrentBigram())) {
                    firstRule.updateRule(r);
                }
            }

            checkRepeat(firstRule);



//TODO need to enforce rule utility - if a rule is used only once, take it out and replace with its link
            //TODO got the nonterminal onlcy occuring once, but how to remove??
            // either way will probably have to update the rules again afterwards...

            List<symbol> allSymbols = new ArrayList<>();
            allSymbols.addAll(firstRule.values);
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

//
            for (symbol s : once) {
                System.out.println(s.getRepresentation());

//                // remove single - // could happen in first rule remmber, only checkingthe others with rules
//                for (rule r : rules) {
//                    if (r.values.contains(s)) {
//                        // should do this in rule? check for unigram??
//                        r.values.addAll(r.values.indexOf(s), rules.get(Integer.valueOf(s.getRepresentation())).values);
//                        r.values.remove(s);
//                    }
//                }
            }






            // as first rule has been updated need to check with new bigram
        }



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

}
