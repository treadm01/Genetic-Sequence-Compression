package GrammarCoder;

import java.util.ArrayList;
import java.util.List;

public class ApproxRepeat {
    Rule mainRule;
    String input;

    ApproxRepeat(Rule mainRule, String input) {
        this.mainRule = mainRule;
        this.input = input;
    }

    //todo should have its own class
    public List<Symbol> checkApproxRepeat(Symbol symbol) {
        // will either be the same symbol sent, an edited one (maybe) or one at the end of a nonterminal sequence
        Symbol nextSymbol = symbol;
        NonTerminal matchingNonTerminal;
        // at this point the next potential terminal will not have been added
        int bestEdit = 0;

        List<Symbol> symbols = new ArrayList<>();
        symbols.add(nextSymbol);
//
        // get the last nonterminal, if it wasn't one then not checking
        if (mainRule.getLast() instanceof NonTerminal) {
            matchingNonTerminal = (NonTerminal) mainRule.getLast();
            int matchingNonTerminalLength = matchingNonTerminal.getRule().getSymbolString(matchingNonTerminal.getRule(), false).length();
            for (NonTerminal nonTerminalSet : matchingNonTerminal.getRule().nonTerminalList) {
                if (matchingNonTerminal.isComplement == nonTerminalSet.isComplement) {
                    //todo neexs to not check itself, nonterminals lost when links change?
                    // cache of strings?
                    List<Symbol> next = new ArrayList<>();
                    Symbol nt = nonTerminalSet.getRight();
                    String lastSequence = "";

                    //try string only terminal method??? benefits might be quicker?
//                System.out.println("right of match is " + nt);
//                System.out.println("match is " + nt.getLeft());
//                System.out.println("left of match is " + nt.getLeft().getLeft());
                    //todo need a way to check benefit here and roll back if one too many
                    while (!nt.isGuard() && nt != matchingNonTerminal
                            && symbol.symbolIndex + lastSequence.length() <= input.length() - 1
                            && lastSequence.length() < 30) {
                        next.add(nt);
                        if (nt instanceof NonTerminal) {
                            lastSequence += ((NonTerminal) nt).getRule().getSymbolString(((NonTerminal) nt).getRule(), nt.isComplement);
                        } else if (nt instanceof Terminal) {
                            lastSequence += nt;
                        }
                        nt = nt.getRight();
                    }

                    String nextSequence;
                    if (symbol.symbolIndex + lastSequence.length() <= input.length()) {
                        nextSequence = input.substring(symbol.symbolIndex, symbol.symbolIndex + lastSequence.length());
                        int editNumber = 0;
                        for (int j = 0; j < lastSequence.length(); j++) {
                            if (lastSequence.charAt(j) != nextSequence.charAt(j)) {
                                editNumber++;
                            }
                        }

                        int combinedLength = (lastSequence.length() + matchingNonTerminalLength);

                        if (editNumber > 0 && editNumber < 3 && editNumber <= combinedLength * 0.1 && next.size() >= 3) {
                            //if (next.size() > 2) {
                            if (combinedLength - editNumber > bestEdit) {
//                                System.out.println(lastSequence);
//                                System.out.println(nextSequence);
                                symbols.clear();
                                bestEdit = combinedLength - editNumber;
                                int indexInString = 0;
                                for (Symbol s : next) {
                                    //      System.out.println("matching symbol " + s);
                                    //todo should be an edit on t, but as subrules need to go through them
                                    if (s instanceof NonTerminal) {
                                        NonTerminal nonTerminalClone = new NonTerminal(((NonTerminal) s).getRule()); //todo symbol sindex?
                                        String nonterminalString = ((NonTerminal) s).getRule().getSymbolString(((NonTerminal) s).getRule(), s.isComplement);
                                        List<Edit> edits = new ArrayList<>();
                                        //        System.out.println(nonterminalString);
                                        for (int x = 0; x < nonterminalString.length(); x++) {
                                            //            System.out.println("well");
                                            int pos = indexInString + x;
                                            if (lastSequence.charAt(pos) != nextSequence.charAt(pos)) {
                                                Boolean isComplement = lastSequence.charAt(pos) == Terminal.reverseSymbol(nextSequence.charAt(pos));
                                                edits.add(new Edit(symbol.symbolIndex + pos, String.valueOf(nextSequence.charAt(pos)), isComplement));
                                                //         System.out.println("edit nonterminal");
                                            }
                                        }
                                        nonTerminalClone.setIsEdit(edits);
                                        nonTerminalClone.isComplement = s.isComplement;
                                        symbols.add(nonTerminalClone);
                                        indexInString += nonterminalString.length();
                                        //System.out.println(indexInString);
                                        nonTerminalClone.symbolIndex = symbol.symbolIndex + indexInString - 1;
                                    } else if (s instanceof Terminal) {
                                        List<Edit> edits = new ArrayList<>();
                                        Symbol terminal = new Terminal(lastSequence.charAt(indexInString));
                                        if (lastSequence.charAt(indexInString) != nextSequence.charAt(indexInString)) {
                                            Boolean isComplement = lastSequence.charAt(indexInString) == Terminal.reverseSymbol(nextSequence.charAt(indexInString));
                                            edits.add(new Edit(symbol.symbolIndex + indexInString, String.valueOf(nextSequence.charAt(indexInString)), isComplement));
                                            terminal.setIsEdit(edits);
                                            //   System.out.println("edit terminal");
                                        }
                                        terminal.symbolIndex = symbol.symbolIndex + indexInString;
                                        indexInString++;

                                        //  System.out.println("INDEX " + terminal.symbolIndex);
                                        symbols.add(terminal);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

//        if (!symbols.get(0).equals(symbol)) {
//            System.out.println(symbols);
//        }
        return symbols;
    }

}
