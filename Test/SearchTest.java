import GrammarCoder.*;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SearchTest {

    //tgcccagttgtacgattcg and cgcatctccactatt - stuck on this.... out of memory...
    // because the amount of possible rules on a large grammar like humdyst is imense
    // but then how does it work on any? yeah donn't think thats the issue

    //INPUT: gtttttggcatcttggccgggatta
    //SEARCH: aaat
    @Test
    public void findingGhost() {
        Compress c = new Compress();
        String compress = "gtttttggcatcttggccgggatta";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertFalse(s.search("aaat"));
    }

//    INPUT: gccggg
//    SEARCH: t
@Test
public void findingSingle() {
    Compress c = new Compress();
    String compress = "gccggg";
    c.processInput(compress);
    Search s = new Search(c.getFirstRule(), c.rules);
    assertFalse(s.search("t"));
}

    @Test
    public void findingSingleLetter() {
        Compress c = new Compress();
        String compress = "gcg";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertFalse(s.search("a"));
    }


//    INPUT: aaccgagacgagaggtctatgactctgcaccagttagaggagttctttcgcaagggccaggcttctggcttggta
//    SEARCH: agagt
    @Test
    public void reverseCompnotfound() {
        Compress c = new Compress();
        String compress = "aaccgagacgagaggtctatgactctgc";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("agagt"));
    }



    //atcgtgataaatccagt reverse complement not found - search gat - passing
    @Test
    public void searchEncodedinReverseComplement() {
        Compress c = new Compress();
        String compress = "aatcctgcaggg";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("cag"));
    }

    //INPUT: tgtcccgaaacgctttaacggacctgctttcatacagcaataggagcggatagaaa
    //SEARCH: ccgaaacgctttaacggacctgctttcatacagca

//    INPUT: gaaagattatgcggaag
//    SEARCH: aagatt

    @Test
    public void searchComplement() { // not a complement issue - no tt digram, split by 4 a
        // if removing original, have to reset reverser complement?? to be the stanard? can't change
        Compress c = new Compress();
        String compress = "gaaagattatgcggaag";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("aagatt"));
    }

    @Test
    public void searchMultiple() { // finding extra really 2 a occurs as part of 4... just need instances of 4
        Compress c = new Compress();
        String compress = "gaaagattatgcggaag";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("gaa"));
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
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("aagggagaa"));
    }

    @Test
    public void searchActualleftDigram() {
        Compress c = new Compress();
        String compress = "acgt";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("ac"));
    }

    @Test
    public void searchActualRIghtdigram() {
        Compress c = new Compress();
        String compress = "acgt";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
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
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("cgca"));
    }

    @Test
    public void searchSimple() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("cgcaa"));
    }

    // still need to check single, or not the first symbol...
    @Test
    public void searchWhole() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("agtcgcaatttagacaacagccaa"));
    }

    @Test
    public void searchOneLess() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("gtcgcaatttagacaacagccaa"));
    }

    @Test
    public void searchSub() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("agccaa"));
    }

    @Test
    public void searchSingle() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("a"));
    }

    @Test
    public void searchSubRule() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("aa"));
    }

    //todo cant search for single...

    @Test
    public void searchAgain() { // begins in a subrule and ends in main rule
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("aattt"));
    }

    @Test
    public void searchTest() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("caac"));
    }

    //tc is not being registered.... the way possible rules are created
    @Test
    public void searchTestTwo() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("agtc"));
    }

    @Test
    public void NotFound() {
        Compress c = new Compress();
        String compress = "gtccttaagcttataagaatggacttcatatgagatc";
        c.processInput(compress);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("agcttataagaatggacttcatat"));
    }

    // gggagaaagcgaggatcag - this string is constructed but not registered as found
    @Test
    public void humdyst() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("aattccgg"));
    }

    @Test
    public void humdystTwo() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        Search s = new Search(c.getFirstRule(), c.rules);
        assertTrue(s.search("aattccggatcac"));
    }

    @Test
    public void humdystThree() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        Search s = new Search(c.getFirstRule(), c.rules);
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

    // not equal search
    //    INPUT: cg
//    SEARCH: a
//    INPUT: ccttcggatcaaacaca
//    SEARCH: gtg

//might be finding reverse complements....
    @Test
    public void searchRandom() {
        Random rand = new Random();
        for (int i = 0; i < 2000; i++) {
            String input = genRand(rand.nextInt((200 - 2) + 1) + 2);
            System.out.println("INPUT: " + input);
            int start = rand.nextInt(input.length());
            assertTrue(start <= input.length());
            //System.out.println("length " +  input.length());
            int end = rand.nextInt(input.length() - start) + start;
//            System.out.println("start " + start);
//            System.out.println("end " + end);
            assertTrue(end <= input.length());
            assertTrue(end >= start);
            //genRand(rand.nextInt((10 - 1) + 1) + 1);
            String search = genRand(start);
            System.out.println("SEARCH: " + search);
            Compress c = new Compress();
            c.processInput(input);
            Search s = new Search(c.getFirstRule(), c.rules);
            if (search.length() != 0) {
                StringBuilder reverseComplement = new StringBuilder();
                for (int j = 0; j < search.length(); j++) {
                    reverseComplement.append(Terminal.reverseSymbol(search.charAt(j)));
                }
                reverseComplement.reverse();
                System.out.println(input.contains(search));
                System.out.println("reverse found " + (input.contains(reverseComplement)) );
                System.out.println(s.search(search));
                assertEquals(input.contains(search)|| (input.contains(reverseComplement)) , s.search(search)); //
            }
            System.out.println();
            System.out.println("count " + i);
            //Assert.assertEquals(input, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
        }
    }

    @Test
    public void searchRandomFromFile() {
        Random rand = new Random();
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String input = io.readFile("humdyst");
        c.processInput(input);
        Search s = new Search(c.getFirstRule(), c.rules);
        for (int i = 0; i < 100; i++) {
            String search = genRand(rand.nextInt((15 - 1) + 1) + 1);
            System.out.println("SEARCH: " + search);
            StringBuilder reverseComplement = new StringBuilder();
            for (int j = 0; j < search.length(); j++) {
                reverseComplement.append(Terminal.reverseSymbol(search.charAt(j)));
            }
            reverseComplement.reverse();
            if (search.length() != 0) {
                assertEquals(input.contains(search)|| (input.contains(reverseComplement)) , s.search(search)); //
            }
            System.out.println();
            System.out.println("count " + i);
        }
    }


}
