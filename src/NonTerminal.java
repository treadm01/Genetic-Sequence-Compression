import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * non terminal regular symbols that don't go anywhere
 */

public class NonTerminal extends Symbol {

    static Integer ruleNumber = 0;
    Integer number; // what number??? The terminal/rule number
    Integer useNumber = 0; // number of uses
    public List<Symbol> values = new ArrayList<>(); // the terminals and nonterminals in the rule
    //Map<Integer, Symbol> values = new HashMap<>();
    Map<Pair<Integer, Integer>, Bigram> bigramMap = new HashMap<>();
    //List<Bigram> bigramList = new ArrayList<>();
    Bigram currentBigram;
    public List<Integer> usedByList = new ArrayList<>();

    /**
     * the actual symbol
     * @param
     */
    public NonTerminal() {//String s) {
        number = ruleNumber;
        this.representation = number.toString();
        ruleNumber++;
    }

    public NonTerminal(int i) {
        number = i;
        this.representation = number.toString();
    }

    public Integer getRuleNumber() {
        return this.number;
    }

//    public void updateBigrams() {
//        if (values.size() > 1) {
//            int left = values.size() - 2;
//            int right = values.size() - 1;
//            Bigram bi = new Bigram(values.get(left), values.get(right));
//            Pair p = new Pair(left, right);
//            bigramMap.putIfAbsent(p, bi);
//        }
//    }



    // add a single non-terminal at the moment to the rule
    public void addValues(Terminal t) {
        values.add(t);
    }

    // adding two values from a found bigram
    public void addValues(Bigram b) {

        if (b.first instanceof NonTerminal) {
            ((NonTerminal) b.first).usedByList.add(this.number); // add this rules number to the terminals list to keep a record of where it is used
            ((NonTerminal) b.first).useNumber++;
        }

        if (b.second instanceof NonTerminal) {
            ((NonTerminal) b.second).usedByList.add(this.number);
            ((NonTerminal) b.second).useNumber++;
        }

        values.add(b.first);
        values.add(b.second);
        setCurrentBigram(b); // set the bigram when creating a rule from one (too cheaty?)
    }

    // only used by decompress??
    public void addValues(NonTerminal nt) {
//        NonTerminal nt = new NonTerminal(i);
        values.add(nt); // only just added this line after uncompressing from binary, check how this
        // code has been working - probably not used, convert to string and send that??
    }

    // similar to add values in that it is changing values, but replacing nonTerminal with
    // whatever it points to
    public void replaceNonTerminal(NonTerminal t) {
        System.out.println(values.indexOf(t));
        values.addAll(values.indexOf(t), t.values);
        values.remove(t);
    }

    @Override
    public String toString() {
        return this.representation;
    }

    public void setCurrentBigram(Bigram currentBigram) {
        this.currentBigram = currentBigram;
    }

    //just a quick method to get the values, using for checking
    //MAKE A TO PRINT
    public String getValues() {
        String valueOuput = "";
        valueOuput += this.getRuleNumber() + " -> ";
        for (Symbol i : values) {
            valueOuput += i.toString() + " ";
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
            Bigram actualB = new Bigram(values.get(values.size() - 2), values.get(values.size() - 1));
            setCurrentBigram(actualB); // clean up
            return getBigrams().contains(actualB); // if bigram is repeated return true
        }
    }

    //TODO - REMOVE USE LIST OF BIGRAMS, USE HASH - GETBIGRAMS
    public List<Bigram> getBigrams() {
        // THIS IS PROBABLY THE PROBLEM... CREATING NEW LIST EACH TIME JUST TO SEE IF REPEATED..
        List<Bigram> lstB = new ArrayList<>();

        // THIS DOES NOT GIVE BACK BIGRAMS FOR GROUPS OF THREE
        // OR EVER THE LAST BIGRAM WHICH WAS THE ORIGINAL INTENTION
        // BUT MESSES UP IF USED ELSEWHERE
        for (int i = 0; i < values.size() - 3; i++) { // set to 3 to stop bigrams being formed of any element of the actual final last pair..... breaks where else??
            Bigram b = new Bigram(values.get(i), values.get(1 + i));
            lstB.add(b);
        }

        return lstB;
    }

    //TODO - REMOVE USE LIST OF BIGRAMS, USE HASH UPDATE RULE
    public void updateRule(NonTerminal r) {
        // THIS ALSO A BIG PROBLEM, CREATING A LIST TO CHECK AGAINST....
        // AND COMPUTE ON 'HEAVILY'
        List<Bigram> lstB = new ArrayList<>();

        for (int i = 0; i < values.size() - 1; i++) {
            Bigram bi = new Bigram(values.get(i), values.get(1 + i));
            lstB.add(bi);
        }

        Bigram ruleBigram = r.currentBigram;

        for (int i = lstB.size() -1; i > -1; i--) {
            if (lstB.get(i).equals(ruleBigram)) {

                // really really bad, if half of a bigram has been changed alter the one made earlier
                // in the list by setting it's right hand to ! or whatever, also have to check that
                // not at the bottom of the list
                if(i-1 >= 0 ) {
                    lstB.get(i-1).second = new Terminal("!");
                }

                // decrementing the use count of nonTerminals
                if (values.get(i+1) instanceof NonTerminal) {
                    ((NonTerminal) values.get(i+1)).usedByList.remove(this.number);
                    ((NonTerminal) values.get(i+1)).useNumber--;
                }

                // decrementing the use count of nonTerminals
                if (values.get(i) instanceof NonTerminal) {
                    ((NonTerminal) values.get(i)).usedByList.remove(this.number);
                    ((NonTerminal) values.get(i)).useNumber--;
                }

                values.remove(i+1);
                values.remove(i);
                values.add(i, r);
                r.useNumber++;
                r.usedByList.add(this.number);
            }
        }
    }

    //TODO - REMOVE USE LIST OF BIGRAMS, USE HASH - GET ALL BIGRAMS
    public List<Bigram> getAllBigrams() {
        // THIS CREATES A NEW LIST OF BIGRAMS EACH TIME! WHY??
        List<Bigram> lstB = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            Bigram bi = new Bigram(values.get(i), values.get(1 + i));
            lstB.add(bi);
        }
        return lstB;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {return true;}
        else {return false;}
    }

    public Bigram getCurrentBigram() {
        return currentBigram;
    }

    //USE THIS!
    public void setRuleNumber(Integer rn) {
        this.number = rn;
    }

    public void getBigramMap() {
        for (Bigram b : bigramMap.values()) {
            System.out.println("bigram left value = " + b.first);
            System.out.println("bigram right value = " + b.second);
        }
    }
}
