import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class uncompressTest {

    @Test
    public void processInput() {
        String expected = "abcdbc";
        compress c = new compress();
        List<rule> rules = c.processInput(expected);
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
    }

    @Test
    public void getOutput() {
    }
}