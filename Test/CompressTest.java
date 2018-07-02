import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompressTest {
    Compress c;

    @Before
    public void setUp() throws Exception {
        c = new Compress();
    }

    @Test
    public void processInput() {
        c.processInput("abcdbc");

        assertEquals("[bc, a1d1]", c.getRules());
    }

        @Test
        public void processInput2() {
            System.out.println();
            c = new Compress();
            c.processInput("abcdbcabc");

            assertEquals("[a1, bc, 2d12]", c.getRules());
        }

        @Test
        public void processInput3() {
            System.out.println();
            c = new Compress();
            c.processInput("abcdbcabcd");
            assertEquals("[bc, a1d, 313]", c.getRules());
        }

    @Test
    public void processInput4() {
        System.out.println();
        c = new Compress();
        c.processInput("aaa");
        assertEquals("[aaa]", c.getRules());
    }

    @Test
    public void processInput5() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaa");
        assertEquals("[aa, 11]", c.getRules());
    }

    @Test
    public void processInput6() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaaa");
        assertEquals("[aa, 11a]", c.getRules());
    }

    @Test
    public void processInput7() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaaaa");
        assertEquals("[aa, 111]", c.getRules());
    }

    @Test
    public void processInput8() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaaaaa");
        assertEquals("[aa, 111a]", c.getRules());
    }

    @Test
    public void longAA() {
        System.out.println();
        c = new Compress();
        c.processInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        assertEquals("[aa, 11, 22, 33, 44]", c.getRules());
    }


    @Test
    public void processInput10() {
        System.out.println();
        c = new Compress();
        c.processInput("abcdbcabcdab"); // 4d is 3 so that should be found and updated, then, as
        // 4 will only be in 3 it should be replaced with a1 and 4 removed
        assertEquals("[bc, 313ab, a1d]", c.getRules());
    }

    @Test
    public void processInput11() {
        System.out.println();
        c = new Compress();
        c.processInput("abcdbcabcdabc"); // 4d is 3 so that should be found and updated, then, as
        // 4 will only be in 3 it should be replaced with a1 and 4 removed
        assertEquals("[bc, 3134, 4d, a1]", c.getRules());
    }

    @Test
    public void processInput12() {
        System.out.println();
        c = new Compress();
        c.processInput("abcdbcabcdabcd"); // 4d is 3 so that should be found and updated, then, as
        // 4 will only be in 3 it should be replaced with a1 and 4 removed
        assertEquals("[bc, 3133, a1d]", c.getRules());
    }


//        System.out.println();
//        c = new CompressNew();
//        c.processInput("abcdbcabcdabcdbcabcd");
//
////        19 -> 26 26
////        20 -> b c
////        22 -> a 20 d
////        26 -> 22 20 22



