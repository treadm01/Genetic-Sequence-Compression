
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindPattern {
    private Map<String, String> grammars = new HashMap<>();;
    int charIndex = 65;
    String sequence = "";


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

    public Integer checkForPattern(String digram) {
        Integer count = 0;
        for (int i = 0; i < grammars.get("S").length()-1; i++) {
            if (grammars.get("S").substring(i, i + 2).equals(digram)) {
                count++;
            }
        }

        return count;
        // check other grammars first
        // check s
    }

    public String checkForFoundPattern(String digram) {
        // because S is part of the same map have to check al lbut one... replace S as a string on its own??
        String symbol = "S";
        for (String s : grammars.keySet()) {
            if (s != "S") {
                if (grammars.values().contains(digram)) {
                    symbol = s;
                }
            }
        }
        return symbol;
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
            String digram = getDigram();
            // check for pattern within itself and update if necessary
            if (checkForPattern(digram) > 1) {
                grammars.put("S", grammars.get("S").replaceAll(digram, String.valueOf((char)charIndex)));
                grammars.put(String.valueOf((char)charIndex), digram); // made a new rule
                charIndex++; //update next grammar symbol
            }

            // check for pattern in previous found patterns
            String s = checkForFoundPattern(digram);
            if (s != "S") {
                grammars.put("S", grammars.get("S").replaceAll(digram, s));
            }

            digram = getDigram();
            // check for pattern within itself and update if necessary
            if (checkForPattern(digram) > 1) {
                grammars.put("S", grammars.get("S").replaceAll(digram, String.valueOf((char)charIndex)));
                grammars.put(String.valueOf((char)charIndex), digram); // made a new rule
                charIndex++; //update next grammar symbol
            }

            System.out.println(grammars.toString());
            //checkForPattern(getDigram());
        }
    }
}
