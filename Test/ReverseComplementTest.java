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
        assertEquals("0 > 2 2' c g a | 2 > a c | ", c.printRules());
        assertEquals("g a, t c, g t, 2' c, 2 2', a c, g 2, c g, ", c.printDigrams());

    }

    //TODO CREATING NEW TERMINAL FROM REVERSE COMPLEMENTS GETTING NULL WHILST CHECKING FOR EXISTING RULE
    @Test
    public void digramUniquenessViolation() { // next c creates a nonterminal terminal rule already implicitly added
        String input = "acgtcgac";
        Compress c = new Compress();
        c.processInput(input); // g2 is created by adding the last c, but g2 was implicitly added
        assertEquals("0 > 2 4 4' | 4 > 2' c | 2 > a c | ", c.printRules());
    }

    @Test
    public void digramUniquenessViolationCheckMap() { // next c creates a nonterminal terminal rule already implicitly added
        String input = "acgtcgac";
        Compress c = new Compress(); // should not have tc
        c.processInput(input); // g2 is created by adding the last c, but g2 was implicitly added
        assertEquals("g t, 2' c, 2 4, a c, 4 4', 4' 2', g 2, ", c.printDigrams());
    }

    @Test
    public void addAterminal() {
        String input = "acgtcgacg";
        Compress c = new Compress();
        c.processInput(input); // is 2 4 reverse of 4' 2' really?
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals("0 > 2 4 4' g | 4 > 2' c | 2 > a c | ", c.printRules());
    }

    @Test
    public void anotherCorrespondingRule() {
        //TODO need to remove overlapping c 4'
        String input = "acgtcgacgt";
        Compress c = new Compress();
        c.processInput(input);
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
        assertEquals("0 > 6 6' | 6 > 2 2' c | 2 > a c | ", c.printRules());
    }

    @Test
    public void anotherCorrespondingRuleCheckDigrams() {
        String input = "acgtcgacgt"; // old digrams from removed rules not removed
        Compress c = new Compress();
        c.processInput(input);
        assertEquals("g t, 2' c, 2 2', a c, g 2, 6 6', ", c.printDigrams());
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
        assertEquals("g a, t c, t a, a 2', a 2, 2 t, 2' t, ", c.printDigrams());
        assertEquals("0 > 2 t a 2 | 2 > g a | ", c.printRules());
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
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void decompress2() {
        String input = "acgtcgac";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void decompress3() {
        String input = "acgtcgacgt";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void decompress4() {
        String input = "gataga";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void decompress5() {
        String input = "gagcattacgatcagctagcta";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test //taaattaatt
    public void decompress6() {
        String input = "ttaaattaatt";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void decompressX() {
        String input = "ttaaatt"; // registering that 2a is the same as 2'a, or t2, because terminal equals does not check value of nonterminals
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void decompressX1() {
        String input = "cagagattttgagcgtg";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void testingRightHandComplementRemoval() {
        String input = "cgtgatattattccaatggctaggcatttcggtatggccctcgcc";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void decompressX2() {
        //last cc creates two instances of g 16' -> creates new rule 18' (should it be complement?) no, if it werent it would be g cc rather than ggg
        String input = "agagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgcc";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    //"agagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgccccatgggatgtcatgggaggtgg";
    @Test
    public void decompressX3() {
        String input = "cagagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgccccatgggatgtcatgggaggtggaagatttcagagtaaaaaagcaatggaggaacggagga";
        Compress c = new Compress();
        c.processInput(input);
        assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void moreDecompressionBugTests() {
        String input = "ctacctatgt";
        Compress c = new Compress();
        c.processInput(input);
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

    //tttcgaaaag
    @Test
    public void decompressX4() {
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            String input = genRand(rand.nextInt((10 - 1) + 1) + 1);
            System.out.println(input);
            Compress c = new Compress();
            c.processInput(input);
            assertEquals(input, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
        }
    }

    //todo bug in hash map management on certain strings
    @Test
    public void decompress7() {
        Compress c = new Compress();
        c.processInput("tttcgaaaag");
        assertEquals("tttcgaaaag", c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void writeFile() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("15000");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void chmpxx() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("chmpxx");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void chntxx() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("chntxx");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void hehcmv() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("hehcmv");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void humdyst() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void humghcs() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humghcs");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void humhbb() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humhbb");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void vaccg() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("vaccg");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void mtpacga() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("mtpacga");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void mpomtcg() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("mpomtcg");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void humprtb() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humprtb");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void humhdab() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humhdab");
        c.processInput(originalFile);
        assertEquals(originalFile, c.decompress(c.getFirstRule(), c.getFirstRule().isComplement));
    }


}
