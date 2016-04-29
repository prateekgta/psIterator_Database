package cyclients.psIterator;
//package Randomaccess.Storage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccess {

	/**
	 * The location of the file on which read/write operations are performed
	 */
	private String path;

	private static int pageAllocated = -1;

	/**
	 * Max allowed storage size is 1MB
	 */
	private final int storageSize = 1024;

	RandomAccess(String path) {
		this.path = path;

	}

	/**
	 * 
	 * @param buffer
	 *            - the buffer into which the data is read
	 * @param pageNumber
	 *            - the start page in an array at which the data is read
	 */
	protected void readPage(byte[] buffer, int pageNumber) {
		int offSet = pageNumber * 1024;
		RandomAccessFile randomAccess = null;
		try {
			randomAccess = new RandomAccessFile(path, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			randomAccess.seek(offSet);
			randomAccess.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				randomAccess.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Writes the byte stream to the randomAcces file from the specified offset
	 * 
	 * @param data
	 *            - the buffer from which the data is written
	 * @param pageNumber
	 *            - the start page in an array at which the data is written
	 */
	protected void writePage(byte[] buffer1, int pageNumber) {
		int offSet = pageNumber * 1024;
		RandomAccessFile randomAccess = null;
		try {
			randomAccess = new RandomAccessFile(path, "rw");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		try {
			randomAccess.seek(offSet);
			randomAccess.write(buffer1);

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				randomAccess.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * Allocates the next available page
	 * 
	 * @return - returns the page number of the next available page
	 * 
	 */
	protected int allocatePage() {

		return pageAllocated++ <= storageSize ? pageAllocated : -1;
	}

	/**
	 * Deallocates the specified page
	 * 
	 * @param pageNumber
	 *            - the number of the page that need to be deallocated
	 */
	protected void deallocatePage(int pageNumber) {
		System.out.println('\n' + "Request for dealltion of page " + pageNumber
				+ " received");
	}
}
