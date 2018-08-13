package GrammarCoder;

public class Rule extends Symbol implements Comparable {
    int count;
    Guard guard;
    static Integer ruleNumber = 0;

    // for decompressing
    Boolean compressed = false;

    // for encoding...
    int timeSeen = 0;
    int position;
    int length; // length of compressed rule
    String symbolRule; // length of uncompressed rule

    public Rule() {
        index = 0; // todo not sure this is used anymore
        this.representation = ruleNumber;
        ruleNumber += 2;
        guard = new Guard(this);
        assignRight(guard);
        assignLeft(guard); // seems necessary for hashcode check, to check left symbol, use to point to guard
        guard.assignRight(guard);
        guard.assignLeft(guard);
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        symbol.assignLeft(guard.left); // left of symbol is current last, actualguard.left
        symbol.assignRight(guard); // symbol right should be actual guard
        guard.left.assignRight(symbol); // assign current last right to this symbol
        guard.assignLeft(symbol); // assign last to symbol

    }

    /**
     * add the two symbols from a digram to a nonTerminal
     * @param left
     * @param right
     */
    public void addSymbols(Symbol left, Symbol right) {
        this.addNextSymbol(left);
        this.addNextSymbol(right);
    }

    /**
     * return the last element
     * @return
     */
    public Symbol getLast() {
        return guard.left;
    }

    public Guard getGuard() { return guard; }

    public int getCount() { return count; }

    public void incrementCount() { count++;}

    public void decrementCount() { count--;}


    /**
     * for debugging can get string output of an entire rule
     * @return
     */
    public String getRuleString() {
        String symbols = "";
        Symbol first = guard.getRight();
        while (!first.isGuard()) {
            symbols += " " + first.toString();
            first = first.getRight();
        }
        return symbols;
    }

    //todo clean up, used by decompress... better position for pieces if split up
    public String getSymbolString(Rule rule, Boolean complement) {
        Symbol s;
        StringBuilder output = new StringBuilder();
        if (complement) {
            s = rule.getLast();
        }
        else {
            s = rule.getGuard().getRight();
        }

        do {
            if (s instanceof Terminal) {
                if (complement) {
                    output.append(Terminal.reverseSymbol(s.toString().charAt(0)));
                }
                else {
                    output.append(s.toString());
                }

                if (complement) {
                    s = s.getLeft();
                }
                else {
                    s = s.getRight();
                }
            }
            else { // IF NONTERMINAL //TODO IF EDIT, THEN GET THE STRING AND DO EDITS AFTERWARDS...
                int currentLength = output.length();
                if (complement) {
                    output.append(getSymbolString(((NonTerminal) s).getRule(), !s.isComplement));
                }
                else {
                    output.append(getSymbolString(((NonTerminal) s).getRule(), s.isComplement));
                }

                //TODO THIS IS ASSUMING THAT ALL INDEXES ARE ONE DIGIT POSITIONS
                //TODO add the edited one to the list and get that way rather than string indexes?
                if (s.isEdited) {
//                    System.out.println(currentLength);
//                    System.out.println(output);
                    String editString = ((NonTerminal) s).edits;
                    int i = 0;
                    String index = "";
                    while (i < editString.length()) {
                        if (Character.isDigit(editString.charAt(i))) {
                             index += String.valueOf(editString.charAt(i));
                        }
                        else {
//                            System.out.println(index);
                            System.out.println(String.valueOf(editString.charAt(i)));
                            System.out.println(output.toString());
                            int intIndex = Integer.parseInt(index); //+ currentLength;
                            System.out.println(intIndex);
                            System.out.println(intIndex + 1);

                            output.replace(intIndex, intIndex + 1, String.valueOf(editString.charAt(i)));
                            index = "";
                        }
                        i++;
                    }
                }

                if (complement) {
                    s = s.getLeft();
                }
                else {
                    s = s.getRight();
                }
            }

        } while (!s.isGuard());
        return output.toString();
    }

    @Override
    public int compareTo(Object o) {
        Rule r = (Rule) o;
        if (r.symbolRule.length() > symbolRule.length()) {
            return 1;
        }
        else if (r.symbolRule.length() < symbolRule.length()) {
            return -1;
        }
        return 0;
    }
}