import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Uncompress {

    public String mutatingString = "";
    Map<String, Terminal> terminals = new HashMap<>();

    //TODO save to file and retrieve in a way that objects are known to be terminal, non terminal, rule
    // from there can start to compare

    public String getOutput(NonTerminal r, List<NonTerminal> compressedRule, String soFar) {
        String uncompressed = soFar;
        for (Symbol s : r.values) {
            if (s instanceof Terminal) {
                uncompressed += s.toString();
            }
            else {
                for (NonTerminal rx : compressedRule) {
                    if (rx.getRuleNumber() == Integer.parseInt(String.valueOf(s.toString()))) {
                        uncompressed = getOutput(rx, compressedRule, uncompressed);
                    }
                }
            }
        }
        return uncompressed;
    }

    public String processInput(List<NonTerminal> compressedRule) {

        NonTerminal firstRule = compressedRule.get(0);
        String uncompressed = "";

        uncompressed += getOutput(firstRule, compressedRule, uncompressed);

        return uncompressed;
    }

    // remove every 7
    public List<NonTerminal> processBinary(String binary) {

        // first thing to do is remove wasted seventh bits
        String removedSevens = "";
        for (int i = 0; i < binary.length(); i++) {
            if (i % 7 != 0) {
                removedSevens += binary.substring(i, i+1);
            }
        }

        List<NonTerminal> buildList = new ArrayList<>();
        NonTerminal.ruleNumber = 0; // shouldn't have to do this

        mutatingString = removedSevens;

        while (mutatingString.length() != 0 && mutatingString.charAt(0) != '0') {
            buildList.add(getRuleFromBinary(mutatingString));
            System.out.println(mutatingString);
        }

        for (NonTerminal r : buildList) {
            System.out.println(r.getValues());
        }

        return buildList;
    }

    public NonTerminal getRuleFromBinary(String bnary) {
        NonTerminal r = new NonTerminal();
        //System.out.println(binary);

        int symbolsToRead = 0;
        while (mutatingString.charAt(symbolsToRead) != '0') {
            symbolsToRead++;
        }

        //System.out.println(binary);
        mutatingString = mutatingString.substring(symbolsToRead+1);
        //System.out.println(binary);

        // issue where the amount of symbols to read from binary is too much
        Integer numberOfSymbols = Integer.parseInt(mutatingString.substring(0, symbolsToRead), 2);

        //System.out.println(numberOfSymbols);

        mutatingString = mutatingString.substring(symbolsToRead);

        //System.out.println(binary);

        int pos = 0;
        for (int i = 0; i < numberOfSymbols; i++) {
            if (mutatingString.charAt(0) == '0') {
          //      System.out.println(binary);
                String rep = getACGT(mutatingString.substring(pos+1, pos+3));

                terminals.putIfAbsent(rep, new Terminal(rep));// create terminals here first
            //    System.out.println(rep);
                mutatingString = mutatingString.substring(3);
                r.addValues(terminals.get(rep)); // for Terminal send a String, for nonTerminal send an int
            }
            else {
                int repre = getNonTerminalFromBinary(mutatingString);
                r.addValues(repre);
            }
        }

        //mutatingString = mutatingString;

        return r;
    }

    public int getNonTerminalFromBinary(String bnary) {
        int symbolsToRead = 0;
        while (mutatingString.charAt(symbolsToRead) != '0') {
            symbolsToRead++;
        }
        //System.out.println(binary);
        mutatingString = mutatingString.substring(symbolsToRead+1);
        //System.out.println(binary);
        Integer numberOfSymbols = Integer.parseInt(mutatingString.substring(0, symbolsToRead), 2);
        //System.out.println(numberOfSymbols);
        mutatingString = mutatingString.substring(symbolsToRead);
        //mutatingString = mutatingString;
        return numberOfSymbols;
    }

    public String getACGT(String binary) {
        String ACGT = "";
        switch (binary) {
            case "00":
                ACGT= "G";
                break;
            case "01":
                ACGT= "C";
                break;
            case "10":
                ACGT= "A";
                break;
            case "11":
                ACGT= "T";
                break;
        }
        return ACGT;
    }

}
