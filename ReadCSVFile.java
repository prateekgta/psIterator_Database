package cyclients.psIterator;
//package com.iastate.edu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ReadCSVFile {
	
	static RandomAccess randomAccess;
	static HashMap<String, Integer> startPageMap = new HashMap<String, Integer>();
	
	
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		
		// Initialize the storage file path and instance 
		String databaseFilePath = Constants.workSpacePath+"EmployeeDB.RAF";
		randomAccess = new RandomAccess(databaseFilePath);
				
		try {
			XMLParser.parseXML(Constants.workSpacePath+"catalog.xml");
			for(String databaseName : XMLParser.databasename) {
				HashMap<String, String> attributeMap = XMLParser.ParentInfo.get(databaseName);
				if(attributeMap!=null && attributeMap.size()>0) {
					String filePath = attributeMap.get("DIW_File");
					System.out.println(attributeMap.keySet()+"\n"+attributeMap);
					int numOfAttributes = Integer.valueOf(attributeMap.get("numOfAttributes"));
					int tupleLength = XMLParser.getChildTotalLength(databaseName);
					int startPage = readCSVFile(databaseName, filePath, numOfAttributes, tupleLength);
					startPageMap.put(databaseName, startPage);
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

/*		
		int pagenumber=0;
		String deptFilePath = "Employee100.csv";
		
		// Read the input file and write it to the storage
		readCSVFile(deptFilePath);
		randomAccess = new RandomAccess(databaseFilePath);
		readDatabase(deptFilePath,pagenumber);
*/		
		// Iterate through the file 
		System.out.println();
		BaseIterator iterator = new BaseIterator(databaseFilePath,Constants.workSpacePath+"catalog.xml");
		// Open the connection to the iterator
		for(String eachDatabase : startPageMap.keySet()) {
				System.out.println("Startpage:"+startPageMap.get(eachDatabase));
				iterator.open(eachDatabase);
				int recordCount = 0;
				while(iterator.hasNext()) {
					byte[] temp = iterator.getNext();
					System.out.println();
					if(eachDatabase.equals("Emp")) 
						System.out.print("Record "+ recordCount +": "+ EmployeeTuple.convertToEmployee(temp) + "");
					else
						System.out.println("Record "+ recordCount +": "+ EmployeeTuple.convertToDepartment(temp) + "");
					System.out.println();
					recordCount++;
				}
				// Close the iterator
				iterator.close();
		}
	}
	
public static int readCSVFile(String databaseName, String filePath, int numOfAttributes, int tupleLength) {
		
		BufferedReader br = null;
		String line = "";
		String csvSeparator = ",";
		int count=0;
		int pageNumber = 0;
		int startPage = 0;
		
		int recordSize = tupleLength;
		int nextPage = -1;
		int maxNumTuples = (Constants.PAGESIZE-Constants.HEADERSIZE)/recordSize; 
		byte[] writeBuffer = new byte[Constants.PAGESIZE];
		int writeBufferCurrentLength = Constants.HEADERSIZE;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			int lineNumber = 0;
			pageNumber = randomAccess.allocatePage();
			startPage = pageNumber;
			while ((line = br.readLine()) != null) {
				// use comma as separator
				if(lineNumber!=0) {
				//	System.out.println("line"+line);
					if(count<maxNumTuples) {
						count++;
						System.out.println("Count value:"+count);
					}
					else {
						//reset count
						nextPage = pageNumber + 1;
						byte[] headerBytes = createHeader(pageNumber, nextPage, maxNumTuples, Constants.PAGESIZE);
						System.arraycopy(headerBytes, 0, writeBuffer, 0, headerBytes.length);
						System.out.println("PageNumber:"+pageNumber);
						randomAccess.writePage(writeBuffer, pageNumber);
						System.out.println("writeBuffer:"+writeBuffer.toString());
						for (int i = 0; i < writeBuffer.length; i++) {
							System.out.print(writeBuffer[i]);
						}
						
						byte[] readBuffer = new byte[Constants.PAGESIZE];
						randomAccess.readPage(readBuffer, 1);
						
						System.out.println("\nreadBuffer:"+readBuffer.toString());
						for (int i = 0; i < readBuffer.length; i++) {
							System.out.print(readBuffer[i]);
						}
						System.out.println();
						
						System.out.println("Count:"+lineNumber);
						count=0;
						count++;
						writeBufferCurrentLength = Constants.HEADERSIZE;
						pageNumber = randomAccess.allocatePage();
						writeBuffer = new byte[Constants.PAGESIZE];
					}
					
					// Assumed file has 4 coulumns - EmpId-4 (int), EmpName-10(String), Department-10 (String), Salary-4(int)
					String[] empData = line.split(csvSeparator);
					byte[] tupleBuffer = new byte[recordSize];
					byte[] tempBuffer;
					int currentLength = 0;
					
					ArrayList<HashMap<String,String>> childArray = XMLParser.ChildInfo.get(databaseName);
					for(int v =0 ; v<childArray.size(); v++) {
						HashMap<String,String> childInfo = childArray.get(v);
						   //System.out.println("--------------"+"Child"+v+"----------------");
						   //System.out.print("AttributeName :"+childInfo.get("attrName")+"");
						   //System.out.print(" AttributeType :"+childInfo.get("attrType")+"");
						   //System.out.println(" AttributeLength :"+childInfo.get("attrLength"));
						   int attrLength = Integer.valueOf(childInfo.get("attrLength"));
						   if(childInfo.get("attrType").equals("integer")) {
							   tempBuffer = IntegerBytes.getByteArray(Integer.parseInt(empData[v]));
						   }
						   // else it is a string datatype
						   else {
							   int strLength = 0;
							   if(empData[v]!=null)
								   strLength = empData[v].length();
							   else 
								   empData[v] = "";
							   if(strLength < attrLength) {
								   for(int hashCount=0;hashCount<=(attrLength-strLength);hashCount++) {
									   empData[v]+='#';
								   }
							   }
							   else {
								   empData[v] = empData[v].substring(0, attrLength);
							   }
							   tempBuffer = StringBytes.getByteArray(empData[v]);
						   }
						   System.arraycopy(tempBuffer, 0, tupleBuffer, currentLength, attrLength);
						   currentLength+=attrLength;
					   }

					System.out.println("writeBufferCurrentLength:"+writeBufferCurrentLength);
					System.arraycopy(tupleBuffer, 0, writeBuffer, writeBufferCurrentLength, tupleBuffer.length);
					writeBufferCurrentLength+=tupleLength;
					
//					System.out.println("\nTemp Buffer:"+tempBuffer);
					//System.out.println("COnverted employee record:"+EmployeeTuple.convertToEmployee(Arrays.copyOfRange(writeBuffer,,));
					
		//			System.out.println("[id= " + empData[0] + " , EmployeeName=" + empData[1] + " ,Dept "+ empData[2]+" , Salary"+ empData[3]+"]");

				}
				lineNumber++;		        
			}
			// Update the header of the last page
			nextPage = -1;
			byte[] headerBytes = createHeader(pageNumber, nextPage, count, Constants.PAGESIZE);
			System.arraycopy(headerBytes, 0, writeBuffer, 0, headerBytes.length);
			randomAccess.writePage(writeBuffer, pageNumber);
			System.out.println("writeBufferLast:"+writeBuffer.toString());
			for (int i = 0; i < writeBuffer.length; i++) {
				System.out.print(writeBuffer[i]);
			}
			System.out.println("Count:"+lineNumber);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return startPage;
	}

	
	
	
	public static byte[] createHeader(int thisPage,int nextPage,int numTuples,int pageSize) {
		byte[] header = new byte[Constants.HEADERSIZE];
		byte[] tempBytes = IntegerBytes.getByteArray(thisPage);
		int usedLength = 0;
		System.arraycopy(tempBytes, 0, header, usedLength, tempBytes.length);
		usedLength += tempBytes.length;
		
		tempBytes = IntegerBytes.getByteArray(nextPage);
		System.arraycopy(tempBytes, 0, header, usedLength, tempBytes.length);
		usedLength += tempBytes.length;
		
		tempBytes = IntegerBytes.getByteArray(numTuples);
		System.arraycopy(tempBytes, 0, header, usedLength, tempBytes.length);
		usedLength += tempBytes.length;
		
		tempBytes = IntegerBytes.getByteArray(pageSize);
		System.arraycopy(tempBytes, 0, header, usedLength, tempBytes.length);
		usedLength += tempBytes.length;		
		return header;
	}
	
	public static void readCSVFile(String filePath) {
		
		BufferedReader br = null;
		String line = "";
		String csvSeparator = ",";
		int count=0;
		int pageNumber = 0;
		
		int recordSize = 28;
		int nextPage = -1;
		int maxNumTuples = (Constants.PAGESIZE-Constants.HEADERSIZE)/recordSize; 
		byte[] writeBuffer = new byte[Constants.PAGESIZE];
		int writeBufferCurrentLength = Constants.HEADERSIZE;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			int lineNumber = 0;
			pageNumber = randomAccess.allocatePage();
			while ((line = br.readLine()) != null) {
				// use comma as separator
				if(lineNumber!=0) {
				//	System.out.println("line"+line);
					if(count<maxNumTuples) {
						count++;
						System.out.println("Count value:"+count);
					}
					else {
						//reset count
						nextPage = pageNumber + 1;
						byte[] headerBytes = createHeader(pageNumber, nextPage, maxNumTuples, Constants.PAGESIZE);
						System.arraycopy(headerBytes, 0, writeBuffer, 0, headerBytes.length);
						System.out.println("PageNumber:"+pageNumber);
						randomAccess.writePage(writeBuffer, pageNumber);
						System.out.println("writeBuffer:"+writeBuffer.toString());
						for (int i = 0; i < writeBuffer.length; i++) {
							System.out.print(writeBuffer[i]);
						}
						
						byte[] readBuffer = new byte[Constants.PAGESIZE];
						randomAccess.readPage(readBuffer, 1);
						
						System.out.println("\nreadBuffer:"+readBuffer.toString());
						for (int i = 0; i < readBuffer.length; i++) {
							System.out.print(readBuffer[i]);
						}
						System.out.println();
						
						System.out.println("Count:"+lineNumber);
						count=0;
						count++;
						writeBufferCurrentLength = Constants.HEADERSIZE;
						pageNumber = randomAccess.allocatePage();
						writeBuffer = new byte[Constants.PAGESIZE];
					}
					
					
					// Assumed file has 4 coulumns - EmpId-4 (int), EmpName-10(String), Department-10 (String), Salary-4(int)
					String[] empData = line.split(csvSeparator);
					byte[] tupleBuffer = new byte[recordSize];
					byte[] tempBuffer;
					int currentLength = 0;
					
					tempBuffer = IntegerBytes.getByteArray(Integer.parseInt(empData[0]));
					System.arraycopy(tempBuffer, 0, tupleBuffer, currentLength, tempBuffer.length);
					currentLength+=tempBuffer.length;
					
					tempBuffer = StringBytes.getByteArray(empData[1]);
					System.arraycopy(tempBuffer, 0, tupleBuffer, currentLength, tempBuffer.length);
					currentLength+=tempBuffer.length;
					
					tempBuffer = StringBytes.getByteArray(empData[2]);
					System.arraycopy(tempBuffer, 0, tupleBuffer, currentLength, tempBuffer.length);
					currentLength+=tempBuffer.length;
					
					tempBuffer = IntegerBytes.getByteArray(Integer.parseInt(empData[3]));
					System.arraycopy(tempBuffer, 0, tupleBuffer, currentLength, tempBuffer.length);
					currentLength+=tempBuffer.length;
					
					System.out.println("writeBufferCurrentLength:"+writeBufferCurrentLength);
					System.arraycopy(tupleBuffer, 0, writeBuffer, writeBufferCurrentLength, tupleBuffer.length);
					writeBufferCurrentLength+=recordSize;
					
//					System.out.println("\nTemp Buffer:"+tempBuffer);
					//System.out.println("COnverted employee record:"+EmployeeTuple.convertToEmployee(Arrays.copyOfRange(writeBuffer,,));
					
		//			System.out.println("[id= " + empData[0] + " , EmployeeName=" + empData[1] + " ,Dept "+ empData[2]+" , Salary"+ empData[3]+"]");

				}
				lineNumber++;		        
			}
			// Update the header of the last page
			nextPage = -1;
			byte[] headerBytes = createHeader(pageNumber, nextPage, count, Constants.PAGESIZE);
			System.arraycopy(headerBytes, 0, writeBuffer, 0, headerBytes.length);
			randomAccess.writePage(writeBuffer, pageNumber);
			System.out.println("writeBufferLast:"+writeBuffer.toString());
			for (int i = 0; i < writeBuffer.length; i++) {
				System.out.print(writeBuffer[i]);
			}
			System.out.println("Count:"+lineNumber);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static void readDatabase(String path, int pageNumber) {

		byte[] readBuffer = new byte[Constants.PAGESIZE];
		randomAccess.readPage(readBuffer, 1);
		
		System.out.println("readBuffer:"+readBuffer.toString());
		for (int i = 0; i < readBuffer.length; i++) {
			System.out.print(readBuffer[i]);
		}
		
		randomAccess.readPage(readBuffer, 2);
		System.out.println("readBuffer:"+readBuffer.toString());
		for (int i = 0; i < readBuffer.length; i++) {
			System.out.print(readBuffer[i]);
		}
		
		randomAccess.readPage(readBuffer, 3);
		System.out.println("readBuffer:"+readBuffer.toString());
		for (int i = 0; i < readBuffer.length; i++) {
			System.out.print(readBuffer[i]);
		}
		
		
		int startPage = 1;
		randomAccess.readPage(readBuffer, 1);
		
		
		
	}
	
	
	public static void getBytes() throws UnsupportedEncodingException {
		Integer i = 60000;
	//	System.out.println(Integer.bitCount(i));
		
		byte [] stringBytes = String.valueOf(i).getBytes("UTF-16");
		for(byte each:stringBytes) {
			System.out.println(each);
		}
		System.out.println(String.valueOf(i).getBytes());
		
		//System.arraycopy(arg0, arg1, arg2, arg3, arg4);
		String a = "Adsa######";
		byte b[] = a.getBytes("UTF-8");
		for(byte each:b) {
			System.out.println(each);
		}
		String s = new String(b);
		System.out.println(s);
	}

}
