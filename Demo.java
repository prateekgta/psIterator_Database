package cyclients.psIterator;


import java.io.IOException;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Demo{

	public static ArrayList<Byte[]> service(String catalogPath, String expTreePath, String relationName) throws SAXException, IOException, ParserConfigurationException {
		
		// Initialize the storage file path and instance 
//		String databaseFilePath = "cyclients/psIterator/Workspace/EmployeeDB.RAF"; 
		ArrayList<Byte[]> byteArrayList = new ArrayList<Byte[]>();				
		SPIterator  iterator = new SPIterator (Constants.databaseFilePath, catalogPath, expTreePath);
				
		// Open the connection to the iterator
				
		iterator.open(relationName);
		int recordCount = 0;
		while(iterator.hasNext() && recordCount<10) {
			byte[] temp = iterator.getNext();
//			System.out.println("temp:"+temp);
			Byte[] tempByteArray = new Byte[temp.length];
			int i=0;
			for(byte b:temp) {
				tempByteArray[i++] = Byte.valueOf(b);
			}
			byteArrayList.add(tempByteArray);
//			System.out.println("relationName : "+relationName);
//			System.out.print("Record "+ recordCount +": "+ StringBytes.getString(Arrays.copyOfRange(temp, 0, 10))+","+StringBytes.getString(Arrays.copyOfRange(temp, 10, 20)));//EmployeeTuple.convertToEmployee(temp) + "");
			
			if(relationName.equals("Emp")) 
			System.out.print("Record "+ recordCount +": "+ EmployeeTuple.convertToEmp(temp) + "");
			else
			System.out.println("Record "+ recordCount +": "+ EmployeeTuple.convertToDept(temp) + "");
			recordCount++;
		}
				// Close the iterator
		iterator.close();
		return byteArrayList;
	}

	public static void main(String [] args) throws SAXException, IOException, ParserConfigurationException{
		Demo.service("cyclients/psIterator/Workspace/Catalog.xml", "cyclients/psIterator/Workspace/output.xml" , "Dept");
	
	}	
}
