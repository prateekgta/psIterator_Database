package cyclients.psIterator.adapter;


import cyclients.psIterator.EmployeeTuple;
import cyclients.psIterator.Demo;
import cysystem.clientsmanager.ClientsFactory;
import cysystem.diwGUI.gui.DBGui;
import cysystem.clientsmanager.CyGUI;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.File;


import java.util.*;
import java.io.IOException;
import java.util.Arrays;


public class PSadapter extends ClientsFactory
{
	protected static String workingPath = "C:\\";
	
	//private DBGui dbgui;
	
	public PSadapter(DBGui m) {
		dbgui = m;
	}
	
	public PSadapter() {
	}
	
	
	@Override
	public void initialize(CyGUI gui, int clientID) {
		dbgui = gui;
	}
	
	@Override
	@SuppressWarnings("unused")
	public void execute(int clientID, String text) {

		if (this.dbgui == null) {
			System.out.println("Error! Testing");
			return;
		}

		String workspacePath = dbgui.getClientsManager().getClientWorkspacePath(clientID).trim();
		if ((workspacePath == null) || workspacePath.isEmpty()) {
			System.out
					.println("Warning: The workspace path of the EDB client system is not set. The query results will be stored into the current working path.");
			dbgui.addConsoleMessage("Warning: The workspace path of the EDB client system is not set. The query results will be stored into the current working path.");
		} else {
				System.out.println("workspace! "+workspacePath);
		}

		text = text.trim();
		String[] commands = text.split(" ");
		String path = null;
		String function = commands[0].trim();
		
		if (function.contains(":>")) {
			path = function.substring(0, function.indexOf(":"));
			function = function.substring(function.indexOf(">") + 1); 
		}
		
		if (function.equalsIgnoreCase("CreatePSIteratorofDisplay10")) {
			
			if (commands.length == 5) {
				String catalogFile = commands[1].trim();
				String expTreeFile = commands[2].trim();
				String relationName = commands[3].trim();
				String outputPath = commands[4].trim();
				
				System.out.println("RelationName : "+relationName);
				try {
					PrintWriter writer = new PrintWriter(outputPath, "UTF-8");
		      
					int recordCount = 0;
					String output = "";
					ArrayList<Byte[]> byteTuples = Demo.service(commands[1].trim(), commands[2].trim(), commands[3].trim());
	
					for(Byte b[] : byteTuples) {
						if(relationName.equals("Emp")) 
						{
							output = "Record "+ recordCount +": "+ EmployeeTuple.convertToEmp(PSadapter.convertByte(b)) + "";
							writer.println(output);
						}
						else
						{
							output = "Record "+ recordCount +": "+ EmployeeTuple.convertToDept(PSadapter.convertByte(b)) + "";
							writer.println(output);
						}
						recordCount++;
					}
					writer.close();
					dbgui.addOutput("File created at path :-- "+outputPath);	
				}
				catch(Exception ex) {
					this.dbgui.addConsoleMessage("Exception Caught: " + ex.getMessage());
					//dbgui.addOutput(ex.getMessage());	
				}	
			}else{
				
				this.dbgui.addConsoleMessage("Exception Caught: Missing Arguments");
			}	
		}
		else{
				this.dbgui.addConsoleMessage("Exception Caught: $PSIter:> Does not contains this commands");
			}	

	}
	
	public static byte[] convertByte(Byte[] byteValues){
		 byte[] bytes = new byte[byteValues.length];
		 
		 for (int i=0;i<bytes.length; i++) {
			bytes[i] = Byte.parseByte(byteValues[i].toString());     
		}
		
		return bytes;
	
}

}
