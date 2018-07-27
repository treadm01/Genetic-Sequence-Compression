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
        String everything = "";
        try(BufferedReader br = new BufferedReader(new FileReader("/home/tread/IdeaProjects/projectGC/textFiles/" + fileName))) {
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

/*
    public String writeFile(List<GrammarCoder.NonTerminal> finalRules) {
        //TODO break this up into process binary and separate write file methods
        System.out.println("writing file");
        String fullBinary = "";

        int largestRuleSize = Integer.toBinaryString(finalRules.size()).length();
        System.out.println(largestRuleSize);

        for (GrammarCoder.NonTerminal r : finalRules) {
            String binaryRuleLength = "";
            String ruleInBinary = Integer.toBinaryString(r.values.size());
            for (int i = 0; i < ruleInBinary.length(); i++) {
                binaryRuleLength += 1;
            }
            binaryRuleLength += 0 + ruleInBinary;

            fullBinary += binaryRuleLength;

            for (GrammarCoder.Symbol s : r.values) {
                String binarySymbolRepresentation = "";
                String lengthOfSymbol = "";
                if (s instanceof GrammarCoder.NonTerminal) {
                    binarySymbolRepresentation = Integer.toBinaryString(Integer.valueOf(s.toString()));

                    for (int i = 0; i < binarySymbolRepresentation.length(); i++) {
                        lengthOfSymbol += 1;
                    }
                    lengthOfSymbol += 0;
                    binarySymbolRepresentation = lengthOfSymbol + binarySymbolRepresentation;
                }
                else {
                    // first 0 to show non terminal second for actg
                    binarySymbolRepresentation = "0";

                    switch (s.toString()) {
                        case "G":
                            binarySymbolRepresentation += "00";
                            break;
                        case "C":
                            binarySymbolRepresentation += "01";
                            break;
                        case "A":
                            binarySymbolRepresentation += "10";
                            break;
                        case "T":
                            binarySymbolRepresentation += "11";
                            break;
                    }
                }
                fullBinary += binarySymbolRepresentation;
            }

        }

        //System.out.println("last good " + fullBinary);

        while (fullBinary.length() % 6 != 0) {
            fullBinary += "0";
            //System.out.println("grow" + fullBinary);
        }

        String reallyFinal = "";
        for (int i = 0; i < fullBinary.length()/6; i++) {
            reallyFinal += "1" + fullBinary.substring(i * 6, i * 6 + 6);
            //  System.out.println("really " + reallyFinal);
        }



//        while (fullBinary.length() % 7 != 0) {
//            fullBinary += "0";
//        }

        //System.out.println(fullBinary);

        byte[] test = new byte[reallyFinal.length()/7];

        //dropping 0

        for (int i = 0; i < reallyFinal.length()/7; i++) {
            byte value = Byte.parseByte(reallyFinal.substring(i * 7, i*7 + 7), 2);
            test[i] = value;
        }
        //test = fullBinary.getBytes();
//        System.out.println(test[0]);


//        System.out.println("bytes in test : ");
//        for (Byte b : test) {
//            System.out.print(Integer.toBinaryString(b));
//        }

        //System.out.println("full binary : ");

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream("/home/tread/IdeaProjects/GeneticCompression/textFiles/compressed.bin");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(test);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Done");
        return reallyFinal;

    }


*/


}