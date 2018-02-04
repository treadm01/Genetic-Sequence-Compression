import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindPattern {
    private Map<String, String> grammars = new HashMap<>();
    private Map<String, Integer> patternList = new HashMap<>();

    public String getRepeat(String input) {
        String found = null;
        for (int i = 0; i < input.length(); i++ ) {
            String s = input.substring(i, i + 1);
            patternList.putIfAbsent(s, 1);

            if (patternList.containsKey(s)) {
                patternList.put(s, patternList.get(s) + 1);
            }
        }

        for (String s : patternList.keySet()) {
            if (patternList.get(s) > 1) {
                found = s;
            }
        }

        return found;
    }
}
