package cyclients.psIterator;
/**
 * 
 */
//package com.iastate.edu;

import java.io.UnsupportedEncodingException;

/**
 * @author Sony
 * 
 */
public class StringBytes {
	
	/**
	 * @description Returns the byte array of the input string
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] getByteArray(String str) throws UnsupportedEncodingException {
		byte stringBytes[] = str.getBytes();
		return stringBytes;
	}
	
	/**
	 * @description Returns the string corresponding to the input byte array
	 * @param bytes
	 * @return
	 */
	public static String getString(byte[] bytes) {
		if(bytes!=null) {
			return new String(bytes);
		}
		return new String("");
	}	
}