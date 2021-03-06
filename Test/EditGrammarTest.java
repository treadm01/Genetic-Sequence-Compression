import GrammarCoder.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.TestCase.assertEquals;


public class EditGrammarTest {
    Compress c;
    Decompress d;

    @Before
    public void setUp() throws Exception {
        c = new Compress();
        d = new Decompress();
    }

    @Test
    public void negativeIndex() {
        c.processInput("tcggtccatgaagacccc", true);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        assertEquals("tcggtccatgaagacccc", c.getFirstRule().getSymbolString(c.getFirstRule(), false));
    }

    @Test
    public void checkIndexes() { // check which digram is replaced
        c.processInput("taactccttagctcctgtagtctc", true);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        assertEquals("taactccttagctcctgtagtctc", c.getFirstRule().getSymbolString(c.getFirstRule(), false));
    }

    @Test
    public void editDistance() { // check which digram is replaced
        c.processInput("taatctaatg", true);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        assertEquals("taatctaatg", c.getFirstRule().getSymbolString(c.getFirstRule(), false));
    }

    @Test
    public void checkIndexesTwo() { // check which digram is replaced
        c.processInput("aaabcabaaacaaabcabaaac", true);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        assertEquals("aaabcabaaacaaabcabaaac", c.getFirstRule().getSymbolString(c.getFirstRule(), false));
    }

    @Test
    public void processMaDi() { // not an edit problem
        c.processInput("tttcagatgggcttcaggcaacatt", true);
        assertEquals("tttcagatgggcttcaggcaacatt", c.getFirstRule().getSymbolString(c.getFirstRule(), false));
    }

    @Test
    public void decompressX4() {
        Compress c = new Compress();
        String compress = "agagattttgagcgtgatattattccaatggctaggcattttcggtatggcc";
        c.processInput(compress, true);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), false));
    }

    @Test
    public void EditGrammarTest() { // approximate repeats with some extra symbols before
        Compress c = new Compress();
        String compress = "gttctctgcctcacttctctgactcac"; //cgattctgttctctgcctcacttctctgactcac
        c.processInput(compress, true);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), false));
    }

//
//    @Test
//    public void multipleEdits() {
//        Compress c = new Compress();
//        String compress = "gctcacgcctgtaatcccagcact"; //todo the a is being set as edited... c set to t, a should just be a....
//        c.processInput(compress, false);
//        assertEquals(" 2 t 4 c 2 6 12 c c 6' 12*2c0c", c.getFirstRule().getRuleString());
//    }

    @Test
    public void multipleEditsTwo() {
        Compress c = new Compress();
        String compress = "tggctcacgcctgtaatcccagcactttggg"; // need to find an example of this
        c.processInput(compress, true);
        assertEquals(" 2 4 t 8 4 6 t 10 t 12 6' 8 10' 2 12'", c.getFirstRule().getRuleString());
    }

    @Test
    public void multipleEditsLong() {
        Compress c = new Compress();
        String compress = "tggctcacgcctgtaatcccagcactttgggaggctgaggcgggcggatcacaaggtcaggagatcgagaccatcctggctaacacggtgaaa";
        c.processInput(compress, true);
        assertEquals("tggctcacgcctgtaatcccagcactttgggaggctgaggcgggcggatcacaaggtcaggagatcgagaccatcctggctaacacggtgaaa", c.getFirstRule().getSymbolString(c.getFirstRule(), false));
    }



    // edit the string ? edit the grammar??? have to find the approx match first
    @Test
    public void EditGrammarTestJustRepeats() {
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgactcac";
        c.processInput(compress, true);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void DecompressApproxRepeat(){
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgactcac";
        c.processInput(compress, true);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void DecompressDoubleDigitIndex() {
        Compress c = new Compress();
        String compress = "aataacaataat";
        c.processInput(compress, true);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void DecompressApproxRepeatOVERLAP(){
        Compress c = new Compress();
        String compress = "gcggaggccg";
        c.processInput(compress, true);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    // works ok for duplicates
    public void EditGrammarTestJustRepeatsDouble() {
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgactcacttctctgcctcacttctctgactcac";
        c.processInput(compress, true);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void EditGrammarTestJustRepeatsSecondEdit() {
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgcatcac";
        c.processInput(compress, true);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }


    @Test
    public void checkApproxRepeat() {
        Compress c = new Compress();
        String compress = "cggtcccc";
        c.processInput(compress, true);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }


    @Test
    public void wrondDecoding() {
        Compress c = new Compress();
        String compress = "taggtatag";
        c.processInput(compress, true);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void wrondDecodingLong() {
        Compress c = new Compress();
        String compress = "tcaatagtttgacgtgggagcgtaagattgccacgaattggcaccggggggccctcgc"; //its 02 again //HAD TO SPECIFY THAT THE LAST SYMBOL WAS ALSO A TERMINAL
        c.processInput(compress, true);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }


    @Test
    public void stringIndexOutofBounds() {
        Compress c = new Compress();
        String compress = "tcgggtctgatctgacagaa";
        c.processInput(compress, true);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void digramsOfeditedRulesRemoved() {
        Compress c = new Compress();
        String compress = "cattgtgtttcacgtg";
        c.processInput(compress, true);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }


    @Test
    public void wrongMatch() {
        Compress c = new Compress();
        String compress = "gtttgacttgcggaacgt";
        c.processInput(compress, true);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    //
    @Test
    public void wrongMatchTwo() {
        Compress c = new Compress();
        String compress = "atttgatgttgctg";
        c.processInput(compress, true);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    //todo keep a comparison to previous lengths for each example
    @Test
    public void humdyst() {
        Compress c = new Compress();
        InputOutput io  = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile, true);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void longerString() {
        Compress c = new Compress();
        InputOutput io  = new InputOutput();
        String originalFile = io.readFile("hehcmv");
        c.processInput(originalFile, true);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void humprtbDE() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humprtb");
        c.processInput(originalFile, true);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }


    String genRand (int length) {
        Random rand = new Random();
        String possibleLetters = "acgt";
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(possibleLetters.charAt(rand.nextInt(possibleLetters.length())));
        return sb.toString();
    }

    //todo don't forget no input
    @Test
    public void decompressApproxTest() {
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            String input = genRand(rand.nextInt((1000 - 1) + 1) + 1);
            Compress c = new Compress();
            Decompress d = new Decompress();
            c.processInput(input, true);
            ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
            String impEncode = ie.getEncodedOutput();
            Rule r = d.buildGrammar(impEncode);
            Assert.assertEquals(input, r.getSymbolString(r, r.getIsComplement()));
        }
    }

}
