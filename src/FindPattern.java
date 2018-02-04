
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindPattern {
    private Map<String, String> grammars;
    private Map<String, Integer> patternList;
    private List<String> patterns;

    public List<String> getRepeat(String input) {
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

    public void initGrammar(String input) {
        grammars = new HashMap<>();
        grammars.putIfAbsent("S", input);
    }
}
