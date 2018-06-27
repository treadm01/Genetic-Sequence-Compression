import java.util.ArrayList;
import java.util.List;

public class NonTerminal extends Symbol {
    List<Symbol> values = new ArrayList<>();
    Symbol guard;

    public NonTerminal(String representation) {
        this.representation = representation;
        left = new Terminal("?");
        right = new Terminal("?");
        //TODO set number for rule
        guard = new Terminal("!");
        guard.left = guard;
        guard.right = guard;
        values.add(new Terminal("!"));
        values.add(new Terminal("!"));
    }

    public void addNextSymbol(Symbol symbol) {
/*        symbol.right = guard;
        symbol.left = guard.right;
        guard.right.right = symbol;
        guard.right = symbol;

        // new symbol left should be whatever the right of guard is pointing to
        // new symbol right should be guard
        // guard right then new symbol
        // guard left never used?*/
        values.add(values.size() - 1, symbol);
        symbol.right = values.get(values.size() - 1);
        symbol.left = values.get(values.size() - 2);
        values.get(values.size() - 2).right = symbol;
        values.get(values.size() - 1).left = symbol;
    }

    public void addSymbols(Symbol symbol) {
        this.addNextSymbol(symbol);
        this.addNextSymbol(symbol.right);
    }

    public Symbol getLast() {
        return values.get(values.size() - 2);
    }
}
