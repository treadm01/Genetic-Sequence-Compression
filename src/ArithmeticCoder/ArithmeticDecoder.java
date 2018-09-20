package ArithmeticCoder;/*
 * Reference arithmetic coding
 * Copyright © 2018 Project Nayuki. (MIT License)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

The Software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the Software or the use or other dealings in the Software.
 *
 * https://www.nayuki.io/page/reference-arithmetic-coding
 * https://github.com/nayuki/Reference-arithmetic-coding
 */

import java.io.IOException;
import java.util.Objects;


/**
 * Reads from an arithmetic-coded bit stream and decodes symbols. Not thread-safe.
 * @see ArithmeticEncoder
 */
public final class ArithmeticDecoder extends ArithmeticCoderBase {
	
	/*---- Fields ----*/
	
	// The underlying bit input stream (not null).
	private BitInputStream input;
	
	// The current raw code bits being buffered, which is always in the range [low, high].
	private long code;
	
	
	
	/*---- Constructor ----*/
	
	/**
	 * Constructs an arithmetic coding decoder based on the
	 * specified bit input stream, and fills the code bits.
	 * @param stateSize the number of bits for the arithmetic coding range
	 * @param in the bit input stream to read from
	 * @throws NullPointerException if the input steam is {@code null}
	 * @throws IllegalArgumentException if stateSize is outside the range [1, 62]
	 * @throws IOException if an I/O exception occurred
	 */
	public ArithmeticDecoder(int stateSize, BitInputStream in) throws IOException {
		super(stateSize);
		input = Objects.requireNonNull(in);
		code = 0;
		for (int i = 0; i < STATE_SIZE; i++)
			code = code << 1 | readCodeBit();
	}
	
	
	
	/*---- Methods ----*/
	
	/**
	 * Decodes the next symbol based on the specified frequency table and returns it.
	 * Also updates this arithmetic coder's state and may read in some bits.
	 * @param freqs the frequency table to use
	 * @return the next symbol
	 * @throws NullPointerException if the frequency table is {@code null}
	 * @throws IOException if an I/O exception occurred
	 */
	public int read(FrequencyTable freqs) throws IOException {
		return read(new CheckedFrequencyTable(freqs));
	}
	
	
	/**
	 * Decodes the next symbol based on the specified frequency table and returns it.
	 * Also updates this arithmetic coder's state and may read in some bits.
	 * @param freqs the frequency table to use
	 * @return the next symbol
	 * @throws NullPointerException if the frequency table is {@code null}
	 * @throws IllegalArgumentException if the frequency table's total is too large
	 * @throws IOException if an I/O exception occurred
	 */
	public int read(CheckedFrequencyTable freqs) throws IOException {
		// Translate from coding range scale to frequency table scale
		long total = freqs.getTotal();
		if (total > MAX_TOTAL)
			throw new IllegalArgumentException("Cannot decode symbol because total is too large");
		long range = high - low + 1;
		long offset = code - low;
		long value = ((offset + 1) * total - 1) / range;
		if (value * range / total > offset)
			throw new AssertionError();
		if (value < 0 || value >= total)
			throw new AssertionError();
		
		// A kind of binary search. Find highest symbol such that freqs.getLow(symbol) <= value.
		int start = 0;
		int end = freqs.getSymbolLimit();
		while (end - start > 1) {
			int middle = (start + end) >>> 1;
			if (freqs.getLow(middle) > value)
				end = middle;
			else
				start = middle;
		}
		if (start + 1 != end)
			throw new AssertionError();
		
		int symbol = start;
		if (offset < freqs.getLow(symbol) * range / total || freqs.getHigh(symbol) * range / total <= offset)
			throw new AssertionError();
		update(freqs, symbol);
		if (code < low || code > high)
			throw new AssertionError("Code out of range");
		return symbol;
	}
	
	
	protected void shift() throws IOException {
		code = ((code << 1) & MASK) | readCodeBit();
	}
	
	
	protected void underflow() throws IOException {
		code = (code & TOP_MASK) | ((code << 1) & (MASK >>> 1)) | readCodeBit();
	}
	
	
	// Returns the next bit (0 or 1) from the input stream. The end
	// of stream is treated as an infinite number of trailing zeros.
	private int readCodeBit() throws IOException {
		int temp = input.read();
		if (temp == -1)
			temp = 0;
		return temp;
	}
	
}
