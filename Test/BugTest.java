import GrammarCoder.Compress;
import org.junit.Assert;
import org.junit.Test;

public class BugTest {

    //tagagcgagatgggaagttcccccgctcgcctcacacgctcttataatacacagacaatggctctttcctcagccattgttatgcggtcgtcgtagcgt
    @Test
    public void HeapLeak() {
        Compress c = new Compress();
        String compress = "tttcggctgaaacggcaggcta";
        c.processInput(compress, false);
    }

    //ccctcagggc - stack overflow cgggagtccc
    // rule that has been implicitly added in an earlier reverse complement,
    // two in a row equals the wrong link?? infinite loop //todo reordered remove rules and check digrams in replace rule... enough to fix?
    @Test
    public void StackOverflow() {
        Compress c = new Compress();
        String compress = "ccctaggg"; // might be ordering of digram checking and overlapping
        c.processInput(compress, true);
    }

    @Test
    public void stackOverflowTwo() { // reverse complement issue...
        Compress c = new Compress();String compress = "cgggagtccc"; // suspect reverse complement matching
        c.processInput(compress, false);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void MissingSymbol() {
        Compress c = new Compress();
        String compress = "gcggagga";
        c.processInput(compress, false);
    }

    //INPUT: atttctgacgcccgcgatcgtcgtctccgcgctctggatggcggtagttcta
    // can't be cast to terminal guard
    @Test
    public void guardCantBeNonTerminal() {
        Compress c = new Compress();
        String compress = "aaggaagctt";
        c.processInput(compress, false);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }

    @Test
    public void singleSymbolRuleWrongDecompression() {
        Compress c = new Compress();
        String compress = "aaagtcatcctttctcccgagccacattcgactgtagagttccagaaagcgtgttaatgggttcataacgattcgcacggggaacgatggagccaacggttacagagctgaatagtcagggcggggcgggtttgacgtcgcggcgcgtcattagatggcacttagcgacccgtctgggtctcctaaaccagatggggaatatctatgtgtacgacgcattgagttggatctaaaaaactcttagctttctgcataaggtatacttgcagagtgacatgaaactccagataagtcaagacacaaacggagtgcaattccgcttgaaaggactgatagcctgcgccaggttaaacaaaggtgatgttcgtagtttcgcaaccctggggccacggcggggactttaagcggataaaacaggtggagtattcaatatgtgagacagcacggattaccttcgcgtggtgccgaactttttaggttgcgtcgcgacctgtcatcgccggggcggtaacacaaggtctaacgggtttaggtatcatcagaaatctcagctaggagacgcctcctatcaa";
        c.processInput(compress, false);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }


    //gcgtcgctctgggatcccagatctcattgcaatttcccagattatgctaacaa
    //aaaccgtttgagatacggcggatcaaactt
    //gaaaggtatacctttccctcgtacccccttcatt
    @Test
    public void guardCantBeCastTerminal() { // might be same issue as above rule 6 > a
        Compress c = new Compress();
        String compress = "aaaccgtttgagatacggcggatcaaactt";
        c.processInput(compress, false);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
    }
}
