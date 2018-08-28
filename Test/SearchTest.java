import GrammarCoder.*;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SearchTest {

    //atcgtgataaatccagt reverse complement not found - search gat
    // doesn't account for rules that start in left hand and in it completely
    // well not for reverse complement
    // for start symbols reverse complements not being checked to the last, left etc
    @Test
    public void searchEncodedinReverseComplement() {
        Compress c = new Compress();
        String compress = "aatcctgcaggg";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("cag"));
    }

    //INPUT: tgtcccgaaacgctttaacggacctgctttcatacagcaataggagcggatagaaa
    //SEARCH: ccgaaacgctttaacggacctgctttcatacagca

//    INPUT: gaaagattatgcggaag
//    SEARCH: aagatt

    @Test
    public void searchComplement() {
        Compress c = new Compress();
        String compress = "gaaagattatgcggaag";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("aagatt"));
    }

    //todo you won't have access to the start symbol things.... will you???
    // either have to build from the search string, which will have ups nd downs....
    // or build from terminals in grammar... possible???
    // will be using build grammar or generate rules???
    @Test // bigger problem, looks like middle digram gg is being removed?? where 4g g2 created?
    // removing a digram that occurs elsewhere as a joint of the digram
    // a g g g -> first a g being removed, next digram g g removed, even though still occurs in the next g g ...
    // pretty specific... wouldn't register as two digrams because overlap, not a new digram to be checked either
    public void searchActualSimple() {
        Compress c = new Compress();
        String compress = "taagggagaag";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("aagggagaa"));
    }

    @Test
    public void searchActualleftDigram() {
        Compress c = new Compress();
        String compress = "acgt";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("ac"));
    }

    @Test
    public void searchActualRIghtdigram() {
        Compress c = new Compress();
        String compress = "acgt";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("gt"));
    }

    // would need every symbol followed by a nonterminal and every symbols precedede by a nonterminal
    // first terminal of every rule, be able to find, then can search by digrams for that
    //g6 , also need last terminal of every rule then can search that 4 c
    // then every single possible match for both??????? 46 yeah
    // so how to efficiently retrieve all nonterminals that start with a particular terminal???
    @Test
    public void searchSmallString() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("cgca"));
    }

    @Test
    public void searchSimple() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("cgcaa"));
    }

    // still need to check single, or not the first symbol...
    @Test
    public void searchWhole() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("agtcgcaatttagacaacagccaa"));
    }

    @Test
    public void searchOneLess() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("gtcgcaatttagacaacagccaa"));
    }

    @Test
    public void searchSub() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("agccaa"));
    }

    @Test
    public void searchSingle() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("a"));
    }

    @Test
    public void searchSubRule() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("aa"));
    }

    //todo cant search for single...

    @Test
    public void searchAgain() { // begins in a subrule and ends in main rule
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("aattt"));
    }

    @Test
    public void searchTest() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("caac"));
    }

    //tc is not being registered.... the way possible rules are created
    @Test
    public void searchTestTwo() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("agtc"));
    }

    // gggagaaagcgaggatcag - this string is constructed but not registered as found
    @Test
    public void humdyst() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("gaattccgg"));
    }

    @Test
    public void humdystTwo() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("gaattccggatcac"));
    }

    @Test
    public void humdystThree() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        Search s = new Search(c.digramMap, c.rules);
        assertTrue(s.search("ccatatgactttgcaaattca"));
    }

    String genRand (int length) {
        Random rand = new Random();
        String possibleLetters = "acgt";
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(possibleLetters.charAt(rand.nextInt(possibleLetters.length())));
        return sb.toString();
    }

    //ataagtaagttat
    //ttgtgataaaag
    // l 77
    // problem with small input gc
    @Test
    public void searchRandom() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            String input = genRand(rand.nextInt((20 - 2) + 1) + 2);
            System.out.println("INPUT: " + input);
            int start = rand.nextInt(input.length());
            assertTrue(start <= input.length());
            //System.out.println("length " +  input.length());
            int end = rand.nextInt(input.length() - start) + start;
//            System.out.println("start " + start);
//            System.out.println("end " + end);
            assertTrue(end <= input.length());
            assertTrue(end >= start);
            String search = input.substring(start, end);
            System.out.println("SEARCH: " + search);
            Compress c = new Compress();
            c.processInput(input);
            Search s = new Search(c.digramMap, c.rules);
            if (search.length() != 0) {
                assertTrue(s.search(search));
            }
            System.out.println();
            System.out.println("count " + i);
//            //Assert.assertEquals(input, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
        }
    }


}
