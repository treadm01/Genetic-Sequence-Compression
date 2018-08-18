//package ArithmeticCoder;/*
// * Reference arithmetic coding
// * Copyright (c) Project Nayuki
// *
// * https://www.nayuki.io/page/reference-arithmetic-coding
// * https://github.com/nayuki/Reference-arithmetic-coding
// */
//
//import GrammarCoder.Compress;
//import GrammarCoder.InputOutput;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * Compression application using adaptive arithmetic coding.
// * <p>Usage: java AdaptiveArithmeticCompress InputFile OutputFile</p>
// * <p>Then use the corresponding "AdaptiveArithmeticDecompress" application to recreate the original input file.</p>
// * <p>Note that the application starts with a flat frequency table of 257 symbols (all set to a frequency of 1),
// * and updates it after each byte encoded. The corresponding decompressor program also starts with a flat
// * frequency table and updates it after each byte decoded. It is by design that the compressor and
// * decompressor have synchronized states, so that the data can be decompressed properly.</p>
// */
//public class AdaptiveArithmeticCompress {
//    static int tableSize;
//
//	public static void main(String[] args) throws IOException {
//		// Handle command line arguments
////		if (args.length != 2) {
////			System.err.println("Usage: java AdaptiveArithmeticCompress InputFile OutputFile");
////			System.exit(1);
////			return;
////		}
////		File inputFile  = new File(args[0]);
////		File outputFile = new File(args[1]);
//
//		Compress c = new Compress();
//        InputOutput io  = new InputOutput();
//        String originalFile = io.readFile("chntxx");
//        c.processInput(originalFile);
//        tableSize = c.highestRule + 129; // 128 offset for symbols, + 1 for eof symbol
//
//		//File inputFile  = new File("/home/tread/files/aetest/src/compress");
//		File outputFile = new File("/home/tread/files/aetest/src/compressed.bin");
//
//		// Perform file compression
//		try (BitOutputStream out = new BitOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
//			compress(c.encodingSymbols, out);
//		}
//	}
//
//
//	// To allow unit testing, this method is package-private instead of private.
//	static void compress(List<String> symbols, BitOutputStream out) throws IOException {
//		FlatFrequencyTable initFreqs = new FlatFrequencyTable(tableSize);
//		String ruleNumber = Integer.toBinaryString(tableSize);
//        System.out.println(ruleNumber.length());
//        System.out.println(tableSize);
//        String gammaCode = "";
//
//        for (int i = 0; i < ruleNumber.length(); i++) {
//            gammaCode += "1";
//        }
//        gammaCode += "0"; // last stop bit
//        gammaCode += ruleNumber;
//        System.out.println(ruleNumber);
//		for (int i = 0; i < gammaCode.length(); i++) {
//            out.write(Integer.parseInt(gammaCode.substring(i, i + 1)));
//        }
//		FrequencyTable freqs = new SimpleFrequencyTable(initFreqs);
//		ArithmeticEncoder enc = new ArithmeticEncoder(32, out);
//		for (String s : symbols) {
//			// Read and encode one byte
//            int symbol;
//
//            // this includes single digit numbers
//            if (s.length() == 1 && (s.charAt(0) > 32 && s.charAt(0) < 128)) {
//                symbol = s.charAt(0);
//                System.out.println((char) symbol + " " + freqs.get(symbol) + symbol);
//              //  freqs.set(symbol, freqs.get(symbol) + 10);
//            }
//            else {
//                symbol = Integer.parseInt(s) + 128;
//            }
//
////			symbol = symbolInts.get(s);
//			if (symbol == -1)
//				break;
//
//			enc.write(freqs, symbol);
//            freqs.set(symbol, freqs.get(symbol) + 10); // having this above enc.write would be best
//			freqs.increment(symbol);
//		}
//		enc.write(freqs, tableSize - 1 );  // EOF
//		enc.finish();  // Flush remaining code bits
////        System.out.println(freqs);
//	}
//
//	// todo probably can be more detailed as to the frequencies, but makes compression worse
//	//if (s.length() == 1 && (s.charAt(0) > 32 && s.charAt(0) < 128)) {
//    //                freqs.set(symbol, freqs.get(symbol) + 10); // todo putting this before enc.write gives good compression, but hard to decompress...
//    //            }
//    //            else {
//    //                if (symbol % 2 == 0) { // if even then not a complemeent... not sure this is working for markers, the lower numbers
//    //                    freqs.set(symbol, freqs.get(symbol) + 8);
//    //                } else {
//    //                    freqs.set(symbol, freqs.get(symbol) + 2);
//    //                }
//    //            }
//
//}
