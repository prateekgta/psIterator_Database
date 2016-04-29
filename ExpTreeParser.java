package cyclients.psIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class ExpTreeParser {

	// Save the parent information  <booleanFactor booleanFactorType="attrOpConst">
	static ArrayList<String> dbRelExpTypeInfo = new ArrayList<String>();
	static ArrayList<HashMap<String,String>> booleanFactorTypechildInfo = new ArrayList<HashMap<String,String>>();
	static ArrayList<ArrayList<HashMap<String,String>>> dbAttrListchildInfo = new ArrayList<ArrayList<HashMap<String,String>>>();
	
	/*
	 * Function used To parse the AWT-XML
	 */
	public static ArrayList<HashMap<String, String>>  parseAWT(String Path) throws SAXException, IOException, ParserConfigurationException{
				
		       // path = "Path Of AWT-XML"
			   File inputFile = new File(Path);
			   DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			   DocumentBuilder dBuilder;
			
			   dBuilder = dbFactory.newDocumentBuilder();
			   Document doc = dBuilder.parse(inputFile);
			   doc.getDocumentElement().normalize();
			  
			   NodeList nList = doc.getElementsByTagName("dbRelExp");
			   
			  
			       for (int i=0; i<nList.getLength()-2; i++){
			    	   Node n1 = nList.item(i);
			       		if (n1.getNodeType() == Node.ELEMENT_NODE && n1.getAttributes().getNamedItem("dbRelExpType").getNodeValue().equals("dbNaturaljoin"))
			        	{
			       			
			       			NodeList childNode1 = n1.getChildNodes();
			       			for(int j = 0;j<childNode1.getLength();j++){
			       				Node n2 = childNode1.item(j);
	
			       				if (n2.getNodeType() == Node.ELEMENT_NODE && n2.getNodeName().equals("dbRelExp")){
			       					
			       					NodeList childNode2 = n2.getChildNodes();
			       					
			       					for(int k =0; k <childNode2.getLength();k++)
			       					{
			       						Node n3 = childNode2.item(k);
			       						
			       						if (n3.getNodeType() == Node.ELEMENT_NODE)
			       						{		
			       							if(n3.getNodeName().equals("dbRelExp")){
			       								
			       								NodeList child3 = n3.getChildNodes();
			       								for(int x =0; x<child3.getLength();x++)
			       								{
			       									Node n4 = child3.item(x);
			       									
			       									if (n4.getNodeType() == Node.ELEMENT_NODE)
			       									{ 
			       										
			       										HashMap<String,String> childinfo = new HashMap<String, String>();
			       										
			       										if(n4.getNodeName().equals("BooleanExp")){
			       											
			       											NodeList child5 = n4.getChildNodes();
			       											
			       											for(int x1=0;x1<child5.getLength();x1++){
			       												
			       												Node n6 = child5.item(x1);
			       												
			       												if (n6.getNodeType() == Node.ELEMENT_NODE && n6.getNodeName().equals("booleanFactor")){
			       													
			       													NodeList child6 = n6.getChildNodes();
			       													
			       													for(int x2=0;x2<child6.getLength();x2++){
			       														
			       													
			       														
			       														Node n7 = child6.item(x2);
			       														
			       														if (n7.getNodeType() == Node.ELEMENT_NODE)
			       														{
			       															if(n7.getNodeName().equals("dbAttr")){
			       																
			       																childinfo.put("dbAttrName", n7.getAttributes().getNamedItem("dbAttrName").getNodeValue());
			       																childinfo.put("dbAttrType", n7.getAttributes().getNamedItem("dbAttrType").getNodeValue());
			       																childinfo.put("dbRelAliasName", n7.getAttributes().getNamedItem("dbRelAliasName").getNodeValue());
			       																childinfo.put("dbRelName", n7.getAttributes().getNamedItem("dbRelName").getNodeValue());
			       															
			       																
			       																
			       															}if(n7.getNodeName().equals("comparisonOp")){
			       																
			       																childinfo.put("opType", n7.getAttributes().getNamedItem("opType").getNodeValue());
			       																
			       																
			       																
			       															}if(n7.getNodeName().equals("dbConstValue")){
			       																
			       																childinfo.put("constType", n7.getAttributes().getNamedItem("constType").getNodeValue());
			       																childinfo.put("constValue", n7.getAttributes().getNamedItem("constValue").getNodeValue());	
			       															}
			       														}	
			       													}
			       													booleanFactorTypechildInfo.add(childinfo);
			       						//							System.out.println(childinfo.keySet());
			       												}
			       											}
			       										}
			       										if(n4.getNodeName().equals("dbRelExp")){
			       											String dbval = n4.getAttributes().getNamedItem("dbRelName").getNodeValue();
			       					//						System.out.println(dbval);
			       											dbRelExpTypeInfo.add(n4.getAttributes().getNamedItem("dbRelName").getNodeValue());	
			       										}
			       										
			       									}
			       									
			       								}
			       								
			       							}
			       							if(n3.getNodeName().equals("dbAttrList")){
			       								
			       								NodeList child4 = n3.getChildNodes();
			       								ArrayList<HashMap<String,String>> al = new ArrayList<HashMap<String,String>>();
			       								
			       								for(int y =0; y<child4.getLength();y++)
			       								{
			       									
			       									Node n5 = child4.item(y);
			       									
			       									if (n5.getNodeType() == Node.ELEMENT_NODE){
			       										HashMap<String,String>  dbAttrInfo = new HashMap<String,String>();
			       										dbAttrInfo.put("dbAttrName", n5.getAttributes().getNamedItem("dbAttrName").getNodeValue());
			       										dbAttrInfo.put("dbAttrType", n5.getAttributes().getNamedItem("dbAttrType").getNodeValue());
			       										dbAttrInfo.put("dbRelAliasName", n5.getAttributes().getNamedItem("dbRelAliasName").getNodeValue());
			       										dbAttrInfo.put("dbRelName", n5.getAttributes().getNamedItem("dbRelName").getNodeValue());
			       										al.add(dbAttrInfo);
			       									}
			       									
			     								
			       								}
			       								dbAttrListchildInfo.add(al);	
			       							}		
			       						}		
			       					
			       					}	
			       			
			       				} 		
			       		
			       			}
			        	}
			       
			       }     
			       return booleanFactorTypechildInfo;
	}
		/*
		 * 
		 * 
		 * static ArrayList<String> dbRelExpTypeInfo = new ArrayList<String>();
	static ArrayList<HashMap<String,String>> booleanFactorTypechildInfo = new ArrayList<HashMap<String,String>>();
	static ArrayList<ArrayList<HashMap<String,String>>> dbAttrListchildInfo = new ArrayList<ArrayList<HashMap<String,String>>>();
	
		 * Utility Function 
		 */
	
	
	public static HashMap<String,String> getbooleanFactor(String relName)
	{
		
		HashMap<String,String> hmap = null;
		if(dbRelExpTypeInfo.contains(relName)){
			int index = dbRelExpTypeInfo.indexOf(relName);
			hmap = booleanFactorTypechildInfo.get(index);
		}
		return hmap;
	}
	
	public static ArrayList<HashMap<String,String>> getdbAttrList(String relName)
	{
		ArrayList<HashMap<String,String>> list = null;
		if(dbRelExpTypeInfo.contains(relName)){
			int index = dbRelExpTypeInfo.indexOf(relName);
			list = dbAttrListchildInfo.get(index);
		}
		return list;
	}
	
	
		

/*
 * To Test
 * 
 */
		
 public static void main(String[] args){
	 try{
		 parseAWT("output.xml");

		 
		 HashMap<String,String> hmapBoolFacetor = getbooleanFactor("Dept");
	/*	 System.out.println("check value "+hmapBoolFacetor.get("dbAttrName"));
		 System.out.println("check value "+hmapBoolFacetor.get("constValue"));
		 System.out.println("check value "+hmapBoolFacetor.get("constType"));
		 System.out.println("check value "+hmapBoolFacetor.get("opType"));
		 System.out.println("check value "+hmapBoolFacetor.get("dbRelName"));*/
		 
		 ArrayList<HashMap<String,String>> list = getdbAttrList("Dept");
		 
		 for(int i=0;i<list.size();i++){
			 System.out.println(list.get(i).get("dbAttrName"));
			
		 } 
		 
		 
		ArrayList<HashMap<String,String>> attributeList = ExpTreeParser.getdbAttrList("Dept");
		for(HashMap hm:attributeList) {
			System.out.println(hm);
		}

	 }
	 catch(Exception e){
		 
	 }
	}
		   
}
