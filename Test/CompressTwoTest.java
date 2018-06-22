import org.junit.Test;

import static org.junit.Assert.*;

public class CompressTwoTest {
    CompressTwo c = new CompressTwo();

    @Test
    public void simpleInputTest() {
        c.processInput("abcdbc");

        //0 -> a 1 d 1
        //1 -> b c

        //TODO once a digram has been replaced it is not being recognised as repeat
        //TODO keep all history of digrams? don't remove old ones? - How to update nonTerminal rules
        System.out.println();
        c = new CompressTwo();
        c.processInput("abcdbcabc");

//        2 -> 4 d 3 4
//        3 -> b c
//        4 -> a 3

//        System.out.println();
//        c = new CompressTwo();
//        c.processInput("abcdbcabcd");

//
//        System.out.println();
//        c = new CompressTwo();
//        c.processInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"); // this doesnt do the right number of checks
//
//        System.out.println();
//        c = new CompressTwo();
//        c.processInput("abcdbcabcdabcd"); // 4d is 3 so that should be found and updated, then, as
//        // 4 will only be in 3 it should be replaced with a1 and 4 removed
//
//        System.out.println();
//        c = new CompressTwo();
//        c.processInput("abcdbcabcdabcdbcabcd");

    }

}