package org.anonymize.anonymizationapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataSource;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.springframework.stereotype.Component;

//component used to create the data object upon call with the dataset name and the extension
@Component
public class DataAspects{
	//to create the data and hierarchies in one step	
	public Data createDataAndHierarchies(final String fileName, final List<String> hierNames) throws IOException {

		String[] dataNameAndExtension = fileName.split("\\.");
		String dataset = dataNameAndExtension[0];//format is [dataset name].[extension]
		String extension = dataNameAndExtension[1];
		
		 Data data = DefaultData.create();//create empty object with full scope to satisfy errors
	       if(extension.equals("csv")) {//to handle csv and excel files
	    	   data = Data.create("src/main/resources/templates/data/" + dataset + "." + extension, StandardCharsets.UTF_8, ';');
	       }
	       /**
	        * Xls and dbs cannot be used as the sequence of fields in a data set cannot be determined through hierarchy file names anymore
	        * this is due to the columns being out of alphabetical order, and the file picker putting them into order regardless of choice
	        */
	       else if(extension.equals("xls") || extension.equals("xlsx")) {
	    	   DataSource dataExcel = DataSource.createExcelSource("src/main/resources/templates/data/" + dataset + "." + extension, 0, true);
	    	   //need to specify column names in here before casting to type Data, so send field names shaved from
	    	   
	    	   //dataExcel.
	    	   
	    	   int i = 0;
	    	   for(String hierName : hierNames) {
	    		   dataExcel.addColumn(hierName);
	    		   ++i;
	    	   }
	    	   data = Data.create(dataExcel);//input hierarchy files
	       }
	       
	       //try internal reference to method instead of included code, could cut codebase down by 100 or so
	       // Read generalization hierarchies
	       FilenameFilter hierarchyFilter = new FilenameFilter() {
	           @Override
	           public boolean accept(File dir, String name) {
	               if (name.matches(dataset + "_hierarchy_(.)+.csv")) {
	                   return true;
	               } else {
	                   return false;
	               }
	           }
	       };
	       // Create definition, hierarchies will always be .csv
	       File testDir = new File("src/main/resources/templates/hierarchy/");
	       File[] genHierFiles = testDir.listFiles(hierarchyFilter);
	       Pattern pattern = Pattern.compile("_hierarchy_(.*?).csv");
	       for (File file : genHierFiles) {
	           Matcher matcher = pattern.matcher(file.getName());
	           if (matcher.find()) {
	               CSVHierarchyInput hier = new CSVHierarchyInput(file, StandardCharsets.UTF_8, ';');
	               String attributeName = matcher.group(1);
	               data.getDefinition().setAttributeType(attributeName, Hierarchy.create(hier.getHierarchy()));
	           }
	       }
	       
	       return data;
	   }
	//for db table handling
	public Data createDataAndHierarchies(final String fileName, final List<String> hierNames, String table) throws IOException, SQLException {

		String[] dataNameAndExtension = fileName.split("\\.");
		String dataset = dataNameAndExtension[0];//format is [dataset name].[extension]
		String extension = dataNameAndExtension[1];
		
		 Data data = DefaultData.create();//create empty object with full scope to satisfy errors
		 if(extension.equals("db")) {
			DataSource dataDatabase = DataSource.createJDBCSource("src/main/resources/templates/data/" + dataset + "." + extension, table);
			data = Data.create(dataDatabase);
		 }
		// Read generalization hierarchies
	       FilenameFilter hierarchyFilter = new FilenameFilter() {
	           @Override
	           public boolean accept(File dir, String name) {
	               if (name.matches(dataset + "_hierarchy_(.)+.csv")) {
	                   return true;
	               } else {
	                   return false;
	               }
	           }
	       };
	       // Create definition, hierarchies will always be .csv
	       File testDir = new File("src/main/resources/templates/hierarchy/");
	       File[] genHierFiles = testDir.listFiles(hierarchyFilter);
	       Pattern pattern = Pattern.compile("_hierarchy_(.*?).csv");
	       for (File file : genHierFiles) {
	           Matcher matcher = pattern.matcher(file.getName());
	           if (matcher.find()) {
	               CSVHierarchyInput hier = new CSVHierarchyInput(file, StandardCharsets.UTF_8, ';');
	               String attributeName = matcher.group(1);
	               data.getDefinition().setAttributeType(attributeName, Hierarchy.create(hier.getHierarchy()));
	           }
	       }
		 return data;
	}
	//eventually intending to implement db file catering 
	public Data createDataAndHierarchies(final String fileName, final List<String> hierNames, String table, String user, String password) throws IOException, SQLException {

		String[] dataNameAndExtension = fileName.split("\\.");
		String dataset = dataNameAndExtension[0];//format is [dataset name].[extension]
		String extension = dataNameAndExtension[1];
		
		 Data data = DefaultData.create();//create empty object with full scope to satisfy errors
		 if(extension.equals("db")) {
			DataSource dataDatabase = DataSource.createJDBCSource("src/main/resources/templates/data/" + dataset + "." + extension, user, password, table);
			data = Data.create(dataDatabase);
		 }
		// Read generalization hierarchies
	       FilenameFilter hierarchyFilter = new FilenameFilter() {
	           @Override
	           public boolean accept(File dir, String name) {
	               if (name.matches(dataset + "_hierarchy_(.)+.csv")) {
	                   return true;
	               } else {
	                   return false;
	               }
	           }
	       };
	       // Create definition, hierarchies will always be .csv
	       File testDir = new File("src/main/resources/templates/hierarchy/");
	       File[] genHierFiles = testDir.listFiles(hierarchyFilter);
	       Pattern pattern = Pattern.compile("_hierarchy_(.*?).csv");
	       for (File file : genHierFiles) {
	           Matcher matcher = pattern.matcher(file.getName());
	           if (matcher.find()) {
	               CSVHierarchyInput hier = new CSVHierarchyInput(file, StandardCharsets.UTF_8, ';');
	               String attributeName = matcher.group(1);
	               data.getDefinition().setAttributeType(attributeName, Hierarchy.create(hier.getHierarchy()));
	           }
	       }
		 return data;
	}
	
	public void deleteFiles() throws IOException, FileNotFoundException {
		//try/catch block for deleting the data file
		try {
		File dataDelete = new File("src/main/resources/templates/data");
	    FileUtils.cleanDirectory(dataDelete);
		}
		catch(FileNotFoundException failure) {
			System.out.println("Deleting data file failed : " + failure.getLocalizedMessage());
		}
		//try/catch block for deleting the hierarchy files
		try {
	    File hierarchyDelete = new File("src/main/resources/templates/hierarchy");
	    FileUtils.cleanDirectory(hierarchyDelete);
		}
		catch(FileNotFoundException failure) {
			System.out.println("Deleting hierarchy file failed : " + failure.getLocalizedMessage());
		}
	}   
	//delete anonymized file from directory, may be used later on
	public void deleteAnonFile(String anonymizedFile) throws IOException, FileNotFoundException {
		//very simple, name the file to be destroyed, it will be
		File dataDelete = new File("src/main/resources/templates/outputs/" + anonymizedFile);
	    boolean success = dataDelete.delete();
	    System.out.println("Success is : " + success);
	}  
}