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
        fp.updateGrammar(input);

        fp = new FindPattern();
        input = "abcdbcabc";
        fp.updateGrammar(input);

        // enforce rule utility
        fp = new FindPattern();
        input = "abcdbcabcd";
        fp.updateGrammar(input);

        fp = new FindPattern();
        input = "abcabdabcab";
        fp.updateGrammar(input);

        fp = new FindPattern();
        input = "abcabdabcabdabcabdabcabd";
        fp.updateGrammar(input);

        // getting duplicate abcabdabcabdabcabdabcabdabcabdabcabd
    }
}