package GrammarCoder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ImplicitEncoder {
    int markerNum = 128; // todo set to 0 check if issue later...
    public List<String> encodingSymbols; // list of symbols required to be encoded by arithmetic
    List<Integer> adjustedMarkers; // for encoding index of rule created used rather than symbol
    public int highestRule; // used for arithmetic coding highest rule will be the number of symbols needed
    Rule grammar;
    String encodedOutput;

    public ImplicitEncoder(Rule grammar) {
        this.grammar = grammar;
        encodingSymbols = new ArrayList<>();
        adjustedMarkers = new ArrayList<>();

        getEncodingSymbols(grammar.getFirst());

        encodedOutput = encode();
//        writeToFile();
        System.out.println("ENCODED: " + encodedOutput + "\nLENGTH: " + getEncodedOutput().length());
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
        for (Edit e : editList) {
            encodingSymbols.add("*"); // has to be added each time for arithmetic coding
            encodingSymbols.add(String.valueOf(e.index));
            encodingSymbols.add(e.symbol);
        }
    }

    // todo not getting a string anymore just applying the ... well strings to the list
    public void getNonTerminalString(int index, NonTerminal nt) {
        // this here, but only really needs to be in the second seen nonterminal
        if (index > highestRule) {
            highestRule = index;
        }

        String complementIndicator = "!";
        if (nt.rule.timeSeen == 1) {
            if (nt.isComplement) {
                complementIndicator = "?";
            }
            else {
                complementIndicator = "$";
            }
        }
        else if (nt.isComplement) {
            index++;
        }

        encodingSymbols.add(complementIndicator);
        encodingSymbols.add(String.valueOf(index));
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
                    markerNum += 2;
                    int length = nt.getRule().getRuleLength();
                    encodingSymbols.add("#");
                    encodingSymbols.add(String.valueOf(length));


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
        try (PrintWriter out = new PrintWriter("/home/tread/IdeaProjects/projectGCG/compressedFiles/compressTest")) {
            out.println(getEncodedOutput());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSymbolList() {
        return encodingSymbols;
    }

}