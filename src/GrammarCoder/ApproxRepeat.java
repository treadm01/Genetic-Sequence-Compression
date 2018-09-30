package GrammarCoder;

import java.util.ArrayList;
import java.util.List;

class ApproxRepeat {
    private Rule mainRule;
    private String input;

    ApproxRepeat(Rule mainRule, String input) {
        this.mainRule = mainRule;
        this.input = input;
    }

    /**
     * main method checking for edits, checks the following sequence
     * of the current last nonterminal created to the seqeunces following all
     * previous instances of the same nonterminal to find sequences that are
     * approximate repeats
     * todo might be ignoring exact matches that would be better than no edits.. needs refactoring
     * @param symbol
     * @return
     */
    List<Symbol> checkApproxRepeat(Symbol symbol) {
        NonTerminal matchingNonTerminal;
        int bestEdit = 0;
        List<Symbol> symbols = new ArrayList<>();
        symbols.add(symbol);
        // get the last nonterminal, if it wasn't one then not checking
        if (mainRule.getLast() instanceof NonTerminal) {
            matchingNonTerminal = (NonTerminal) mainRule.getLast();
            Rule nonterminalRule = matchingNonTerminal.getRule();
            int matchingNonTerminalLength = nonterminalRule.getSymbolString(matchingNonTerminal.getRule(), false).length();
            for (NonTerminal nonTerminalSet : nonterminalRule.nonTerminalList) {
                // has to be the same complement value and not the same object
                if (matchingNonTerminal.getIsComplement() == nonTerminalSet.getIsComplement()
                        && matchingNonTerminal != nonTerminalSet) {
                    // list of the symbols that encode the previous sequences
                    List<Symbol> next = new ArrayList<>();
                    Symbol nt = nonTerminalSet.getRight();
                    // sequence following previous instances of nonterminals
                    StringBuilder lastSequence = new StringBuilder();

                    // adding to list of symbols and the last sequence string
                    //todo this needs to be dependent on the results rather than fixed length
                    while ((!nt.isGuard() && nt != matchingNonTerminal)
                            && lastSequence.length() < 30) {
                        next.add(nt);
                        lastSequence.append(getSymbolsString(nt));
                        nt = nt.getRight();
                    }

                    // if the two sequences do not overlap each other or the end of the string
                    // process number of edits to see whether worth doing
                    if (symbol.symbolIndex + lastSequence.length() <= input.length()) {
                        String nextSequence = input.substring(symbol.symbolIndex, symbol.symbolIndex + lastSequence.length());
                        int editNumber = numberOfEdits(lastSequence.toString(), nextSequence);
                        int combinedLength = (lastSequence.length() + matchingNonTerminalLength);
                        if (editNumber > 0 && editNumber < 3 && editNumber <= combinedLength * 0.1 && next.size() >= 3) {
                            if (combinedLength - editNumber > bestEdit) {
                                symbols.clear();
                                bestEdit = combinedLength - editNumber;
                                symbols.addAll(createNewSymbols(next, lastSequence.toString(), nextSequence, symbol));
                            }
                        }
                    }
                }
            }
        }
        return symbols;
    }

    /**
     *
     * @param symbol
     * @return
     */
    private String getSymbolsString(Symbol symbol) {
        StringBuilder symbolString = new StringBuilder();
        if (symbol instanceof NonTerminal) {
            symbolString.append(((NonTerminal) symbol).getRule().getSymbolString(((NonTerminal) symbol).getRule(),
                    symbol.getIsComplement()));
        } else if (symbol instanceof Terminal) {
            symbolString.append(symbol);
        }
        return symbolString.toString();
    }


    private int numberOfEdits(String lastSequence, String nextSequence) {
        int count = 0;
        for (int j = 0; j < lastSequence.length(); j++) {
            if (lastSequence.charAt(j) != nextSequence.charAt(j)) {
                count++;
            }
        }
        return count;
    }

    private List<Symbol> createNewSymbols(List<Symbol> next, String lastSequence, String nextSequence, Symbol symbol) {
        List<Symbol> newSymbols = new ArrayList<>();
        int indexInString = 0;
        for (Symbol s : next) {
            if (s instanceof NonTerminal) {
                NonTerminal nonTerminalClone = new NonTerminal(((NonTerminal) s).getRule());
                String nonterminalString = ((NonTerminal) s).getRule().getSymbolString(((NonTerminal) s).getRule(), s.getIsComplement());
                List<Edit> edits = new ArrayList<>();
                for (int x = 0; x < nonterminalString.length(); x++) {
                    int pos = indexInString + x;
                    if (lastSequence.charAt(pos) != nextSequence.charAt(pos)) {
                        edits.add(new Edit(symbol.symbolIndex + pos, String.valueOf(nextSequence.charAt(pos))));
                    }
                }
                nonTerminalClone.setIsEdit(edits);
                nonTerminalClone.setIsComplement(s.getIsComplement());
                newSymbols.add(nonTerminalClone);
                indexInString += nonterminalString.length();
                nonTerminalClone.symbolIndex = symbol.symbolIndex + indexInString - 1;
            }
            else if (s instanceof Terminal) {
                List<Edit> edits = new ArrayList<>();
                Symbol terminal = new Terminal(lastSequence.charAt(indexInString));
                if (lastSequence.charAt(indexInString) != nextSequence.charAt(indexInString)) {
                    edits.add(new Edit(symbol.symbolIndex + indexInString, String.valueOf(nextSequence.charAt(indexInString))));
                    terminal.setIsEdit(edits);
                }
                terminal.symbolIndex = symbol.symbolIndex + indexInString;
                indexInString++;
                newSymbols.add(terminal);
            }
        }
        return newSymbols;
    }

}
