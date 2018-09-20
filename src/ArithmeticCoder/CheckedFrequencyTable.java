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

import java.util.Objects;


/**
 * A wrapper that checks the preconditions (arguments) and postconditions (return value)
 * of all the frequency table methods. Useful for finding faults in a frequency table
 * implementation. However, arithmetic overflow conditions are not checked.
 */
public final class CheckedFrequencyTable implements FrequencyTable {

	/*---- Fields ----*/

	// The underlying frequency table that holds the data (not null).
	private FrequencyTable freqTable;



	/*---- Constructor ----*/

	public CheckedFrequencyTable(FrequencyTable freq) {
		freqTable = Objects.requireNonNull(freq);
	}



	/*---- Methods ----*/

	public int getSymbolLimit() {
		int result = freqTable.getSymbolLimit();
		if (result <= 0)
			throw new AssertionError("Non-positive symbol limit");
		return result;
	}


	public int get(int symbol) {
		int result = freqTable.get(symbol);
		if (!isSymbolInRange(symbol))
			throw new AssertionError("IllegalArgumentException expected");
		if (result < 0)
			throw new AssertionError("Negative symbol frequency");
		return result;
	}


	public int getTotal() {
		int result = freqTable.getTotal();
		if (result < 0)
			throw new AssertionError("Negative total frequency");
		return result;
	}


	public int getLow(int symbol) {
		if (isSymbolInRange(symbol)) {
			int low   = freqTable.getLow (symbol);
			int high  = freqTable.getHigh(symbol);
			if (!(0 <= low && low <= high && high <= freqTable.getTotal()))
				throw new AssertionError("Symbol low cumulative frequency out of range");
			return low;
		} else {
			freqTable.getLow(symbol);
			throw new AssertionError("IllegalArgumentException expected");
		}
	}


	public int getHigh(int symbol) {
		if (isSymbolInRange(symbol)) {
			int low   = freqTable.getLow (symbol);
			int high  = freqTable.getHigh(symbol);
			if (!(0 <= low && low <= high && high <= freqTable.getTotal()))
				throw new AssertionError("Symbol high cumulative frequency out of range");
			return high;
		} else {
			freqTable.getHigh(symbol);
			throw new AssertionError("IllegalArgumentException expected");
		}
	}


	public String toString() {
		return "CheckFrequencyTable (" + freqTable.toString() + ")";
	}


	public void set(int symbol, int freq) {
		freqTable.set(symbol, freq);
		if (!isSymbolInRange(symbol) || freq < 0)
			throw new AssertionError("IllegalArgumentException expected");
	}


	public void increment(int symbol) {
		freqTable.increment(symbol);
		if (!isSymbolInRange(symbol))
			throw new AssertionError("IllegalArgumentException expected");
	}


	private boolean isSymbolInRange(int symbol) {
		return 0 <= symbol && symbol < getSymbolLimit();
	}

}
