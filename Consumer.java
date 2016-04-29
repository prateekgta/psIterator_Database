package cyclients.psIterator;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Consumer {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		
		// Initialize the storage file path and instance 
		String databaseFilePath = Constants.workSpacePath+"EmployeeDB.RAF"; 
				
		SPIterator iterator = new SPIterator(databaseFilePath,Constants.workSpacePath+"catalog.xml",Constants.workSpacePath+"expTree.xml");
		
		// Open the connection to the iterator
		
		iterator.open("Emp");
		int recordCount = 0;
		while(iterator.hasNext()) {
			byte[] temp = iterator.getNext();
//			System.out.println(temp);
			System.out.print("Record "+ recordCount +": "+ StringBytes.getString(Arrays.copyOfRange(temp, 0, 10))+","+StringBytes.getString(Arrays.copyOfRange(temp, 10, 20)));//EmployeeTuple.convertToEmployee(temp) + "");

			//if(eachDatabase.equals("Emp")) 
			//System.out.print("Record "+ recordCount +": "+ EmployeeTuple.convertToEmployee(temp) + "");
			//else
			//	System.out.println("Record "+ recordCount +": "+ EmployeeTuple.convertToDepartment(temp) + "");
			System.out.println();
			recordCount++;
		}
				// Close the iterator
		iterator.close();
	}
}
