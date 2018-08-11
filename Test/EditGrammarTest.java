import GrammarCoder.Compress;
import GrammarCoder.Decompress;
import org.junit.Before;
import org.junit.Test;


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
    //todo example of multiple edits not being recorded
    public void EditGrammarReverseComplementTestTwo() {
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
    public void ats() {
        Compress c = new Compress();
        String compress = "";
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
    }

    //humhdab
    //humghcs
    //humhbb
    //mtpacga
    @Test
    public void longerString() {
        Compress c = new Compress();
        InputOutput io  = new InputOutput();
        String originalFile = io.readFile("humhdab");
        c.processInput(originalFile);
    }

    @Test
    public void humprtbDE() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humprtb");
        c.processInput(originalFile);
    }

}
