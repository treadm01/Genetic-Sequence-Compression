import java.util.HashMap;
import java.util.Map;

public class RuleImpl implements RuleInterface {
    private Integer ruleSize = 0;
    private Integer ruleUsage = 0;
    Map<Integer, Symbol> symbolHashMap = new HashMap<>();

    @Override
    public Integer getSize() {
        return ruleSize;
    }

    @Override
    public Integer getUseAmount() {
        return ruleUsage;
    }

    @Override
    public void incrementUse() {
        ruleUsage++;
    }

    @Override
    public void decrementUse() {
        ruleUsage--;
    }

    @Override
    public void replaceDigram() {

    }

    @Override
    public void replaceNonTerminal() {

    }

    @Override
    public Boolean checkDigram() {
        return null;
    }

    @Override
    public void addTerminal(Terminal terminal) {

    }
}
