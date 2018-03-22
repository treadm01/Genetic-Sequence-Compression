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
     * @param
     */
    public nonTerminal() {//String s) {
        number = ruleNumber;
        this.representation = number.toString();
        ruleNumber++;
    }

    public nonTerminal(int i) {//String s) {
        number = i;
        this.representation = number.toString();
    }

    public Integer getRuleNumber() {
        return this.number;
    }

    // add a single non-terminal at the moment to the rule
    public void addValues(String s) {
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
        nonTerminal nt = new nonTerminal(i);
        values.add(nt); // only just added this line after uncompressing from binary, check how this
        // code has been working - probably not used, convert to string and send that??
    }

    /**
     * quick way to get the symbol
     * @return
     */
    public String getRepresentation() {
        return this.representation;
    }

    public void setCurrentBigram(bigram currentBigram) {
        this.currentBigram = currentBigram;
    }

    //just a quick method to get the values, using for checking
    //MAKE A TO PRINT
    public String getValues() {
        String valueOuput = "";
        valueOuput += this.getRuleNumber() + " -> ";
        for (symbol i : values) {
            valueOuput += i.getRepresentation() + " ";
        }
        return valueOuput;
    }

    /**
     * check the last two values for repetition, false if nothing found
     * @return
     */
    public boolean checkBigram() {
        if (values.size() <= 3) {
            return false;
        }
        else {
            bigram actualB = new bigram(values.get(values.size() - 2), values.get(values.size() - 1));
            setCurrentBigram(actualB); // clean up
            // keep list of bigrams instead????
            // YOU NEED TO LOOK INTO PROPER COMPARE AND EQUALS HASHCODE ETC
            return getBigrams().contains(actualB); // if bigram is repeated return true
        }
    }

    public List<bigram> getBigrams() {
        List<bigram> lstB = new ArrayList<>();

        // THIS DOES NOT GIVE BACK BIGRAMS FOR GROUPS OF THREE
        // OR EVER THE LAST BIGRAM WHICH WAS THE ORIGINAL INTENTION
        // BUT MESSES UP IF USED ELSEWHERE
        for (int i = 0; i < values.size() - 3; i++) { // set to 3 to stop bigrams being formed of any element of the actual final last pair..... breaks where else??
            bigram b = new bigram(values.get(i), values.get(1 + i));
            lstB.add(b);
        }

        return lstB;
    }

    public void updateRule(nonTerminal r) {
        List<bigram> lstB = new ArrayList<>();

        for (int i = 0; i < values.size() - 1; i++) {
            bigram bi = new bigram(values.get(i), values.get(1 + i));
            lstB.add(bi);
        }

        bigram ruleBigram = r.currentBigram;

        //System.out.println("bigram you are checking is " + r.currentBigram.first.getRepresentation() + " " + r.currentBigram.second.getRepresentation());

        for (int i = lstB.size() -1; i > -1; i--) {
//            System.out.println(lstB.get(i).first.getRepresentation());
            //          System.out.println(lstB.get(i).second.getRepresentation());
            if (lstB.get(i).equals(ruleBigram)) {


                // really really bad, if half of a bigram has been changed alter the one made earlier
                // in the list by setting it's right hand to ! or whatever, also have to check that
                // not at the bottom of the list
                if(i-1 >= 0 ) {
                    lstB.get(i-1).second = new terminal("!");
                }

                values.remove(i+1);
                values.remove(i);
                values.add(i, r);
            }
        }
    }

    public List<bigram> getAllBigrams() {
        List<bigram> lstB = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            bigram bi = new bigram(values.get(i), values.get(1 + i));
            lstB.add(bi);
        }
        return lstB;
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

    public bigram getCurrentBigram() {
        return currentBigram;
    }

    public void setRuleNumber(Integer rn) {
        this.number = rn;
    }
}
