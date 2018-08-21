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
    public void checkIndexes() { // check which digram is replaced
        c.processInput("aaagcagaaagc");
        System.out.println(c.printRules());
    }

    @Test
    public void processMaDi() { // check which digram is replaced
        c.processInput("tgtc");
        System.out.println(c.printRules());
    }


    @Test
    public void decompressX() {
        Compress c = new Compress();
        String compress = "gtgttcc";
        c.processInput(compress);
        System.out.println(c.printRules());
        String s = "";//c.encode(c.getFirstRule().getGuard().getRight(), "");
        System.out.println("Encoded: " + s);
        System.out.println("Length: " + s.length());
    }

    @Test
    public void decompressX4() {
        Compress c = new Compress();
        String compress = "cagagattttgagcgtgatattattccaatggctaggcattttcggtatggccctcgccccatgggatgtcatgggaggtggaagatttcagagtaaaaaagcaatggaggaacggagga";
        c.processInput(compress);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
    }

    @Test
    public void EditGrammarTest() { // approximate repeats with some extra symbols before
        Compress c = new Compress();
        String compress = "cgattctgttctctgcctcacttctctgactcac";
        c.processInput(compress);
    }

    @Test
    public void EditGrammarReverseComplementTest() {
        Compress c = new Compress();
        String compress = "cgtgatattattccaatggctaggcatttcggtatggccctcgcc";
        c.processInput(compress);
    }

    @Test
    // indexes corrected
    public void EditGrammarCheckIndexes() {
        Compress c = new Compress();
        String compress = "tggctcacgcctgtaatcccagcac";
        c.processInput(compress);
    }


    @Test
    //todo example of multiple edits not being recorded
    public void multipleEdits() {
        Compress c = new Compress();
        String compress = "gctcacgcctgtaatcccagcact"; //todo the a is being set as edited... c set to t, a should just be a....
        c.processInput(compress);
        assertEquals(" 2 t 4 c 2 6 12 c c 6' 12*2c0c", c.getFirstRule().getRuleString());
    }

    @Test
    public void multipleEditsTwo() {
        Compress c = new Compress();
        String compress = "tggctcacgcctgtaatcccagcactttggg"; // need to find an example of this
        c.processInput(compress);
        assertEquals(" 2 4 t 8 4 6 t 10 t 12 6' 8 10' 2 12'", c.getFirstRule().getRuleString());
    }

    @Test
    public void multipleEditsLong() {
        Compress c = new Compress();
        String compress = "tggctcacgcctgtaatcccagcactttgggaggctgaggcgggcggatcacaaggtcaggagatcgagaccatcctggctaacacggtgaaa";
        c.processInput(compress);
    }



    // edit the string ? edit the grammar??? have to find the approx match first
    @Test
    public void EditGrammarTestJustRepeats() {
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgactcac";
        c.processInput(compress);
        ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
        assertEquals(" 20 20*7a", c.getFirstRule().getRuleString());
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void DecompressApproxRepeat(){
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgactcac";
        c.processInput(compress);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    //todo worth considering , doesn't register as approx repeat...
    // aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaacaa
    @Test
    public void DecompressDoubleDigitIndex() {
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgcctcacttctctgcctcacttctctgactcac";
        c.processInput(compress);
        assertEquals(" 54 54*20a", c.getFirstRule().getRuleString());
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void DecompressApproxRepeatOVERLAP(){
        Compress c = new Compress();
        String compress = "gcggaggccg";
        c.processInput(compress);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    // works ok for duplicates
    public void EditGrammarTestJustRepeatsDouble() {
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgactcacttctctgcctcacttctctgactcac";
        c.processInput(compress);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    //todo example of second part of digram not matching.. suspect not worth doing
    // approximate repeats like this couldn't be found so easily, had it working with method
    // but, not sure, effectively like checking every symbol...
    // no just if there is a 50% similarity between, approx repeats not added if
    //both following symbols are different.....
    public void EditGrammarTestJustRepeatsSecondEdit() {
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgcatcac";
        c.processInput(compress);
    }


    @Test
    public void checkApproxRepeat() {
        Compress c = new Compress();
        String compress = "cggtcccc";
        c.processInput(compress);
    }

    // rule that has been implicitly added in an earlier reverse complement,
    // two in a row equals the wrong link?? infinite loop //todo reordered remove rules and check digrams in replace rule... enough to fix?
    @Test
    public void stackOverflow() { // reverse complement issue...
        Compress c = new Compress();String compress = "cgggagtccc"; // suspect reverse complement matching
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    //aaagctttgg
    @Test
    public void HeapLeak() {
        Compress c = new Compress();
        String compress = "aaagctttgg";
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    // doesn't work with new order but encoding before was not correct??? oh wait
    @Test
    public void guardCantBeNonTerminal() {
        Compress c = new Compress();
        String compress = "aaggaagctt";
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void wrondDecoding() {
        Compress c = new Compress();
        String compress = "taggtatag"; //its 02 again //HAD TO SPECIFY THAT THE LAST SYMBOL WAS ALSO A TERMINAL
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void wrondDecodingLong() {
        Compress c = new Compress();
        String compress = "tcaatagtttgacgtgggagcgtaagattgccacgaattggcaccggggggccctcgc"; //its 02 again //HAD TO SPECIFY THAT THE LAST SYMBOL WAS ALSO A TERMINAL
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void stringIndexOutofBounds() {
        //subrules were being created with edits rather than passed on to new nonterminals
        // now oldterminals and symbols are updated and symbols added to rules are wiped of edits
        Compress c = new Compress();
        String compress = "tcgggtctgatctgacagaa";
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void digramsOfeditedRulesRemoved() {
        //subrules were being created with edits rather than passed on to new nonterminals
        // now oldterminals and symbols are updated and symbols added to rules are wiped of edits
        // have to remove digram from edited rules?
        Compress c = new Compress();
        String compress = "cattgtgtttcacgtg";
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test // not sure what is happening here
    public void wrongMatch() {
        Compress c = new Compress();
        String compress = "gtttgacttgcggaacgt";
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    //
    @Test // not sure what is happening here, disappearing subrule again?
    public void wrongMatchTwo() {
        Compress c = new Compress();
        String compress = "atttgatgttgctg";
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    //todo keep a comparison to previous lengths for each example
    @Test
    public void humdyst() {
        Compress c = new Compress();
        InputOutput io  = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    //humhdab - symbols getting lost?
    //humghcs
    //humhbb
    //mtpacga - apparent improvement - 21594 - chunk of seqence not returned 32846
    //chmpxx
    //chntxx
    //mpomtcg -
    //vaccg - apparent improvement 58103 - not matching but only 2 symbols less..... 57640, 57631
    //hehcmv
    @Test
    public void longerString() {
        Compress c = new Compress();
        InputOutput io  = new InputOutput();
        String originalFile = io.readFile("hehcmv");
        c.processInput(originalFile);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void humprtbDE() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humprtb");
        c.processInput(originalFile);
        assertEquals(originalFile, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    String genRand (int length) {
        Random rand = new Random();
        String possibleLetters = "acgt";
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(possibleLetters.charAt(rand.nextInt(possibleLetters.length())));
        return sb.toString();
    }

    //cggtcccc - editing when a complement but they are not the same
    //gtagcgtag - did have the terminal 2 error one time gcggagga , gcggaggccg
    //ccctcagggc - stack overflow cgggagtccc
    @Test
    public void decompressApproxTest() {
        Random rand = new Random();
        for (int i = 0; i < 10000; i++) {
            String input = genRand(rand.nextInt((50 - 1) + 1) + 1);
            System.out.println("INPUT: " + input);
            Compress c = new Compress();
            Decompress d = new Decompress();
            c.processInput(input);
            ImplicitEncoder ie = new ImplicitEncoder(c.getFirstRule());
            String impEncode = ie.getEncodedOutput();
            Rule r = d.buildGrammar(impEncode);
            Assert.assertEquals(input, r.getSymbolString(r, r.isComplement));
            //Assert.assertEquals(input, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
        }
    }

}
