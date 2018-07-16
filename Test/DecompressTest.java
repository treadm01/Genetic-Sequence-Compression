import org.junit.Test;

import static org.junit.Assert.*;

public class DecompressTest {

//    @Test
//    public void buildGrammar() {
//        Decompress d = new Decompress();
//        assertEquals("abcdbc", d.decompress(d.buildGrammar("abcd(1,2)")));
//    }
//
//    @Test
//    public void buildGrammar2() { // check which digram is replaced
//        Decompress d = new Decompress();
//        assertEquals("aaabcabaa", d.decompress(d.buildGrammar("aaabc(2,2)(0,2)")));
//    }
//
//    @Test
//    public void buildGrammar3() {
//        Decompress d = new Decompress();
//        assertEquals("abcdbcabc", d.decompress(d.buildGrammar("abcd(1,2)(0,2)")));
//    }
//
//
//    @Test
//    public void buildGrammar4() {
//        Decompress d = new Decompress();
//        assertEquals("abcdbcabcd", d.decompress(d.buildGrammar("abcd(1,2)(0,3)")));
//    }
//
//    @Test
//    public void buildGrammar5() {
//        Decompress d = new Decompress();
//        Compress c = new Compress();
//        String compress = "aaaaaa";
//        c.processInput(compress);
//        String input = c.encode(c.getFirstRule().getGuard().getRight());
//        System.out.println(input);
//        assertEquals(compress, d.decompress(d.buildGrammar(input)));
//    }
//
//    @Test
//    public void buildGrammar6() {
//        Decompress d = new Decompress();
//        Compress c = new Compress();
//        String compress = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//        c.processInput(compress);
//        String input = c.encode(c.getFirstRule().getGuard().getRight());
//        System.out.println(input);
//        assertEquals(compress, d.decompress(d.buildGrammar(input)));
//    }
//
//    @Test
//    public void buildGrammar7() {
//        Decompress d = new Decompress();
//        Compress c = new Compress();
//        String compress = "abcdbcabcdabcdbcabcdabcdbc";
//        c.processInput(compress);
//        String input = c.encode(c.getFirstRule().getGuard().getRight());
//        System.out.println(input);
//        assertEquals(compress, d.decompress(d.buildGrammar(input)));
//    }
//
//
//    @Test
//    public void decompress() {
//    }
}