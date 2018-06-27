import java.util.ArrayList;
import java.util.List;

public class NonTerminal extends Symbol {
    List<Symbol> values = new ArrayList<>();

    public NonTerminal() {
        //TODO set number for rule
        values.add(new Terminal("!"));
    }

    public void addNextSymbol(Symbol symbol) {
        values.add(symbol);
    }
}
