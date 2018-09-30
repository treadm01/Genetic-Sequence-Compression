package GrammarCoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputOutput {
    private String PATH = System.getProperty("user.dir");

    /**
     * method to open file and return contents as a string
     * @param file
     * @return
     */
    public String readFile(File file) {
        String sequence = "";
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            sequence = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sequence;
    }

    /**
     * write decompressed string to file
     * @param output
     */
    public void writeToFile(String output) {
        try (PrintWriter out = new PrintWriter(PATH + "/decompressed")) {
            out.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *  used for testing only of direct file names
     */
    public String readFile(String fileName) {
        String everything = "";
        try(BufferedReader br = new BufferedReader(new FileReader(PATH + "/sourceFiles/" + fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return everything;
    }
}