package GrammarCoder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputOutput {

    public String readCompressedFile() {
        String everything = "";
        byte[] fileContents = new byte[0];
        Path path = Paths.get("/home/tread/IdeaProjects/projectGC/textFiles/compressed.bin");
        try {
            fileContents = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // use more of the byte methods!!
        for (Byte b : fileContents) {
            everything += Integer.toBinaryString(b);
        }

        return everything;
    }

    public String readFile(String fileName) {
        //TODO improve implementation
        String everything = ""; ///home/tread/IdeaProjects/projectGC/compressTest
        try(BufferedReader br = new BufferedReader(new FileReader("/home/tread/IdeaProjects/projectGCG/sourceFiles/" + fileName))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                //sb.append(System.lineSeparator()); //REMOVED AS NEW LINED SYMBOL ADDED WAS MESSING UP.... OK FOR THIS BUT NOT OTHERS
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File Read");
        return everything;
    }

    public String readFile(File file) {
        //TODO improve implementation
        String everything = ""; ///home/tread/IdeaProjects/projectGC/compressTest
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                //sb.append(System.lineSeparator()); //REMOVED AS NEW LINED SYMBOL ADDED WAS MESSING UP.... OK FOR THIS BUT NOT OTHERS
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File Read");
        return everything;
    }
}