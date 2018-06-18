import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NonTerminalTest {

    NonTerminal nt = new NonTerminal();

    @Test
    public void updateBigramsTerminal() {
        nt.addValues(new Terminal("a"));
        nt.addValues(new Terminal("b"));
        nt.addValues(new Terminal("b"));

        //nt.getBigramMap();
    }

    @Test
    public void updateBigramsBirgram() {
//        nt.addValues(1);
//        nt.addValues(2);
//        nt.addValues(3);
//        nt.addValues(4);
//        nt.addValues(5);
//        nt.addValues(6);

        //nt.getBigramMap();
    }

    @Test
    public void removeBigram() {
        int indexToRemove = 3;
//        nt.addValues(1);
//        nt.addValues(2);
//        nt.addValues(3);
//        nt.addValues(4);
//        nt.addValues(5);
//        nt.addValues(6);
//
//
//        Pair<Integer, Integer> oL = new Pair(indexToRemove-1, indexToRemove);
//        Pair<Integer, Integer> oR = new Pair(indexToRemove, indexToRemove + 1);
//        nt.bigramMap.remove(oR);
//        Digram b = new Digram(nt.values.get(indexToRemove-1), nt.values.get(indexToRemove + 1));
//        nt.bigramMap.replace(oL, b);
//        nt.values.remove(indexToRemove);
//        System.out.println(nt.values);
//        System.out.println(nt.bigramMap.size());
//
//       // nt.getBigramMap();

        //assertEquals(nt.values//.size()-1, nt.bigramMap.size());
    }

    @Test
    public void replaceNonTerminal() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        NonTerminal nt = new NonTerminal(1);
        nt.addValues(a);
        nt.addValues(b);
        NonTerminal ntTwo = new NonTerminal(2);
        ntTwo.addValues(nt);

        List<Symbol> expected = new ArrayList<>();
        expected.add(a);
        expected.add(b);

        ntTwo.replaceNonTerminal(nt);

        assertEquals(expected.toString(), ntTwo.values.toString());
    }

    @Test
    public void rebuildBiList() {
        Terminal a = new Terminal("a");
        Terminal b = new Terminal("b");
        NonTerminal ntOne = new NonTerminal(1);
        ntOne.addValues(a);
        ntOne.addValues(b);

        Terminal c = new Terminal("c");
        NonTerminal ntTwo = new NonTerminal(2);
        ntTwo.addValues(c);
        ntTwo.addValues(b);

        NonTerminal ntThree = new NonTerminal(3);
        ntThree.addValues(ntOne); // a nonterminal with a nonterminal pointing to ab
        ntThree.addValues(ntTwo);
        ntThree.addValues(ntOne);

        // ntThree is a nonterminal 3 -> 1 2 1
        // with bigrams 1 2 and 2 1
        // want to replace 2 with cb, so 3 -> 1 c b 1

        ntThree.printBigram(ntThree.digramList);

        ntThree.replaceNonTerminal(ntTwo);
        System.out.println("BREAK");

        ntThree.printBigram(ntThree.digramList);


    }
}