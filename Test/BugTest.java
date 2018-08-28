import GrammarCoder.Compress;
import org.junit.Assert;
import org.junit.Test;

public class BugTest {

    @Test
    public void HeapLeak() {
        Compress c = new Compress();
        String compress = "tttcggctgaaacggcaggcta";
        c.processInput(compress);
    }

    //ccctcagggc - stack overflow cgggagtccc
    // rule that has been implicitly added in an earlier reverse complement,
    // two in a row equals the wrong link?? infinite loop //todo reordered remove rules and check digrams in replace rule... enough to fix?
    @Test
    public void StackOverflow() {
        Compress c = new Compress();
        String compress = "ccctcagggc";
        c.processInput(compress);
    }

    @Test
    public void stackOverflowTwo() { // reverse complement issue...
        Compress c = new Compress();String compress = "cgggagtccc"; // suspect reverse complement matching
        c.processInput(compress);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void MissingSymbol() {
        Compress c = new Compress();
        String compress = "gcggagga";
        c.processInput(compress);
    }

}
