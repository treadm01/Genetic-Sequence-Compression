import GrammarCoder.Compress;
import GrammarCoder.InputOutput;
import org.junit.Test;

import static org.junit.Assert.*;

    public class CompressionTest {
        //todo replace old values with values using integers for rules

//        @Test
//        public void compressBook() {
//            Compress c = new Compress();
//            GrammarCoder.InputOutput io = new GrammarCoder.InputOutput();
//            String originalFile = io.readFile("30000");
//            c.processInput(originalFile.toLowerCase());
//            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
//            System.out.println("original " + originalFile.length());
//            System.out.println("compressed " + compare.length());
//            System.out.println("Difference " + (originalFile.length() - compare.length()));
//            System.out.println("length of rules " + c.printRules().length());
//            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
//            assertTrue(compare.length() < originalFile.length());
//        }

//        @Test
//        public void writeFile() {
//            Compress c = new Compress();
//            GrammarCoder.InputOutput io = new GrammarCoder.InputOutput();
//            String originalFile = io.readFile("testLong");
//            c.processInput(originalFile);
//            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
//            System.out.println("original " + originalFile.length());
//            System.out.println("compressed " + compare.length());
//            System.out.println("Difference " + (originalFile.length() - compare.length()));
//            System.out.println("length of rules " + c.printRules().length());
//            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
//            assertTrue(compare.length() < originalFile.length());
//        }

        @Test
        public void chmpxx() {
            int old = 91642;
            String fileName = "chmpxx";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void chntxx() {
            int old = 123125;
            String fileName = "chntxx";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void hehcmv() {

            int old = 181765;
            String fileName = "hehcmv";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void humdyst() {
            int old = 31795;
            String fileName = "humdyst";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void humghcs() {
            int old = 32800;
            String fileName = "humghcs";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void humhbb() {
            int old = 55925;
            String fileName = "humhbb";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void vaccg() {
            int old = 143942;
            String fileName = "vaccg";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            System.out.println(compare);
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void mtpacga() {
            int old = 76925;
            String fileName = "mtpacga";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void mpomtcg() {
            int old = 145702;
            String fileName = "mpomtcg";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void humprtb() {
            int old = 45159;
            String fileName = "humprtb";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

        @Test
        public void humhdab() {
            int old = 46018;
            String fileName = "humhdab";
            Compress c = new Compress();
            InputOutput io = new InputOutput();
            String originalFile = io.readFile(fileName);
            c.processInput(originalFile);
            String compare = c.encode(c.getFirstRule().getGuard().getRight(), "");
            System.out.println("File " + fileName);
            System.out.println("original " + originalFile.length());
            System.out.println("compressed " + compare.length() + " old " + old);
            System.out.println("Difference " + (originalFile.length() - compare.length()));
            System.out.println("length of rules " + c.printRules().length());
            System.out.println("BPC " + (float)(compare.length() * 8) / originalFile.length());
            assertTrue(compare.length() < originalFile.length());
            assertTrue(compare.length() <= old);
        }

}
