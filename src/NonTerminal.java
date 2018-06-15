import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * non terminal regular symbols that don't go anywhere
 */
public class NonTerminal extends Symbol {
    //TODO too much, need to split up class
    static Integer ruleNumber = 0;
    Integer number; // what number??? The terminal/rule number
    Integer useNumber = 0; // number of uses
    public List<Symbol> values = new ArrayList<>(); // the terminals and nonterminals in the rule
    Map<Pair<Integer, Integer>, Bigram> bigramMap = new HashMap<>();
    List<Bigram> bigramList = new ArrayList<>();
    Bigram currentBigram;
    public List<Integer> usedByList = new ArrayList<>();

    /**
     * constructors for terminal, if generic create new rule based on current rule amount
     * @param
     */
    public NonTerminal() {//String s) {
        number = ruleNumber;
        this.representation = number.toString();
        ruleNumber++;
    }

    /**
     * create a specific nonTerminal from a number
     * @param terminalNumber
     */
    public NonTerminal(int terminalNumber) {
        number = terminalNumber;
        this.representation = number.toString();
    }

    /**
     * should be removed or changed to work without rebuilding the list
     * currently rebuilds entire bigram list when the elements that this
     * nonterminal points to are changed somewhere other than the end, eg
     * a nonterminal is removed and replaced with two terminals etc
     */
    //TODO should just be one method, not rebuilding list... not for looping through list
    public void rebuildBiList() {
        bigramList.clear();
        for (int i = 0; i < values.size() - 1; i++) {
            Bigram bi = new Bigram(values.get(i), values.get(1 + i));
            bigramList.add(bi);
        }
    }

    /**
     * updates the bigram list just at the end when a new terminal or
     * nonterminal is added
     */
    public void updateBiList() {
        if (values.size() > 1) {
            int left = values.size() - 2;
            int right = values.size() - 1;
            Bigram bi = new Bigram(values.get(left), values.get(right));
            bigramList.add(bi);
        }
    }

    /**
     * add terminal to the list of symbols this nonterminal points to
     * @param terminal
     */
    // add a single non-terminal at the moment to the rule
    public void addValues(Terminal terminal) {
        values.add(terminal);
        updateBiList();
    }

    /**
     * add bigram to the list of symbols this nonterminal points to
     * @param bigram
     */
    // adding two values from a found bigram
    public void addValues(Bigram bigram) {
        if (bigram.first instanceof NonTerminal) {
            ((NonTerminal) bigram.first).usedByList.add(this.number); // add this rules number to the terminals list to keep a record of where it is used
            ((NonTerminal) bigram.first).useNumber++;
        }

        if (bigram.second instanceof NonTerminal) {
            ((NonTerminal) bigram.second).usedByList.add(this.number);
            ((NonTerminal) bigram.second).useNumber++;
        }

        values.add(bigram.first);
        values.add(bigram.second);
        setCurrentBigram(bigram); // set the bigram when creating a rule from one (too cheaty?)
        updateBiList();
    }


    /**
     * add nonTerminal to the list of symbols this nonTerminal points to
     * @param nonTerminal
     */
    // only used by decompress??
    public void addValues(NonTerminal nonTerminal) {
        values.add(nonTerminal);
        updateBiList();
    }

    /**
     * takes out a nonterminal symbol from the values list for this nonterminal
     * and replaces it with the symbols it points to
     * @param t
     */
    // similar to add values in that it is changing values, but replacing nonTerminal with
    // whatever it points to
    public void replaceNonTerminal(NonTerminal t) {
        values.addAll(values.indexOf(t), t.values);
        values.remove(t);
        rebuildBiList();
    }

    /**
     * sets the specific value of current bigram to the two most recent symbols added
     * @param currentBigram
     */
    public void setCurrentBigram(Bigram currentBigram) {
        this.currentBigram = currentBigram;
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

            return count > 0;// if the bigram is found then return true
        }
    }

    /**
     * updates the some of the values that this nonterminal points to with a nonterminal that
     * represents those values eg 1 -> a b c , replaces with 1 -> 2 c
     * @param r
     */
    //TODO - REMOVE USE LIST OF BIGRAMS, USE HASH UPDATE RULE
    public void updateRule(NonTerminal r) {
        Bigram ruleBigram = r.currentBigram;

        // TODO yes, no loop of entire list
        for (int i = bigramList.size() -1; i > -1; i--) {
            if (bigramList.get(i).equals(ruleBigram)) {

                // really really bad, if half of a bigram has been changed alter the one made earlier
                // in the list by setting it's right hand to ! or whatever, also have to check that
                // not at the bottom of the list

                // THESE ALL CONDITIONAL, JUST AVOIDED IF TERMINAL
                if(i-1 >= 0 ) {
                    bigramList.get(i-1).second = new Terminal("!");
                }

                //TODO PUT THESE TWO SAME CODE IN A METHOD
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

    public Integer getRuleNumber() {
        return this.number;
    }

    @Override
    public String toString() {
        return this.representation;
    }

    /**
     * kind og like toString but for the values, maybe incorporate or separate
     * @return
     */
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

    public void printBigram(List<Bigram> lstB) {
        for (Bigram b : lstB) {
            System.out.println("bigram left value = " + b.first);
            System.out.println("bigram right value = " + b.second);
        }
    }
}
