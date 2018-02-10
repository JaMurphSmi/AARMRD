package org.anonymize.anonymizationapp.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataSource;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.springframework.stereotype.Component;

//component used to create the data object upon call with the dataset name and the extension
@Component
public class DataAspects{
	//for initial display purposes, only to obtain rows for display in jsp
	public Data createData (final String fileName) throws IOException {
	
		String[] dataNameAndExtension = fileName.split("\\.");
		String dataset = dataNameAndExtension[0];//format is [dataset name].[extension]
		String extension = dataNameAndExtension[1];
		
		 Data data = DefaultData.create();//create empty object with full scope to satisfy errors
	       if(extension.equals("csv")) {//to handle csv and excel files
	    	   data = Data.create("src/main/resources/templates/data/" + dataset + "." + extension, StandardCharsets.UTF_8, ';');
	       }
	       else if(extension.equals("xls") || extension.equals("xlsx")) {
	    	   DataSource dataExcel = DataSource.createExcelSource("src/main/resources/templates/data/" + dataset + "." + extension, 0, true);
	    	   data = Data.create(dataExcel);
	       }
	       return data;
	}
	//for object needed to perform anonymization	
	public Data createDataAndHierarchies(final String fileName) throws IOException {

		String[] dataNameAndExtension = fileName.split("\\.");
		String dataset = dataNameAndExtension[0];//format is [dataset name].[extension]
		String extension = dataNameAndExtension[1];
		
		 Data data = DefaultData.create();//create empty object with full scope to satisfy errors
	       if(extension.equals("csv")) {//to handle csv and excel files
	    	   data = Data.create("src/main/resources/templates/data/" + dataset + "." + extension, StandardCharsets.UTF_8, ';');
	       }
	       else if(extension.equals("xls") || extension.equals("xlsx")) {
	    	   DataSource dataExcel = DataSource.createExcelSource("src/main/resources/templates/data/" + dataset + "." + extension, 0, true);
	    	   data = Data.create(dataExcel);
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
	   
}