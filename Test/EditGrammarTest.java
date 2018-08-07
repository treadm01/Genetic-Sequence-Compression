import GrammarCoder.Compress;
import GrammarCoder.Decompress;
import GrammarCoder.Rule;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        System.out.println(c.printRules());
        System.out.println(c.printRules().length());
        System.out.println(c.getFirstRule().getRuleString().length());
    }

    @Test
    public void EditGrammarTest() { // approximate repeats with some extra symbols before
        Compress c = new Compress();
        String compress = "cgattctgttctctgcctcacttctctgactcac";
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
    public void checkApproxRepeat() {
        Compress c = new Compress();
        String compress = "ttctc";
        c.processInput(compress);
    }

    @Test
    public void humdyst() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
    }

}
