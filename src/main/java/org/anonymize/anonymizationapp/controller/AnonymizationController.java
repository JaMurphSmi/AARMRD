package org.anonymize.anonymizationapp.controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
// Importing ARX required modules, dependencies etc
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.DataType;
import org.deidentifier.arx.aggregates.StatisticsFrequencyDistribution;
//import org.deidentifier.arx.aggregates.StatisticsQuality;//does not exist in the current jar
import org.deidentifier.arx.criteria.DistinctLDiversity;
import org.deidentifier.arx.criteria.EnhancedBLikeness;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.criteria.OrderedDistanceTCloseness;
import org.deidentifier.arx.criteria.EqualDistanceTCloseness;
import org.deidentifier.arx.criteria.EntropyLDiversity;
import org.deidentifier.arx.criteria.RecursiveCLDiversity;
import org.deidentifier.arx.criteria.BasicBLikeness;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelSampleSummary;
import org.anonymize.anonymizationapp.dao.ExampleDaoImpl;
import org.anonymize.anonymizationapp.model.AlgorithmObject;
import org.anonymize.anonymizationapp.model.AnonymizationBase;
import org.anonymize.anonymizationapp.model.AnonymizationObject;
import org.anonymize.anonymizationapp.model.AnonymizationReport;
import org.anonymize.anonymizationapp.model.Person;
import org.anonymize.anonymizationapp.model.RiskObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
// ARX related stuff 
import org.apache.commons.math3.util.Pair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

//hopefully for jasper report
import net.sf.jasperreports.engine.JRException; 
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.anonymize.anonymizationapp.util.DataAspects;
import org.anonymize.anonymizationapp.util.FileStructureAspects;
import org.anonymize.anonymizationapp.util.PieChartGenerator;


@Controller	
@Scope("session")//implements session control
public class AnonymizationController extends AnonymizationBase {
	//solution to the source data issue was to elevate the object to class scope
	private Data sourceData;//now available in all methods within the anonymization controller
	private ARXResult result;//global for risk analysis
	private String orgName;//name of employee's organization
	private String empName;//name of employee
	private String empOrg;//string concatenation of the two, in employee->organization order
	private String sourceDataFileName;//global filename of the raw data, used to overcome restarts
	private List<String> hierarchyDataFiles = new ArrayList<String>();
	private String anonymizedDataFileName;//global anonymized dataset file name, for download purposeses
	private List<String[]> dataRows = new ArrayList<String[]>();//extract to global variables to allow access in entire application
	private List<String[]> anonyRows = new ArrayList<String[]>();
	private List<String> headerRow = new ArrayList<String>();//making global to improve accessibility and reduce need to pass between methods
	private List<String> hierFileList = new ArrayList<String>();//used to determine what fields do not have a supplied hierarchy
	private String secret;//for encryption of 
	final String[] countries = {"Australia", "Brazil", "Canada", "China", "Europe", 
			"European Union", "France", "Germany", "India", "North America", "South America",
			"United Kingdom", "United States"};//countries and regions for risk metrics
	//also remove from anonymization object
	private AnonymizationReport anonReport = new AnonymizationReport();
	private ArrayList<AlgorithmObject> algorithmStats = new ArrayList<AlgorithmObject>();
	private Person person = new Person();
	//may restrict all anonymization actions to the anonymization controller? not have multiple controllers?
	
	//private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
	
	@Autowired
	private DataAspects dataAspectsHelper;
	
	@Autowired
	private FileStructureAspects fileStructureAspects;

	@Autowired
	private PieChartGenerator pieChartGenerator;
	
	@Autowired 
	ExampleDaoImpl checkCredsHelper;
	
	//preconfig to house a global object of user details for verification
	/*@Autowired
    public AnonymizationController(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
       this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }*/
	
	
   @RequestMapping("/")
   public String index(Model model) {
	   person = new Person();//destroy and recreate person object at each attempt
      return "login";
   }
  
   
   
   @RequestMapping("/home") 
   public String getDetails(@RequestParam(value="orgName", required=false) String orgaName, @RequestParam(value="empName", required=false) String emplName,
		   @RequestParam(value="empPass", required=false) String empPass, Model model) {
	   person = new Person(emplName, orgaName, empPass);
   
//	   if (!checkCredsHelper.fetch(person)) {
//		   model.addAttribute("errorMsg", "Incorrect Details");
//		   //didn't work, so return to login with message
//		   return "login";
//	   }
	   
	   //generate secret key for filestructureaspects immediately 
	   String generatedString = RandomStringUtils.randomAlphanumeric(16);
	   
	   System.out.println(generatedString.length());
	   //fileStructureAspects.setSecret(generatedString);//should be unique for each user?
	   this.secret = generatedString;
	   generatedString = null;
	   
	   //orgName will be the shared password between employees in same establishment
	   //empName will be their unique identifier
	   if(person.getOrgName() != null && person.getEmpName() != null) {
		   //inMemoryUserDetailsManager.createUser(User.withUsername(emplName).password(orgaName).roles("USER").build());
		   //if the org & emp names are available, add to the model
		   this.orgName = person.getOrgName();
		   this.empName = person.getEmpName();
		   
		   //create the unique file structure for a user to avoid conflicts with multiple users
		   //directory name is concat of details entered previous screen
		   
		   this.empOrg = person.getEmpName() + person.getOrgName();
		   this.empOrg = this.empOrg.replace(' ', '_');//avoid errors with spaces in directory path
		   
		   System.out.println(this.empOrg);
		   
		   //jasper directory
		   File dir = new File("src/main/resources/"+ this.empOrg);
		   //attempt to create the jasper directory here
		   fileStructureAspects.makeDirectory(dir); 
		    dir = new File("src/main/resources/templates/data/" + this.empOrg);
		   //attempt to create the data directory here
		   fileStructureAspects.makeDirectory(dir);
		    dir = new File("src/main/resources/templates/hierarchy/" + this.empOrg);
		   //attempt to create the hierarchy directory here
		   fileStructureAspects.makeDirectory(dir);
		    dir = new File("src/main/resources/templates/outputs/" + this.empOrg);
		   //attempt to create the hierarchy directory here
		   fileStructureAspects.makeDirectory(dir);
		    dir = new File("src/main/resources/META-INF/resources/images/" + this.empOrg);
		   //attempt to create the piechart images directory here
		    fileStructureAspects.makeDirectory(dir);
		   
		   model.addAttribute("orgName", person.getOrgName());
		   model.addAttribute("empName", person.getEmpName());
	   }
	   return "index";
   }
   
