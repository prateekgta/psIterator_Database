package cyclients.psIterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class SPIterator extends BaseIterator{
	
	private byte[] nextTuple;
	private boolean hasNextTuple = false;
	private String relationName;
	private ArrayList<HashMap<String,String>> criteriaMapList;
	
	/**
	 * @description Creates an iterator instance for SP iteration by taking Catalog file path and AbstractSyntaxTree file path as input
	 * @param filePath
	 * @param astFilePath
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public SPIterator(String filePath, String catalogPath, String astFilePath) throws SAXException, IOException, ParserConfigurationException {
		super(filePath, catalogPath);
		criteriaMapList = ExpTreeParser.parseAWT(astFilePath);
	}
	
	/**
	 * @description Overrides the open function of BaseIterator - considers Selection criteria for the relation and sets the pointer to the first tuple that meets the selection criteria
	 * @param relationName
	 */
	@Override
	public void open(String relationName) {
//		System.out.println("In open");
//		System.out.println("RelationName:"+relationName);
		this.relationName = relationName;
		super.open(relationName);
		while(super.hasNext()) {
//			System.out.println("super.hasNext():"+super.hasNext());
			byte[] tempTuple = super.getNext();
//			System.out.println(EmployeeTuple.convertToEmployee(tempTuple));
//			System.out.println("evaluateCriteria(tempTuple):"+evaluateCriteria(tempTuple));
			if(evaluateCriteria(tempTuple)) {
//				System.out.println(evaluateCriteria(tempTuple));
				nextTuple = tempTuple;
				hasNextTuple = true;
				break;
			}
		}
	}
	
	/**
	 * @description Returns the current tuple pointed to by the pointer and sets the pointer to the next tuple that meets the selection criteria
	 * @return
	 */
	@Override
	public byte[] getNext() {
		
		byte[] currentTuple = nextTuple;
		boolean foundAMatch = false;
		
		if(!super.hasNext()) {
//			System.out.println(EmployeeTuple.convertToEmployee(currentTuple));
			hasNextTuple = false;
			nextTuple = null;
			return applyProjection(currentTuple);
		}
		
		while(super.hasNext()) {
//			System.out.println(EmployeeTuple.convertToEmployee(currentTuple));
			byte[] tempTuple = super.getNext();
//			System.out.println("evaluateCriteria(tempTuple):"+evaluateCriteria(tempTuple));
			if(evaluateCriteria(tempTuple)) {
				nextTuple = tempTuple;
				foundAMatch = true;
				hasNextTuple = true;
				break;
			}
		}
		if(!foundAMatch) {
			hasNextTuple = false;
			nextTuple = null;
		}
		return applyProjection(currentTuple);
	}
	
	// Assumes "And" operation of all available filters
	/**
	 * @description Evaluates all the criteria in the AST XML file for the relation and returns true if the tuple meets the criteria, else returns false
	 * @param tuple
	 * @return
	 */
	private boolean evaluateCriteria(byte[] tuple) {
		boolean hasMetCriteria = true;
		
		for(HashMap<String, String> eachCriteria : criteriaMapList) {
//			System.out.println("eachCriteria.get(dbRelName):"+eachCriteria.get("dbRelName")+", databaseName:"+relationName);
			if(eachCriteria.get("dbRelName").equals(relationName)) {
//				System.out.println("eachCriteria.get(dbRelName):"+eachCriteria.get("dbRelName")+", databaseName:"+relationName);
				String dbAttrName = eachCriteria.get("dbAttrName");
				String dbAttrType = eachCriteria.get("dbAttrType");
//				System.out.println("dbAttrType:"+dbAttrType);
				String dbRelName = eachCriteria.get("dbRelName");
				String opType = eachCriteria.get("opType");
				String constType = eachCriteria.get("constType");
				String constValue = eachCriteria.get("constValue");	
				
				int integerValue = 0;
				String stringValue = "";
				
				int startPostion = XMLParser.getChildStartPosition(relationName,dbAttrName);
				int attrLength = XMLParser.getChildLength(relationName, dbAttrName);
//				System.out.println("startPosition:"+startPostion + "AttrLength:"+attrLength);
				byte[] currentAttribute = Arrays.copyOfRange(tuple, startPostion, startPostion+attrLength);
				if(dbAttrType.equals("integer")) {
					integerValue = IntegerBytes.getInteger(currentAttribute);
//					System.out.println("Current value of attribute1:"+integerValue);
				}
				else {
					stringValue = StringBytes.getString(currentAttribute);
//					System.out.println("Current value of attribute:"+stringValue);

				}
//				System.out.println("opType:"+opType);
				switch(opType) {
					case ">" : 	if(dbAttrType.equals("integer") && constType.equals("integer")) {
//									System.out.println("In greater than function");
									hasMetCriteria &= integerValue>Integer.valueOf(constValue)?true:false; 
								}
								else {
									hasMetCriteria &= false;
								}
								break;
					case "<" : 	if(dbAttrType.equals("integer") && constType.equals("integer")) {
									hasMetCriteria &= integerValue<Integer.valueOf(constValue)?true:false; 
								}
								else {
									hasMetCriteria &= false;
								}
								break;
					case "=" : 	if(dbAttrType.equals("integer") && constType.equals("integer")) {
									hasMetCriteria &= integerValue==Integer.valueOf(constValue)?true:false; 
								}
								else {
									hasMetCriteria &= stringValue.equals(constValue);
								}
								break;
					default :	hasMetCriteria &= false;
								break; 						
					
				}
			}
		}
		
		return hasMetCriteria;
	}
	
	/**
	 * @description Returns true if there is the pointer is pointing to a tuple satisfying the criteria, else returns false
	 */
	@Override
	public boolean hasNext() {
//		System.out.println(hasNextTuple);
		// TODO Auto-generated method stub
		return hasNextTuple;
	}
	
	public byte[] applyProjection(byte[] tuple) {
		byte[] tupleProjection = null;
		
		ArrayList<HashMap<String,String>> attributeList = ExpTreeParser.getdbAttrList(relationName);
//		System.out.println("attributeList"+attributeList);
		for(HashMap<String,String> hm:attributeList) {
			String dbAttrName = hm.get("dbAttrName");
//			System.out.println(hm.get("dbAttrName"));
//			System.out.println("dbAttrName"+dbAttrName);
			int startPostion = XMLParser.getChildStartPosition(relationName, dbAttrName);
			int attrLength = XMLParser.getChildLength(relationName, dbAttrName);
			byte[] currentAttribute = Arrays.copyOfRange(tuple, startPostion, startPostion+attrLength);
			tupleProjection = mergeArrays(tupleProjection, currentAttribute);
		}
//		System.out.println("Tuple projected:"+new String(tupleProjection));
		return tupleProjection;
	}
	
	private byte[] mergeArrays(byte[] first, byte[] second) {
		if(first==null && second==null)
			return null;
		else if (first==null)
			return second;
		else if(second==null) {
			return first;
		}
		byte[] result = new byte[first.length+second.length];
		System.arraycopy(first, 0, result, 0, first.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}