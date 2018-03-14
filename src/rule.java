import java.util.ArrayList;
import java.util.List;

/**
 * class for the main rules to list of symbols
 */
public class rule {
    static int ruleNumber = 0;
    Integer number;
    public List<symbol> values = new ArrayList<>(); // the terminals and nonterminals in the rule
    bigram currentBigram;

    // number for the rule
    public rule() {
        this.number = ruleNumber;
        ruleNumber++;
    }

    /**
     * check the last two values for repetition, false if nothing found
     * @return
     */
    public boolean checkBigram() {
        // not enough to check return false
        if (values.size() <= 1) {
            return false;
        }
        else {
            List<bigram> lstB = new ArrayList<>();

            for (int i = 0; i < values.size() - 2; i++) {
                bigram b = new bigram(values.get(i), values.get(1+i));
                lstB.add(b);
            }

            bigram actualB = new bigram(values.get(values.size() - 2), values.get(values.size() - 1));
            setCurrentBigram(actualB); // clean up
            // keep list of bigrams instead????

            return lstB.contains(actualB); // if bigram is repeated return true
        }
    }

    public void updateRule(rule r) {
        List<bigram> lstB = new ArrayList<>();

        for (int i = 0; i < values.size() - 1; i++) {
            bigram bi = new bigram(values.get(i), values.get(1+i));
            lstB.add(bi);
        }

        bigram ruleBigram = r.currentBigram;

        for (int i = lstB.size() -1; i > -1; i--) {
            if (lstB.get(i).equals(ruleBigram)) {
                values.remove(i+1);
                values.remove(i);
                char c = r.getRuleNumber().toString().toCharArray()[0];
                values.add(i, new nonTerminal(c));
            }
        }

        // check bigrams to rule values,  form of bigram
        // rewrite values of rule 1 where overlapped with rule number as nonterminal
        //

    }

    // add a single non-terminal at the moment to the rule
    public void addValues(char s) {
        terminal t = new terminal(s);
        values.add(t);
    }

    // adding two values from a found bigram
    public void addValues(bigram b) {
        values.add(b.first);
        values.add(b.second);
        setCurrentBigram(b); // set the bigram when creating a rule from one (too cheaty?)
    }

    public void addValues(Integer i) {
        char c = i.toString().toCharArray()[0];
        nonTerminal nt = new nonTerminal(c);
    }

    //just a quick method to get the values, using for checking
    //MAKE A TO PRINT
    public void getValues() {
        System.out.print(this.getRuleNumber() + " -> ");
        for (symbol i : values) {
            System.out.print(i.getRepresentation() + " " );
        }
        System.out.println();
    }

    public bigram getCurrentBigram() {
        return currentBigram;
    }

    public void setCurrentBigram(bigram currentBigram) {
        this.currentBigram = currentBigram;
    }

    public Integer getRuleNumber() {
        return this.number;
    }
}
