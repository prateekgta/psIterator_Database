package cyclients.psIterator;
//package Randomaccess.Storage;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {
	public static void main(String args[]) {

		/**
		 * Buffer to write the data to the file
		 */
		byte[] writeBuffer = new byte[1024];
		writeBuffer = "HelloWorld".getBytes();

		/**
		 * Location of the file to which the read/write operations are performed
		 */
		String path = "device_0.RAF";

		/**
		 * Buffer to read the data from the file
		 */
		byte[] readBuffer = new byte[1024];

		int pageNumber = -1;

		RandomAccess randomAccess = new RandomAccess(path);

		pageNumber = randomAccess.allocatePage();
		
		
		System.out.println("PageNumber:"+pageNumber);
		if (pageNumber < 0) {
			System.out.println("Page allocation failed");
		} else {
			System.out.println("Page successfully allocated");
		}
		
		randomAccess.writePage(writeBuffer, pageNumber);

		randomAccess.readPage(readBuffer, pageNumber);

		/**
		 * prints the data read from the file
		 */
		for (int i = 0; i < readBuffer.length; i++) {
			System.out.print(readBuffer[i]);
		}
		
		randomAccess.deallocatePage(pageNumber);
	}

}
