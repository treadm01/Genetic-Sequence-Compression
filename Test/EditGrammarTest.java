import GrammarCoder.Compress;
import GrammarCoder.Decompress;
import org.junit.Before;
import org.junit.Test;

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
        String s = c.encode(c.getFirstRule().getGuard().getRight(), "");
        System.out.println("Encoded: " + s);
        System.out.println("Length: " + s.length());
    }

    @Test
    public void decompressX4() {
        Compress c = new Compress();
        String compress = "cagagattttgagcgtgatattattccaatggctaggcattttcggtatggccctcgccccatgggatgtcatgggaggtggaagatttcagagtaaaaaagcaatggaggaacggagga";
        c.processInput(compress);
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
        String compress = "tggctcacgcctgtaatcccagcactttggg";
        c.processInput(compress);
        assertEquals(" 2 4 t 2' c 4 6 12 16 6' 12*2c0c 14' 16' g", c.getFirstRule().getRuleString());
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
        assertEquals(" 20 20*7a", c.getFirstRule().getRuleString());
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
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    @Test
    public void DecompressApproxRepeatLargeIndex(){ // TODO NOT SURE WHAT IS HAPPENING HERE... EDIT GIVEN AS A 2 NONTERMINAL... REVERSE COMPLEMENT? TYPE NOT SPECIFIC ENOUGH??
        Compress c = new Compress();
        String compress = "ttctctttttttttttttttttttttttttttttttttttttcttttttttttttttatttttttttttatttttttttattgcctcaccctctcttcttttcttcttcacataaccacccctcattacatacatgaacatccccacac";
        c.processInput(compress);
        assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    // works ok for duplicates
    public void EditGrammarTestJustRepeatsDouble() {
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgactcacttctctgcctcacttctctgactcac";
        c.processInput(compress);
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
        String compress = "ttctc";
        c.processInput(compress);
    }

    @Test
    public void humdyst() {
        Compress c = new Compress();
        InputOutput io  = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        //assertEquals(originalFile, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    //humhdab - symbols getting lost?
    //humghcs
    //humhbb
    //mtpacga - apparent improvement - 21594 - chunk of seqence not returned
    //vaccg - apparent improvement 58103 - not matching but only 2 symbols less.....
    @Test
    public void longerString() {
        Compress c = new Compress();
        InputOutput io  = new InputOutput();
        String originalFile = io.readFile("humhdab");
        c.processInput(originalFile);
        //assertEquals(originalFile, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void humprtbDE() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humprtb");
        c.processInput(originalFile);
    }

}
