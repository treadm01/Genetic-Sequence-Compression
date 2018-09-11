package GrammarCoder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplicitEncoder {
    String PATH = System.getProperty("user.dir");
    String SOURCE_PATH = PATH + "/sourceFiles";
    String COMPRESSED_PATH = PATH + "/compressedFiles";
    int markerNum = 0; // todo set to 0 check if issue later...
    public List<String> encodingSymbols; // list of symbols required to be encoded by arithmetic
    List<Integer> adjustedMarkers; // for encoding index of rule created used rather than symbol
    public int highestRule; // used for arithmetic coding highest rule will be the number of symbols needed
    Rule grammar;
    String encodedOutput;
    public Map<String, Integer> allSymbols = new HashMap<>();

    public ImplicitEncoder(Rule grammar) {
        this.grammar = grammar;
        encodingSymbols = new ArrayList<>();
        adjustedMarkers = new ArrayList<>();

        getEncodingSymbols(grammar.getFirst());

        encodedOutput = encode();
        writeToFile();

        for (String c : encodingSymbols) {
            if (c.charAt(0) > highestRule) {
                highestRule = c.charAt(0);
            }
            if (allSymbols.containsKey(c)) {
                Integer count = allSymbols.get(c);
                allSymbols.put(c, count + 1);
            }
            else {
                allSymbols.put(c, 1);
            }

        }


        System.out.println("ENCODED: " + encodedOutput +
                "\nLENGTH: "
                        + getEncodedOutput().length() + "\nAMOUNT OF SYMBOLS " + encodingSymbols.size());

    }

    //TODO clean up
    public String encode() {
        String output = "";
        for (String s : getSymbolList()) {
            output += s;
        }
        return output;
    }

    public void addEdits(List<Edit> editList) {
//        //todo need to actually implement this method of reduced symbols
//        if (editList.size() == 1) {
//            encodingSymbols.add("*");
//        }
//        for (Edit e : editList) {
//            // if edits greater than one just have * at beginning and end
//            // different symbols, if one, just *
//            // if more than one surround with two symbols
//            if (editList.size() != 1) {
//                encodingSymbols.add("`");
//            }
//            // but what if index is the same as logo
//            // which is better? removing complements or *?
//            //  encodingSymbols.add("*"); // has to be added each time for arithmetic coding
//            encodingSymbols.add(String.valueOf(e.index));
//            // if (!e.isComplement) {
//            encodingSymbols.add(e.symbol);
//            //}
//        }
//        if (editList.size() != 1) {
//            encodingSymbols.add("`"); // has to be added each time for arithmetic coding
//        }
        for (Edit e : editList) {
            encodingSymbols.add("*"); // has to be added each time for arithmetic coding
            encodingSymbols.add(String.valueOf((char)e.index));
            //if (!e.isComplement) {
                encodingSymbols.add(e.symbol);
            //}
        }
    }

    // todo not getting a string anymore just applying the ... well strings to the list
    public void getNonTerminalString(int index, NonTerminal nt) {
        // this here, but only really needs to be in the second seen nonterminal
        if (index > highestRule) {
            highestRule = index;
        }

        String complementIndicator = "";
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
        else{
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

    // length is often 2 so only add if not - REMOVED....
    //todo just going to encode the length, will need to change arithmetic coder
    //because youre not encoding the !... but using odd and even numbers...
    public void getEncodingSymbols(Symbol symbol) {
        Symbol current = symbol;
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                NonTerminal nt = (NonTerminal) current;
                if (nt.rule.timeSeen == 0) {
                    nt.rule.timeSeen++; // count for number of times rule has been seen
                    //separate method?
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
                encodingSymbols.add(current.toString());
            }
            current = current.getRight(); // move to next symbol
        }
    }

    public String getEncodedOutput() {
        return encodedOutput;
    }

    public void writeToFile() {
        //todo implement properly
        try (PrintWriter out = new PrintWriter(COMPRESSED_PATH + "/compressTest.txt")) {
            out.println(getEncodedOutput());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSymbolList() {
        return encodingSymbols;
    }

}