import java.util.HashMap;
import java.util.Map;

public class CompressTwo {
    Map<Integer, RuleImpl> ruleMap = new HashMap<>();
    Map<Digram, NonTerminalTwo> digramMap = new HashMap<>();

    public void processInput(String input) {
        RuleImpl mainRule = new RuleImpl();
        ruleMap.put(ruleMap.size(), mainRule);
        for (int i = 0; i < input.length(); i++) {
            String ch = input.substring(i, i+1);
            mainRule.addTerminal(new Terminal(ch));
            if (mainRule.getSize() > 1) {
                if (mainRule.checkDigram()){
                    RuleImpl ri = new RuleImpl();
                    ri.addTerminal(mainRule.getTail().first);
                    ri.addTerminal(mainRule.getTail().second);
                    NonTerminalTwo nonTerminal = new NonTerminalTwo(String.valueOf(ruleMap.size()));
                    nonTerminal.setRule(ri);
                    digramMap.put(mainRule.getTail(), nonTerminal);
                    mainRule.replaceDigram(nonTerminal);
                    ruleMap.put(ruleMap.size(), ri);
                }
                else if (digramMap.containsKey(mainRule.getTail())) {
                    mainRule.replaceDigram(digramMap.get(mainRule.getTail()));
                    if (mainRule.checkDigram()){
                        RuleImpl ri = new RuleImpl();
                        ri.addTerminal(mainRule.getTail().first);
                        ri.addTerminal(mainRule.getTail().second);
                        NonTerminalTwo nonTerminal = new NonTerminalTwo(String.valueOf(ruleMap.size()));
                        nonTerminal.setRule(ri);
                        digramMap.put(mainRule.getTail(), nonTerminal);
                        mainRule.replaceDigram(nonTerminal);
                        ruleMap.put(ruleMap.size(), ri);
                    }
                }
            }
        }
        System.out.println(digramMap);
        System.out.println(mainRule.getSymbolHashMap());
        System.out.println(mainRule.getNonTerminalHashMap());
        System.out.println(ruleMap);
    }

}
