import org.junit.Test;

import static org.junit.Assert.*;

public class ReverseComplementTest {

    //TODO remove digrams properly, sort the use of iscomplement properly, rethink how to do its a mess
    //a == t and c == g, the swapped values and reverse order
    // reverse oder first and then switch
    // so cg = gc convert cg, either either, gc -> swap cg
    @Test
    public void checkDigrams() {
        String input = "acg";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals("g t, a c, c g, ", c.printDigrams());
    }

    @Test
    public void firstRuleCreated() {
        //todo its adding the complement of the check... might be ok, but you need to remove complement, when removing something else
        String input = "acgt";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        //assertEquals("g t, a c, c g, ", c.printDigrams());
    }

    @Test
    public void nextTerminalsAdded() {
        String input = "acgtcga"; //TODO need to manage digram map, think some values added implicitly,
        //TODO need to add explicitly only because the links have changed, not even that
        // todo g2 should be here, but it is not
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        //ga, tc, gt, 2c, 22, ac, cg - missing g2
        //{AA', ac, gt, A'c, gA, cg, ga, tc}
        //assertEquals("g t, a c, c g, ", c.printDigrams());
        //0 > 2 2' c g a | 2 > a c |
    }

    @Test
    public void digramUniquenessViolation() { // next c creates a nonterminal terminal rule already implicitly added
        String input = "acgtcgac";
        Compress c = new Compress();
        c.processInput(input); // g2 is created by adding the last c, but g2 was implicitly added
        System.out.println(c.printDigrams());

        //0 > 2 2 c g 2 | 2 > a c |
        // that should be creating a new rule to look like
        // 0 > 2 4 4 | 2 > a c | 4 > 2c
        // with digrams something like {AB, B' A' , BB' , ac, gt, A'c, gA}
        //g t, 2' c, 2 4, a c, 4 4', 4' 2', g 2,
        System.out.println(c.printRules());
    }

    @Test
    public void addAterminal() {
        String input = "acgtcgacg";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printRules());
        //0 > 2 4 4' g | 4 > 2' c | 2 > a c |
    }

    @Test
    public void anotherCorrespondingRule() {
        //TODO rule utility of 4 being used only once not getting picked up,
        String input = "acgtcgacgt";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        //should be 0 > 6 6' | 6 > 2 2' c | 2 > a c |
    }


    //a == t and c == g, the swapped values and reverse order
    // reverse oder first and then switch
    // so cg = gc convert cg, either either, gc -> swap cg
    // reverse of ag is ct so digrams added ok
    @Test
    public void longerString() { // as symbols are added and new digrams created and checked it messes up,
        // ga = tc, at = at, ta = ta, ag = ct, ga
        String input = "gataga"; // so getting to ga, which is already seen, not a complement, just straight up match
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printRules());
    }

    @Test
    public void longerStringtwo() { // as symbols are added and new digrams created and checked it messes up,
        // ga = tc, at = at, ta = ta, ag = ct, ga
        String input = "gagcattacgatcagctagcta"; // so getting to ga, which is already seen, not a complement, just straight up match
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printRules());
    }
}
