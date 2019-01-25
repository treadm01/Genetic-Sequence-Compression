import ArithmeticCoder.AdaptiveArithmeticCompress;
    import ArithmeticCoder.AdaptiveArithmeticDecompress;
    import GrammarCoder.*;
    import org.junit.Assert;
    import org.junit.Test;

    import java.io.File;
    import java.io.IOException;
    import java.util.Random;

    import static org.junit.Assert.*;

public class DecompressTest {

    @Test
    public void buildGrammar() throws IOException {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "ACAGAGATTTTGAGCGTGATATTATTCCAATGGCTAGGCATTTCGGTATGGCCCTCGCCC\n" +
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
                "GTGTGTGTGGGTGTGGTGTGTGGGTGTGGGTGTGTGGGTGTGGTGGGTGTGGTGTGTGTG";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        assertEquals(compress, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void buildGrammar2() { // check which digram is replaced
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "aaabcabaa";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar3() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabc";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar4() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabcd";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar5() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "aaaaaa";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar6() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar7() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "aaaaaaagaaaa";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar8() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabcdabcdbcabcdabcdbc";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar9() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabcdabcdbcabcdabcdbcabcd"; //looks like rule number is not correct...
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar10() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "dbcadbcabcabc";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar11() { // multiple rules that are not decompressed
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "cdbcadbcabbcabc";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void decompressX3() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "cagagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgccccatgggatgtcatgggaggtggaagatttcagagtaaaaaagcaatggaggaacggagga";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        Rule r = d.buildGrammar(input);
        assertEquals(compress, c.getFirstRule().getSymbolString(r, r.getIsComplement())); //todo get with getter
    }

    @Test
    public void decompressX4() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "cagagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgccccatgggatgtcatgggaggtggaagatttcagagtaaaaaagcaatggaggaacggagga";
        c.processInput(compress, false);
        InputOutput io = new InputOutput();
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        Rule r = d.buildGrammar(input);
        assertEquals(compress, c.getFirstRule().getSymbolString(r, r.getIsComplement()));
    }


    @Test
    public void buildGrammar12() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("S288C_chrIII_BK006937.2");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
        io.writeToFile(d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void chntxxDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("chntxx");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        //Rule r = d.buildGrammar(input);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }


    @Test
    public void chmpxxDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("chmpxx");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        //Rule r = d.buildGrammar(input);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void hehcmvDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("hehcmv");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        //Rule r = d.buildGrammar(input);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void humdystDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        //Rule r = d.buildGrammar(input);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void humghcsDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humghcs");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }


    @Test
    public void humhbbDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("abc"); //"abc" "30000"
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
//        String input = ie.getEncodedOutput();
//        Rule r = d.buildGrammar(input);
//        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.getIsComplement()));
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        //assertEquals(ie.getEncodedOutput(), aad.getImplicitEncoding());
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void vaccgDE() throws IOException {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        Decompress d = new Decompress();
        String originalFile = io.readFile("vaccg");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void mtpacgaDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("mtpacga");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void mpomtcgDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("mpomtcg");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void humprtbDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humprtb");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
    }

    @Test
    public void humhdabDE() throws IOException {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humhdab");
        c.processInput(originalFile, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
        File f = new File(System.getProperty("user.dir") + "/compressed.bin");
        AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
        assertEquals(originalFile, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
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
    public void decompressApproxTest() throws IOException {
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            String input = genRand(rand.nextInt((1000 - 1) + 1) + 1);
            Compress c = new Compress();
            Decompress d = new Decompress();
            c.processInput(input, false);
            ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
//            String impEncode = ie.getEncodedOutput();
//            Rule r = d.buildGrammar(impEncode);
//            Assert.assertEquals(input, r.getSymbolString(r, r.getIsComplement()));
            AdaptiveArithmeticCompress aac = new AdaptiveArithmeticCompress(ie.highestRule, ie.getSymbolList());
            File f = new File(System.getProperty("user.dir") + "/compressed.bin");
            AdaptiveArithmeticDecompress aad = new AdaptiveArithmeticDecompress(f);
            assertEquals(input, d.decompress(d.buildGrammar(aad.getImplicitEncoding())));
        }
    }

    @Test
    public void decompressEdits() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        String compress = "ttctctgcctcacttctctgactcac";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        Rule r = (d.buildGrammar(input));

        //assertEquals(" 20 20*20a", c.getFirstRule().getRuleString());
        assertEquals(compress, r.getSymbolString(r, r.getIsComplement()));
    }
}