   @RequestMapping("/hadAnError")
   public String getError(Model model) {
	   sourceData = null;//remove data because of error
	   try {
		dataAspectsHelper.deleteAllFiles(this.empOrg);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   model.addAttribute("orgName", person.getOrgName());
	   model.addAttribute("empName", person.getEmpName());
	   return "index";
   }
   
   // will be a page to handle the uploading of files
   // making dataFiles and hierFiles not required to allow user to return to here without returning to the upload screen
   @RequestMapping("/uploadFiles")//only take in the files, establish data fields first, then allow user to select attribute types, algos etc
   public String getData(@RequestParam(value="dataFile", required=false) MultipartFile dataFile, @RequestParam(value="hiddenTable", required=false) String table,
		   @RequestParam(value="userName", required=false) String userName, @RequestParam(value="password", required=false) String password,
		   @RequestParam(value="hierFiles", required=false) MultipartFile[] hierFiles, 
		   @RequestParam(value="hierChecks", required=false) String[] hierChecks, Model model) throws IOException, ParseException, SQLException {
		///////////////// Creating the data object
		
	   if(sourceData != null) {
		   System.out.println("Data object already exists, skipping the annoying bit");
		   File directory=new File("src/main/resources/" + empOrg );
			
		   if(!directory.exists()) {
			   fileStructureAspects.makeDirectory(directory);
		   }
	   }
	   else if(sourceData == null && hierChecks == null) {
			//attempting to cast multipartfile object to file(can be modularized later if successful)
			//prepare dataset name
			sourceDataFileName = dataFile.getOriginalFilename();
			System.out.println("sourceDataFileName name is : " + sourceDataFileName);
			model.addAttribute("fileName", sourceDataFileName);
			
			// save the dataset to the project hierarchy(lol)
			File convertedDataFile = new File("src/main/resources/templates/data/" + empOrg + "/" + sourceDataFileName); //for saving
			convertedDataFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convertedDataFile);
			fos.write(dataFile.getBytes());
			fos.close();
			
			//////////////////// finished doing stuff for Data
			
			////////////////////creating the hierarchies in file path
			//adding extra functionality for 'lack of hierarchy' comparison list
			for(MultipartFile mulFile: hierFiles) {
				String fileName = mulFile.getOriginalFilename();
				String filePath = "src/main/resources/templates/hierarchy/" + empOrg + "/" + fileName;
				File convertedHierFile = new File(filePath);
				
				System.out.println("hierfileName is : " + fileName);
				convertedHierFile.createNewFile();
				
				FileOutputStream fost = new FileOutputStream(convertedHierFile);
				fost.write(mulFile.getBytes());
				fost.close();//works
				
				String[] tempArray = fileName.split("[\\_\\.]");//split by underscore and dot		  0         1         2
				hierFileList.add(tempArray[2]);//add file name to list for display reasons further on [dataset]_hierarchy_[column]
				System.out.println("for hierFileList " + tempArray[2]);
				hierarchyDataFiles.add(filePath);
			}//removing the headerRow from this, need to make it in proper order
			
			headerRow.clear();//zero out header row to avoid duplicate field headers in tables
			String[] checkExtension = sourceDataFileName.split("\\.");
			
			if(checkExtension[1].equals("csv")) {
				//needed to keep the order of fields correct
				
				BufferedReader fileReader = new BufferedReader(new FileReader("src/main/resources/templates/data/" + empOrg + "/" + sourceDataFileName));
				
				try {
					 // "Prime" the while loop        
				    String headerLine = fileReader.readLine();
				    
				    String[] fields = headerLine.split(";");
				    for(int i= 0; i < fields.length; ++i) {
				    	headerRow.add(fields[i]);//should be in order now
				    }
				}
				finally {
					fileReader.close();
				}
			}//header row must be read from the xls or xlsx file in another way
			else if ((checkExtension[1].equals("xls")) || (checkExtension[1].equals("xlsx")))
			{
				InputStream inp = new FileInputStream("src/main/resources/templates/data/" + empOrg + "/" + sourceDataFileName);
				try {
					
					XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(inp);
					int FIRST_ROW_TO_GET = 0; // 0 based
					Sheet s = wb.getSheetAt(0);
					
					// Create a DataFormatter to format and get each cell's value as String
			        DataFormatter dataFormatter = new DataFormatter();
					
					for (int i = FIRST_ROW_TO_GET; i < 1; i++) {
					   Row row = s.getRow(i);
					   if (row == null) {
					      // The whole row is blank
					   }
					   else {
					      for (int cellNum=row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
					         Cell cell = row.getCell(cellNum, Row.RETURN_BLANK_AS_NULL);
					         if (cell == null) {
					            // The cell is empty
					         } else {
					        	 String cellValue = dataFormatter.formatCellValue(cell);
					             System.out.print(cellValue + "\t");
					             headerRow.add(cellValue);  
					         }
					      }
					   }
					}
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				}
				finally {
					inp.close();
				}
			}
			System.out.println("After getting the header row if no problems arise");
			System.out.println("Has successfully worked");
			// making data creation variable, normal files xls, csv
			System.out.println("before creating the data and hierarchies");
			if(table == null && (!sourceDataFileName.substring(sourceDataFileName.lastIndexOf(".") + 1).equals("db"))) {
				System.out.println("creating a data object from file : " + sourceDataFileName);//this call doesn't discriminate csv and xls
				sourceData = dataAspectsHelper.createDataAndHierarchies(empOrg, sourceDataFileName, headerRow);//hopefully this way works 
			}// using db files and tables
			else if (table != null && (userName == null || password == null)) {
				System.out.println("creating a data object from table : " + table);
				sourceData = dataAspectsHelper.createDataAndHierarchies(empOrg, sourceDataFileName, headerRow, table);
			}
			else if (table != null && userName != null && password != null) {
				System.out.println("creating a data object from table with credentials Table Name :" + table + ", userName : " + userName + ", password : " + password);
				sourceData = dataAspectsHelper.createDataAndHierarchies(empOrg, sourceDataFileName, headerRow, table, userName, password);
			}

			File dataEncryptFile = new File("src/main/resources/templates/data/" + empOrg + "/" + sourceDataFileName);
			//finally encrypt files while not in use
			/*fileStructureAspects.fileEnDeCrypt(Cipher.ENCRYPT_MODE, secret, dataEncryptFile, dataEncryptFile);
			for (String path : hierarchyDataFiles) {
				File hierPath = new File(path);
				fileStructureAspects.fileEnDeCrypt(Cipher.ENCRYPT_MODE, secret, hierPath, hierPath);
			}*/
			dataRows = dataAspectsHelper.createDataRows(sourceData);
			System.out.println("created the dataRows");
	   }
	   
	   if(sourceData != null && hierChecks != null) {//to accommodate if the user wants to create hierarchies for a field
		   //this is where integer field hierarchies are created

		   //hierChecks will be used here
		   for(int i = 0; i < hierChecks.length; ++i) {
			 	System.out.println("The input index that need hierarchies are : " + hierChecks[i]);
			    String[] dataNeedsHierarchy = dataAspectsHelper.getCertainFieldValues(hierChecks[i], dataRows);
			    for(String data : dataNeedsHierarchy) {
			    	System.out.println("value " + data);
			    }
			    System.out.println("Setting definition for " + headerRow.get(Integer.valueOf(hierChecks[i])));
			    sourceData.getDefinition().setHierarchy(headerRow.get(Integer.valueOf(hierChecks[i])), dataAspectsHelper.makeRedactionBasedHierarchy(dataNeedsHierarchy));
		   }
	   }
	   else if(sourceData != null && hierChecks == null) {			
			//used as a controller for whether or not the option to create a hierarchy should be presented to user
			//controlled based on order of headerRow as headerRow is correct order
			int[] noHierArr = new int[headerRow.size()];
			boolean noHierarchyDetected = false;
			int i = 0;
			
			for(String headerMember : headerRow) {//compare each header row value with every hierFile value
				noHierArr[i] = dataAspectsHelper.compareWithHierFileNames(headerMember, hierFileList);
				if(noHierArr[i] == -1) {
					noHierarchyDetected = true;
				}
				i++;
			}
			if(noHierarchyDetected) {
				System.out.println("Field with no hierarchy detected, rerouting to assignment screen");
				model.addAttribute("headerRow", headerRow);
				model.addAttribute("noHierArr", noHierArr);
				return "needHierarchies";
			}
			else {
				System.out.println("All hierarchies discovered");
			}
	   }
	   System.out.println("after creating the data successfully and all hierarchy aspects");
	   	
		
		System.out.println(">" + headerRow + "<");//very weird as this shows the file has been opened successfully...
		
		model.addAttribute("headerRow", headerRow);//only need to get the header once as it will do for the resulting dataset also
		for (String colName : headerRow) {
			System.out.println(colName + ",");//testing if random space at the start
		}
		
		
		//had to create object to push to jsp, to use modelAttribute
		//other variables instantiated as empty arrays based on the now constant header.length()
		AnonymizationObject anonForm = new AnonymizationObject(sourceDataFileName, headerRow.size());//constant for an individual user
		//////// use header row to allow user to set individual algorithms for each field
		String[] models = {"k-anonymity","l-diversity", "recursive cl-diversity",
				"entropy l-diversity", "t-closeness", "enhanced b likeness", "basic b likeness",
				"equal distance t-closeness"};//remove delta presence for now,"δ-presence"};
		String[] attributeTypes = {"Identifying", "Quasi-Identifying", "Sensitive", "Insensitive"}; 
		for (String mod : models) {
			System.out.println(mod + ",");//testing if models in array
		}
		//object of type Data cannot be handled by the jsp, as such needs to be recreated between each view
		System.out.println("ModelsChosen length: " + anonForm.getModelsChosen().length);
		System.out.println("AttributesChosen length: " + anonForm.getAttributesChosen().length);
		System.out.println("ValuesChosen length: " + anonForm.getValuesForModels().length);
		
		model.addAttribute("anonForm", anonForm);
		model.addAttribute("models", models);
		model.addAttribute("attributes", attributeTypes);
		model.addAttribute("dataRows", dataRows);
	    return "setAnonDetails";
   }


