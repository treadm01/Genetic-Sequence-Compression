import java.util.HashMap;
import java.util.Map;

public class Compress {
    Map<Symbol, Symbol> digramMap = new HashMap<>();

    public void processInput(String input) {
        NonTerminal firstRule = new NonTerminal();
        for (int i = 0; i < input.length(); i++) {
            firstRule.addNextSymbol(new Terminal(input.substring(i, i + 1)));
        }

        System.out.println(firstRule.values);
    }
}
