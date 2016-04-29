package cyclients.psIterator;
//package com.iastate.edu;

public class IntegerBytes {
	
	/**
	 * @description Returns a byte array for a given integer value
	 * @param value
	 * @return
	 */
	public static byte[] getByteArray(int value) {
		byte[] intBytes = new byte[Constants.INTEGERSIZE];
		
		for(int i=Constants.INTEGERSIZE-1;i>=0;i--) {
			intBytes[i] = (byte) ((value >> (Constants.INTEGERSIZE-1-i)*8) & 0xFF);
		}
/*		intBytes[0] = (byte) ((value >> 24) & 0xFF);
		intBytes[1] = (byte) ((value >> 16) & 0xFF);
		intBytes[2] = (byte) ((value >> 8) & 0xFF);
		intBytes[3] = (byte) (value & 0xFF);
*/
		return intBytes;
	}
	
	/**
	 * @description Returns the integer value corresponding to the input byte array
	 * @param bytes
	 * @return
	 */
	public static int getInteger(byte[] bytes) {
		int value = 0;
	
		for (int i = 0; i < Constants.INTEGERSIZE; i++) {
			int shift = (Constants.INTEGERSIZE - 1 - i) * 8;
			value += (bytes[i] & 0x000000FF) << shift;
		}
		return value;
	}
}