	// attempting to handle file uploads successfully
		@RequestMapping("/anonymizeData")
		public String performAnonymization(@Valid @ModelAttribute("anonForm") AnonymizationObject anonForm, 
				BindingResult bindResult, Model model) 
				throws IOException, ParseException, SQLException, ClassNotFoundException, NoSuchAlgorithmException {
			
			//Data object was made global to allow access at all times and reduce computational requirement
			//inconsequential with small sets, however improves performance ~300% across anonymization and risk screens
			//by making the creation of the Data object a one-time event
			String[] modelsChosen = anonForm.getModelsChosen();
			String[] attributesChosen = anonForm.getAttributesChosen();
			double[] valuesForModels = anonForm.getValuesForModels();
			double[] extraValues = anonForm.getExtraForModels();
			//System.out.println(dataset);
			//System.out.println(extension);
			for(String aColumn : headerRow) {
				System.out.print(aColumn + " ");
			}
			System.out.println("Attempting proof of concept");
			for(String aModel : modelsChosen) {
				System.out.println("The model is: " + aModel);
			}
			System.out.println("After printing the selected models");
			for(double aValue : valuesForModels) {
				System.out.println("The value is: " + aValue);
			}
			System.out.println("Printing extra values");
			for(double extra : extraValues) {
				System.out.println("The attribute is: " + extra);
			}
			System.out.println("Printing attribute types chosen");
			for(String anAttribute : attributesChosen) {
				System.out.println("The attribute is: " + anAttribute);
			}
			algorithmStats = new ArrayList<AlgorithmObject>();
			
			System.out.println("Possibly proved concept?");
			
			//creating all AlgorithmObjects and placing into collection for later report use
			int index = 0;
			for(String column : headerRow) {
				AlgorithmObject algObject = new AlgorithmObject(column);
				
				algObject.setAlgorithm(modelsChosen[index]);
				algObject.setValue(String.valueOf(valuesForModels[index]));
				algObject.setAttributeType(attributesChosen[index]);
				
				algorithmStats.add(algObject);
				++index;
			}
	
			DataHandle handle = sourceData.getHandle();

			int i = 0;
			
			//define attribute type & determine the column's data type
			for(String header : headerRow) {
				//determine the type of the specific field
				if((attributesChosen[i].equals("Identifying")) || (attributesChosen[i].equals("- - NONE - -"))){//identifying by default
					sourceData.getDefinition().setAttributeType(header, AttributeType.IDENTIFYING_ATTRIBUTE);
					System.out.println("Set the attribute value to identifying for: " + header);
				}
				else if(attributesChosen[i].equals("Quasi-Identifying")){//field affected by k-anonymity if defined for any field
					sourceData.getDefinition().setAttributeType(header, AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
					System.out.println("Set the attribute value to quasi-identifying for: " + header);
				}
				else if(attributesChosen[i].equals("Sensitive")){//sole field affected by one algorithm declaration
					sourceData.getDefinition().setAttributeType(header, AttributeType.SENSITIVE_ATTRIBUTE);
					System.out.println("Set the attribute value to sensitive for: " + header);
				}
				else if((attributesChosen[i].equals("Insensitive"))){//not affected by any algorithm
					sourceData.getDefinition().setAttributeType(header, AttributeType.INSENSITIVE_ATTRIBUTE);
					System.out.println("Set the attribute value to insensitive for: " + header);
				}
				sourceData.getDefinition().setDataType(header, determineDataType(handle, i));
				++i;
			}
			handle.release();//garbage collect, potentially fix handle held issue?
	
		    // Create an instance of the anonymizer
	        ARXAnonymizer anonymizer = new ARXAnonymizer();//create object
	        ARXConfiguration anonymizationConfiguration = ARXConfiguration.create();//defining the privacy model
	        
	        i = 0;
	        
	        //specifying everything variable to do with an anonymization, and the anonymizationConfiguration
	        for(String header : headerRow) {//can be run anywhere from 1 to x times per dataset size
	        	if (modelsChosen[i].equals("k-anonymity")){//will hopefully eventually be a large number of algorithms
	        		anonymizationConfiguration.addPrivacyModel(new KAnonymity((int)valuesForModels[i]));//for all quasi-identifying fields
	        	}
	        	else if (modelsChosen[i].equals("l-diversity")) {//for particular sensitive fields
	        		anonymizationConfiguration.addPrivacyModel(new DistinctLDiversity(header, (int)valuesForModels[i]));
	        	}
	        	else if (modelsChosen[i].equals("t-closeness")) {//for particular sensitive fields
	        		anonymizationConfiguration.addPrivacyModel(new OrderedDistanceTCloseness(header, valuesForModels[i]));
	        	}
	        	else if (modelsChosen[i].equals("recursive cl-diversity")) {//for particular sensitive fields
	        		anonymizationConfiguration.addPrivacyModel(new RecursiveCLDiversity(header, valuesForModels[i], (int) extraValues[i]));
	        	}
	        	else if (modelsChosen[i].equals("entropy l-diversity")) {//for particular sensitive fields
	        		anonymizationConfiguration.addPrivacyModel(new EntropyLDiversity(header, valuesForModels[i]));
	        	}
	        	else if (modelsChosen[i].equals("enhanced b likeness")) {//for particular sensitive fields
	        		anonymizationConfiguration.addPrivacyModel(new EnhancedBLikeness(header, valuesForModels[i]));
	        	}
	        	else if (modelsChosen[i].equals("basic b likeness")) {//for particular sensitive fields
	        		anonymizationConfiguration.addPrivacyModel(new BasicBLikeness(header, valuesForModels[i]));
	        	}
	        	else if (modelsChosen[i].equals("equal distance t-closeness")) {//for particular sensitive fields
	        		anonymizationConfiguration.addPrivacyModel(new EqualDistanceTCloseness(header, valuesForModels[i]));
	        	}	
	        	++i;//needed sentinel i to control algorithm aspect
	        }
	        
	        anonymizationConfiguration.setMaxOutliers(0d);

	        try {
	        	result = anonymizer.anonymize(sourceData, anonymizationConfiguration);
	        }
	        catch (IOException e) {
	        	e.printStackTrace();
	        	model.addAttribute("empName", empName);
	        	model.addAttribute("orgName", orgName);
	        	return "index";
	        }
	        //handle = sourceData.getHandle();
	        
	        //// Display data collection
	        //Iterator<String[]> itHandle = handle.iterator();
	        
	        //removed due to moving the dataRows to global scope, only needs to be done once
	        //String flubRow = Arrays.toString(itHandle.next());//remove the header row for display purposes
	        
	        /*while((itHandle.hasNext()) && (i % 801 != 0)) {//needs extra cleanup to remove empty column
				String row = Arrays.toString(itHandle.next());
				String[] dataTemp = row.split("[\\[\\],]");
				String[] data = new String[dataTemp.length - 1];
				for(int j = 0; j < dataTemp.length - 1; j++) {
					data[j] = dataTemp[j+1].trim();
				}
				dataRows.add(data);
				++i;
			}*/
	        
	        String file = anonForm.getFileName();
	        System.out.println("Name of the file is : " + file);
	        
	      //used once 'result' outcome becomes relevant, still wish to display original dataset
	        if(!result.isResultAvailable()) {//testing now as next lines are dependent on the presence of a valid ARXResult object
	        	System.out.println("There was no solution to the provided configuration, you may need to start again");
	        	String errorMessage = "There was no solution to the provided configuration, you may need to start again";
	        	//perhaps return to setAnonDetails to give user immediate retry chance
	        	result = null; //trying to handle the dataHandle issue
	        	model.addAttribute("headerRow", headerRow);
	        	model.addAttribute("dataRows", dataRows);
	        	model.addAttribute("errorMessage", errorMessage);//implement in jsp
	        	return "compareSets";//return prematurely due to lack of correct result, avoid error failure
	        }
	        
	        String[] nameSplitFromExtension = file.split("\\.");
	        String dataSetName = nameSplitFromExtension[0];//save anonymized file to the project structure, allowed as it is secure.
	        anonymizedDataFileName = dataSetName + "_anonymized.csv";//anonymized file will always be in a csv format for now
	        anonReport.setFileName(anonymizedDataFileName);
	        result.getOutput(false).save("src/main/resources/templates/outputs/" + empOrg + "/" + anonymizedDataFileName, ';');
	        File anonFile = new File("src/main/resources/templates/outputs/" + empOrg + "/" + anonymizedDataFileName);
	        //fileStructureAspects.fileEnDeCrypt(Cipher.ENCRYPT_MODE, secret, anonFile, anonFile);
	        //attempting to implement some utility metrics, just printing atm
	        /*for (ARXNode[] level : result.getLattice().getLevels()) {
	            for (ARXNode node : level) {
	                Iterator<String[]> transformed = result.getOutput(node, false).iterator();
	                System.out.println("Transformation : "+Arrays.toString(node.getTransformation()));
	                System.out.println("InformationLoss: "+node.getHighestScore());
	                while (transformed.hasNext()) {
	                    System.out.print("   ");
	                    System.out.println(Arrays.toString(transformed.next()));
	                }
	            }
	        }*/
	        anonyRows = new ArrayList<String[]>(); //needed to void the anonymized rows as they were just continuously added to
	        Iterator<String[]> transformed = result.getOutput(false).iterator();
	        
	        String flubRow = Arrays.toString(transformed.next());// to remove header from answer also
	       
	        while((transformed.hasNext()) && (i % 201 != 0)) {
				String row = Arrays.toString(transformed.next());
				String[] dataTemp = row.split("[\\[\\],]");//all data needs formatting to remove empty columns
				String[] data = new String[dataTemp.length - 1];
				for(int j = 0; j < dataTemp.length - 1; j++) {
					data[j] = dataTemp[j+1].trim();
				}
				anonyRows.add(data);
				++i;
			}
	        
	        transformed = null;
	        ////// Display data collection
	        
	        String[] stats = printResult(result, sourceData);
	        //throw into model for display	/// now adding statistical information about the sets
	     // ===================================================
	        // assigning values to the anonymization report object, use the print result method
	        anonReport.setInformationLoss(stats[1]);
	        anonReport.setTimeTaken(stats[0]);
	        // ===================================================
	        
	        model.addAttribute("headerRow", headerRow);
		    model.addAttribute("dataRows", dataRows);
		    model.addAttribute("anonyRows", anonyRows);
		    
		return "compareSets";
	}
		/**
		 * Access method to allow users to return to the compareSets jsp
		 * 
		 */
		@RequestMapping("/returnSender")
		public String returnToSender(Model model) {
			model.addAttribute("headerRow", headerRow);
			model.addAttribute("dataRows", dataRows);
			model.addAttribute("anonyRows", anonyRows);
			
			return "compareSets";//returns to jsp page with all attributes set as before
		}
		
		/** gateway method to the risk page, passing AnonyRows, dataRows is already there
		 *  deleting files functionality can be accessed by returning to view anonymization page
		 *  same with downloading files, all handles and objects needed can be accessed on backend
		 *  through the persistent sourceData and result objects
		 */
		@RequestMapping("/goToRiskPage")
		public String showRiskPage(Model model) {
			
	        model.addAttribute("countries", countries);
			model.addAttribute("headerRow", headerRow);
			model.addAttribute("anonyRows", anonyRows);
			return "showRisks";
		}
		
		//method used to analyze the risks of a dataset
		@RequestMapping("/analyseRisks")
		public String analyseRisks(@RequestParam("populationRegion") String region,
				@RequestParam("THRESHOLD") double threshold, Model model) {
			RiskObject riskObject = new RiskObject();
			
			riskObject.setCountry(region);
			anonReport.setCountry(region);//region added to specific anonymization report object
			//riskObject.setThreshold(threshold);
			
			//riskObject.getDataSetDistributionMetrics() = getDistributionStatistics();
			riskObject = getDistributionStatistics(riskObject);//add the maps of data to the object
			
	        System.out.println("\n ------------------------------------------------");
	        System.out.println("\n ------------------------------------------------");
	        
	        
	        // Perform risk analysis
	        System.out.println("\n - Risk analysis:");
	        System.out.println(" 	");
	        riskObject = analyzeDataRisk(result.getOutput(false), region, threshold, riskObject);//add the model metrics to the object
	        
	        model.addAttribute("riskObject", riskObject);
	        model.addAttribute("anonyRows", anonyRows);
	        model.addAttribute("headerRow", headerRow);
	        model.addAttribute("countries", countries);
	        
			return "showRisks";
		}
		
		//method that would be used to analyze the utility of a dataset 
		//utility is highly volatile in current ARX API, breaks whole application if used
		/*@RequestMapping("/analyseUtility")
		public String analyseUtility() {
			// Access statistics
		       StatisticsQuality utility = sourceData.getHandle().getStatistics().getQualityStatistics();
		       System.out.println("Input:");
		       System.out.println(" - Ambiguity: " + utility.getAmbiguity().getValue());
		       System.out.println(" - AECS: " + utility.getAverageClassSize().getValue());
		       System.out.println(" - Discernibility: " + utility.getDiscernibility().getValue());
		       System.out.println(" - Granularity: " + utility.getGranularity().getArithmeticMean(false));
		       System.out.println(" - Attribute-level SE: " + utility.getAttributeLevelSquaredError().getArithmeticMean(false));
		       System.out.println(" - Non-Uniform Entropy: " + utility.getNonUniformEntropy().getArithmeticMean(false));
		       System.out.println(" - Precision: " + utility.getGeneralizationIntensity().getArithmeticMean(false));
		       System.out.println(" - Record-level SE: " + utility.getRecordLevelSquaredError().getValue());
		       
		       DataHandle theOutput = result.getHandle();
		       
		       // Access statistics
		       utility = theOutput.getStatistics().;
		       System.out.println("Output:");
		       System.out.println(" - Ambiguity: " + utility.getAmbiguity().getValue());
		       System.out.println(" - AECS: " + utility.getAverageClassSize().getValue());
		       System.out.println(" - Discernibility: " + utility.getDiscernibility().getValue());
		       System.out.println(" - Granularity: " + utility.getGranularity().getArithmeticMean(false));
		       System.out.println(" - MSE: " + utility.getAttributeLevelSquaredError().getArithmeticMean(false));
		       System.out.println(" - Non-Uniform Entropy: " + utility.getNonUniformEntropy().getArithmeticMean(false));
		       System.out.println(" - Precision: " + utility.getGeneralizationIntensity().getArithmeticMean(false));
		       System.out.println(" - SSE: " + utility.getRecordLevelSquaredError().getValue());
			return "showUtilities";
		}*/
		
		//Used to quickly determine the data type of each column in the dataset
		   private static DataType<?> determineDataType(DataHandle handle, int column) {
		       //System.out.println(" - Potential data types for attribute: "+handle.getAttributeName(column));
		       List<Pair<DataType<?>, Double>> types = handle.getMatchingDataTypes(column);

		       //After a substantial amount of testing, this is no longer needed, 
		       //however printed entries sorted by match percentage
		       
		       //for (Pair<DataType<?>, Double> entry : types) {
		           //System.out.print("   * ");
		           //System.out.print(entry.getKey().getDescription().getLabel());
		           //if (entry.getKey().getDescription().hasFormat()) {
		           //    System.out.print("[");
		           //    System.out.print(((DataTypeWithFormat) entry.getKey()).getFormat());
		           //    System.out.print("]");
		           //}
		           //System.out.print(": ");
		           //System.out.println(entry.getValue());
		       //}
		       
		       return types.get(0).getKey(); //first identified is usually the correct data type
		   }
		
	//method that allows the user to delete all files related to their anonymization from the final page
		@RequestMapping("/deleteDataAndHierarchies")//available on first page? to allow users to cancel securely, remove all traces
		public void deleteDataAndHierarchies() throws IOException {
			//when work complete and user wants record gone, need to remove from file system
			try {
				System.out.println("Just before delete");
				dataAspectsHelper.deleteFiles(empOrg);
				System.out.println("Just after delete");
			}
			catch (IOException failure) {
				System.out.println("Error deleting your files: " + failure.getLocalizedMessage());
			}
			sourceData = null;//remove reference to object so that memory is garbage collected, fully remove data from the application	
		}
		
		
		// TODO : integrate this
		//method that allows the user to delete the anonymized file from the application
				@RequestMapping("/deleteDataAndHierarchiesAnonymizedFile")//available on first page? to allow users to cancel securely, remove all traces
				public void deleteAnonymizedData() throws IOException {
					//when work complete and user wants record gone, need to remove from file system
					try {//will need to search through directory to get list for combo box
						System.out.println("Just before delete");
						dataAspectsHelper.deleteAllFiles(empOrg);
						System.out.println("Just after delete");
					}
					catch (IOException failure) {
						System.out.println("Error deleting your files: " + failure.getLocalizedMessage());
					}
					sourceData = null;//remove reference to object so that memory is garbage collected, fully remove data from the application	
				}

		   //create file for user to download   
		   @RequestMapping("/downloadAnonymizedFile")
		    public void doDownload(HttpServletRequest request,
		            HttpServletResponse response) throws IOException {
		 
		        // get absolute path of the application
		        ServletContext context = request.getServletContext();
		        
		        // construct the complete absolute path of the file
		        String fullPath = "src/main/resources/templates/outputs/" + empOrg + "/" + anonymizedDataFileName;  
		        
		        File downloadFile = new File(fullPath);
		        // quickly decrypt before downloading
		        //fileStructureAspects.fileEnDeCrypt(Cipher.DECRYPT_MODE, secret, downloadFile, downloadFile);
		        
		        FileInputStream inputStream = new FileInputStream(downloadFile);
		         
		        // get MIME type of the file
		        String mimeType = context.getMimeType(fullPath);
		        if (mimeType == null) {
		            // set to binary type if MIME mapping not found
		            mimeType = "application/octet-stream";
		        }
		        System.out.println("MIME type: " + mimeType);
		 
		        // set content attributes for the response
		        response.setContentType(mimeType);
		        response.setContentLength((int) downloadFile.length());
		 
		        // set headers for the response
		        String headerKey = "Content-Disposition";
		        String headerValue = String.format("attachment; filename=\"%s\"",
		                downloadFile.getName());
		        response.setHeader(headerKey, headerValue);
		 
		        // get output stream of the response
		        OutputStream outStream = response.getOutputStream();
		 
		        byte[] buffer = new byte[4096];//hardcode for now
		        int bytesRead = -1;
		 
		        // write bytes read from the input stream into the output stream
		        while ((bytesRead = inputStream.read(buffer)) != -1) {
		            outStream.write(buffer, 0, bytesRead);
		        }
		 
		        inputStream.close();
		        outStream.close();
		        //GDPR compliance, user identity protection through deletion
		        //downloading anonymized data set implies conclusion of transaction
		        dataAspectsHelper.deleteFiles(empOrg);
		        sourceData = null;//destroy original data object to remove any possible traces
		    }
		   
		//no need to remove anonymized dataset as of now, can be requested if needed later on, but not imperetive now
	////////////////////////////////////////////////////////////////////////////////////////////// THIS IS WHERE THE VARIABLE INPUT TEST ENDS
	//->>>>>change return type eventually to supply these values
	private RiskObject getDistributionStatistics(RiskObject riskObject) {//potentially hierNames, or plain index?
		//print frequencies
		System.out.println("Inside getDistributionStatistics");
		HashMap<String, HashMap<String, String>> inputDistributions = new HashMap<String, HashMap<String, String>>();
		HashMap<String, HashMap<String, String>> outputDistributions = new HashMap<String, HashMap<String, String>>();
		//HashMap<String, String> inputValues = new HashMap<String, String>();//these have to be local to the for loop
		//HashMap<String, String> outputValues = new HashMap<String, String>();//otherwise just keep adding to them continuously
		StatisticsFrequencyDistribution distribution;
		DataHandle dataHandle = sourceData.getHandle();
		DataHandle resultHandle = result.getOutput(false);
		
		int i = 0;
		for(String header : headerRow) {
			System.out.println(" - Distribution of attribute " + header + " in input:");
			
			HashMap<String, String> inputValues = new HashMap<String, String>();
			HashMap<String, String> inputValuesClean = new HashMap<String, String>();
			
			HashMap<String, String> outputValues = new HashMap<String, String>();
			HashMap<String, String> outputValuesClean = new HashMap<String, String>();
			
			distribution = dataHandle.getStatistics().getFrequencyDistribution(i, false);//can split this to individual values?
			System.out.println("   " + Arrays.toString(distribution.values));
			System.out.println("   " + Arrays.toString(distribution.frequency));
			String[] values = distribution.values;//get unique values of input
			double[] frequency = distribution.frequency;//get frequency of input
				System.out.print("   ");
				for(int j = 0; j < frequency.length; ++j) {
					DecimalFormat f = new DecimalFormat("##.00");
					String tempFreq = f.format(frequency[j]*100.0);// + "%";
				    System.out.print(tempFreq);
				    inputValues.put(values[j], tempFreq);
				    tempFreq = f.format(frequency[j]*100.0);
				    inputValuesClean.put(values[j], tempFreq);
				}
				//pieChartGenerator.makePieChart(empOrg, header, inputValuesClean);
				System.out.println();
			inputDistributions.put(header, inputValues);
			// Print frequencies
			System.out.println(" - Distribution of attribute " + header + " in output:");
			distribution = resultHandle.getStatistics().getFrequencyDistribution(i, true);// can split this to individual values?
			System.out.println("   " + Arrays.toString(distribution.values));
			System.out.println("   " + Arrays.toString(distribution.frequency));
			values = distribution.values;
			frequency = distribution.frequency;//get frequency of output
				System.out.print("   ");
				for(int j = 0; j < frequency.length; ++j) {
					DecimalFormat f = new DecimalFormat("##.00");
					String tempFreq = f.format(frequency[j]*100.0);// + "%";
				    System.out.print(tempFreq);
				    outputValues.put(values[j], tempFreq);
				    tempFreq = f.format(frequency[j]*100.0);
				    outputValuesClean.put(values[j], tempFreq);
				}
				//pieChartGenerator.makePieChart(empOrg,header+"_1",outputValuesClean);
				System.out.println();
				outputDistributions.put(header, outputValues);
				++i;
		}
		
		riskObject.setDataSetInputDistributionMetrics(inputDistributions);
		riskObject.setDataSetOutputDistributionMetrics(outputDistributions);
		
		System.out.println("Leaving getDistributionStatistics");
		return(riskObject);
	}
	
	/**
	 * Perform risk analysis
	 * @param handle
	 * @param populationRegion
	 * @param THRESHOLD -> The lowest percentage risk value that is allowed for a particular record
	 * @extra Now adds values to the object used for the anonymization risk report
	 */
	private RiskObject analyzeDataRisk(DataHandle handle, String populationRegion, double THRESHOLD, RiskObject riskObject) {   
	    
		String[] proseJourn = new String[5];//temp string array for prosecutor/journalist to put into riskObject
		
		riskObject.setThreshold(getPercent(THRESHOLD));
		anonReport.setThreshold(getPercent(THRESHOLD));
		
		ARXPopulationModel populationmodel = determineRegion(populationRegion);//chosen from risk screen
	    //variable models based on significant area
	       
	    RiskEstimateBuilder builder = handle.getRiskEstimator(populationmodel);
	    RiskModelSampleSummary risks = builder.getSampleBasedRiskSummary(THRESHOLD);
	       
	    System.out.println(" * Baseline risk threshold: " + getPercent(THRESHOLD));
	    
	    //prosecutor risk adding to riskObject
	    System.out.println(" * Prosecutor attacker model");
	    System.out.println("   - Records at risk: " + getPercent(risks.getProsecutorRisk().getRecordsAtRisk()));
	    System.out.println("   - Highest risk: " + getPercent(risks.getProsecutorRisk().getHighestRisk()));
	    System.out.println("   - Success rate: " + getPercent(risks.getProsecutorRisk().getSuccessRate()));
	    
	    proseJourn[0] = getPercent(risks.getProsecutorRisk().getRecordsAtRisk());
	    anonReport.setProsecutorRecordsAtRisk(getPercent(risks.getProsecutorRisk().getRecordsAtRisk()));
	    proseJourn[1] = getPercent(risks.getProsecutorRisk().getHighestRisk());
	    anonReport.setProsecutorHighestRisk(getPercent(risks.getProsecutorRisk().getHighestRisk()));
	    proseJourn[2] = getPercent(risks.getProsecutorRisk().getSuccessRate());
	    anonReport.setProsecutorSuccessRate(getPercent(risks.getProsecutorRisk().getSuccessRate()));
	    if(getPercent(risks.getProsecutorRisk().getSuccessRate()).equals("0%")) {
	    	anonReport.setProsecutorGDPR("Compliant");
	    	proseJourn[3] = "Compliant";
	    	proseJourn[4] = "#35d733";
	    } else {
	    	anonReport.setProsecutorGDPR("Non-Compliant");
	    	proseJourn[3] = "Non-Compliant";
	    	proseJourn[4] = "#e16830";
	    }
	    //write prosecutor risks
	    riskObject.setProsecutorStats(proseJourn);
	    
	    //journalist risk adding to riskObject
	    System.out.println(" * Journalist attacker model");
	    System.out.println("   - Records at risk: " + getPercent(risks.getJournalistRisk().getRecordsAtRisk()));
	    System.out.println("   - Highest risk: " + getPercent(risks.getJournalistRisk().getHighestRisk()));
	    System.out.println("   - Success rate: " + getPercent(risks.getJournalistRisk().getSuccessRate()));
	    //overwrite with journalist model values
	    proseJourn[0] = getPercent(risks.getJournalistRisk().getRecordsAtRisk());
	    anonReport.setJournalistRecordsAtRisk(getPercent(risks.getJournalistRisk().getRecordsAtRisk()));
	    proseJourn[1] = getPercent(risks.getJournalistRisk().getHighestRisk());
	    anonReport.setJournalistHighestRisk(getPercent(risks.getJournalistRisk().getHighestRisk()));
	    proseJourn[2] = getPercent(risks.getJournalistRisk().getSuccessRate());
	    anonReport.setJournalistSuccessRate(getPercent(risks.getJournalistRisk().getSuccessRate()));
	    if(getPercent(risks.getProsecutorRisk().getSuccessRate()).equals("0%")) {
	    	anonReport.setJournalistGDPR("Compliant");
	    	proseJourn[3] = "Compliant";
	    	proseJourn[4] = "#35d733";
	    } else {
	    	anonReport.setJournalistGDPR("Non-Compliant");
	    	proseJourn[3] = "Non-Compliant";
	    	proseJourn[4] = "#e16830";
	    }
	    
	    riskObject.setJournalistStats(proseJourn);
	    
	    System.out.println(" * Marketer attacker model");
	    System.out.println("   - Success rate: " + getPercent(risks.getMarketerRisk().getSuccessRate()));
	    
	    riskObject.setMarketerStat(getPercent(risks.getMarketerRisk().getSuccessRate()));
	    anonReport.setMarketerStat(getPercent(risks.getMarketerRisk().getSuccessRate()));
	   
	    return riskObject;
		}
	   
	   /**
	    * Returns a region to be used for risk metrics
	    * @param area
	    * @return
	    */
	   private static ARXPopulationModel determineRegion(String populationRegion) {
		   ARXPopulationModel populationmodel = ARXPopulationModel.create(Region.NONE);
		   
		   if(populationRegion.equals("Africa")) {
	    	   populationmodel = ARXPopulationModel.create(Region.AFRICA);
	       } else if(populationRegion.equals("Australia")) {
	    	   populationmodel = ARXPopulationModel.create(Region.AUSTRALIA);
	       } else if(populationRegion.equals("Brazil")) {
	    	   populationmodel = ARXPopulationModel.create(Region.BRASIL);
	       } else if(populationRegion.equals("Canada")) {
	    	   populationmodel = ARXPopulationModel.create(Region.CANADA);
	       } else if(populationRegion.equals("China")) {
	    	   populationmodel = ARXPopulationModel.create(Region.CHINA);
	       } else if(populationRegion.equals("Europe")) {
	    	   populationmodel = ARXPopulationModel.create(Region.EUROPE);
	       } else if(populationRegion.equals("European Union")) {
	    	   populationmodel = ARXPopulationModel.create(Region.EUROPEAN_UNION);
	       } else if(populationRegion.equals("France")) {
	    	   populationmodel = ARXPopulationModel.create(Region.FRANCE);
	       } else if(populationRegion.equals("Germany")) {
	    	   populationmodel = ARXPopulationModel.create(Region.GERMANY);
	       } else if(populationRegion.equals("India")) {
	    	   populationmodel = ARXPopulationModel.create(Region.INDIA);
	       } else if(populationRegion.equals("North America")) {
	    	   populationmodel = ARXPopulationModel.create(Region.NORTH_AMERICA);
	       } else if(populationRegion.equals("South America")) {
	    	   populationmodel = ARXPopulationModel.create(Region.SOUTH_AMERICA);
	       } else if(populationRegion.equals("United Kingdom")) {
	    	   populationmodel = ARXPopulationModel.create(Region.UK);
	       } else if(populationRegion.equals("United States")) {
	    	   populationmodel = ARXPopulationModel.create(Region.USA);
	       }
		   return populationmodel;
	   }

	   /**
	    * Returns a formatted string, for value distribution percentages
	    * @param value
	    * @return
	    */
	   private static String getPercent(double value) {
	       return (int)(Math.round(value * 100)) + "%";
	   }
		   
	   @RequestMapping("/makeJasperReport")
	   public void makeJasperReport(HttpServletRequest request,
	            HttpServletResponse response) {
	      //used to first compile the report build
		   String jrxmlFileName = "src/main/resources/anonReport.jrxml";
		  //use to create all jasper reports generated by the application
		   String jasperFileName = "src/main/resources/anonReport.jasper";
		   //becomes this jrprint format
		   String jrprintFile = "src/main/resources/anonReport"+ empOrg + ".jrprint";
		  //which then becomes this printable pdf format
		   String pdfReport = "src/main/resources/"+ empOrg +"/anonReport.pdf";
		   
		   System.out.println("in the jasper report method");
		   
		   
		   //check if file has been compiled already, only then compile as compilation is costly
		   if(!new File(jasperFileName).exists()) {
			   File jasperFile = new File(jasperFileName); 
			   try {
				   JasperCompileManager.compileReportToFile(jrxmlFileName, jasperFileName);
			   } 
			   catch (JRException e) {
				   System.out.println("There was a problem compiling the report to \\.jasper");
				   e.printStackTrace();
			   }
		   }
		 
	      //create the data source using the array of field records
	      JRBeanCollectionDataSource beanColDataSource =
	      new JRBeanCollectionDataSource(algorithmStats);
	
	      Map<String,Object> parameters = new HashMap<String,Object>();
	      /**
	       * Creating parameters for some headings on risk report
	       */
	      parameters.put("ReportTitle", "Anonymization Risk Report");
	      parameters.put("Author", "Provided by AARMRD");
	      parameters.put("Organisation", this.orgName);
	      parameters.put("Employee", this.empName);
	      parameters.put("fileName", anonReport.getFileName());
	      parameters.put("timeTaken", anonReport.getTimeTaken());
	      parameters.put("country", anonReport.getCountry());
	      parameters.put("threshold", anonReport.getThreshold());
	      parameters.put("informationLoss", anonReport.getInformationLoss());
	      parameters.put("journalistHighestRisk", anonReport.getJournalistHighestRisk());
	      parameters.put("journalistRecordsAtRisk", anonReport.getJournalistRecordsAtRisk());
	      parameters.put("journalistSuccessRate", anonReport.getJournalistSuccessRate());
	      parameters.put("prosecutorHighestRisk", anonReport.getProsecutorHighestRisk());
	      parameters.put("prosecutorRecordsAtRisk", anonReport.getProsecutorRecordsAtRisk());
	      parameters.put("prosecutorSuccessRate", anonReport.getProsecutorSuccessRate());
	      parameters.put("marketerStat", anonReport.getMarketerStat());
	      parameters.put("journalistGDPR", anonReport.getJournalistGDPR());
	      parameters.put("prosecutorGDPR", anonReport.getProsecutorGDPR());
	      
	      
	      if(!new File(jrprintFile).exists()) {
	    	  File jasperFile = new File(jrprintFile);
	    	  try {
	    		  JasperFillManager.fillReportToFile(jasperFileName, jrprintFile, parameters, beanColDataSource);
		      } 
		      catch (JRException e) {
		    	 System.out.println("There was a problem creating \\.jrprint");
		         e.printStackTrace();
		      }
	      }
	      //convert the generated jrprintFile as a PDF
	      if(!new File(pdfReport).exists()) {
	    	  File jasperFile = new File(pdfReport);
	    	  try {
		    	  JasperExportManager.exportReportToPdfFile(jrprintFile, pdfReport);
		      }
		      catch (JRException e) {
		    	  System.out.println("There was a problem creating \\.pdf");
		    	  e.printStackTrace();
		      }
	      }
	      File deleteJRPrint = new File(jrprintFile);
	      try {
			FileUtils.forceDelete(deleteJRPrint);
		  } catch (IOException e) {
			e.printStackTrace();
		  }
	 
	   }
	   
	 //create report file for user to download  
	   @RequestMapping("/doPDFDownload")
	    public void doPDFDownload(HttpServletRequest request,
	            HttpServletResponse response) throws IOException {
	    	// get absolute path of the application
	        ServletContext context = request.getServletContext();
	        
	        // construct the complete absolute path of the file
	        String fullPath = "src/main/resources/" + empOrg + "/anonReport.pdf";      
	        File downloadFile = new File(fullPath);
	        FileInputStream inputStream = new FileInputStream(downloadFile);
	         
	        // get MIME type of the file
	        String mimeType = context.getMimeType(fullPath);
	        if (mimeType == null) {
	            // set to binary type if MIME mapping not found
	            mimeType = "application/octet-stream";
	        }
	        System.out.println("MIME type: " + mimeType);
	 
	        // set content attributes for the response
	        response.setContentType(mimeType);
	        response.setContentLength((int) downloadFile.length());
	 
	        // set headers for the response
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                downloadFile.getName());
	        response.setHeader(headerKey, headerValue);
	 
	        // get output stream of the response
	        OutputStream outStream = response.getOutputStream();
	 
	        byte[] buffer = new byte[4096];//hardcode for now
	        int bytesRead = -1;
	 
	        // write bytes read from the input stream into the output stream
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
	 
	        inputStream.close();
	        outStream.close();
	        
	        try {
	        	File deleteFile = new File("src/main/resources/" + empOrg + "/");
				FileUtils.cleanDirectory(deleteFile);
			    FileUtils.deleteDirectory(deleteFile);
			  } catch (IOException e) {
				e.printStackTrace();
			  }
	   }
}