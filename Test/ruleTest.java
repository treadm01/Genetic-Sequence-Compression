//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class ruleTest {
//    rule testRule = new rule();
//
//    @Test
//    public void addValues() {
//        testRule.addValues("a");
//        testRule.addValues("b");
//    }
//
//    @Test
//    public void checkDigram() {
//        testRule.addValues("a");
//        assertEquals(false, testRule.checkDigram());
//        testRule.addValues("b");
//        assertEquals(false, testRule.checkDigram());
//        testRule.addValues("a");
//        assertEquals(false, testRule.checkDigram());
//        testRule.addValues("b");
//        assertEquals(true, testRule.checkDigram());
//
//        testRule.values.clear();
//
//        testRule.addValues("a");
//        assertEquals(false, testRule.checkDigram());
//        testRule.addValues("a");
//        assertEquals(false, testRule.checkDigram());
//        testRule.addValues("a");
//        assertEquals(false, testRule.checkDigram());
//        testRule.addValues("a");
//        assertEquals(true, testRule.checkDigram());
//
//        testRule.values.clear();
//
//        // check with first string
//        testRule.addValues("a");
//        assertEquals(false, testRule.checkDigram()); // not actually creating or connected to
//        testRule.addValues("b");
//        assertEquals(false, testRule.checkDigram()); // not actually creating or connected to
//        testRule.addValues("c");
//        assertEquals(false, testRule.checkDigram()); // not actually creating or connected to
//        testRule.addValues("d");
//        assertEquals(false, testRule.checkDigram()); // not actually creating or connected to
//        testRule.addValues("b");
//        assertEquals(false, testRule.checkDigram()); // not actually creating or connected to
//        testRule.addValues("c");
//        assertEquals(true, testRule.checkDigram()); // not actually creating or connected to
//
//        testRule.values.clear();
//        String input = "abcdbc";
//
//        for (int i = 0; i < input.length(); i++) {
//            testRule.addValues(input.substring(i, i+1));
//        }
//        assertEquals(true, testRule.checkDigram()); // not actually creating or connected to
//
//    }
//
//    @Test
//    public void getValues() {
//        testRule.addValues("a");
//        testRule.addValues("b");
//        testRule.getValues();
//    }
//
//    @Test
//    public void updateRule() {
//        terminal t = new terminal("a");
//        nonTerminal nt = new nonTerminal();
//
//        bigram b = new bigram(t, nt);
//
//        rule testRuleTwo = new rule();
//        testRuleTwo.addValues(b);
//        testRuleTwo.number = 1;
//
//        testRule.number = 0;
//
//        testRule.addValues("a");
//        testRule.addValues("1");
//        testRule.addValues("d");
//
//        testRule.updateRule(testRuleTwo);
//
//        assertEquals("0 -> 1 d ", testRule.getValues());
//    }
//
//    @Test
//    public void updateRuleTwo() {
//        terminal t = new terminal(" ");
//        nonTerminal nt = new nonTerminal();
//
//        bigram b = new bigram(t, nt);
//
//        rule testRuleTwo = new rule();
//        testRuleTwo.addValues(b);
//        testRuleTwo.number = 1;
//
//        testRule.number = 0;
//
//        testRule.addValues("d");
//        testRule.addValues(" ");
//        testRule.addValues("f");
//
//        testRule.updateRule(testRuleTwo);
//
//        assertEquals("0 -> d 1 ", testRule.getValues());
//    }
//
//    @Test
//    public void getCurrentBigram() {
//    }
//
//    @Test
//    public void setCurrentBigram() {
//    }
//
//    @Test
//    public void addValues1() {
//        testRule.addValues(2);
//    }
//
//    @Test
//    public void getBigramsSpaceTHREE() {
//        testRule.addValues("d");
//        testRule.addValues(" ");
//        testRule.addValues("f");
//
//        List<bigram> testBigramList = new ArrayList<>();
//
//        terminal leftS = new terminal("d");
//        terminal rightS = new terminal(" ");
//        bigram testBigram = new bigram(leftS, rightS);
//        testBigramList.add(testBigram);
//
//        leftS = new terminal(" ");
//        rightS = new terminal("f");
//        testBigram = new bigram(leftS, rightS);
//        testBigramList.add(testBigram);
//
//        assertEquals(testBigramList, testRule.getBigrams());
//    }
//
//    @Test
//    public void getBigramsTwo() {
//        testRule.addValues("a");
//        testRule.addValues("a");
//
//        List<bigram> testBigramList = new ArrayList<>();
//
//        assertEquals(testBigramList, testRule.getBigrams());
//    }
//
//
//    @Test
//    public void getBigramsFour() {
//        testRule.addValues("a");
//        testRule.addValues("a");
//        testRule.addValues("a");
//        testRule.addValues("a");
//
//        List<bigram> testBigramList = new ArrayList<>();
//
//        terminal leftS = new terminal("a");
//        terminal rightS = new terminal("a");
//        bigram testBigram = new bigram(leftS, rightS);
//        testBigramList.add(testBigram);
//
//        leftS = new terminal("a");
//        rightS = new terminal("a");
//        testBigram = new bigram(leftS, rightS);
//        testBigramList.add(testBigram);
//
//        assertEquals(testBigramList, testRule.getBigrams());
//    }
//
//    @Test
//    public void getBigrams() {
//        testRule.addValues("a");
//        testRule.addValues("b");
//        testRule.addValues("c");
//        testRule.addValues("d");
//        testRule.addValues("b");
//        testRule.addValues("c");
//
//        List<bigram> testBigramList = new ArrayList<>();
//
//        terminal leftS = new terminal("a");
//        terminal rightS = new terminal("b");
//        bigram testBigram = new bigram(leftS, rightS);
//        testBigramList.add(testBigram);
//
//        leftS = new terminal("b");
//        rightS = new terminal("c");
//        testBigram = new bigram(leftS, rightS);
//        testBigramList.add(testBigram);
//
//        leftS = new terminal("c");
//        rightS = new terminal("d");
//        testBigram = new bigram(leftS, rightS);
//        testBigramList.add(testBigram);
//
//        leftS = new terminal("d");
//        rightS = new terminal("b");
//        testBigram = new bigram(leftS, rightS);
//        testBigramList.add(testBigram);
//
//        assertEquals(testBigramList, testRule.getBigrams());
//
//    }
//
//    @Test
//    public void getRuleNumber() {
//    }
//}