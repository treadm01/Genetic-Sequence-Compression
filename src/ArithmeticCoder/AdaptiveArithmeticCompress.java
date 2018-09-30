package ArithmeticCoder;
/*
 * Reference arithmetic coding
 * Copyright © 2018 Project Nayuki. (MIT License)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

The Software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the Software or the use or other dealings in the Software.
 *
 * https://www.nayuki.io/page/reference-arithmetic-coding
 * https://github.com/nayuki/Reference-arithmetic-coding
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;


/**
 * Compression application using adaptive arithmetic coding.
 * <p>Usage: java AdaptiveArithmeticCompress InputFile OutputFile</p>
 * <p>Then use the corresponding "AdaptiveArithmeticDecompress" application to recreate the original input file.</p>
 * <p>Note that the application starts with a flat frequency table of 257 symbols (all set to a frequency of 1),
 * and updates it after each byte encoded. The corresponding decompressor program also starts with a flat
 * frequency table and updates it after each byte decoded. It is by design that the compressor and
 * decompressor have synchronized states, so that the data can be decompressed properly.</p>
 */
public class AdaptiveArithmeticCompress {
    private static int NONTERMINAL_OFFSET = 130;
    private int tableSize;
    private String PATH = System.getProperty("user.dir");
    private Long finalFileSize;
    private static Set<Character> symbolMarker = Set.of('{', '?', '\'', '!', '#', '*');

    public AdaptiveArithmeticCompress(int numberOfSymbols, List<Character> encodingSymbols) throws IOException {
        tableSize = numberOfSymbols + NONTERMINAL_OFFSET;
        File outputFile = new File(PATH + "/compressed.bin"); //todo name file
        // Perform file compression
        try (BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
            compress(encodingSymbols, out);
        }
        setFinalFileSize(outputFile.length());
    }

    public void compress(List<Character> symbols, BitOutputStream out) throws IOException {
        FlatFrequencyTable initFreqs = new FlatFrequencyTable(tableSize);
        gammaCode(tableSize, out);
        FrequencyTable freqs = new SimpleFrequencyTable(initFreqs);
        ArithmeticEncoder enc = new ArithmeticEncoder(32, out);
        int lastSymbol = -1;

        for (Character s : symbols) {
            // Read and encode one byte
            int symbol = getCorrectSymbol(lastSymbol, s);

            if (symbol != '\'' && symbol != '!' || symbolMarker.contains((char)lastSymbol)) {
                enc.write(freqs, symbol);
                freqs.set(symbol, freqs.get(symbol) + 10); // having this above enc.write would be best
                freqs.increment(symbol);
            }
            if (symbolMarker.contains((char)lastSymbol)) {
                lastSymbol = -1;
            }
            else {
                lastSymbol = s;
            }
        }
        enc.write(freqs, tableSize - 1);  // EOF
        enc.finish();  // Flush remaining code bits
    }

    private void setFinalFileSize(Long size) {
        this.finalFileSize = size;
    }

    public String constructCompressionOutput(Long originalFileSize) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        double percentage = (1 - ((double)finalFileSize / originalFileSize)) * 100;
        double BPC = ((double) (finalFileSize * 8) / (double)originalFileSize);
        String out = "File compressed from " + originalFileSize + " to ";
        out += finalFileSize + " bytes.\n";
        out += decimalFormat.format(percentage) + "% compression.\n";
        out += "Bits per character: " + decimalFormat.format(BPC);
        return out;
    }

    private void gammaCode(int number, BitOutputStream out) throws IOException {
        // encode number of rules
        String ruleNumber = Integer.toBinaryString(number);
        StringBuilder gammaCode = new StringBuilder();
        for (int i = 0; i < ruleNumber.length(); i++) {
            gammaCode.append("1");
        }
        gammaCode.append("0"); // last stop bit
        gammaCode.append(ruleNumber);
        for (int i = 0; i < gammaCode.length(); i++) {
            out.write(Integer.parseInt(gammaCode.substring(i, i + 1)));
        }
    }

    private int getCorrectSymbol(int lastSymbol, char s) {
        if (lastSymbol == '\'' || lastSymbol == '!') { // checking for hash symbol here breaks different, you need a proper solution
            if (lastSymbol == '\'') {
                s += 129;
            }
            else {
                s += 128;
            }
        }
        return s;
    }
}