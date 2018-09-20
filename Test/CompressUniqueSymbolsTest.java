import GrammarCoder.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class CompressUniqueSymbolsTest {

    @Test
    public void buildGrammar() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "6[^*2>>a0{[[#>6'^'3>g3?[0'%9[[690%#074{253t8'caga%27'?5^c{6*>aa92c*9g['971672#[!133^20t%97{#>7{%02033[^?2%60c4^[%^!a2?608t?a{!40g^6'?75^'3>[89%3305[{366{at{[3t89?#^";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        Rule r = d.buildGrammar(input);
        assertEquals(compress, r.getSymbolString(r, r.isComplement));
    }


    @Test
    public void reversecomplementNumbers() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "87939cg0t0g3714a907916931a75ag5g8g93";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        Rule r = d.buildGrammar(input);
        assertEquals(compress, r.getSymbolString(r, r.isComplement));
    }

    @Test
    public void encodingNumbers() {
        Decompress d = new Decompress();
        Compress c = new Compress();
        String compress = "ggfczgtaaacccgaaaaaccgfcz";
        c.processInput(compress, false);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        String input = ie.getEncodedOutput();
        Rule r = d.buildGrammar(input);
        assertEquals(compress, r.getSymbolString(r, r.isComplement));
    }

    @Test
    public void humprtbDE() {
        Compress c = new Compress();
        Decompress d = new Decompress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("fastatest");
        c.processInput(originalFile, false);

        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());

        String input = ie.getEncodedOutput();
        System.out.println(input);
        Rule r = d.buildGrammar(input);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(r, r.isComplement));
    }

    String genRand (int length) {
        Random rand = new Random();
        String possibleLetters = "acgt0123456789";
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(possibleLetters.charAt(rand.nextInt(possibleLetters.length())));
        return sb.toString();
    }

    // casting guard rule with no symbols
    @Test
    public void decompressApproxTest() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            String input = genRand(rand.nextInt((200 - 1) + 1) + 1);
            System.out.println("INPUT: " + input);
            Compress c = new Compress();
            Decompress d = new Decompress();
            c.processInput(input, false);
            ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
            String impEncode = ie.getEncodedOutput();
            Rule r = d.buildGrammar(impEncode);
            Assert.assertEquals(input, r.getSymbolString(r, r.isComplement));
            //Assert.assertEquals(input, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
        }
    }
}
