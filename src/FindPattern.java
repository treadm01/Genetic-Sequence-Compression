
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List<Integer> checkForPattern(String digram) {
        List listOfIndex = new ArrayList();
        for (int i = 0; i < grammars.get("S").length()-1; i++) {
            if (grammars.get("S").substring(i, i + 2).equals(digram)) {
                listOfIndex.add(i);
            }
        }

        if (listOfIndex.size() > 1) {
            System.out.println("match");
        }
        return listOfIndex;
        // check other grammars first
        // check s
    }

    public void createRule() {

    }

    public void updateGrammar(String input) {
        // add symbol
        // check digram
        //
        addSymbol("S", input.substring(0, 1));
        for (int i = 1; i < input.length(); i++) {
            addSymbol("S", input.substring(i, i + 1));
            checkForPattern(getDigram());
            System.out.println(grammars.toString());
            //checkForPattern(getDigram());
        }
    }
}
