import GrammarCoder.Compress;
import org.junit.Test;

import static org.junit.Assert.*;

    public class CompressionTest {

//        @Test
//        public void compressBook() {
//            Compress c = new Compress();
//            InputOutput io = new InputOutput();
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
//            InputOutput io = new InputOutput();
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
            int old = 38656;
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
            int old = 50165;
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

            int old = 74259;
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
            int old = 14918;
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
            int old = 21787;
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
            int old = 26797;
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
            int old = 57154;
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
            int old = 33785;
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
            int old = 58697;
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
            int old = 20670;
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
            int old = 21497;
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
