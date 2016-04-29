package cyclients.psIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class AWTParser {

	// Save the parent information  <booleanFactor booleanFactorType="attrOpConst">
	static HashMap<String,String> booleanFactorInfo = new HashMap<String,String>();
	
	/*                        Save the child Information | Get value by booleanFactorTypechildInfo.get(0);
	 *  <dbAttr dbAttrName="Salary" dbAttrType="integer" dbRelAliasName="e" dbRelName="Emp" />
                  <comparisonOp opType="&gt;" />
                  <dbConstValue constType="integer" constValue="70000" />
	 * 
	 */
	static ArrayList<HashMap<String,String>> booleanFactorTypechildInfo = new ArrayList<HashMap<String,String>>();
	
	/*
	 * Function used To parse the AWT-XML
	 */
	public static ArrayList<HashMap<String,String>> parseAWT(String Path) throws SAXException, IOException, ParserConfigurationException{
				
		       // path = "Path Of AWT-XML"
			   File inputFile = new File(Path);
			   DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			   DocumentBuilder dBuilder;
			
			   dBuilder = dbFactory.newDocumentBuilder();
			   Document doc = dBuilder.parse(inputFile);
			   doc.getDocumentElement().normalize();
			  
			   NodeList nList = doc.getElementsByTagName("booleanFactor");
			   
			   if (nList.getLength() > 0){
				  
			       for (int i=0; i<nList.getLength(); i++){
			        	      
			        	Node n1 = nList.item(i);
			        	if (n1.getNodeType() == Node.ELEMENT_NODE)
			        	{
			            booleanFactorInfo.put("booleanFactorType",n1.getAttributes().getNamedItem("booleanFactorType").getNodeValue());
			        	NodeList childNode = n1.getChildNodes();
			        	HashMap<String,String> temp = new HashMap<String,String>();
			        	for(int j = 0;j<childNode.getLength();j++){
			        		
			        		if(childNode.item(j).getNodeType() == Node.ELEMENT_NODE){
			        			
			        			if(childNode.item(j).getNodeName().equals("dbAttr")){
			        	
			        				temp.put("dbAttrName", childNode.item(j).getAttributes().getNamedItem("dbAttrName").getNodeValue());
			        				temp.put("dbAttrType", childNode.item(j).getAttributes().getNamedItem("dbAttrType").getNodeValue());
			        				temp.put("dbRelAliasName", childNode.item(j).getAttributes().getNamedItem("dbRelAliasName").getNodeValue());
			        				temp.put("dbRelName", childNode.item(j).getAttributes().getNamedItem("dbRelName").getNodeValue());
			        			}
			        			else if(childNode.item(j).getNodeName().equals("comparisonOp")){
			        				
			        				temp.put("opType", childNode.item(j).getAttributes().getNamedItem("opType").getNodeValue());
			        			}
			        			else if(childNode.item(j).getNodeName().equals("dbConstValue")){
			        				
			        				temp.put("constType", childNode.item(j).getAttributes().getNamedItem("constType").getNodeValue());
			        				temp.put("constValue", childNode.item(j).getAttributes().getNamedItem("constValue").getNodeValue());
			        			}
			        			
			         }
			        		
			       }
//			        	System.out.println(temp.keySet());
	        			booleanFactorTypechildInfo.add(temp);
			        	
			
			   }
			}
		}
			   return booleanFactorTypechildInfo;
	}
		/*
		 * Function Used To Print Xml 
		 */
		public static void printAWT(){
			String s = "booleanFactorType";
			String boolFactorType = booleanFactorInfo.get(s);
//			System.out.println("booleanFactorType ----:"+boolFactorType);
			
			/*
			 * Map contains all the child attributes information 
			 * 
			 */
			HashMap<String,String> childinfo =booleanFactorTypechildInfo.get(0);
		
			String s0 =childinfo.get("dbAttrName");
			System.out.println("dbAttrName :"+s0);
			String s1 = childinfo.get("dbAttrType");
			System.out.println("dbAttrType :"+s1);
			String s2 = childinfo.get("dbRelAliasName");
			System.out.println("dbRelAliasName :"+s2);
			String s3 = childinfo.get("dbRelName");
			System.out.println("dbRelName :"+s3);
			
			String s4 = childinfo.get("opType");
			System.out.println("opType :"+s4);
			String s5 = childinfo.get("constType");
			System.out.println("constType :"+s5);
			String s6 = childinfo.get("constValue");
			System.out.println("constValue :"+s6);
			
			
		}

/*
 * To Test
 * 
 */
		
 public static void main(String[] args){
	 try{
		 parseAWT("AST.xml");
		 printAWT();

	 }
	 catch(Exception e){
		 
	 }
	}
		   
}
