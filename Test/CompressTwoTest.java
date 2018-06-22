import org.junit.Test;

import static org.junit.Assert.*;

public class CompressTwoTest {
    CompressTwo c = new CompressTwo();

    @Test
    public void simpleInputTest() {
        c.processInput("abcdbc");

        //0 -> a 1 d 1
        //1 -> b c

        System.out.println();
        c = new CompressTwo();
        c.processInput("abcdbcabc");

//        2 -> 4 d 3 4
//        3 -> b c
//        4 -> a 3

        System.out.println();
        c = new CompressTwo();
        c.processInput("abcdbcabcd");

//        5 -> 8 6 8
//        6 -> b c
//        8 -> a 6 d


//        System.out.println();
//        c = new CompressTwo();
//        c.processInput("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"); // this doesnt do the right number of checks
//
////        9 -> 13 13
////        10 -> a a
////        11 -> 10 10
////        12 -> 11 11
////        13 -> 12 12
//
//        System.out.println();
//        c = new CompressTwo();
//        c.processInput("abcdbcabcdabcd"); // 4d is 3 so that should be found and updated, then, as
//        // 4 will only be in 3 it should be replaced with a1 and 4 removed
//
////        14 -> 17 15 17 17
////        17 -> a 15 d
////        15 -> b c
//
//        System.out.println();
//        c = new CompressTwo();
//        c.processInput("abcdbcabcdabcdbcabcd");

//        19 -> 26 26
//        20 -> b c
//        22 -> a 20 d
//        26 -> 22 20 22


    }

}