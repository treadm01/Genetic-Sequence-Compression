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
                    output += "#";
                    encodingSymbols.add("#");
                    // length is often 2 so only add if not
                    int length = nt.getRule().getRuleLength();
                    if (length != 2) {
                        output += "*" + length;
                        encodingSymbols.add("*");
                        encodingSymbols.add(length + "");
                    }

                    markerNum += 2;
                    output = encode(nt.getRule().getGuard().getRight(), output); // if nonterminal need to recurse back
                }
                else if (nt.rule.timeSeen == 1) {
                    //TODO use even odd distinction of rules??
                    nt.rule.timeSeen++;
                    int index = adjustedMarkers.indexOf(nt.rule.position); // get index of current list that is used by both
                    int indexComplement = 0;
                    String complementIndicator = "!"; // non complement //todo why two? if not there then noncomplement??
                    if (nt.isComplement) {
                        indexComplement = 1;
                        //complementIndicator = " "; // complement
                    }

                    //todo NEED TO SPLIT UP EDIT SYMBOLS *, INDEX, SYMBOL
                    String isEdit = "";
                    if (nt.isEdited) {
                        isEdit += nt.getEdits(); //todo seems extra to keep c reating??
                    }

                    output += complementIndicator + (index + indexComplement) + isEdit; // the index of the rule position can be used instead but corresponds to the correct value
                    encodingSymbols.add((index + indexComplement) + "");

                    String sym = "";
                    for (int i = 0; i < isEdit.length(); i++) {
                        if (isEdit.charAt(i) == '*') {
                            encodingSymbols.add("*");
                        }
                        else if (Character.isDigit(isEdit.charAt(i))) {
                            sym += isEdit.charAt(i);
                        }
                        else {
                            encodingSymbols.add(sym);
                            sym = "";
                            encodingSymbols.add(String.valueOf(isEdit.charAt(i)));
                        }
                    }

                    adjustedMarkers.remove(index);// remove when used
                }
                else {

                    String isEdit = "";
                    if (nt.isEdited) {
                        isEdit += nt.getEdits();
                    }

                    int index = nt.rule.position; // get index of current list that is used by both

                    if (index > highestRule) {
                        highestRule = index;
                    }
                    String complementIndicator = "!"; // non complement
                    if (nt.isComplement) {
                        index--;
                        //complementIndicator = " "; // complement
                        output += complementIndicator + index + isEdit; //+ rules.size();
                        encodingSymbols.add(String.valueOf(index));
                        String sym = "";
                        for (int i = 0; i < isEdit.length(); i++) {
                            if (isEdit.charAt(i) == '*') {
                                encodingSymbols.add("*");
                            }
                            else if (Character.isDigit(isEdit.charAt(i))) {
                                sym += isEdit.charAt(i);
                            }
                            else {
                                encodingSymbols.add(sym);
                                sym = "";
                                encodingSymbols.add(String.valueOf(isEdit.charAt(i)));
                            }
                        }

                    }
                    else {
                        output += complementIndicator + index + isEdit;
                        encodingSymbols.add(String.valueOf(index));
                        String sym = "";
                        for (int i = 0; i < isEdit.length(); i++) {
                            if (isEdit.charAt(i) == '*') {
                                encodingSymbols.add("*");
                            }
                            else if (Character.isDigit(isEdit.charAt(i))) {
                                sym += isEdit.charAt(i);
                            }
                            else {
                                encodingSymbols.add(sym);
                                sym = "";
                                encodingSymbols.add(String.valueOf(isEdit.charAt(i)));
                            }
                        }

                    }
                }
            }
            else {
                output += current; // add regular symbols to it
                encodingSymbols.add(current.toString());
            }
            current = current.getRight(); // move to next symbol
        }


        //todo implement properly
        try (PrintWriter out = new PrintWriter("/home/tread/IdeaProjects/projectGC/textFiles/compressTest")) {
            out.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }

    public String getEncodedOutput() {
        return encodedOutput;
    }

}
