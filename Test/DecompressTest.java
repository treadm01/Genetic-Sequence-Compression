    import GrammarCoder.Compress;
    import GrammarCoder.Decompress;
    import GrammarCoder.Rule;
    import org.junit.Test;

import static org.junit.Assert.*;

public class DecompressTest {

    @Test
    public void buildGrammar() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbc";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar2() { // check which digram is replaced
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "aaabcabaa";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        System.out.println(input);
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar3() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabc";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }


    @Test
    public void buildGrammar4() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabcd";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar5() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "aaaaaa";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        System.out.println(input);
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar6() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar7() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabcdabcdbcabcdabc";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        System.out.println(input);
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar8() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabcdabcdbcabcdabcdbc";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar9() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabcdabcdbcabcdabcdbcabcd"; //looks like rule number is not correct...
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar10() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "dbcadbcabcabc";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        System.out.println(input);
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar11() { // multiple rules that are not decompressed
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "cdbcadbcabbcabc";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        System.out.println(input);
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void decompressX3() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "acgt";//"cagagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgccccatgggatgtcatgggaggtggaagatttcagagtaaaaaagcaatggaggaacggagga";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        Rule r = d.buildGrammar(input);
        assertEquals(compress, c.getFirstRule().getSymbolString(r, r.isComplement)); //todo get with getter
    }

    @Test
    public void decompressX4() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "cagagattttgagcgtgatattattccaatggctaggcatttcggtatggccctcgccccatgggatgtcatgggaggtggaagatttcagagtaaaaaagcaatggaggaacggagga";
        c.processInput(compress);
        InputOutput io = new InputOutput();
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        Rule r = d.buildGrammar(input);
        assertEquals(compress, c.getFirstRule().getSymbolString(r, r.isComplement));
    }


    @Test
    public void buildGrammar12() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abc";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void buildGrammar13() { // comparison failure
        Decompress d = new Decompress();
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String compress = io.readFile("30000");
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }

    @Test
    public void chntxxDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("chntxx");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }


    @Test
    public void chmpxxDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("chmpxx");
        c.processInput(originalFile);
        //todo have this called in process input
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        System.out.println(input);
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }

    @Test
    public void hehcmvDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("hehcmv");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        //System.out.println(input);
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
        //20475
    }

    @Test
    public void humdystDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        System.out.println(input);
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }

    @Test
    public void humghcsDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humghcs");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }

    @Test
    public void humhbbDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humhbb");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }

    @Test
    public void vaccgDE() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        Decompress d = new Decompress();
        String originalFile = io.readFile("vaccg");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        System.out.println(input);
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }

    @Test
    public void mtpacgaDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("mtpacga");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }

    @Test
    public void mpomtcgDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("mpomtcg");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }

    @Test
    public void humprtbDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humprtb");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        System.out.println(input);
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }

    @Test
    public void humhdabDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humhdab");
        c.processInput(originalFile);
        c.encode(c.getFirstRule().getGuard().getRight(), "");
        String input = io.readFile("compressTest");
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }


    @Test
    public void quickCheck() {
        assertTrue(check("45000"));
    }

    public Boolean check(String s) {
        Decompress d = new Decompress();
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String compress = io.readFile(s);
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        return compress.equals(d.decompress(d.buildGrammar(input)));
    }
}