import GrammarCoder.*;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SearchTest {


//    INPUT: aagcggc
//    SEARCH: ct

    @Test
    public void shouldFindReverse() {
        Compress c = new Compress();
        String compress = "aagcggc";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ct"));
    }

//    INPUT: ccgacggtcatgaaa
//    SEARCH: gtca
//    INPUT: cttcttt
//    SEARCH: cttt
    @Test
    public void ruleMatchedInLongMiddleSubRule() {
        Compress c = new Compress();
        String compress = "cttcttt";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("cttt"));
    }


    @Test
    public void hanging() {
        Compress c = new Compress();
        String compress = "tgtcca";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("atca"));
    }

    @Test
    public void notFindingReverseComplementNonTerminalDigram() {
        Compress c = new Compress();
        String compress = "ctgtacaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("acaa"));
    }


    @Test
    public void notFindingReverseComplementDigram() {
        Compress c = new Compress();
        String compress = "gactctgag";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ca"));
    }

    @Test
    public void ruleToLongToCheck() {
        Compress c = new Compress();
        String compress = "atcatacatt";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("tc"));
    }

    @Test
    public void checkConsecutiveOnRuleTwo() {
        Compress c = new Compress();
        String compress = "ctaacaac";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("gtgt"));
    }

    @Test
    public void notFindingADigram() {
        Compress c = new Compress();
        String compress = "ttccgtgaac";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("gt"));
    }

    @Test
    public void splitFindingReveresComplementOtherDirections() {
        Compress c = new Compress();
        String compress = "ttaacaattg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("tgt"));
    }

    @Test
    public void splitAcrossComplements() {
        Compress c = new Compress();
        String compress = "gctagcgga";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ta"));
    }

    @Test
    public void findingDigramNotThere() {
        Compress c = new Compress();
        String compress = "ccttgcaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("ga"));
    }

    @Test
    public void notFinding() {
        Compress c = new Compress();
        String compress = "tctcat";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("tct"));
    }

    @Test
    public void splitNonTerminal() {
        Compress c = new Compress();
        String compress = "cttgttga";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("gt")); //gt - ttga
    }


    @Test
    public void findingAgain() {
        Compress c = new Compress();
        String compress = "ggcacgta";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("gggc"));
    }

    @Test
    public void finding() {
        Compress c = new Compress();
        String compress = "gtgcttgtag";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("tga"));
    }


    @Test
    public void FindingReverseComplement() {
        Compress c = new Compress();
        String compress = "tccctgct";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ag"));
    }

    @Test
    public void FindingSubRule() {
        Compress c = new Compress();
        String compress = "tccctgct";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ct"));
    }



    @Test
    public void needMoreFalse() {
        Compress c = new Compress();
        String compress = "tccctgct";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("aa"));
    }

    @Test
    public void findSingle() {
        Compress c = new Compress();
        String compress = "a";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("a"));
    }

    @Test
    public void findDouble() {
        Compress c = new Compress();
        String compress = "aa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aa"));
    }

    @Test
    public void findDoubleAcrossNonTerminals() {
        Compress c = new Compress();
        String compress = "aacc";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ac"));
    }

    @Test
    public void findReverseComplementSubRule() {
        Compress c = new Compress();
        String compress = "aatt";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("tt"));
    }

    @Test
    public void findDoubleAcrossReverseComplementNonTerminals() {
        Compress c = new Compress();
        String compress = "aatt";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("at"));
    }

    @Test
    public void findAsSubRule() {
        Compress c = new Compress();
        String compress = "aaaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aa"));
    }


    @Test
    public void memoryLeak() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String input = io.readFile("humdyst");
        c.processInput(input, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("cgcatctccactatt"));
    }

    //    INPUT: gtacctg
//    SEARCH: ta

    //INPUT: gtttttggcatcttggccgggatta
    //SEARCH: aaat
    @Test
    public void findingGhost() {
        Compress c = new Compress();
        String compress = "gtacctg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ta"));
    }

//    INPUT: gccggg
//    SEARCH: t
@Test
public void findingSingle() {
    Compress c = new Compress();
    String compress = "gccggg";
    c.processInput(compress, false);
    Search s = new Search(c.getFirstRule());
    assertFalse(s.search("t"));
}

    @Test
    public void findingSingleLetter() {
        Compress c = new Compress();
        String compress = "gcg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("a"));
    }


