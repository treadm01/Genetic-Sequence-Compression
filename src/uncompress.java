import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class uncompress {

    //TODO save to file and retrieve in a way that objects are known to be terminal, non terminal, rule
    // from there can start to compare

    public String getOutput(rule r, List<rule> compressedRule, String soFar) {
        String uncompressed = soFar;
        for (symbol s : r.values) {
            if (s instanceof terminal) {
                uncompressed += s.getRepresentation();
            }
            else {
                for (rule rx : compressedRule) {
                    if (rx.getRuleNumber() == Integer.parseInt(String.valueOf(s.getRepresentation()))) {
                        uncompressed = getOutput(rx, compressedRule, uncompressed);
                    }
                }
            }
        }
        return uncompressed;
    }

    public String processInput(List<rule> compressedRule) {

        rule firstRule = compressedRule.get(0);
        String uncompressed = "";

        uncompressed += getOutput(firstRule, compressedRule, uncompressed);

        return uncompressed;
    }

    public String readFile() {
        String everything = "";
        byte[] fileContents = new byte[0];
        Path path = Paths.get("/home/tread/IdeaProjects/GeneticCompression/textFiles/compressed.bin");
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
}
