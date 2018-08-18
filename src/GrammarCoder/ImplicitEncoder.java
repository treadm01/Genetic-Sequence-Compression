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

        //todo having to send empty string...
        encodedOutput = encode(grammar.getFirst(), "");

        System.out.println("ENCODED: " + encodedOutput + "\nLENGTH: " + getEncodedOutput().length());
    }

    //TODO clean up
    public String encode(Symbol symbol, String output) {
        Symbol current = symbol;
        while (!current.isGuard()) {
            if (current instanceof NonTerminal) {
                NonTerminal nt = (NonTerminal) current;
                if (nt.rule.timeSeen == 0) {
                    nt.rule.timeSeen++; // count for number of times rule has been seen
                    nt.rule.position = markerNum; // 'position' really an indicator of the marker assigne to it
                    adjustedMarkers.add(markerNum); // add number for index of list, when removed, corresponds with list
                    int length = nt.getRule().getRuleLength();
                    output += "#" + length;
                    encodingSymbols.add("#");
                    encodingSymbols.add(length + "");
                    // length is often 2 so only add if not - REMOVED....
                    //todo just going to encode the length, will need to change arithmetic coder
                    markerNum += 2;
                    output = encode(nt.getRule().getGuard().getRight(), output); // if nonterminal need to recurse back
                }
                else if (nt.rule.timeSeen == 1) {
                    nt.rule.timeSeen++;
                    int index = adjustedMarkers.indexOf(nt.rule.position);
                    output += getNonTerminalString(index, nt);
                    adjustedMarkers.remove(index);// remove when used
                }
                else {
                    output += getNonTerminalString(nt.rule.position, nt);
                }
            }
            else {
                output += current; // add regular symbols to it
                encodingSymbols.add(current.toString());
            }
            current = current.getRight(); // move to next symbol
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

    public String getNonTerminalString(int index, NonTerminal nt) {
        String ntString = "";
        if (index > highestRule) {
            highestRule = index;
        }

        if (nt.isComplement) {
            index++; // is there a reason why this is down and the other is up?
        }
        ntString += "!" + index + nt.getEdits();
        encodingSymbols.add(String.valueOf(index));
        addEdits(nt.editList);

        return ntString;
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
