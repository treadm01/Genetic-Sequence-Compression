
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindPattern {
    private Map<String, String> grammars = new HashMap<>();;
    private Map<String, Integer> patternList;
    private List<String> patterns;
    private Integer count;

    public List<String> getRepeat(String input) {
        // needs to be implemented properly, symbol by symbol checking, check all grammar, proper terminal symbols etc
        patternList = new HashMap<>();
        patterns = new ArrayList<>();
        String found = null;

        for (int i = 0; i < input.length()-1; i++ ) {
            String s = input.substring(i, i + 2);

            if (patternList.containsKey(s)) {
                patternList.put(s, patternList.get(s) + 1);
            }
            patternList.putIfAbsent(s, 1);
        }

        for (String s : patternList.keySet()) {
            if (patternList.get(s) > 1) {
                patterns.add(s);
            }
        }

        return patterns;
    }

    public void applyNonTerminals() {

    }

    public void addPattern() {
        for (String x : getRepeat(grammars.get("1"))) {
            count++;
            grammars.putIfAbsent(count.toString(), x);
        }
    }

    public void initGrammar(String input) {
        count = 1;
        grammars = new HashMap<>();
        grammars.putIfAbsent("S", input.substring(0, 1));
    }

    public Map<String, String> getGrammars() {
        return this.grammars;
    }

    public void addSymbol(String input) {
        if (grammars.containsKey(input)) {
            grammars.put("S", grammars.get("S") + input);
        }
        else {
            grammars.putIfAbsent("S", input);
        }
    }
}
