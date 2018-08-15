package HuffmanCoder;
import GrammarCoder.Decompress;

import GrammarCoder.Compress;
import GrammarCoder.InputOutput;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class EncoderTest {

    @Test
    public void smallerTest() {
        Compress c = new Compress();
        Encoder e = new Encoder();
        String compress = "acacggcacgaa";
        c.processInput(compress);
        e.getOrderedNodes(c.allSymbols);
        e.buildTree();
        e.traverseTree();
    }

    @Test
    public void getOrderedNodes() {
        Compress c = new Compress();
        Encoder e = new Encoder();
        String compress = "tggctcacgcctgtaatcccagcactttgggaggctgaggcgggcggatcacaaggtcaggagatcgagaccatcctggctaacacggtgaaa";
        c.processInput(compress);
        e.getOrderedNodes(c.allSymbols);
        e.buildTree();
        e.traverseTree();
    }

    @Test
    public void checkLarger() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        Encoder e = new Encoder();
        //GrammarCoder.InputOutput io  = new GrammarCoder.InputOutput();
        String originalFile = io.readFile("vaccg");
        c.processInput(originalFile);
        e.getOrderedNodes(c.allSymbols);
        e.buildTree();
        e.traverseTree();
        e.workOutLength();
    }

}