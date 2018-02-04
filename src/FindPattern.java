import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindPattern {
    private Map<String, String> grammars = new HashMap<>();
    private Map<String, Integer> patternList = new HashMap<>();

    public String getRepeat(String input) {
        String found;
        for (int i = 0; i < input.length(); i++ ) {
            String s = input.substring(i, i + 1);
            patternList.add(s);
        }


        return "";
    }
}
