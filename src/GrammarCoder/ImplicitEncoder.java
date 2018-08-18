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

        //todo having to send empty string...
        encodedOutput = encode();

        System.out.println("ENCODED: " + encodedOutput + "\nLENGTH: " + getEncodedOutput().length());
    }

    //TODO clean up
    public String encode() {
        String output = "";
        for (String s : encodingSymbols) {
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

        if (nt.isComplement) {
            index++; // is there a reason why this is down and the other is up?
        }
        encodingSymbols.add("!");
        encodingSymbols.add(String.valueOf(index));
        addEdits(nt.editList);
    }


    // length is often 2 so only add if not - REMOVED....
    //todo just going to encode the length, will need to change arithmetic coder
    //because youre not encoding the !... but using odd and even numbers...
    public void getEncodingSymbols(Symbol symbol) {
        Symbol current = symbol;
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                NonTerminal nt = (NonTerminal) current;
                nt.rule.timeSeen++; // count for number of times rule has been seen
                if (nt.rule.timeSeen == 0) {
                    //separate method?
                    nt.rule.position = markerNum; // 'position' really an indicator of the marker assigne to it
                    adjustedMarkers.add(markerNum); // add number for index of list, when removed, corresponds with list
                    markerNum += 2;

                    int length = nt.getRule().getRuleLength();
                    encodingSymbols.add("#");
                    encodingSymbols.add(String.valueOf(length));

                    getEncodingSymbols(nt.getRule().getGuard().getRight()); // if nonterminal need to recurse back
                }
                else if (nt.rule.timeSeen == 1) {
                    int index = adjustedMarkers.indexOf(nt.rule.position);
                    getNonTerminalString(index, nt);
                    adjustedMarkers.remove(index);// remove when used
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
        try (PrintWriter out = new PrintWriter("/home/tread/IdeaProjects/projectGC/textFiles/compressTest")) {
            out.println(getEncodedOutput());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
