
import java.util.HashMap;
import java.util.Map;

public class FindPattern {
    private Map<String, String> grammars = new HashMap<>();;


    public Map<String, String> getGrammars() {
        return this.grammars;
    }

    /**
     * add new symbol from input
     * @param NonTerminal
     * @param input
     */
    public void addSymbol(String NonTerminal, String input) {
        if (grammars.containsKey(NonTerminal)) {
            grammars.put(NonTerminal, grammars.get(NonTerminal) + input);
        }
        else {
            grammars.putIfAbsent(NonTerminal, input);
        }
    }

    /**
     * get the last two symbols of input
     * @return
     */
    public String getDigram() {
        String previousSymbol = grammars.get("S").substring(grammars.get("S").length()-2);
        return previousSymbol;
    }

    public int checkForPattern(String digram) {
        int count = 0;
        for (int i = 0; i < grammars.get("S").length()-1; i++) {
            if (grammars.get("S").substring(i, i + 2).equals(digram)) {
                count++;
            }
        }
        return count;
        // check other grammars first
        // check s
    }
}
