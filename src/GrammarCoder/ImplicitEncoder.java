package GrammarCoder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class ImplicitEncoder {
    private int markerNum = 0;
    private List<String> encodingSymbols; // list of symbols required to be encoded by arithmetic
    private List<Integer> adjustedMarkers; // for encoding index of rule created used rather than symbol
    public int highestRule; // used for arithmetic coding highest rule will be the number of symbols needed
    private String encodedOutput;
    public Map<String, Integer> allSymbols = new HashMap<>();
    private static String PATH = System.getProperty("user.dir") + "/compressedFiles";
    private Set<Character> uniqueSymbols = new HashSet<>();

    public ImplicitEncoder(Rule grammar) {
        encodingSymbols = new ArrayList<>();
        adjustedMarkers = new ArrayList<>();
        uniqueSymbols.add('*');
        uniqueSymbols.add('!');
        uniqueSymbols.add('?');
        uniqueSymbols.add('%');
        uniqueSymbols.add('[');
        uniqueSymbols.add('{');
        uniqueSymbols.add('\'');
        uniqueSymbols.add('^');
        uniqueSymbols.add('#');

        getEncodingSymbols(grammar.getFirst());

        encodedOutput = encode();

        for (String c : encodingSymbols) {
            if (c.charAt(0) > highestRule) {
                highestRule = c.charAt(0);
            }
        }
//        System.out.println("ENCODED: " + encodedOutput +
//                "\nLENGTH: "
//                        + getEncodedOutput().length() + "\nAMOUNT OF SYMBOLS " + encodingSymbols.size());
        writeToFile(encodedOutput);
    }

    public String encode() {
        String output = "";
        for (String s : getSymbolList()) {
            output += s;
        }
        return output;
    }

    public void addEdits(List<Edit> editList) {
        for (Edit e : editList) {
            encodingSymbols.add("*"); // has to be added each time for arithmetic coding
            encodingSymbols.add(String.valueOf((char)e.index));
            encodingSymbols.add(e.symbol);
        }
    }

    // todo not getting a string anymore just applying the ... well strings to the list
    public void getNonTerminalString(int index, NonTerminal nt) {
        if (index > highestRule) {
            highestRule = index;
        }

        String complementIndicator;
        if (nt.rule.timeSeen == 1) { // from the stack of rules rather than the symbol needs separate indicator
            if (nt.isComplement) {
                complementIndicator = "{";
            }
            else { // if standard still needs an indicator
                complementIndicator = "?";
            }

            if (index == 1) {
                if (nt.isComplement) {
                    complementIndicator = "%";
                }
                else {
                    complementIndicator = "[";
                }
                encodingSymbols.add(complementIndicator);
            }
            else {
                encodingSymbols.add(complementIndicator);
                encodingSymbols.add(String.valueOf((char)index));
            }
        }
        else {
            if (nt.isComplement) {
                complementIndicator = "'";
            }
            else { // if standard still needs an indicator
                complementIndicator = "!";
            }
            encodingSymbols.add(complementIndicator);
            encodingSymbols.add(String.valueOf((char) index));
        }


        if (nt.isEdited) {
            addEdits(nt.editList);
        }
    }

    public void getEncodingSymbols(Symbol symbol) {
        Symbol current = symbol;
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                NonTerminal nt = (NonTerminal) current;
                if (nt.rule.timeSeen == 0) {
                    nt.rule.timeSeen++; // count for number of times rule has been seen
                    nt.rule.position = markerNum; // 'position' really an indicator of the marker assigne to it
                    adjustedMarkers.add(markerNum); // add number for index of list, when removed, corresponds with list
                    markerNum+=2;
                    int length = nt.getRule().getRuleLength();
                    if (length == 2) {
                        encodingSymbols.add("^");
                    }
                    else {
                        encodingSymbols.add("#");
                        encodingSymbols.add(String.valueOf((char)length));
                    }

                    // have to add edits for rules that are first time see with edits
                    if (nt.isEdited) {
                        addEdits(nt.editList);
                    }

                    getEncodingSymbols(nt.getRule().getGuard().getRight()); // if nonterminal need to recurse back
                }
                else if (nt.rule.timeSeen == 1) {
                    int index = adjustedMarkers.indexOf(nt.rule.position);
                    getNonTerminalString(index, nt);
                    adjustedMarkers.remove(index);// remove when used
                    nt.rule.timeSeen++; // count for number of times rule has been seen
                }
                else {
                    getNonTerminalString(nt.rule.position, nt);
                }
            }
            else {
                if (uniqueSymbols.contains(current.toString().charAt(0))) {
                    encodingSymbols.add(String.valueOf((char)0));
                }
                encodingSymbols.add(current.toString());
            }
            current = current.getRight(); // move to next symbol
        }
    }

    public String getEncodedOutput() {
        return encodedOutput;
    }

    public List<String> getSymbolList() {
        return encodingSymbols;
    }

    public void writeToFile(String output) {
        try (PrintWriter out = new PrintWriter(PATH + "/compressTest.txt")) {
            out.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}