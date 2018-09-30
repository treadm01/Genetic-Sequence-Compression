import GrammarCoder.Compress;
import org.junit.Assert;
import org.junit.Test;

public class BugTest {

    @Test
    public void HeapLeak() {
        Compress c = new Compress();
        String compress = "tttcggctgaaacggcaggcta";
        c.processInput(compress, false);
    }

    @Test
    public void StackOverflow() {
        Compress c = new Compress();
        String compress = "ccctaggg";
        c.processInput(compress, true);
    }

    @Test
    public void stackOverflowTwo() {
        Compress c = new Compress();String compress = "cgggagtccc";
        c.processInput(compress, false);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void MissingSymbol() {
        Compress c = new Compress();
        String compress = "gcggagga";
        c.processInput(compress, false);
    }

    @Test
    public void guardCantBeNonTerminal() {
        Compress c = new Compress();
        String compress = "aaggaagctt";
        c.processInput(compress, false);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void singleSymbolRuleWrongDecompression() {
        Compress c = new Compress();
        String compress = "aaagtcatcctttctcccgagccacattcgactgtagagttccagaaagcgtgttaatgggttcataacgattcgcacggggaacgatggagccaacggttacagagctgaatagtcagggcggggcgggtttgacgtcgcggcgcgtcattagatggcacttagcgacccgtctgggtctcctaaaccagatggggaatatctatgtgtacgacgcattgagttggatctaaaaaactcttagctttctgcataaggtatacttgcagagtgacatgaaactccagataagtcaagacacaaacggagtgcaattccgcttgaaaggactgatagcctgcgccaggttaaacaaaggtgatgttcgtagtttcgcaaccctggggccacggcggggactttaagcggataaaacaggtggagtattcaatatgtgagacagcacggattaccttcgcgtggtgccgaactttttaggttgcgtcgcgacctgtcatcgccggggcggtaacacaaggtctaacgggtttaggtatcatcagaaatctcagctaggagacgcctcctatcaa";
        c.processInput(compress, false);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }

    @Test
    public void guardCantBeCastTerminal() {
        Compress c = new Compress();
        String compress = "aaaccgtttgagatacggcggatcaaactt";
        c.processInput(compress, false);
        Assert.assertEquals(compress, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().getIsComplement()));
    }
}
