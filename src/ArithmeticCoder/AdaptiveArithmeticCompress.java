package ArithmeticCoder;/*
 * Reference arithmetic coding
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/reference-arithmetic-coding
 * https://github.com/nayuki/Reference-arithmetic-coding
 */

import GrammarCoder.Compress;
import GrammarCoder.InputOutput;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    static int tableSize;
	
	public static void main(String[] args) throws IOException {
		// Handle command line arguments
//		if (args.length != 2) {
//			System.err.println("Usage: java AdaptiveArithmeticCompress InputFile OutputFile");
//			System.exit(1);
//			return;
//		}
//		File inputFile  = new File(args[0]);
//		File outputFile = new File(args[1]);

		Compress c = new Compress();
        InputOutput io  = new InputOutput();
        String originalFile = io.readFile("humdyst");
        c.processInput(originalFile);
        tableSize = c.highestRule + 1;

		//File inputFile  = new File("/home/tread/files/aetest/src/compress");
		File outputFile = new File("/home/tread/files/aetest/src/compressed.bin");
		
		// Perform file compression
		try (BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
			compress(c.encodingSymbols, out);
		}
	}
	
	
	// To allow unit testing, this method is package-private instead of private.
	static void compress(List<String> symbols, BitOutputStream out) throws IOException {

        Map<String, Integer> symbolInts = new HashMap<>();
        List<String> orderedListOfSymbols = new ArrayList<>();
        for (String s : symbols) {
            if (!symbolInts.containsKey(s)) {
                symbolInts.put(s, symbolInts.size());
                orderedListOfSymbols.add(s);
            }
        }

        //todo need to specify the necessary number of symbols, the more accurate and lower the better
        // basically the highest number symbol....
        System.out.println(orderedListOfSymbols);
        System.out.println(orderedListOfSymbols.size());

		// get number of unique symbols from encoding and send that through
		FlatFrequencyTable initFreqs = new FlatFrequencyTable(2450);
        // store in array of special objects? with string for symbol, if not seen add to next
        // if seen use it
		FrequencyTable freqs = new SimpleFrequencyTable(initFreqs);
		ArithmeticEncoder enc = new ArithmeticEncoder(32, out);
		for (String s : symbols) {
			// Read and encode one byte

            // then rather than symbol stream send through list of symbols... no wait
            // must be the source, so create a list of strings from the encoding,
            // store in a hashset for int values, then when symbol seen get hash value and
            // encode that

            // if not a number send as char int, else symbol equals parse int

            int symbol;
            // if a normal symbol just encode as such, and set complement to false

            //todo issue could be that some weird symbols register as a digit, need to use specific values
            // yeah but, they would be registered in the same way the other end right?
            //but bein assesed different, string ??? vs int... not sure, without changes, it works...
            // and may be able to use frequency again
            if (s.length() == 1 && s.charAt(0) > 32 && s.charAt(0) < 128) {
                symbol = s.charAt(0);
                System.out.println((char) symbol + " " + freqs.get(symbol) + symbol);
//                freqs.set(symbol, freqs.get(symbol) + 10);
            }
            else {
                //todo make sure that the actual correct symbols are all being dealt with here

                //System.out.println((char)Integer.parseInt(s));
                symbol = Integer.parseInt(s);
                System.out.println(s + " " + freqs.get(symbol) + " " + (char)symbol);
//                if (symbol % 2 == 0) { // if even then not a complemeent... not sure this is working for markers, the lower numbers
//                    freqs.set(symbol, freqs.get(symbol) + 8);
//                } else {
//                    freqs.set(symbol, freqs.get(symbol) + 2);
//                }
            }

//			symbol = symbolInts.get(s);
			if (symbol == -1)
				break;

			enc.write(freqs, symbol);
			freqs.increment(symbol);
		}
		enc.write(freqs, 2450-1);  // EOF
		enc.finish();  // Flush remaining code bits
        System.out.println(freqs);
	}
	
}