//    INPUT: aaccgagacgagaggtctatgactctgcaccagttagaggagttctttcgcaagggccaggcttctggcttggta
//    SEARCH: agagt
    @Test
    public void reverseCompnotfound() {
        Compress c = new Compress();
        String compress = "aaccgagacgagaggtctatgactctgc";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("agagt"));
    }


    //atcgtgataaatccagt reverse complement not found - search gat - passing
    //todo searching gg finds multiple of the same 6' g and gg should just be 6'g
    @Test
    public void searchEncodedinReverseComplement() {
        Compress c = new Compress();
        String compress = "aatcctgcaggg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("cag"));
    }

    @Test
    public void searchSubRuleNotDigram() {
        Compress c = new Compress();
        String compress = "aatatgctgggg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ctg"));
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
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aagatt"));
    }

    @Test
    public void search() { // not a complement issue - no tt digram, split by 4 a
        // if removing original, have to reset reverser complement?? to be the stanard? can't change
        Compress c = new Compress();
        String compress = "aaag";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aag"));
    }


    @Test
    public void searchaag() { // not a complement issue - no tt digram, split by 4 a
        // if removing original, have to reset reverser complement?? to be the stanard? can't change
        Compress c = new Compress();
        String compress = "gaaaga";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aag"));
    }


    @Test
    public void searchMultiple() { // finding extra really 2 a occurs as part of 4... just need instances of 4
        Compress c = new Compress();
        String compress = "gaaagattatgcggaag";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
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
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("t"));
    }

    @Test
    public void searchActualleftDigram() {
        Compress c = new Compress();
        String compress = "acgt";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ac"));
    }

    @Test
    public void searchActualRIghtdigram() {
        Compress c = new Compress();
        String compress = "acgt";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
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
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("cgca"));
    }

    @Test
    public void searchSimple() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("cgcaa"));
    }

    // still need to check single, or not the first symbol...
    @Test
    public void searchWhole() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("agtcgcaatttagacaacagccaa"));
    }

    @Test
    public void searchOneLess() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("gtcgcaatttagacaacagccaa"));
    }

    @Test
    public void searchSub() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("agccaa"));
    }

    @Test
    public void searchSingle() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("a"));
    }

    @Test
    public void searchSubRule() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aa"));
    }

    //todo cant search for single...

    @Test
    public void searchSubRulesAndMainRule() { // begins in a subrule and ends in main rule
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aattt"));
    }

    @Test
    public void searchTest() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("caac"));
    }

    //tc is not being registered.... the way possible rules are created
    @Test
    public void searchTestTwo() {
        Compress c = new Compress();
        String compress = "agtcgcaatttagacaacagccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("caatt"));
    }

    @Test
    public void NotFound() {
        Compress c = new Compress();
        String compress = "gtccttaagcttataagaatggacttcatatgagatc";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("agcttataagaatggacttcatat"));
    }

    // gggagaaagcgaggatcag - this string is constructed but not registered as found
    // need to be built digram by digram
    // if digram exists and its a full rule then continue up
    @Test
    public void humdyst() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aattccgg"));
    }

    @Test
    public void humdystTwo() {
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aattccggatcac"));
    }

    @Test
    public void humdystThree() { // last part of string
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ccatatgactttgcaaattca"));
    }


//    INPUT: ttccagt
//    SEARCH: cac
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
////    SEARCH: gtg
//
////might be finding reverse complements....

//    INPUT: cacgctt
//    SEARCH: agcag



    @Test
    public void searchRandom() {
        Random rand = new Random();
        for (int i = 0; i < 1000; i++) {
            String input = genRand(rand.nextInt((8 - 2) + 1) + 2);
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
            c.processInput(input, false);
            Search s = new Search(c.getFirstRule());
            if (search.length() != 0) {
                StringBuilder reverseComplement = new StringBuilder();
                for (int j = 0; j < search.length(); j++) {
                    reverseComplement.append(Terminal.reverseSymbol(search.charAt(j)));
                }
                reverseComplement.reverse();
//                System.out.println(input.contains(search));
//                System.out.println("reverse found " + (input.contains(reverseComplement)) );
//                System.out.println(s.search(search));//
                 //
                if (search.length() > 1 && c.rules.size() > 1) {
                    assertEquals(input.contains(search) || input.contains(reverseComplement), s.search(search)); //
                }
            }
            System.out.println();
            System.out.println("count " + i);
            //Assert.assertEquals(input, c.getFirstRule().getSymbolString(c.getFirstRule(), c.getFirstRule().isComplement));
        }
    }
//
    @Test
    public void searchRandomFromFile() {
        Random rand = new Random();
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String input = io.readFile("humdyst");
        c.processInput(input, false);
        Search s = new Search(c.getFirstRule());
        for (int i = 0; i < 100; i++) {
            String search = genRand(rand.nextInt((15 - 1) + 1) + 1);
            System.out.println("SEARCH: " + search);
            StringBuilder reverseComplement = new StringBuilder();
            for (int j = 0; j < search.length(); j++) {
                reverseComplement.append(Terminal.reverseSymbol(search.charAt(j)));
            }
            reverseComplement.reverse();
            if (search.length() != 0) {
                assertEquals(input.contains(search) || input.contains(reverseComplement), s.search(search)); //
            }
            System.out.println();
            System.out.println("count " + i);
        }
    }


}
