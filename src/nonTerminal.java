import java.util.ArrayList;
import java.util.List;

/**
 * non terminal regular symbols that don't go anywhere
 */

public class nonTerminal extends symbol {

    static Integer ruleNumber = 0;
    Integer number;
    Integer useNumber = 0;
    Integer ruleSize;
    public List<symbol> values = new ArrayList<>(); // the terminals and nonterminals in the rule
    bigram currentBigram;

    /**
     * the actual symbol
     * @param s
     */
    public nonTerminal(String s) {
        this.representation = ruleNumber;
    }

    /**
     * quick way to get the symbol
     * @return
     */
    public String getRepresentation() {
        return this.representation;
    }


    @Override
    public boolean equals(Object o) {
        nonTerminal t = null;
        if (!(o instanceof nonTerminal) && !(o instanceof terminal)) {
            throw new ClassCastException("Must be nontermin. Received " + o.getClass());
        }
        else if (o instanceof terminal) {
            return false;
        }
        else {
            t = (nonTerminal) o;
        }

        return  (t.getRepresentation().equals(this.getRepresentation()));
    }

}
