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
    Map<Pair<Integer, Integer>, Bigram> bigramMap = new HashMap<>();
    List<Bigram> bigramList = new ArrayList<>();
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

    //TODO should just be one method, not rebuilding list... not for looping through list
    public void rebuildBiList() {
        bigramList.clear();
        for (int i = 0; i < values.size() - 1; i++) {
            Bigram bi = new Bigram(values.get(i), values.get(1 + i));
            bigramList.add(bi);
        }
    }

    public void updateBiList() {
        if (values.size() > 1) {
            int left = values.size() - 2;
            int right = values.size() - 1;
            Bigram bi = new Bigram(values.get(left), values.get(right));
            bigramList.add(bi);
        }
    }

    // add a single non-terminal at the moment to the rule
    public void addValues(Terminal t) {
        values.add(t);
        updateBiList();
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
        updateBiList();
    }

    // only used by decompress??
    public void addValues(NonTerminal nt) {
        values.add(nt);
        updateBiList();
    }

    // similar to add values in that it is changing values, but replacing nonTerminal with
    // whatever it points to
    public void replaceNonTerminal(NonTerminal t) {
        values.addAll(values.indexOf(t), t.values);
        values.remove(t);
        rebuildBiList();
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

            //TODO need a better method than this list dupe and remove
            List<Bigram> removedLast = new ArrayList<>(bigramList); // create a new list to check
            // those not from the most recent bigram

            removedLast.remove(removedLast.size()-1);
            removedLast.remove(removedLast.size()-1);

            long count = removedLast.stream()
                    .filter(x -> x.equals(actualB))
                    .count();

            return count > 0;//getBigrams().contains(actualB); // if bigram is repeated return true
        }
    }

    //TODO - REMOVE USE LIST OF BIGRAMS, USE HASH UPDATE RULE
    public void updateRule(NonTerminal r) {
        Bigram ruleBigram = r.currentBigram;

        // TODO yes, no loop of entire list
        for (int i = bigramList.size() -1; i > -1; i--) {
            if (bigramList.get(i).equals(ruleBigram)) {

                // really really bad, if half of a bigram has been changed alter the one made earlier
                // in the list by setting it's right hand to ! or whatever, also have to check that
                // not at the bottom of the list
                if(i-1 >= 0 ) {
                    bigramList.get(i-1).second = new Terminal("!");
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
                //TODO shouldn't use rebuild list
                rebuildBiList();
                r.useNumber++;
                r.usedByList.add(this.number);
            }
        }
    }

    //TODO USE HASH RATHER THAN LIST
    public List<Bigram> getAllBigrams() {
        return bigramList;
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

    public void printBigram(List<Bigram> lstB) {
        for (Bigram b : lstB) {
            System.out.println("bigram left value = " + b.first);
            System.out.println("bigram right value = " + b.second);
        }
    }
}