//
//    @Test
//    public void testLongerInput() {
//        System.out.println();
//        c = new Compress();
//        c.processInput("ACAGAGATTTTGAGCGTGATATTATTCCAATGGCTAGGCATTTCGGTATGGCCCTCGCCC\n" +
//                "CATGGGATGTCATGGGAGGTGGAAGATTTCAGAGTAAAAAAGCAATGGAGGAACGGAGGA\n" +
//                "AGAATGGAGAGGGTATTCGTTCTTTCGTTGGCGCCTCCGAACAAACAGATGCAGAAATCA\n" +
//                "AGATTAGTGAAGCATTGGCCAAGATTGCTGAGGAACATGGCACTGAGTCTGTTACTGCTA\n" +
//                "TTGCTATTGCCTATGTTCGCTCTAAGGCGAAAAATTTTTTTCCGTCGGTTGAAGGAGGAA\n" +
//                "AAATTGAGGATCTCAAAGAGAACATTAAGGCTCTCAGTATCGATCTAACGCCAGACAATA\n" +
//                "TAAAATACTTAGAAAGTATAGTTCCTTTTGACATCGGATTTCCTAATAATTTTATCGTGT\n" +
//                "TAAATTCCTTGACTCAAAAATATGGTACGAATAATGTTTAGATAATTTTTCAGTAATCAA\n" +
//                "CTACGCAAGTAAAGCAGTAAATACGTTACTGCTGGTATTAATGTCATGTATTGAGGCAAT\n" +
//                "GATGCTATGCTCTTAACGACATGTAGCTTACAAAACGTTCCTATGGTTTTACTGCATGGT\n" +
//                "ATTTACAAATTAGATAGCAGTTCCATCCGCCCTTGACATATTATTATTCAGACAAGGTGT\n" +
//                "ATATGAGCATAAATATGTATATATGCACATGAGGTTTGTATTAACCTTGCAACTGTACCA\n" +
//                "AAATACAATCTTCTTTGCTGCTATTTAACGGCGTATGTGCAGTTCATAAATGCGTGTTCT\n" +
//                "GTATAGTACATCCGCGCACATTCTTCCTAGCGGAAGACATATTAACGTAGCCCGTACGCC\n" +
//                "CTGGTTAAGACTTGGTATAGTTCCAATAATTGGAATATTACTGCATAGTGGTCCAATAGC\n" +
//                "AGGATTTAGCATAAACATGATAATATTTAGAGATGCTTTCATCCCTCTGACGAAGGTGGT\n" +
//                "AGAGAACAAGAAAATGAATAACATTATATCATATTCCTATATATATATATATATATATAT\n" +
//                "ATATATCATATAACGGTGGAAAATCCCGGCGATATTACAGATAAACATTACACCCGCATG\n" +
//                "AATGTGAGCCACTACTATATTATAACTTAGTGAATAAAGAGTGTTACAATAGTGAGGTGC\n" +
//                "ATATTATTCTAATGTAAGGTCTGTATAAGTACGAAATATTCTGAAGTGGCATCCAGTCAA\n" +
//                "GCAAATGGTAATATTAAGGAACTTTTAAGTTAATGACGTCATGGTAGTGCTCGTACTTCA\n" +
//                "AGTCAAAGTGTTTATGTATTATGGTTGAAGAATAGAATATTTTTATGTTTAGGTGATTTT\n" +
//                "AGTGGTGATTTTTCTGTAATATTGACATAAGTGTATATAAATTAAGTGGTTAGTATACGG\n" +
//                "TGAAAAAGAGGTATAACGTATGTATTAAGGGAATTTATACGATATTTGGGCCCGCCGAAT\n" +
//                "GAGATATAGATATTAAAATGTGGATAATCGTGGGCTTTATGGGTAAATGGCACAGGGTAT\n" +
//                "AGACCGCTGAGGCAAGTGCCGTGCATAATGATGTGGGTGCATTTGGTACTGATTTAGTGA\n" +
//                "GAATGGGCCATGGATTGGAGTGTGAGAGTAGGGTAACTTGAGAGTGGTATATACTGTAGC\n" +
//                "ATCCGTGTGCGTATGCCCCATCAATATAAGTGAAGGTGAGTATGGCATGTGGTGGTGGTA\n" +
//                "TAGAGTGGTAGGGTAAGTATGTATGTATTATTTACGATCATTTGTTAACGTTTCAATATG\n" +
//                "GTGGGTGAACAACAGTACAGTGAGTAGGACATGGTGGATGGTAGGGTAATAGTAGGGTAA\n" +
//                "GTGGTGGTGGAGTTGGATATGGGTAATTGGAGGGTAACGGTTATGATGGGCGGTGGATGG\n" +
//                "TGGTAGTAAGTAGAGAGATGGATGGTGGTTGGGAGTGGTATGGTTGAGTGGGGCAGGGTA\n" +
//                "ACGAGTGGGGAGGTAGGGTAATGTGAGGGTAGGTTTGGAGACAGGTAAAATCAGGGTTAG\n" +
//                "AATAGGGTAGTGTTAGGGTAGTGTGTGGGTGTGGGTGTGTGGGTGTGGTGTGTGGGTGTG\n" +
//                "GTGTGTGTGGGTGTGGTGTGTGGGTGTGGGTGTGTGGGTGTGGTGGGTGTGGTGTGTGTG");
//    }

//
//    @Test
//    public void writeFile() {
//        Compress c = new Compress();
//        InputOutput io = new InputOutput();
//        String originalFile = io.readFile();
//        c.processInput(originalFile);
//    }

}