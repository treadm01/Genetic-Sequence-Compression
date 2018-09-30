package GrammarCoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputOutput {
    private String PATH = System.getProperty("user.dir");
    public String readFile(File file) { //todo
        String everything = "";
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
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

    public void writeToFile(String output) {
        try (PrintWriter out = new PrintWriter(PATH + "/compressTest.txt")) {
            out.println(output);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // used for testing only
    public String readFile(String fileName) {
        String everything = "";
        try(BufferedReader br = new BufferedReader(new FileReader("/home/tread/IdeaProjects/projectGCG/sourceFiles/" + fileName))) {
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