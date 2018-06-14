import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class uncompressTest {

    @Test
    public void processInput() {
        String expected = "abcdbc";
        compress c = new compress();
        List<nonTerminal> rules = c.processInput(expected);
        uncompress u = new uncompress();
        assertEquals(expected, u.processInput(rules));

        rules.clear();

        expected = "abcdbcabc";
        c = new compress();
        rules = c.processInput(expected);
        u = new uncompress();
        assertEquals(expected, u.processInput(rules));

        rules.clear();

        expected = "abcdbcabcd";
        c = new compress();
        rules = c.processInput(expected);
        u = new uncompress();
        assertEquals(expected, u.processInput(rules));

        expected = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        c = new compress();
        rules = c.processInput(expected);
        u = new uncompress();
        assertEquals(expected, u.processInput(rules));

        expected = "abcdbcabcdabcdbcabcd";
        c = new compress();
        rules = c.processInput(expected);
        u = new uncompress();
        assertEquals(expected, u.processInput(rules));

        expected = "abcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcd";
        c = new compress();
        rules = c.processInput(expected);
        u = new uncompress();
        assertEquals(expected, u.processInput(rules));
//
        expected = "ACAGAGATTTTGAGCGTGATATTATTCCAATGGCTAGGCATTTCGGTATGGCCCTCGCCC" +
                "CATGGGATGTCATGGGAGGTGGAAGATTTCAGAGTAAAAAAGCAATGGAGGAACGGAGGA" +
                "AGAATGGAGAGGGTATTCGTTCTTTCGTTGGCGCCTCCGAACAAACAGATGCAGAAATCA" +
                "AGATTAGTGAAGCATTGGCCAAGATTGCTGAGGAACATGGCACTGAGTCTGTTACTGCTA" +
                "TTGCTATTGCCTATGTTCGCTCTAAGGCGAAAAATTTTTTTCCGTCGGTTGAAGGAGGAA" +
                "AAATTGAGGATCTCAAAGAGAACATTAAGGCTCTCAGTATCGATCTAACGCCAGACAATA" +
                "TAAAATACTTAGAAAGTATAGTTCCTTTTGACATCGGATTTCCTAATAATTTTATCGTGT" +
                "TAAATTCCTTGACTCAAAAATATGGTACGAATAATGTTTAGATAATTTTTCAGTAATCAA" +
                "CTACGCAAGTAAAGCAGTAAATACGTTACTGCTGGTATTAATGTCATGTATTGAGGCAAT" +
                "GATGCTATGCTCTTAACGACATGTAGCTTACAAAACGTTCCTATGGTTTTACTGCATGGT" +
                "ATTTACAAATTAGATAGCAGTTCCATCCGCCCTTGACATATTATTATTCAGACAAGGTGT" +
                "ATATGAGCATAAATATGTATATATGCACATGAGGTTTGTATTAACCTTGCAACTGTACCA" +
                "AAATACAATCTTCTTTGCTGCTATTTAACGGCGTATGTGCAGTTCATAAATGCGTGTTCT" +
                "GTATAGTACATCCGCGCACATTCTTCCTAGCGGAAGACATATTAACGTAGCCCGTACGCC" +
                "CTGGTTAAGACTTGGTATAGTTCCAATAATTGGAATATTACTGCATAGTGGTCCAATAGC" +
                "AGGATTTAGCATAAACATGATAATATTTAGAGATGCTTTCATCCCTCTGACGAAGGTGGT" +
                "AGAGAACAAGAAAATGAATAACATTATATCATATTCCTATATATATATATATATATATAT" +
                "ATATATCATATAACGGTGGAAAATCCCGGCGATATTACAGATAAACATTACACCCGCATG" +
                "AATGTGAGCCACTACTATATTATAACTTAGTGAATAAAGAGTGTTACAATAGTGAGGTGC" +
                "ATATTATTCTAATGTAAGGTCTGTATAAGTACGAAATATTCTGAAGTGGCATCCAGTCAA" +
                "GCAAATGGTAATATTAAGGAACTTTTAAGTTAATGACGTCATGGTAGTGCTCGTACTTCA" +
                "AGTCAAAGTGTTTATGTATTATGGTTGAAGAATAGAATATTTTTATGTTTAGGTGATTTT" +
                "AGTGGTGATTTTTCTGTAATATTGACATAAGTGTATATAAATTAAGTGGTTAGTATACGG" +
                "TGAAAAAGAGGTATAACGTATGTATTAAGGGAATTTATACGATATTTGGGCCCGCCGAAT" +
                "GAGATATAGATATTAAAATGTGGATAATCGTGGGCTTTATGGGTAAATGGCACAGGGTAT" +
                "AGACCGCTGAGGCAAGTGCCGTGCATAATGATGTGGGTGCATTTGGTACTGATTTAGTGA" +
                "GAATGGGCCATGGATTGGAGTGTGAGAGTAGGGTAACTTGAGAGTGGTATATACTGTAGC" +
                "ATCCGTGTGCGTATGCCCCATCAATATAAGTGAAGGTGAGTATGGCATGTGGTGGTGGTA" +
                "TAGAGTGGTAGGGTAAGTATGTATGTATTATTTACGATCATTTGTTAACGTTTCAATATG" +
                "GTGGGTGAACAACAGTACAGTGAGTAGGACATGGTGGATGGTAGGGTAATAGTAGGGTAA" +
                "GTGGTGGTGGAGTTGGATATGGGTAATTGGAGGGTAACGGTTATGATGGGCGGTGGATGG" +
                "TGGTAGTAAGTAGAGAGATGGATGGTGGTTGGGAGTGGTATGGTTGAGTGGGGCAGGGTA" +
                "ACGAGTGGGGAGGTAGGGTAATGTGAGGGTAGGTTTGGAGACAGGTAAAATCAGGGTTAG" +
                "AATAGGGTAGTGTTAGGGTAGTGTGTGGGTGTGGGTGTGTGGGTGTGGTGTGTGGGTGTG" +
                "GTGTGTGTGGGTGTGGTGTGTGGGTGTGGGTGTGTGGGTGTGGTGGGTGTGGTGTGTGTG";

        c = new compress();
        rules = c.processInput(expected);
        u = new uncompress();
        assertEquals(expected, u.processInput(rules));

        expected = "ACAGAGATTTTGAGCGTGATATTATTCCAATGGCTAGGCATTTCGGTATGGCCCTCGCCC" +
                "CATGGGATGTCATGGGAGGTGGAAGATTTCAGAGTAAAAAAGCAATGGAGGAACGGAGGA" +
                "AGAATGGAGAGGGTATTCGTTCTTTCGTTGGCGCCTCCGAACAAACAGATGCAGAAATCA" +
                "AGATTAGTGAAGCATTGGCCAAGATTGCTGAGGAACATGGCACTGAGTCTGTTACTGCTA" +
                "TTGCTATTGCCTATGTTCGCTCTAAGGCGAAAAATTTTTTTCCGTCGGTTGAAGGAGGAA" +
                "AAATTGAGGATCTCAAAGAGAACATTAAGGCTCTCAGTATCGATCTAACGCCAGACAATA" +
                "TAAAATACTTAGAAAGTATAGTTCCTTTTGACATCGGATTTCCTAATAATTTTATCGTGT" +
                "TAAATTCCTTGACTCAAAAATATGGTACGAATAATGTTTAGATAATTTTTCAGTAATCAA";

        c = new compress();
        rules = c.processInput(expected);
        u = new uncompress();
        assertEquals(expected, u.processInput(rules));

        rules.clear();
        c = new compress();
        InputOutput io = new InputOutput();
        expected = io.readFile();
        rules = c.processInput(expected);
        u = new uncompress();

        assertEquals(expected, u.processInput(rules));
    }

    @Test
    public void getOutput() {
    }

    @Test
    public void getBinaryFromFile() {
        uncompress u = new uncompress();
        InputOutput io = new InputOutput();
        System.out.println(io.readCompressedFile());
    }

    @Test
    public void readFile() {
        //TODO working for smaller groups but not larger, changed the byte parse to int... in uncompress
        uncompress u = new uncompress();
        compress c = new compress();
        InputOutput io = new InputOutput();
        String expected = io.writeFile(c.processInput("ACGACT"));
        String result = io.readCompressedFile();

        System.out.println(expected);
        System.out.println(result);

        u.processBinary(result);

        System.out.println(u.processInput(u.processBinary(result)));

        assertEquals(expected, result);
    }

    @Test
    public void comparisonCheck() {
        uncompress u = new uncompress();
        compress c = new compress();
        InputOutput io = new InputOutput();
        String expected = io.writeFile(c.processInput("ACAGAGATTTTGAGCGTGATATTATTCCAATGGCTAGGCATTTCGGTATGGCCCTCGCCC\n" +
                "CATGGGATGTCATGGGAGGTGGAAGATTTCAGAGTAAAAAAGCAATGGAGGAACGGAGGA\n" +
                "AGAATGGAGAGGGTATTCGTTCTTTCGTTGGCGCCTCCGAACAAACAGATGCAGAAATCA\n" +
                "AGATTAGTGAAGCATTGGCCAAGATTGCTGAGGAACATGGCACTGAGTCTGTTACTGCTA\n" +
                "TTGCTATTGCCTATGTTCGCTCTAAGGCGAAAAATTTTTTTCCGTCGGTTGAAGGAGGAA\n" +
                "AAATTGAGGATCTCAAAGAGAACATTAAGGCTCTCAGTATCGATCTAACGCCAGACAATA\n" +
                "TAAAATACTTAGAAAGTATAGTTCCTTTTGACATCGGATTTCCTAATAATTTTATCGTGT\n" +
                "TAAATTCCTTGACTCAAAAATATGGTACGAATAATGTTTAGATAATTTTTCAGTAATCAA\n"));

        String result = io.readCompressedFile();

        assertEquals(expected, result);
    }

    @Test
    public void checkLongerStrings() {
        uncompress u = new uncompress();
        compress c = new compress();
        InputOutput io = new InputOutput();

        // "AGCTGCAGCT"
        String originalFile = io.readFile(); // for final check the original string

        List<nonTerminal> originalCompressedRules = c.processInput(originalFile);
        String expected = io.writeFile(originalCompressedRules); // compressed

        String result = io.readCompressedFile(); // receiving the compressed
        assertEquals(expected, result); // check the same compressed stuff is received
        List<nonTerminal> uncompressedRules = u.processBinary(result);

        String uncompressedFile = u.processInput(uncompressedRules);

        System.out.println("going in " + originalFile);
        System.out.println("coming o " + uncompressedFile);

        assertEquals(originalFile, uncompressedFile);
    }


    @Test
    public void testCompressUncompress() {
        uncompress u = new uncompress();
        compress c = new compress();
        InputOutput io = new InputOutput();
        String expected = io.readFile();

        // compress file
        io.writeFile(c.processInput(expected));

        String binaryFromFile = io.readCompressedFile();
        List<nonTerminal> processedFromBinary = u.processBinary(binaryFromFile);
        String answer = u.processInput(processedFromBinary);

        assertEquals(expected, answer);
    }
}