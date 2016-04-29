package cyclients.psIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XMLParser {
	// Contains the Name of Databases
	static ArrayList<String> databasename = new ArrayList<String>();
	// Contains The Child Information of Database
	static HashMap<String,ArrayList<HashMap<String,String>>> ChildInfo = new HashMap<String,ArrayList<HashMap<String,String>>>();
	// Contains the parent Information of Database
	static HashMap<String,HashMap<String,String>> ParentInfo = new HashMap<String,HashMap<String,String>>();
	
	/*
	 * Function used To parse the XML
	 */
	public static void parseXML(String Path) throws SAXException, IOException, ParserConfigurationException{
				
       // path = "Path Of XML"
		File inputFile = new File(Path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
	
		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
	  
		NodeList nList = doc.getElementsByTagName("dbRel");
	   
		if (nList.getLength() > 0){
		  
			for (int i=0; i<nList.getLength(); i++){
	        	      
				Node n1 = nList.item(i);
	        	if (n1.getNodeType() == Node.ELEMENT_NODE) {
		            HashMap<String,String> parentAttributeInfo = new HashMap<String,String>();
		            parentAttributeInfo.put("relName",n1.getAttributes().getNamedItem("relName").getNodeValue());
		            parentAttributeInfo.put("numOfAttributes",n1.getAttributes().getNamedItem("numOfAttributes").getNodeValue());
		            parentAttributeInfo.put("StartPage", n1.getAttributes().getNamedItem("StartPage").getNodeValue());
		        	parentAttributeInfo.put("DIW_File", n1.getAttributes().getNamedItem("DIW_File").getNodeValue());
		            
		        	NodeList childNode = n1.getChildNodes();
		        	
		        	ArrayList<HashMap<String,String>> arrayofchild = new ArrayList<HashMap<String,String>>();
		        	for(int j = 0;j<childNode.getLength();j++){
		        		if(childNode.item(j).getNodeType() == Node.ELEMENT_NODE){
		        			HashMap<String,String> hm = new HashMap<String,String>();
		        			Node n2 = childNode.item(j);
		        			hm.put("attrName",n2.getAttributes().getNamedItem("attrName").getNodeValue());
		        			hm.put("attrType",n2.getAttributes().getNamedItem("attrType").getNodeValue());
		        			hm.put("attrLength", n2.getAttributes().getNamedItem("attrLength").getNodeValue());
		        			arrayofchild.add(hm);
		        			
		        		}
		        	}
		        	ChildInfo.put(n1.getAttributes().getNamedItem("relName").getNodeValue(), arrayofchild);
		        	ParentInfo.put(n1.getAttributes().getNamedItem("relName").getNodeValue(), parentAttributeInfo);
		        	databasename.add(n1.getAttributes().getNamedItem("relName").getNodeValue());
	        	}
			}
		}  
	}
		/*
		 * Function Used To Print Xml 
		 */
		public static void printXML(){
			 for(int z =0; z<databasename.size(); z++){
				   String s = databasename.get(z);
				   
				   System.out.println("---------------------Get Parent Info-----------------------");
				 
				   HashMap<String,String> parentInfo = ParentInfo.get(s);
				   System.out.print("DataBaseName : "+parentInfo.get("relName")+"");
				   System.out.print(" Number Of Database : "+parentInfo.get("numOfAttributes")+"");
				   System.out.print(" File Path : "+parentInfo.get("DIW_File"));
				   System.out.println();
				   
				   System.out.println("Get Child Info");
				   ArrayList<HashMap<String,String>> childArray = ChildInfo.get(s);
				   for(int v =0 ; v<childArray.size(); v++){
					   
					   HashMap<String,String> childInfo = childArray.get(v);
					   System.out.println("--------------"+"Child"+v+"----------------");
					   System.out.print("AttributeName :"+childInfo.get("attrName")+"");
					   System.out.print(" AttributeType :"+childInfo.get("attrType")+"");
					   System.out.println(" AttributeLength :"+childInfo.get("attrLength"));
					   
				   }
				   
			   }
			
			
		}
		/*
		 * Get the start position of an attribute from the Catalog file
		 */
		public static int getChildStartPosition(String DatabaseName,String NameofAttrbute){
		    int length = 0;
		    ArrayList<HashMap<String,String>> childArray = ChildInfo.get(DatabaseName);
		    int v = 0;
		    //System.out.println("++++++"+v);
		    while(v<childArray.size()){
		        HashMap<String,String> childInfo = childArray.get(v);
//		        System.out.println(NameofAttrbute);
		        if(!childInfo.get("attrName").equals(NameofAttrbute))
		        {
//		            System.out.println("name of attribute parsed : "+childInfo.get("attrName"));
		            length = length + Integer.valueOf(childInfo.get("attrLength"));   
		        }
		        else
		        {
//		            System.out.println("name of attribute parsed : "+childInfo.get("attrName"));
		            break;
		        }
		        v++;
		    }
		    
		    return length;
		}
		
		public static int getChildLength(String DatabaseName,String attributeName){
		    ArrayList<HashMap<String,String>> childArray = ChildInfo.get(DatabaseName);
		    int v = 0;
//		    System.out.println(attributeName);
	        while(v<childArray.size()) {
		    	HashMap<String,String> childInfo = childArray.get(v);
//		        System.out.println(childInfo.get("attrName"));
		        if(childInfo.get("attrName").equals(attributeName)) {
		            return Integer.valueOf(childInfo.get("attrLength"));
		        }
		        v++;
		    }
		    return 0;
		}
		
/*
 * Utility Functions 
 * 
 */
		
public static HashMap<String,String> getDataTypeOfChild(String DatabaseName){
	
	HashMap<String,String> ChildDataType = new HashMap<String,String>();
	ArrayList<HashMap<String,String>> childArray = ChildInfo.get(DatabaseName);
	   for(int v =0 ; v<childArray.size(); v++){   
		   HashMap<String,String> childInfo = childArray.get(v);
		   ChildDataType.put(childInfo.get("attrName"), childInfo.get("attrType"));
	   }
	return ChildDataType;
	
}
/*
 * Utility Functions 
 * 
 */	
		
public static int getChildTotalLength(String DatabaseName){
	
	   int length = 0;
	   ArrayList<HashMap<String,String>> childArray = ChildInfo.get(DatabaseName);
	   for(int v =0 ; v<childArray.size(); v++){   
		   HashMap<String,String> childInfo = childArray.get(v);
		   length = length + Integer.valueOf(childInfo.get("attrLength"));   
	   }
	return length;
	
}
/*
 * To Test
 * 
 */
		
 public static void main(String[] args){
	 try{
	 parseXML("Catalog.xml");
	 printXML();
	 int length = getChildTotalLength("Dept");
	 System.out.println("Total Length :"+length  );
	 }
	 catch(Exception e){
		 
	 }
	}
		   
}
