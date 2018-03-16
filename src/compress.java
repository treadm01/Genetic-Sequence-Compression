import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public void threeRule(rule fr) {
        String checkValues = "";
        String currentValues = fr.getValues();
        while (!currentValues.equals(checkValues)) {
            currentValues = fr.getValues();
            //System.out.println("current " + currentValues);
            checkRepeat(fr); // check for pattern in first rule
//
//            for (rule r : rules) {
//                System.out.println(r.getValues());
//            }



//            if (fr.values.size() > 4) {
//                System.out.println(fr.getCurrentBigram().first.getRepresentation());
//                System.out.println(fr.getCurrentBigram().second.getRepresentation());
//            }
            // if bigram in another rule update first rule again
            existingBigram(fr);
//
//            for (rule r : rules) {
//                System.out.println(r.getValues());
//            }



            ruleUtility(fr);

            checkValues = fr.getValues();
            //System.out.println("check " + checkValues);
        }

//        for (rule r : rules) {
//            r.setCurrentBigram(new bigram(r.values.get(r.values.size() - 2), r.values.get(r.values.size() - 1)));
//        }
    }

    public String readFile() {
        String everything = "";
        try(BufferedReader br = new BufferedReader(new FileReader("/home/tread/IdeaProjects/GeneticCompression/textFiles/test.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return everything;
    }

    // receives a string of whatever and works on it char by char
    public List<rule> processInput(String input) {
        rule.ruleNumber = 0;
        rule firstRule = new rule();
        for (int i = 0; i < input.length(); i++) {
            // add the element to string to first rule
            String ch = input.substring(i, i+1);

            if (ch.equals("\n")) {
                ch = "->";
            }

            firstRule.addValues(ch);

//            if (firstRule.values.size() >=4) {
//                bigram actualB = new bigram(firstRule.values.get(firstRule.values.size() - 2), firstRule.values.get(firstRule.values.size() - 1));
//                firstRule.setCurrentBigram(actualB); // clean up
//            }

            threeRule(firstRule);

        }

        // print out the final values

        List<rule> finalRules = new ArrayList<>();
        System.out.println(firstRule.getValues());
        finalRules.add(firstRule);
        for (rule r : rules) {
            finalRules.add(r);
            System.out.println(r.getValues());
         //   System.out.println(r.getCurrentBigram().first.getRepresentation());
        //    System.out.println(r.getCurrentBigram().second.getRepresentation());
        }

        System.out.println(rules.size());

        return finalRules;
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

        // needs to handle never used rules not sure it does actually
        for (symbol s : ntList) {
            if (once.contains(s)){ // not the same objects....
                onceNoMore.add(s);
                once.remove(s);
            }

            if (!onceNoMore.contains(s)) {
                once.add(s);
            }
        }

        //NEED TO GET THE PROPER INDEX OF THE RULE, AS WHEN THEY ARE REMOVED
        // VALUE DOESN'T CHANGE. YOU GET INDEX FROM ORIGINAL CREATION INDEX.
        // THINK IT POSSIBLE TO HACK OUT
        // BUT SHOULD PROBABLY UPDATE RULE NUMBERS ETC AS CHANGED OCCUR

        // remove the single rules
        List<rule> removalList = new ArrayList<>();

        for (symbol s : once) {
            for (rule r : rules) {

                if (r.values.contains(s)) {
                    int index = 0;
                    for (rule rx : rules) {
                        if (rx.getRuleNumber() == Integer.parseInt(String.valueOf(s.getRepresentation()))) {
                            index = rules.indexOf(rx);
                        }
                    }

                    r.values.addAll(r.values.indexOf(s),
                            rules.get(index).values);
                    //checkRepeat(r); check for repeat in rule????
                    r.values.remove(s);

                    removalList.add(rules.get(index));
                }
            }
        }

        for (rule r : removalList) {
            rules.remove(r);
        }
    }

    // took firstrule out of rules to make this run easier but could have other issues
    public void existingBigram(rule fr) {
        List<rule> newRuleLst = new ArrayList<>();
        for (rule r : rules) {
            bigram actualB = new bigram(r.values.get(r.values.size() - 2), r.values.get(r.values.size() - 1));
            r.setCurrentBigram(actualB); // clean up
//            System.out.println("BREAK");
//            System.out.println(r.getCurrentBigram().first.getRepresentation());
//            System.out.println(r.getCurrentBigram().second.getRepresentation());
//            System.out.println("BREAK 2");

            //THIS WILL UPDATE THE FIRST RULE EVEN IF THE RULE IS LARGE THAN TWO... DOESN'T MATTER RIGHT?
            // IF THEY EQUAL SHOULD UPDATE AND MAKE A NEW RULE...

            // IT'S FISHY AND COULD BE AVOIDED??????

            // had to specify size of values to two, whole rule to work the second half, not whole
            // rule with the get all bigrams method.....
            if (fr.getCurrentBigram().equals(r.getCurrentBigram()) && r.values.size() == 2) {
                fr.updateRule(r);
            }
            else if (r.getAllBigrams().contains(fr.getCurrentBigram())) {
             //   System.out.println(fr.getCurrentBigram().first.getRepresentation());
              //  System.out.println(fr.getCurrentBigram().second.getRepresentation());
                rule newRule = new rule();
                newRule.addValues(fr.getCurrentBigram()); // how to get bigram from first rule??
                newRuleLst.add(newRule);
                fr.updateRule(newRule);
                r.updateRule(newRule);

//                System.out.println(newRule.getCurrentBigram().first.getRepresentation());
//                System.out.println(newRule.getCurrentBigram().second.getRepresentation());
            }
        }

        for (rule r : newRuleLst) {
            rules.add(r);
        }
    }

}
