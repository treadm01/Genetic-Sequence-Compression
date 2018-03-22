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

    public void threeRule(nonTerminal fr) {
        String checkValues = "";
        String currentValues = fr.getValues();
        while (!currentValues.equals(checkValues)) {
            currentValues = fr.getValues();
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

    public String writeFile(List<nonTerminal> finalRules) {
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

    // receives a string of whatever and works on it char by char
    public List<nonTerminal> processInput(String input) {
        nonTerminal.ruleNumber = 0;
        nonTerminal firstNTRule = new nonTerminal();

        for (int i = 0; i < input.length(); i++) {

            //System.out.println(firstRule.getValues());

            System.out.println("working through symbol " + i + " of " + input.length());
            // add the element to string to first rule
            String ch = input.substring(i, i+1);
//
//            if (ch.equals("\n")) {
//                ch = "->";
//            }

            firstNTRule.addValues(ch);

//            if (firstRule.values.size() >=4) {
//                bigram actualB = new bigram(firstRule.values.get(firstRule.values.size() - 2), firstRule.values.get(firstRule.values.size() - 1));
//                firstRule.setCurrentBigram(actualB); // clean up
//            }

            threeRule(firstNTRule);
////
//            System.out.println(firstNTRule.getValues());
//            for (nonTerminal r : NTrules) {
//                System.out.println(r.getValues());
//                System.out.println("use number " + r.useNumber);
////            System.out.println(r.useNumber);
//            }

        }

        //TODO reorder rules is messing something up
        reorderRules(firstNTRule);

        // print out the final values

        List<nonTerminal> finalRules = new ArrayList<>();
        System.out.println(firstNTRule.getValues());
        finalRules.add(firstNTRule);
        for (nonTerminal r : NTrules) {
            finalRules.add(r);
            System.out.print(r.getValues());
            System.out.print(" - use number " + r.useNumber);
            System.out.println();
        }

        return finalRules;
    }

    public void checkRepeat(nonTerminal fr) {
        if (fr.checkBigram()) {
            nonTerminal newRule = new nonTerminal();
            newRule.addValues(fr.getCurrentBigram()); // how to get bigram from first rule??
            NTrules.add(newRule);
            fr.updateRule(newRule);
        }
    }

    public void ruleUtility(nonTerminal fr){
        List<symbol> allSymbols = new ArrayList<>();
        allSymbols.addAll(fr.values);
        for (nonTerminal r : NTrules) {
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
        List<nonTerminal> removalList = new ArrayList<>();

        for (symbol s : once) {
            for (nonTerminal r : NTrules) {

                if (r.values.contains(s)) {
                    int index = 0;
                    for (nonTerminal rx : NTrules) {
                        if (rx.getRuleNumber() == Integer.parseInt(String.valueOf(s.getRepresentation()))) {
                            index = NTrules.indexOf(rx);
                        }
                    }

                    r.values.addAll(r.values.indexOf(s),
                            NTrules.get(index).values);
                    //checkRepeat(r); check for repeat in rule????
                    r.values.remove(s);

                    removalList.add(NTrules.get(index));
                }
            }
        }

        for (nonTerminal r : removalList) {
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

//        NTrules = NTrules.stream()
//                .sorted((x, y) -> y.useNumber.compareTo(x.useNumber))
//                //.sorted((x, y) -> y.ruleSize.compareTo(x.ruleSize)) // sort by size messes up
//                .collect(Collectors.toList());


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
