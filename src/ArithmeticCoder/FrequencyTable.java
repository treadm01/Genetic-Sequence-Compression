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


/**
 * A table of symbol frequencies. The table holds data for symbols numbered from 0
 * to getSymbolLimit()&minus;1. Each symbol has a frequency, which is a non-negative integer.
 * <p>Frequency table objects are primarily used for getting cumulative symbol
 * frequencies. These objects can be mutable depending on the implementation.
 * The total of all symbol frequencies must not exceed Integer.MAX_VALUE.</p>
 */
public interface FrequencyTable {
	
	/**
	 * Returns the number of symbols in this frequency table, which is a positive number.
	 * @return the number of symbols in this frequency table
	 */
	public int getSymbolLimit();
	
	
	/**
	 * Returns the frequency of the specified symbol. The returned value is at least 0.
	 * @param symbol the symbol to query
	 * @return the frequency of the symbol
	 * @throws IllegalArgumentException if the symbol is out of range
	 */
	public int get(int symbol);
	
	
	/**
	 * Sets the frequency of the specified symbol to the specified value.
	 * The frequency value must be at least 0.
	 * @param symbol the symbol to set
	 * @param freq the frequency value to set
	 * @throws IllegalArgumentException if the frequency is negative or the symbol is out of range
	 * @throws ArithmeticException if an arithmetic overflow occurs
	 */
	public void set(int symbol, int freq);
	
	
	/**
	 * Increments the frequency of the specified symbol.
	 * @param symbol the symbol whose frequency to increment
	 * @throws IllegalArgumentException if the symbol is out of range
	 * @throws ArithmeticException if an arithmetic overflow occurs
	 */
	public void increment(int symbol);
	
	
	/**
	 * Returns the total of all symbol frequencies. The returned value is at
	 * least 0 and is always equal to {@code getHigh(getSymbolLimit() - 1)}.
	 * @return the total of all symbol frequencies
	 */
	public int getTotal();
	
	
	/**
	 * Returns the sum of the frequencies of all the symbols strictly
	 * below the specified symbol value. The returned value is at least 0.
	 * @param symbol the symbol to query
	 * @return the sum of the frequencies of all the symbols below {@code symbol}
	 * @throws IllegalArgumentException if the symbol is out of range
	 */
	public int getLow(int symbol);
	
	
	/**
	 * Returns the sum of the frequencies of the specified symbol
	 * and all the symbols below. The returned value is at least 0.
	 * @param symbol the symbol to query
	 * @return the sum of the frequencies of {@code symbol} and all symbols below
	 * @throws IllegalArgumentException if the symbol is out of range
	 */
	public int getHigh(int symbol);
	
}
