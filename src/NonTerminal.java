import java.util.ArrayList;
import java.util.List;

public class NonTerminal extends Symbol {
    List<Symbol> values = new ArrayList<>();
    Symbol guard;
    static int ruleNumber = 0;

    public NonTerminal() {
        this.representation = String.valueOf(ruleNumber);
        ruleNumber++;
        left = new Terminal("?");
        right = new Terminal("?");
        //TODO set number for rule
        guard = new Terminal("!");
        guard.left = guard;
        guard.right = guard;
        values.add(new Terminal("!"));
        values.add(new Terminal("!"));
    }

    /**
     * adds a symbol to the last symbol
     * @param symbol
     */
    public void addNextSymbol(Symbol symbol) {
        //TODO add nodes to each other without using a list
        symbol.right = values.get(values.size() - 1);
        symbol.left = values.get(values.size() - 2);
        values.get(values.size() - 2).right = symbol;
        values.get(values.size() - 1).left = symbol;

        values.add(values.size() - 1, symbol);
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
     * update this nonTerminal/rules digram with a nonTerminal
     * @param symbol
     */
    public void updateNonTerminal(Symbol symbol) {
        //TODO clean up. use values or nodes, use a generic addSymbol with index.
        int index = values.indexOf(symbol);
        NonTerminal nonTerminal = new NonTerminal(); // create the nonTerminal here as it needs to be a different object
        nonTerminal.addSymbols(symbol, symbol.right); // though should be matching symbols, TODO figure out rule numbers

        nonTerminal.right = values.get(index).right; // update node nonTerminal right
        values.get(index).right.left = nonTerminal; // update values for corresponding thing
        values.remove(index);

        values.add(index, nonTerminal); // add the nonTerminal to the list....

        nonTerminal.left = values.get(index - 1).left; // do the same as above but for other side
        values.get(index - 1).left.right = nonTerminal;
        values.remove(index - 1);

        //addNextSymbol(nonTerminal);
    }

    /**
     * return the last element of the list, not the buffer
     * @return
     */
    public Symbol getLast() {
        return values.get(values.size() - 2);
    }
}
