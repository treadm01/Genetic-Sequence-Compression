import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ReverseComplementTest {

    //TODO remove digrams properly, sort the use of iscomplement properly, rethink how to do its a mess
    //a == t and c == g, the swapped values and reverse order
    // reverse oder first and then switch
    // so cg = gc convert cg, either either, gc -> swap cg

    @Test
    public void checkReverseComplement() {
        Compress c = new Compress();
        Terminal tl = new Terminal('a');
        Terminal tr = new Terminal('c');
        tr.assignLeft(tl);
        assertEquals("t", c.getReverseComplement(tr).toString());
    }

    @Test
    public void checkReverseComplement2() {
        Compress c = new Compress();
        Terminal tl = new Terminal('a');
        Terminal tr = new Terminal('c');
        tr.assignLeft(tl);
        assertEquals("g", c.getReverseComplement(tr).getLeft().toString());
    }

    @Test
    public void checkLeftComplement2() {
        Compress c = new Compress();
        Terminal tl = new Terminal('a');
        Terminal tr = new Terminal('c');
        tr.assignLeft(tl);
        assertEquals("a", c.getReverseComplement(tr).complement.toString());
    }

    @Test
    public void checkRightComplement2() {
        Compress c = new Compress();
        Terminal tl = new Terminal('a');
        Terminal tr = new Terminal('c');
        tr.assignLeft(tl);
        assertEquals("c", c.getReverseComplement(tr).getLeft().complement.toString());
    }

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
    public void checkDigrams2() {
        String input = "acga";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals("g a, t c, g t, a c, c g, ", c.printDigrams());
    }

    @Test
    public void removeDigrams() {
        String input = "acga";
        Compress c = new Compress();
        c.processInput(input);
        Terminal tl = new Terminal('a');
        Terminal tr = new Terminal('c');
        tr.assignLeft(tl);
        Terminal t = (Terminal) c.digramMap.get(tr);
        c.removeDigrams(t); // should remove ac and gt
        assertEquals("g a, t c, c g, ", c.printDigrams());
    }

    @Test
    public void testUsingOriginalDigram() {
        String input = "acga";
        Compress c = new Compress();
        c.processInput(input);
        Terminal tl = new Terminal('t');
        Terminal tr = new Terminal('c');
        tr.assignLeft(tl); // sending in tc which was implicitly added, should return ga
        assertEquals("a", c.getOriginalDigram(tr).toString());
        assertEquals("g", c.getOriginalDigram(tr).getLeft().toString());
    }

    @Test
    public void testNotReverse() {
        String input = "acga";
        Compress c = new Compress();
        c.processInput(input);
        Terminal tl = new Terminal('a');
        Terminal tr = new Terminal('c');
        tr.assignLeft(tl); // sending in tc which was implicitly added, should return ga
        assertEquals("a", c.getOriginalDigram(tr).getLeft().toString());
        assertEquals("c", c.getOriginalDigram(tr).toString());
    }

    @Test
    public void firstRuleCreated() {
        String input = "acgt";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printRules());
        assertEquals("0 > 2 2' | 2 > a c | ", c.printRules());
    }

    @Test
    public void firstRuleCreatedDigramsRemoved() {
        String input = "acgt";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals("g t, 2 2', a c, ", c.printDigrams());
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
        // 0 > 2 4 4' | 2 > a c | 4 > 2c
        // with digrams something like {AB, B' A' , BB' , ac, gt, A'c, gA} -> c4' not being removed....
        //g t, 2' c, 2 4, a c, 4 4', 4' 2', g 2,
        System.out.println(c.printRules());
    }

    @Test
    public void addAterminal() {
        String input = "acgtcgacg";
        Compress c = new Compress();
        c.processInput(input); // is 2 4 reverse of 4' 2' really?
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        //0 > 2 4 4' g | 4 > 2' c | 2 > a c |
    }

    @Test
    public void anotherCorrespondingRule() {
        //TODO need to remove overlapping c 4'
        String input = "acgtcgacgt";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        // looks like old nonterminal digrams are not being removed
        //g t, 2' c, 2 4, 2 2', a c, 4' 2', g 2, 6 6', -> 2 4 does not appear or 4' 2'
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
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
    }

    @Test
    public void longerStringtwo() { // as symbols are added and new digrams created and checked it messes up,
        // ga = tc, at = at, ta = ta, ag = ct, ga
        String input = "gagcattacgatcagctagcta"; // so getting to ga, which is already seen, not a complement, just straight up match
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
    }


    @Test
    public void decompress() {
        String input = "acgt";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void decompress2() {
        String input = "acgtcgac";
        //4' needs to convert 2'c to g2, ie gac
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void decompress3() {
        String input = "acgtcgacgt";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void decompress4() {
        String input = "gataga";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void decompress5() {
        String input = "gagcattacgatcagctagcta";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test //taaattaatt
    public void decompress6() {
        String input = "ttaaattaatt";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void decompressX() {
        String input = "ttaaatt"; // registering that 2a is the same as 2'a, or t2, because terminal equals does not check value of nonterminals
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void decompressX1() {
        //todo next symbol being g gets a hit on tg or ca, but these no longer occur in the grammar
        //todo remove digrams from map properly, kind of fixed for nonterminals, not for terminals it seems
        // todo ca added at beginning is it ever removed?
        // todo ca is removed but not tg, same problem, not accessing correct complement/not setting correctly
        String input = "cagagattttgagcgtg";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void testingRightHandComplementRemoval() {
        String input = "cgtgatattattccaatggctaggcatttcggtatggccctcgcc";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void decompressX2() {
        //last cc creates two instances of g 16' -> creates new rule 18' (should it be complement?) no, if it werent it would be g cc rather than ggg
        String input = "agagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgcc";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    //"agagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgccccatgggatgtcatgggaggtgg";
    @Test
    public void decompressX3() {
        String input = "cagagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgccccatgggatgtcatgggaggtggaagatttcagagtaaaaaagcaatggaggaacggagga";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    //"tatcgtctggctcgtatgacgcaggagaggtgtacttgaaagtgc"; // rule 2 not being removed
    //taaagacaaactgga
    //caagtgcaaactgg
    //acagacactt
    //gctggctca
    //aatcaatga
    //ctacctatgt
    @Test
    public void moreDecompressionBugTests() { //todo infinite recursoin of rules
        String input = "ctacctatgt"; //todo rules only used once not getting removed???
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    String genRand (int length) {
        Random rand = new Random();
        String possibleLetters = "acgt";
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(possibleLetters.charAt(rand.nextInt(possibleLetters.length())));
        return sb.toString();
    }

    @Test
    public void decompressX4() {
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            String input = genRand(rand.nextInt((10 - 1) + 1) + 1);
            System.out.println(input);
            Compress c = new Compress();
            c.processInput(input);
            System.out.println(c.printDigrams());
            System.out.println(c.printRules());
            assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
        }
    }


//
//    @Test
//    public void decompress7() {
//        Compress c = new Compress();
//        InputOutput io = new InputOutput();
//        String originalFile = io.readFile("humprtb");
//        c.processInput(originalFile);
//        System.out.println(c.printRules());
//        System.out.println(c.getFirstRule().getRuleString());
//        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
//    }


}
