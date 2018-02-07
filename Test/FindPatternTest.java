import org.junit.Test;

import static org.junit.Assert.*;

public class FindPatternTest {
    FindPattern fp = new FindPattern();


    @Test
    public void getGrammars() {
    }

    @Test
    public void addSymbol() {
        String input = "abcdbc";
        for (int i = 0; i < input.length(); i++) {
            fp.addSymbol(0, input.substring(i, i + 1));
        }
        System.out.println(fp.getGrammars().toString());
    }

    @Test
    public void getDigram() {
        String input = "ab";
        for (int i = 0; i < input.length(); i++) {
            fp.addSymbol(0, input.substring(i, i + 1));
        }

        assertEquals("ab", fp.getDigram());

        input = "abcdefgh";
        for (int i = 0; i < input.length(); i++) {
            fp.addSymbol(0, input.substring(i, i + 1));
        }

        assertEquals("gh", fp.getDigram());

    }

    @Test
    public void checkForPattern() {
        String input = "abcdbc";
        for (int i = 0; i < input.length(); i++) {
            fp.addSymbol(0, input.substring(i, i + 1));
        }
        Integer test = 2;

        assertEquals(test, fp.checkForPattern(fp.getDigram()));
    }

    @Test
    public void createRule() {
    }

    @Test
    public void updateGrammar() {
        String input = "abcdbc";
//        fp.updateGrammar(input);
//        assertEquals("a1d1", fp.getGrammars().get(0));
//
//        fp = new FindPattern();
//        input = "abcdbcabc";
//        fp.updateGrammar(input);
//        assertEquals("2d12", fp.getGrammars().get(0));
//
//        // enforce rule utility
//        fp = new FindPattern();
//        input = "abcdbcabcd";
//        fp.updateGrammar(input);
//        assertEquals("313", fp.getGrammars().get(0));
//
//        fp = new FindPattern();
//        input = "abcabdabcab";
//        fp.updateGrammar(input);
//        assertEquals("3d3", fp.getGrammars().get(0));
//
//        // works but not in the most concise way, makes duplicate rules...
//        fp = new FindPattern();
//        input = "abcabdabcabdabcabdabcabd";
//        fp.updateGrammar(input);
//
////        //HANDLE SINGLE INPUT
////        fp = new FindPattern();
////        input = "a";
////        fp.updateGrammar(input);
//
//        //{0=4466882222, 2=aa, 4=22, 6=22, 8=22}
//        //vs
//        //
//        //0 → 1 1
//        //1 → 2 2
//        //2 → 3 3
//        //3 → 4 4
//        //4 → a a
//
//        // does it again here, have to sort out the loop
//        //NO PAIR OF ADJACENT SYMBOLS APPEAR MORE THAN ONCE IN THE GRAMMAR, NEEDS TO BE A CHECK
//        //THROUGHOUT
//        fp = new FindPattern();
//        input = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//        fp.updateGrammar(input);

        fp = new FindPattern();
        input = "pease porridge hot pease porridge hot";
        fp.updateGrammar(input);

    }
}