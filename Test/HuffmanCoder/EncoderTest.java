package HuffmanCoder;

import GrammarCoder.Compress;
import org.junit.Test;

import static org.junit.Assert.*;

public class EncoderTest {

    @Test
    public void smallerTest() {
        Compress c = new Compress();
        Encoder e = new Encoder();
        String compress = "tggctcacgcctgtaatcccagcactttgggaggctgaggcgggcggatcacaaggtcaggagatcgagaccatcctggctaacacggtgaaa";
        c.processInput(compress);
        e.getOrderedNodes(c.allSymbols);
        e.buildTree();
        //e.traverseTree();
    }

    @Test
    public void getOrderedNodes() {
        Compress c = new Compress();
        Encoder e = new Encoder();
        String compress = "tggctcacgcctgtaatcccagcactttgggaggctgaggcgggcggatcacaaggtcaggagatcgagaccatcctggctaacacggtgaaa";
        c.processInput(compress);
        e.getOrderedNodes(c.allSymbols);
        e.buildTree();
        //e.traverseTree();
    }

}