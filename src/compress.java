import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * intend to do most of the work here, not sure if will be possible
 * keeps a list of the rules, but then how to search that by rule number?
 * and how to access the values stored in the rules?
 */
public class compress {
    //List<rule> rules = new ArrayList<>();
    List<nonTerminal> NTrules = new ArrayList<>();


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
     * takes file and reads it in as a String
     * @return
     */
    public String readFile() {
        //TODO improve implementation
        String everything = "";
        try(BufferedReader br = new BufferedReader(new FileReader("/home/tread/IdeaProjects/GeneticCompression/textFiles/test.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                //sb.append(System.lineSeparator()); //REMOVED AS NEW LINED SYMBOL ADDED WAS MESSING UP.... OK FOR THIS BUT NOT OTHERS
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File Read");
        return everything;
    }

    /**
     * takes the final rule list and converts it into a binary string,
     * which is then converted into bytes and written to bin file
     * @param finalRules
     * @return
     */
    public String writeFile(List<nonTerminal> finalRules) {
        //TODO break this up into process binary and separate write file methods
        System.out.println("writing file");
        String fullBinary = "";

        int largestRuleSize = Integer.toBinaryString(finalRules.size()).length();
        System.out.println(largestRuleSize);

        for (nonTerminal r : finalRules) {
            String binaryRuleLength = "";
            String ruleInBinary = Integer.toBinaryString(r.values.size());
            for (int i = 0; i < ruleInBinary.length(); i++) {
                binaryRuleLength += 1;
            }
            binaryRuleLength += 0 + ruleInBinary;

            fullBinary += binaryRuleLength;

            for (symbol s : r.values) {
                String binarySymbolRepresentation = "";
                String lengthOfSymbol = "";
                if (s instanceof nonTerminal) {
                    binarySymbolRepresentation = Integer.toBinaryString(Integer.valueOf(s.getRepresentation()));

                    for (int i = 0; i < binarySymbolRepresentation.length(); i++) {
                        lengthOfSymbol += 1;
                    }
                    lengthOfSymbol += 0;
                    binarySymbolRepresentation = lengthOfSymbol + binarySymbolRepresentation;
                }
                else {
                    // first 0 to show non terminal second for actg
                    binarySymbolRepresentation = "0";

                    switch (s.getRepresentation()) {
                        case "G":
                            binarySymbolRepresentation += "00";
                            break;
                        case "C":
                            binarySymbolRepresentation += "01";
                            break;
                        case "A":
                            binarySymbolRepresentation += "10";
                            break;
                        case "T":
                            binarySymbolRepresentation += "11";
                            break;
                    }
                }
                fullBinary += binarySymbolRepresentation;
            }

        }

        //System.out.println("last good " + fullBinary);

        while (fullBinary.length() % 6 != 0) {
            fullBinary += "0";
            //System.out.println("grow" + fullBinary);
        }

        String reallyFinal = "";
        for (int i = 0; i < fullBinary.length()/6; i++) {
            reallyFinal += "1" + fullBinary.substring(i * 6, i * 6 + 6);
          //  System.out.println("really " + reallyFinal);
        }



//        while (fullBinary.length() % 7 != 0) {
//            fullBinary += "0";
//        }

        //System.out.println(fullBinary);

        byte[] test = new byte[reallyFinal.length()/7];

        //dropping 0

        for (int i = 0; i < reallyFinal.length()/7; i++) {
            byte value = Byte.parseByte(reallyFinal.substring(i * 7, i*7 + 7), 2);
            test[i] = value;
        }
        //test = fullBinary.getBytes();
//        System.out.println(test[0]);


//        System.out.println("bytes in test : ");
//        for (Byte b : test) {
//            System.out.print(Integer.toBinaryString(b));
//        }

        //System.out.println("full binary : ");

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream("/home/tread/IdeaProjects/GeneticCompression/textFiles/compressed.bin");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(test);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Done");
        return reallyFinal;

    }


    /**
     * Main method to take string of the input, and run through the symbol,
     * currently holds re-order and prints out the rules at the end, returns the
     * list too
     * @param input
     * @return
     */
    public List<nonTerminal> processInput(String input) {
        //TODO decide where reorder will go, manage where things are created
        nonTerminal.ruleNumber = 0;
        nonTerminal firstNTRule = new nonTerminal();

        for (int i = 0; i < input.length(); i++) {
            //System.out.println("working through symbol " + i + " of " + input.length());

            // add the element to string to first rule
            String ch = input.substring(i, i+1);
            firstNTRule.addValues(ch); // created a new terminal,... shouldn't be hidden like that

            threeRule(firstNTRule);
        }

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
     * small method to print out rules
     * @param r
     */
    public void printRules(List<nonTerminal> r) {
        for (nonTerminal nt : r) {
            System.out.println(nt.getValues());
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

    // took firstrule out of rules to make this run easier but could have other issues
    public void existingBigram(nonTerminal fr) {
        List<nonTerminal> newRuleLst = new ArrayList<>();
        for (nonTerminal r : NTrules) {
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
                //r.useNumber++;
            }
            else if (r.getAllBigrams().contains(fr.getCurrentBigram())) {
             //   System.out.println(fr.getCurrentBigram().first.getRepresentation());
              //  System.out.println(fr.getCurrentBigram().second.getRepresentation());
                nonTerminal newRule = new nonTerminal();
                newRule.addValues(fr.getCurrentBigram()); // how to get bigram from first rule??
                newRuleLst.add(newRule);
                fr.updateRule(newRule);
                r.updateRule(newRule);

//                System.out.println(newRule.getCurrentBigram().first.getRepresentation());
//                System.out.println(newRule.getCurrentBigram().second.getRepresentation());
            }
        }

        for (nonTerminal r : newRuleLst) {
            NTrules.add(r);
        }
    }

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

}
