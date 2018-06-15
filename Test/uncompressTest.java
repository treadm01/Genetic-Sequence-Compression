import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class uncompressTest {

    @Test
    public void processInput() {
        String expected = "abcdbc";
        Compress c = new Compress();
        List<NonTerminal> rules = c.processInput(expected);
        Uncompress u = new Uncompress();
        assertEquals(expected, u.processInput(rules));

        rules.clear();

        expected = "abcdbcabc";
        c = new Compress();
        rules = c.processInput(expected);
        u = new Uncompress();
        assertEquals(expected, u.processInput(rules));

        rules.clear();

        expected = "abcdbcabcd";
        c = new Compress();
        rules = c.processInput(expected);
        u = new Uncompress();
        assertEquals(expected, u.processInput(rules));

        expected = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        c = new Compress();
        rules = c.processInput(expected);
        u = new Uncompress();
        assertEquals(expected, u.processInput(rules));

        expected = "abcdbcabcdabcdbcabcd";
        c = new Compress();
        rules = c.processInput(expected);
        u = new Uncompress();
        assertEquals(expected, u.processInput(rules));

        expected = "abcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcd";
        c = new Compress();
        rules = c.processInput(expected);
        u = new Uncompress();
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

        c = new Compress();
        rules = c.processInput(expected);
        u = new Uncompress();
        assertEquals(expected, u.processInput(rules));

        expected = "ACAGAGATTTTGAGCGTGATATTATTCCAATGGCTAGGCATTTCGGTATGGCCCTCGCCC" +
                "CATGGGATGTCATGGGAGGTGGAAGATTTCAGAGTAAAAAAGCAATGGAGGAACGGAGGA" +
                "AGAATGGAGAGGGTATTCGTTCTTTCGTTGGCGCCTCCGAACAAACAGATGCAGAAATCA" +
                "AGATTAGTGAAGCATTGGCCAAGATTGCTGAGGAACATGGCACTGAGTCTGTTACTGCTA" +
                "TTGCTATTGCCTATGTTCGCTCTAAGGCGAAAAATTTTTTTCCGTCGGTTGAAGGAGGAA" +
                "AAATTGAGGATCTCAAAGAGAACATTAAGGCTCTCAGTATCGATCTAACGCCAGACAATA" +
                "TAAAATACTTAGAAAGTATAGTTCCTTTTGACATCGGATTTCCTAATAATTTTATCGTGT" +
                "TAAATTCCTTGACTCAAAAATATGGTACGAATAATGTTTAGATAATTTTTCAGTAATCAA";

        c = new Compress();
        rules = c.processInput(expected);
        u = new Uncompress();
        assertEquals(expected, u.processInput(rules));

        rules.clear();
        c = new Compress();
        InputOutput io = new InputOutput();
        expected = io.readFile();
        rules = c.processInput(expected);
        u = new Uncompress();

        assertEquals(expected, u.processInput(rules));
    }

    @Test
    public void getOutput() {
    }

    @Test
    public void getBinaryFromFile() {
        Uncompress u = new Uncompress();
        InputOutput io = new InputOutput();
        System.out.println(io.readCompressedFile());
    }

    @Test
    public void readFile() {
        //TODO working for smaller groups but not larger, changed the byte parse to int... in uncompress
        Uncompress u = new Uncompress();
        Compress c = new Compress();
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
        Uncompress u = new Uncompress();
        Compress c = new Compress();
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
        Uncompress u = new Uncompress();
        Compress c = new Compress();
        InputOutput io = new InputOutput();

        // "AGCTGCAGCT"
        String originalFile = io.readFile(); // for final check the original string

        List<NonTerminal> originalCompressedRules = c.processInput(originalFile);
        String expected = io.writeFile(originalCompressedRules); // compressed

        String result = io.readCompressedFile(); // receiving the compressed
        assertEquals(expected, result); // check the same compressed stuff is received
        List<NonTerminal> uncompressedRules = u.processBinary(result);

        String uncompressedFile = u.processInput(uncompressedRules);

        System.out.println("going in " + originalFile);
        System.out.println("coming o " + uncompressedFile);

        assertEquals(originalFile, uncompressedFile);
    }


    @Test
    public void testCompressUncompress() {
        Uncompress u = new Uncompress();
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String expected = io.readFile();

        // compress file
        io.writeFile(c.processInput(expected));

        String binaryFromFile = io.readCompressedFile();
        List<NonTerminal> processedFromBinary = u.processBinary(binaryFromFile);
        String answer = u.processInput(processedFromBinary);

        assertEquals(expected, answer);
    }
}