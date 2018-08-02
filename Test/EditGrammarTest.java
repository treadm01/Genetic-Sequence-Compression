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
    public void processMatchingDigrams() { // check which digram is replaced
        c.processInput("aaagcagaa");
        System.out.println(c.printRules());
    }


    @Test
    public void decompressX4() {
        Compress c = new Compress();
        String compress = "cagagattttgagcgtgatattattccaatggctaggcattttcggtatggccctcgccccatgggatgtcatgggaggtggaagatttcagagtaaaaaagcaatggaggaacggagga";
        c.processInput(compress);
        System.out.println(c.printRules());
        System.out.println(c.printRules().length());
        System.out.println(c.getFirstRule().getRuleString().length());
        c.orderRulesByLength();
    }

    @Test
    public void EditGrammarTest() { // approximate repeats with some extra symbols before
        Compress c = new Compress();
        String compress = "cgattctgttctctgcctcacttctctgactcac";
        c.processInput(compress);
        System.out.println(c.printRules());
        System.out.println(c.encode(c.getFirstRule().getGuard().getRight(), "").length());
    }


    // edit the string ? edit the grammar??? have to find the approx match first
    @Test
    public void EditGrammarTestJustRepeats() {
        Compress c = new Compress();
        String compress = "ttctctgcctcacttctctgactcac";
        c.processInput(compress);
        System.out.println(c.printRules());
        System.out.println(c.encode(c.getFirstRule().getGuard().getRight(), "").length());
        c.findApproximateRepeats();
    }



}
