import GrammarCoder.Compress;
import GrammarCoder.Decompress;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class CompressTest {
    Compress c;
    Decompress d;

    @Before
    public void setUp() throws Exception {
        c = new Compress();
        d = new Decompress();
    }

    @Test
    public void processInput() { // create rules from digrams
        c.processInput("abcdbc");
        System.out.println(c.printDigrams());
        assertEquals("0 > a 2 d 2 | 2 > b c | ", c.printRules());
    }

    @Test
    public void processMatchingDigrams() { // check which digram is replaced
        c.processInput("aaabcabaa");
        c.printRules();
        assertEquals("0 > 4 2 c 2 4 | 4 > a a | 2 > a b | ", c.printRules());
    }

    @Test
    public void processMatchingDigrams2() { // check which digram is replaced
        c.processInput("aaabcabaaabc");
        c.printRules();
        assertEquals("0 > 8 2 8 | 8 > a a 2 c | 2 > a b | ", c.printRules());
    }

        @Test
        public void processInput2() { // check for and use existing rules
            System.out.println();
            c = new Compress();
            c.processInput("abcdbcabc");
            assertEquals("0 > 4 d 2 4 | 4 > a 2 | 2 > b c | ", c.printRules());
        }

        @Test
        public void processInput3() { // rule utility
            System.out.println();
            c = new Compress();
            c.processInput("abcdbcabcd");
            assertEquals("0 > 6 2 6 | 6 > a 2 d | 2 > b c | ", c.printRules());
        }

    @Test
    public void processInput4() { // no overlap of digrams
        System.out.println();
        c = new Compress();
        c.processInput("aaa");
        assertEquals("0 > a a a | ", c.printRules());
    }

    @Test
    public void processInput5() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaa");
        assertEquals("0 > 2 2 | 2 > a a | ", c.printRules());
    }

    @Test
    public void processInput6() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaaa");
        assertEquals("0 > 2 2 a | 2 > a a | ", c.printRules());
    }

    @Test
    public void processInput7() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaaaa");
        assertEquals("0 > 2 2 2 | 2 > a a | ", c.printRules());
    }

    @Test
    public void processInput8() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaaaaa");
        assertEquals("0 > 2 2 2 a | 2 > a a | ", c.printRules());
    }

    @Test
    public void longAA() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertEquals("0 > 8 8 | 8 > 6 6 | 6 > 4 4 | 4 > 2 2 | 2 > a a | ", c.printRules());
    }

    @Test
    public void processInputAgain() {
        c = new Compress();
        String input = "ACAGAGATTTTGAGCGTGATATTATTCCAATGGCTAGGCATTTCGGTATGGCCCTCGCCCCATGGGATGTCATGGGAGGTGGAAGATTTCAGAGTAAAAAAGCAATGGAGGAACGGAGGA";
        c.processInput(input);
        System.out.println(c.printRules());
        Decompress d = new Decompress();
        //assertEquals(input, d.decompress(c.getFirstRule()));
    }


    @Test
    public void processInput10() {
        System.out.println();
        c = new Compress();
        c.processInput("abcdbcabcdab"); // 4d is 3 so that should be found and updated, then, as
        // 4 will only be in 3 it should be replaced with a1 and 4 removed
        assertEquals("0 > 6 2 6 a b | 6 > a 2 d | 2 > b c | ", c.printRules());
    }

    @Test
    public void processInput11() { // matching digram in existing rule, create new rule
        System.out.println();
        c = new Compress();
        c.processInput("abcdbcabcdabc");
        assertEquals("0 > 6 2 6 8 | 8 > a 2 | 6 > 8 d | 2 > b c | ", c.printRules());
    }


    @Test
    public void processInput12() {
        System.out.println();
        c = new Compress();
        c.processInput("abcdbcabcdabcd");
        assertEquals("0 > 6 2 6 6 | 6 > a 2 d | 2 > b c | ", c.printRules());
    }

    @Test
    public void processInput13() {
        System.out.println();
        c = new Compress();
        c.processInput("bdhudhbdhu");
        System.out.println(c.printDigrams());
        System.out.println(c.printRules());
    }

    @Test
    public void processInput14() {
        System.out.println();
        c = new Compress();
        c.processInput("abcdbcabcdabcdbcabcdabcdbc");
    }



    @Test
    public void implicitEncoding() {
        System.out.println();
        c = new Compress();
        c.processInput("abcdebcdfbcdebcdfg");
    }

    @Test
    public void implicitEncoding2() {
        System.out.println();
        c = new Compress();
        c.processInput("abcdbcabcdbc");
    }

    @Test
    public void implicitEncoding3() {
        System.out.println();
        c = new Compress();
        c.processInput("acaccacacc");
    }

    @Test
    public void implicitEncoding4() {
        System.out.println();
        c = new Compress();
        c.processInput("dbcadbcabcabc");
    }

    @Test
    public void testLongerInput() {
        System.out.println();
        c = new Compress();
        c.processInput("ACAGAGATTTTGAGCGTGATATTATTCCAATGGCTAGGCATTTCGGTATGGCCCTCGCCC\n" +
                "CATGGGATGTCATGGGAGGTGGAAGATTTCAGAGTAAAAAAGCAATGGAGGAACGGAGGA\n" +
                "AGAATGGAGAGGGTATTCGTTCTTTCGTTGGCGCCTCCGAACAAACAGATGCAGAAATCA\n" +
                "AGATTAGTGAAGCATTGGCCAAGATTGCTGAGGAACATGGCACTGAGTCTGTTACTGCTA\n" +
                "TTGCTATTGCCTATGTTCGCTCTAAGGCGAAAAATTTTTTTCCGTCGGTTGAAGGAGGAA\n" +
                "AAATTGAGGATCTCAAAGAGAACATTAAGGCTCTCAGTATCGATCTAACGCCAGACAATA\n" +
                "TAAAATACTTAGAAAGTATAGTTCCTTTTGACATCGGATTTCCTAATAATTTTATCGTGT\n" +
                "TAAATTCCTTGACTCAAAAATATGGTACGAATAATGTTTAGATAATTTTTCAGTAATCAA\n" +
                "CTACGCAAGTAAAGCAGTAAATACGTTACTGCTGGTATTAATGTCATGTATTGAGGCAAT\n" +
                "GATGCTATGCTCTTAACGACATGTAGCTTACAAAACGTTCCTATGGTTTTACTGCATGGT\n" +
                "ATTTACAAATTAGATAGCAGTTCCATCCGCCCTTGACATATTATTATTCAGACAAGGTGT\n" +
                "ATATGAGCATAAATATGTATATATGCACATGAGGTTTGTATTAACCTTGCAACTGTACCA\n" +
                "AAATACAATCTTCTTTGCTGCTATTTAACGGCGTATGTGCAGTTCATAAATGCGTGTTCT\n" +
                "GTATAGTACATCCGCGCACATTCTTCCTAGCGGAAGACATATTAACGTAGCCCGTACGCC\n" +
                "CTGGTTAAGACTTGGTATAGTTCCAATAATTGGAATATTACTGCATAGTGGTCCAATAGC\n" +
                "AGGATTTAGCATAAACATGATAATATTTAGAGATGCTTTCATCCCTCTGACGAAGGTGGT\n" +
                "AGAGAACAAGAAAATGAATAACATTATATCATATTCCTATATATATATATATATATATAT\n" +
                "ATATATCATATAACGGTGGAAAATCCCGGCGATATTACAGATAAACATTACACCCGCATG\n" +
                "AATGTGAGCCACTACTATATTATAACTTAGTGAATAAAGAGTGTTACAATAGTGAGGTGC\n" +
                "ATATTATTCTAATGTAAGGTCTGTATAAGTACGAAATATTCTGAAGTGGCATCCAGTCAA\n" +
                "GCAAATGGTAATATTAAGGAACTTTTAAGTTAATGACGTCATGGTAGTGCTCGTACTTCA\n" +
                "AGTCAAAGTGTTTATGTATTATGGTTGAAGAATAGAATATTTTTATGTTTAGGTGATTTT\n" +
                "AGTGGTGATTTTTCTGTAATATTGACATAAGTGTATATAAATTAAGTGGTTAGTATACGG\n" +
                "TGAAAAAGAGGTATAACGTATGTATTAAGGGAATTTATACGATATTTGGGCCCGCCGAAT\n" +
                "GAGATATAGATATTAAAATGTGGATAATCGTGGGCTTTATGGGTAAATGGCACAGGGTAT\n" +
                "AGACCGCTGAGGCAAGTGCCGTGCATAATGATGTGGGTGCATTTGGTACTGATTTAGTGA\n" +
                "GAATGGGCCATGGATTGGAGTGTGAGAGTAGGGTAACTTGAGAGTGGTATATACTGTAGC\n" +
                "ATCCGTGTGCGTATGCCCCATCAATATAAGTGAAGGTGAGTATGGCATGTGGTGGTGGTA\n" +
                "TAGAGTGGTAGGGTAAGTATGTATGTATTATTTACGATCATTTGTTAACGTTTCAATATG\n" +
                "GTGGGTGAACAACAGTACAGTGAGTAGGACATGGTGGATGGTAGGGTAATAGTAGGGTAA\n" +
                "GTGGTGGTGGAGTTGGATATGGGTAATTGGAGGGTAACGGTTATGATGGGCGGTGGATGG\n" +
                "TGGTAGTAAGTAGAGAGATGGATGGTGGTTGGGAGTGGTATGGTTGAGTGGGGCAGGGTA\n" +
                "ACGAGTGGGGAGGTAGGGTAATGTGAGGGTAGGTTTGGAGACAGGTAAAATCAGGGTTAG\n" +
                "AATAGGGTAGTGTTAGGGTAGTGTGTGGGTGTGGGTGTGTGGGTGTGGTGTGTGGGTGTG\n" +
                "GTGTGTGTGGGTGTGGTGTGTGGGTGTGGGTGTGTGGGTGTGGTGGGTGTGGTGTGTGTG");
    }


    // TODO thought speed of book was comparable when printing out, seems slow again(?)
    @Test
    public void writeFile() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("15000");
        c.processInput(originalFile);
        System.out.println(c.printRules());
    }

    @Test
    public void chmpxx() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("chmpxx");
        c.processInput(originalFile);
        System.out.println(c.printRules());
    }

    @Test
    public void chntxx() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("chntxx");
        c.processInput(originalFile);
    }

    @Test
    public void hehcmv() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("hehcmv");
        c.processInput(originalFile);
    }

    @Test
    public void humdyst() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
    }

    @Test
    public void humghcs() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humghcs");
        c.processInput(originalFile);
    }

    @Test
    public void humhbb() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humhbb");
        c.processInput(originalFile);
    }

    @Test
    public void vaccg() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("vaccg");
        c.processInput(originalFile);
        System.out.println(c.printRules());
    }

    @Test
    public void mtpacga() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("mtpacga");
        c.processInput(originalFile);
    }

    @Test
    public void mpomtcg() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("mpomtcg");
        c.processInput(originalFile);
    }

    @Test
    public void humprtb() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humprtb");
        c.processInput(originalFile);
    }

    @Test
    public void humhdab() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humhdab");
        c.processInput(originalFile);
    }

    @Test
    public void getSymbols() {
        Compress c = new Compress();
//        c.getSymbols();
        System.out.println((char) 22 == (char) 23);
        final long nrChars = IntStream.rangeClosed(0, 0x10ffff)
                .mapToObj(Character.UnicodeBlock::of)
                .filter(Objects::nonNull)
                .count();
        System.out.println(nrChars);
    }
}