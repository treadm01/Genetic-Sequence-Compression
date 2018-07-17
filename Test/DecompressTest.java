import org.junit.Test;

import static org.junit.Assert.*;

public class DecompressTest {

    @Test
    public void buildGrammar() {
        Decompress d = new Decompress();
        assertEquals("abcdbc", d.decompress(d.buildGrammar("aM2bcd(1)")));
    }

    @Test
    public void buildGrammar2() { // check which digram is replaced
        Decompress d = new Decompress();
        assertEquals("aaabcabaa", d.decompress(d.buildGrammar("M2aaM2abc(2)(1)")));
    }

    @Test
    public void buildGrammar3() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabc";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        System.out.println(input);
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }


    @Test
    public void buildGrammar4() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "abcdbcabcd";
        c.processInput(compress);
        String input = c.encode(c.getFirstRule().getGuard().getRight(), "");
        System.out.println(input);
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
        System.out.println(input);
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
        System.out.println(input);
        assertEquals(compress, d.decompress(d.buildGrammar(input)));
    }


    @Test
    public void decompress() {
    }
}