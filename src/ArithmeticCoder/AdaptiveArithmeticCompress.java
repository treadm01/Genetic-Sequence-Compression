package ArithmeticCoder;/*
 * Reference arithmetic coding
 * Copyright (c) Project Nayuki
 *
 * https://www.nayuki.io/page/reference-arithmetic-coding
 * https://github.com/nayuki/Reference-arithmetic-coding
 */

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    int tableSize;
    String PATH = System.getProperty("user.dir") + "/compressedFiles";

    public AdaptiveArithmeticCompress(int numberOfSymbols, List<String> encodingSymbols) throws IOException {
        tableSize = numberOfSymbols + 130; // 128 offset for symbols, + 1 for eof symbol
        File outputFile = new File(PATH + "/compressed.bin"); //todo use previous filename?

        // Perform file compression
        try (BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
            compress(encodingSymbols, out);
        }
    }

    // supect its ok initially but have to worry about byte completion when adding in middle of stream
    public void gammaCode(int number, BitOutputStream out) throws IOException {
        // encode number of rules
        String ruleNumber = Integer.toBinaryString(number);
        String gammaCode = "";
        for (int i = 0; i < ruleNumber.length(); i++) {
            gammaCode += "1";
        }
        gammaCode += "0"; // last stop bit
        gammaCode += ruleNumber;
        System.out.println(gammaCode);
        for (int i = 0; i < gammaCode.length(); i++) {
            out.write(Integer.parseInt(gammaCode.substring(i, i + 1)));
        }
    }

    // To allow unit testing, this method is package-private instead of private.
    public void compress(List<String> symbols, BitOutputStream out) throws IOException {
        FlatFrequencyTable initFreqs = new FlatFrequencyTable(tableSize);
        gammaCode(tableSize, out);
        FrequencyTable freqs = new SimpleFrequencyTable(initFreqs);
        ArithmeticEncoder enc = new ArithmeticEncoder(32, out);
        String lastSymbol = "";

        for (String s : symbols) {
            // Read and encode one byte
            int symbol;

            if (s.length() == 1 && (s.charAt(0) > 32 && s.charAt(0) < 128)) {
                symbol = s.charAt(0);
            }
            else {
                symbol = Integer.parseInt(s) + 128;
            }

            if (symbol == -1)
                break;

            // todo also have to send the ! symbol for the string although not used here
            if (symbol != '!') {
                enc.write(freqs, symbol);
                freqs.set(symbol, freqs.get(symbol) + 10); // having this above enc.write would be best
                freqs.increment(symbol);
            }
        }
        enc.write(freqs, tableSize - 1);  // EOF
        enc.finish();  // Flush remaining code bits
//        System.out.println(freqs);
    }

    // todo probably can be more detailed as to the frequencies, but makes compression worse
    //if (s.length() == 1 && (s.charAt(0) > 32 && s.charAt(0) < 128)) {
    //                freqs.set(symbol, freqs.get(symbol) + 10); // todo putting this before enc.write gives good compression, but hard to decompress...
    //            }
    //            else {
    //                if (symbol % 2 == 0) { // if even then not a complemeent... not sure this is working for markers, the lower numbers
    //                    freqs.set(symbol, freqs.get(symbol) + 8);
    //                } else {
    //                    freqs.set(symbol, freqs.get(symbol) + 2);
    //                }
    //            }

}