package cyclients.psIterator;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class BaseIterator {
	
	private int filePointer = 0;
	private int currentPageNumber = 0;
	private int nextPage = -1;
	private int numOfTuples = 0;
	private int currentPageSize = 0;
	private int tuplesReadSoFar = 0; 
	private byte[] readBuffer;
	private byte[] header = new byte[Constants.HEADERSIZE];
	private boolean hasNextTuple = false;
	private int tupleLength = 0;
	private RandomAccess randomAccess;
	private byte nextTuple[];
	
	/**
	 * @description Creates an iterator instance by taking the Catalog file as an input
	 * @param filePath
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public BaseIterator(String filePath, String catalogPath) throws SAXException, IOException, ParserConfigurationException {
		randomAccess = new RandomAccess(filePath);
		XMLParser.parseXML(catalogPath);
		
	}
	
	/**
	 * @description Fetches relation details from Catalog file and sets the pointer to the first tuple of the relation in the storage
	 * @param relationName
	 */
	public void open(String relationName) {
		tupleLength = XMLParser.getChildTotalLength(relationName);
		HashMap<String, String> attributeMap = XMLParser.ParentInfo.get(relationName);
		int startPage = Integer.valueOf(attributeMap.get("StartPage"));
		
//		System.out.println("StartPage:"+startPage);
//		System.out.println("tupleLength:"+tupleLength+", startPage:"+  startPage);
		
		readBlock(startPage);
	}
	
	/**
	 * @description Returns the current tuple pointed to by the pointer and sets the pointer to the next available tuple, returns null otherwise 
	 * @return
	 */
	public byte[] getNext() {

		byte[] currentTuple = nextTuple;
		if(tuplesReadSoFar >= numOfTuples-1) { 
			if(nextPage==-1) {
				hasNextTuple = false;
				nextTuple = null;
			}
			else {
				readBlock(nextPage);
			}
			return currentTuple;			
		}
		tuplesReadSoFar++;
		nextTuple = new byte[tupleLength];
		System.arraycopy(readBuffer, Constants.HEADERSIZE+(tupleLength*tuplesReadSoFar), nextTuple, 0, tupleLength);
		hasNextTuple = true;
		
		return currentTuple;
	}
	
	/**
	 * @description Returns false if there are no more tuples to be iterated, else returns true
	 * @return
	 */
	public boolean hasNext() {
		return hasNextTuple;		
	}
	
	/**
	 * @description Closes the iterator instance
	 */
	public void close() {
		
	}
	
	/**
	 * @description Helper function that reads header bytes and returns the integer values of the header for use by the iterator 
	 * @param headerBytes
	 * @return
	 */
	private int[] readHeader(byte[] headerBytes) {
		int headerValues[] = new int[4];
		
		for(int i=0;i<4;i++) {
			byte tempBytes[] = new byte[4];
			System.arraycopy(headerBytes, 4*i, tempBytes, 0, 4);
			headerValues[i] = IntegerBytes.getInteger(tempBytes);		
		}
		
		return headerValues; 
	}
	
	/**
	 * @description Helper function that takes care of reading header and initiating the pointer to the first tuple given a new page
	 * @param pageNumber
	 * @return
	 */
	private int readBlock(int pageNumber) {
		readBuffer = new byte[Constants.PAGESIZE];
		randomAccess.readPage(readBuffer, pageNumber);
//		System.out.println("PageNumber:"+pageNumber+", Read Buffer data:"+new String(readBuffer));
/*		for (int i = 0; i < readBuffer.length; i++) {
			System.out.print(readBuffer[i]);
		}
*/
		System.arraycopy(readBuffer, 0, header, 0, Constants.HEADERSIZE);
		int []headerValues = readHeader(header);
		currentPageNumber = headerValues[0];
		nextPage = headerValues[1];
		numOfTuples = headerValues[2];
		currentPageSize = headerValues[3];
		
		tuplesReadSoFar = 0; 
		
		nextTuple = new byte[tupleLength];
		System.arraycopy(readBuffer, Constants.HEADERSIZE+(tupleLength*tuplesReadSoFar), nextTuple, 0, tupleLength);
//		System.out.println("\n1st Tuple in the page:");
/*		for (int i = 0; i < nextTuple.length; i++) {
			System.out.print(nextTuple[i]);
		}
*/
		hasNextTuple = true;
				
//		System.out.println("Header data:"+headerValues);
/*		for(int i=0;i<headerValues.length;i++) 
			System.out.print(headerValues[i] + " ");
*/
		return filePointer;
	}
}
