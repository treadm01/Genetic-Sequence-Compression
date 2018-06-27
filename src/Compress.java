import java.util.HashMap;
import java.util.Map;

public class Compress {
    Map<Symbol, Symbol> digramMap = new HashMap<>();
    Map<NonTerminal, NonTerminal> nonTerminalMap = new HashMap<>();

    public void processInput(String input) {
        NonTerminal firstRule = new NonTerminal("0");
        nonTerminalMap.put(firstRule, firstRule);
        for (int i = 0; i < input.length(); i++) {
            firstRule.addNextSymbol(new Terminal(input.substring(i, i + 1)));

            if (digramMap.containsKey(firstRule.getLast())) {
                NonTerminal newRule = new NonTerminal(String.valueOf(nonTerminalMap.size()));
                newRule.addSymbols(firstRule.getLast());
                digramMap.put(newRule, newRule);
            }
            else {
                digramMap.put(firstRule.getLast(), firstRule.getLast());
            }


        }

        System.out.println(digramMap);
        System.out.println(firstRule.values);

    }
}
