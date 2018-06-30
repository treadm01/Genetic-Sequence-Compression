import java.util.ArrayList;
import java.util.List;

public class NonTerminal extends Symbol implements Cloneable {
    List<Symbol> values = new ArrayList<>();
    Symbol guard, last;
    int count;

    public NonTerminal(int ruleNumber) {
        this.representation = String.valueOf(ruleNumber);
        left = new Terminal("?");
        right = new Terminal("?");
        //TODO set number for rule
        guard = new Terminal("!");
        guard.left = left;
        guard.right = right;
        last = guard.left;
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
        symbol.left = last;
        symbol.right = guard.right;
        last.right = symbol;
        last = symbol;


        //guard.right = symbol;
//        System.out.println("GUARD" + guard + " guard riught " + guard.right );
//        System.out.println("RIGHT LEFT " + guard.right.left);
//        System.out.println("RIGHT LEFT right " + guard.right.left.right);
//        System.out.println("RIGHT right " + guard.right.right);
//
//        System.out.println("LEFT " + guard.left.right);

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
    public void updateNonTerminal(Rule rule, Symbol symbol) {
        //TODO clean up. use values or nodes, use a generic addSymbol with index.
        int index = values.indexOf(symbol);

        rule.right = values.get(index + 1); // update node nonTerminal right
        rule.left = values.get(index - 2);

        values.get(index + 1).left = rule; // update values for corresponding thing

        values.remove(index);

        values.add(index, rule); // add the nonTerminal to the list....

        //nonTerminal.left = values.get(index - 1).left; // do the same as above but for other side
        values.get(index - 2).right = rule;

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
