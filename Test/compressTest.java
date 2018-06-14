import org.junit.Test;

public class compressTest {
    compress c = new compress();

    @Test
    public void simpleInputTest() {
        c.processInput("abcdbc");

        System.out.println();
        c = new compress();
        c.processInput("abcdbcabc");

        System.out.println();
        c = new compress();
        c.processInput("abcdbcabcd");


        System.out.println();
        c = new compress();
        c.processInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"); // this doesnt do the right number of checks

        System.out.println();
        c = new compress();
        c.processInput("abcdbcabcdabcd"); // 4d is 3 so that should be found and updated, then, as
        // 4 will only be in 3 it should be replaced with a1 and 4 removed

        System.out.println();
        c = new compress();
        c.processInput("abcdbcabcdabcdbcabcd");

    }

    @Test
    public void processInput() {
        c.processInput("abcdbc");

        System.out.println();
        c = new compress();
        c.processInput("abcdbcabc");

        System.out.println();
        c = new compress();
        c.processInput("abcdbcabcd");

        System.out.println();
        c = new compress();
        c.processInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"); // this doesnt do the right number of checks

        System.out.println();
        c = new compress();
        c.processInput("abcdbcabcdabcd"); // 4d is 3 so that should be found and updated, then, as
        // 4 will only be in 3 it should be replaced with a1 and 4 removed

        System.out.println();
        c = new compress();
        c.processInput("abcdbcabcdabcdbcabcd");

        System.out.println();
        c = new compress();
        c.processInput("abcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcdabcdbcabcd");

        System.out.println();
        c = new compress();
        c.processInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        // not accounting for spaces... and other things??? - it is taking _f as 16... not updating the rule?? 16 has a d at the front....
        System.out.println();
        c = new compress();
        c.processInput("Sequitur is a method for inferring compositional hierarchies from strings. It detects repetition and factors it out of the string by f");

        System.out.println();
        c = new compress();
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


        System.out.println();
        c = new compress();
       // c.processInput(c.readFile());


//        c.processInput("Sequitur is a method for inferring compositional hierarchies from strings. It detects repetition and factors it out of the string by f"); // this is the last time# of rules match
    }

    @Test
    public void reOrderRules() {
        System.out.println();
        c = new compress();
        c.processInput("abcabdabcabdabcabdabcabdabcabdabcabdabcabdabcabd");
    }

    @Test
    public void threeRule() {
    }

    @Test
    public void readFile() {
        compress c = new compress();
        InputOutput io = new InputOutput();
        System.out.println(io.readFile());
    }

    @Test
    public void checkRepeat() {
    }

    @Test
    public void ruleUtility() {
    }

    @Test
    public void existingBigram() {
    }

    @Test
    public void writeFile() {
        compress c = new compress();
        InputOutput io = new InputOutput();
    //    System.out.println(c.writeFile(c.processInput("a")));
        String originalFile = io.readFile();
        System.out.println("original " + originalFile);

        System.out.println(io.writeFile(c.processInput(originalFile)));

//        0 → 1 C A 2 3 4 3 2 1 5 4 T 3
//        1 → T T C                                         TTC
//        2 → A T                                           AT
//        3 → 5 C                                           GGC
//        4 → T A                                           TA
//        5 → G G

    }

    @Test
    public void reorderRules() {
    }
}