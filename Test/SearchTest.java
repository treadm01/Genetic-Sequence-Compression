import GrammarCoder.*;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SearchTest {

    @Test
    public void hangingTwo() {
        Compress c = new Compress();
        String compress = "ccctaggggacgaccag";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("cga"));
    }

    @Test
    public void findingDigrams() {
        Compress c = new Compress();
        String compress = "ccctagtcaggttccgggg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ccctag")); //tcaggttatgctccgggggtgt
    }

    @Test
    public void findingDigramsSimple() {
        Compress c = new Compress();
        String compress = "acac";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("gt"));
    }

    @Test
    public void findingDigramsSimpleTwo() {
        Compress c = new Compress();
        String compress = "aattccggaattccgg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("aattccggaatt"));
    }

    @Test
    public void leak() { // left hand side wont be matched too short
        Compress c = new Compress();
        String compress = "tggaaatagcactacg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("tgc"));
    }


    @Test
    public void reverseComp() { //sequence too short to make rule
        Compress c = new Compress();
        String compress = "ggccggcctc";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ag"));
    }

    @Test
    public void shouldBeFalse() {
        Compress c = new Compress();
        String compress = "cgagacagagtcgctggaattaat";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("catgagtagctcgcccaac"));
    }

    //todo hanging
    @Test
    public void shouldBeFound() {
        Compress c = new Compress();
        String compress = "taagtctgtaagg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("tgtaag"));
    }


    @Test
    public void orderingOfDigramCreation() {
        Compress c = new Compress();
        String compress = "tacgcggtgacaagaccaa";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("acg"));
    }

    @Test
    public void longerSubRules() { // too short to make left hand digram 4
        Compress c = new Compress();
        String compress = "tgattaaattagataat";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ttt"));
    }

    @Test
    public void longerSubRulesAgain() {
        Compress c = new Compress();
        String compress = "gtgaacggtggactcac";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ct"));
    }

    @Test
    public void anotherHiddenDigram() { // wasn't assigning correct link to nonterminal
        Compress c = new Compress();
        String compress = "caatgggtatggagc"; // right hand sub rule too long
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("tt"));
    }

    //todo main rule issue? don't just check to sub rules
//    @Test
//    public void reverseComplementinMainRule() { // wasn't assigning correct link to nonterminal
//        Compress c = new Compress();
//        String compress = "tagtgactttca";
//        c.processInput(compress, false);
//        Search s = new Search(c.getFirstRule());
//        assertTrue(s.search("aaagt"));
//    }

    @Test
    public void reverseComplementSubruleWithNonTerminalRight() {
        Compress c = new Compress();
        String compress = "acaccggt";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("gg"));
    }

    @Test
    public void reverseComplementSubruleWithNonTerminalLeft() {
        //split across rules too, 6 (acct) t 6'(aggt)
        Compress c = new Compress();
        String compress = "ctgaccttaggta";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ttag"));
    }

    @Test
    public void notFindingSequenceInMainRule() {
        Compress c = new Compress();
        String compress = "cgcgcag"; // not enough to create a digram on the other side need cg
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("gcag"));
    }


    @Test
    public void ruleNotCreatedProperly() {
        Compress c = new Compress();
        String compress = "gatcagg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertFalse(s.search("gac"));
    }

    @Test
    public void shouldFindReverse() {
        Compress c = new Compress();
        String compress = "aagcggc";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("gct")); // todo problem with reverse complements that are the same forwards and backwards
    }

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
    public void notFindingReverseComplementDigram() { // too small to form a digram
        Compress c = new Compress();
        String compress = "gactctgag";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ca"));
    }

    @Test
    public void ruleTooLongToCheck() {
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
        assertTrue(s.search("tgt")); // not long enough to form rules
    }

    @Test
    public void splitAcrossComplements() {
        Compress c = new Compress();
        String compress = "gctagcgga";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ta")); // too small
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

    @Test
    public void splitAcrossNonTerminals() {
        Compress c = new Compress();
        String compress = "gtacctg";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("ta")); // rule too short
    }


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

    @Test
    public void reverseCompnotfound() {
        Compress c = new Compress();
        String compress = "aaccgagacgagaggtctatgactctgc";
        c.processInput(compress, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("agagt")); // should be 2 for a, 10 10, so rule not long enough
        //gactct
        //todo though reverse complement not found either
    }

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

    @Test
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
        assertTrue(s.search("gtcgcaatttagacaacagccaa")); // not found as first digram cant be formed
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
        assertTrue(s.search("aattt")); // not long enough to find first nonterminal
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
        assertTrue(s.search("aattccggatcaca"));
    }

    @Test
    public void humdystThree() { // last part of string
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile, false);
        Search s = new Search(c.getFirstRule());
        assertTrue(s.search("atatgactttgcaaattca")); //ttcc
    }

    String genRand (int length) {
        Random rand = new Random();
        String possibleLetters = "acgt";
        StringBuilder sb = new StringBuilder(length);
        for(int i = 0; i < length; i++)
            sb.append(possibleLetters.charAt(rand.nextInt(possibleLetters.length())));
        return sb.toString();
    }

//
    @Test
    public void searchRandom() {
        Random rand = new Random();
        String search;
        for (int i = 0; i < 1000; i++) {
            String input = genRand(rand.nextInt((30 - 2) + 1) + 2);
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
            //todo generate random start and finsigh substring
            if (i % 2 == 0) {
                search = input.substring(start, end);
            }
            else {
                search = genRand(start);
            }
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
                if (search.length() > 1 && c.rules.size() > 1) {
                    System.out.println("nomral found " + input.contains(search) );
                    System.out.println("reverse complement found " + input.contains(reverseComplement) );
                    if (s.search(search)) {
                        assertTrue(input.contains(search) || input.contains(reverseComplement));
                    }
                 //   assertEquals(input.contains(search) || input.contains(reverseComplement), s.search(search)); //
                }
            }
            System.out.println();
            System.out.println("count " + i);
        }
    }

    @Test
    public void searchRandomFromFile() {
        Random rand = new Random();
        Compress c = new Compress();
        InputOutput io = new InputOutput();
        String input = io.readFile("humdyst");
        c.processInput(input, false);
        Search s = new Search(c.getFirstRule());
        for (int i = 0; i < 1000; i++) {
            String search = input.substring(rand.nextInt((2 - 1) + 1) + 1, rand.nextInt((20 - 15) + 1) + 15);
            System.out.println("SEARCH: " + search);
            StringBuilder reverseComplement = new StringBuilder();
            for (int j = 0; j < search.length(); j++) {
                reverseComplement.append(Terminal.reverseSymbol(search.charAt(j)));
            }
            reverseComplement.reverse();
            if (search.length() != 0) {
                //System.out.println("nomral found " + input.contains(search) );
                //System.out.println("reverse complement found " + input.contains(reverseComplement) );
                //System.out.println();

                if (s.search(search)) {
                    System.out.println("FOUND");
                    assertTrue(input.contains(search) || input.contains(reverseComplement));
                }
//                assertEquals(input.contains(search) || input.contains(reverseComplement), s.search(search)); //
            }
            System.out.println();
            System.out.println("count " + i);
        }
    }


}
