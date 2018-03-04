package org.anonymize.anonymizationapp.controller;


import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
// Importing ARX required modules, dependencies etc
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.security.NoSuchAlgorithmException;
//import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXCostBenefitConfiguration;
import org.deidentifier.arx.ARXLattice.ARXNode;
import org.deidentifier.arx.ARXLogisticRegressionConfiguration;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.AttributeType.MicroAggregationFunction;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.DataGeneralizationScheme;
import org.deidentifier.arx.DataGeneralizationScheme.GeneralizationDegree;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.DataSelector;
import org.deidentifier.arx.DataSource;
import org.deidentifier.arx.DataSubset;
import org.deidentifier.arx.DataType;
import org.deidentifier.arx.DataType.DataTypeDescription;
import org.deidentifier.arx.DataType.DataTypeWithFormat;
import org.deidentifier.arx.aggregates.StatisticsContingencyTable;
import org.deidentifier.arx.aggregates.StatisticsContingencyTable.Entry;
import org.deidentifier.arx.aggregates.StatisticsFrequencyDistribution;
import org.deidentifier.arx.aggregates.StatisticsSummary;
import org.deidentifier.arx.certificate.ARXCertificate;
import org.deidentifier.arx.aggregates.AggregateFunction.AggregateFunctionBuilder;
import org.deidentifier.arx.aggregates.HierarchyBuilder;
import org.deidentifier.arx.aggregates.HierarchyBuilder.Type;
import org.deidentifier.arx.aggregates.HierarchyBuilderGroupingBased.Level;
import org.deidentifier.arx.aggregates.HierarchyBuilderIntervalBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderIntervalBased.Interval;
import org.deidentifier.arx.aggregates.HierarchyBuilderIntervalBased.Range;
import org.deidentifier.arx.aggregates.HierarchyBuilderOrderBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased.Order;
import org.deidentifier.arx.criteria.AverageReidentificationRisk;
import org.deidentifier.arx.criteria.DPresence;
import org.deidentifier.arx.criteria.DistinctLDiversity;
import org.deidentifier.arx.criteria.EDDifferentialPrivacy;
import org.deidentifier.arx.criteria.HierarchicalDistanceTCloseness;
import org.deidentifier.arx.criteria.Inclusion;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.criteria.OrderedDistanceTCloseness;
import org.deidentifier.arx.criteria.KMap;
import org.deidentifier.arx.criteria.ProfitabilityJournalist;
import org.deidentifier.arx.criteria.ProfitabilityJournalistNoAttack;
import org.deidentifier.arx.criteria.ProfitabilityProsecutor;
import org.deidentifier.arx.criteria.ProfitabilityProsecutorNoAttack;
import org.deidentifier.arx.criteria.EntropyLDiversity;
import org.deidentifier.arx.criteria.RecursiveCLDiversity;
import org.deidentifier.arx.exceptions.RollbackRequiredException;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.deidentifier.arx.io.CSVSyntax;
import org.deidentifier.arx.metric.Metric;
import org.deidentifier.arx.metric.v2.MetricSDNMPublisherPayout;
import org.deidentifier.arx.metric.v2.QualityMetadata;
import org.deidentifier.arx.metric.v2.__MetricV2;
import org.deidentifier.arx.risk.HIPAAIdentifierMatch;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelAttributes;
import org.deidentifier.arx.risk.RiskModelAttributes.QuasiIdentifierRisk;
import org.deidentifier.arx.risk.RiskModelHistogram;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness.PopulationUniquenessModel;
import org.deidentifier.arx.risk.RiskModelSampleRisks;
import org.deidentifier.arx.risk.RiskModelSampleSummary;
import org.deidentifier.arx.risk.RiskModelSampleUniqueness;
import org.anonymize.anonymizationapp.model.AnonymizationBase;
import org.anonymize.anonymizationapp.model.AnonymizationObject;
import org.anonymize.anonymizationapp.model.RiskObject;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang.StringUtils;
// ARX related stuff 
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.anonymize.anonymizationapp.util.DataAspects;

//import cern.colt.Arrays;

@Controller											//implements
public class AnonymizationController extends AnonymizationBase {
	//solution to the source data issue was to elevate the object to class scope
	Data sourceData;//now available in all methods within the anonymization controller
	ARXResult result;//global for risk and utility analysis
	String anonymizedDataFileName;//global anonymized dataset file name, for download purposeses 
	List<String[]> dataRows = new ArrayList<String[]>();//extract to global variables to allow access in entire application
	List<String[]> anonyRows = new ArrayList<String[]>();
	List<String> headerRow = new ArrayList<String>();//making global to improve accessibility and reduce need to pass between methods
	final String[] countries = {"America", "Australia", "Brasil", "Canada", "China", "Europe", 
			"European Union", "France", "Germany", "India", "North America", "South America",
			"United Kingdom", "United States"};//countries and regions for risk metrics
	//also remove from anonymization object
	
	//may restrict all anonymization actions to the anonymization controller? not have multiple controllers?
	
	@Autowired
	private DataAspects dataAspectsHelper;

   @RequestMapping("/")
   public String index() {
      return "index";
   }
   
   // will be a page to handle the uploading of files
   @RequestMapping("/uploadFiles")//only take in the files, establish data fields first, then allow user to select attribute types, algos etc
   public String getData(@RequestParam("dataFile") MultipartFile dataFile, @RequestParam(value="hiddenTable", required=false) String table,
		   @RequestParam(value="userName", required=false) String userName, @RequestParam(value="password", required=false) String password,
		   @RequestParam("hierFiles") MultipartFile[] hierFiles, Model model) throws IOException, ParseException, SQLException {
		///////////////// Creating the data object
				
		//attempting to cast multipartfile object to file(can be modularized later if successful)
		//prepare dataset name
		String datasetFile = dataFile.getOriginalFilename();
		System.out.println("datasetfile name is : " + datasetFile);
		model.addAttribute("fileName", datasetFile);
		
		// save the dataset to the project hierarchy(lol)
		File convertedDataFile = new File("src/main/resources/templates/data/" + datasetFile); //for saving
		convertedDataFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convertedDataFile);
		fos.write(dataFile.getBytes());
		fos.close();
		
		//////////////////// finished doing stuff for Data
		
		////////////////////creating the hierarchies in file path
			    
		//defining hierarchy files and names
		//List<String> headerRow = new ArrayList<String>();//for hierarchy names to display
		//headerRow removed to global
		//List<Hierarchy> hierarchies = new ArrayList<Hierarchy>();//list to hold the created hierarchy objects
		
		for(MultipartFile mulFile: hierFiles) {
			String fileName = mulFile.getOriginalFilename();
			File convertedHierFile = new File("src/main/resources/templates/hierarchy/" + fileName);
			System.out.println("hierfileName is : " + fileName);
			convertedHierFile.createNewFile();
			
			FileOutputStream fost = new FileOutputStream(convertedHierFile);
			fost.write(mulFile.getBytes());
			fost.close();//works
			
			//String[] tempArray = fileName.split("[\\_\\.]");//split by underscore and dot		  0         1         2
			//headerRow.add(tempArray[2]);//add file name to list for display reasons further on [dataset]_hierarchy_[column]
		}//removing the headerRow from this, need to make it in proper order
		
		//needed to keep the order of fields correct
		BufferedReader fileReader = new BufferedReader(new FileReader("src/main/resources/templates/data/" + datasetFile));
		
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
		
		// making data creation variable, normal files xls, csv
		System.out.println("before creating the data and hierarchies");
		if(table == null && (!datasetFile.substring(datasetFile.lastIndexOf(".") + 1).equals("db"))) {
			System.out.println("creating a data object from file : " + datasetFile);
			sourceData = dataAspectsHelper.createDataAndHierarchies(datasetFile, headerRow);//hopefully this way works 
		}// using db files and tables
		else if (table != null && (userName == null || password == null)) {
			System.out.println("creating a data object from table : " + table);
			sourceData = dataAspectsHelper.createDataAndHierarchies(datasetFile, headerRow, table);
		}
		else if (table != null && userName != null && password != null) {
			System.out.println("creating a data object from table with credentials Table Name :" + table + ", userName : " + userName + ", password : " + password);
			sourceData = dataAspectsHelper.createDataAndHierarchies(datasetFile, headerRow, table, userName, password);
		}
			
		System.out.println("after creating the data successfully");
		
		DataHandle handle = sourceData.getHandle();//acquiring data handle
		
		System.out.println("inHandle rows name is " + handle.getNumRows());
	    System.out.println("inHandle columns name is " + handle.getNumColumns());
	       
		
		Iterator<String[]> itHandle = handle.iterator();
		String flubRow = Arrays.toString(itHandle.next());//get the header of the dataset to display in bold
		System.out.println(">" + headerRow + "<");
		//String[] headerTemp = headerRow.split("[\\[\\],]");
		//String[] header = new String[headerTemp.length - 1];
		//for(int i = 0; i < headerTemp.length - 1; i++) {
		//	header[i] = headerTemp[i+1].trim();
		//}//shift all values 
		//model.addAttribute("headerData", headerTemp);//was incorrect
		model.addAttribute("headerRow", headerRow);//only need to get the header once as it will do for the resulting dataset also
		for (String colName : headerRow) {
			System.out.println(colName + ",");//testing if random space at the start
		}
		int i = 1;
		while((itHandle.hasNext()) && (i % 801 != 0)) {
			String row = Arrays.toString(itHandle.next());
			String[] dataTemp = row.split("[\\[\\],]");//all data needs formatting to remove empty columns
			String[] data = new String[dataTemp.length - 1];
			for(int j = 0; j < dataTemp.length - 1; j++) {
				data[j] = dataTemp[j+1].trim();
			}
			dataRows.add(data);
			++i;
		}
		
		//had to create object to push to jsp, to use modelAttribute
		//other variables instantiated as empty arrays based on the now constant header.length()
		AnonymizationObject anonForm = new AnonymizationObject(datasetFile, headerRow.size());//constant for an individual user
		//////// use header row to allow user to set individual algorithms for each field
		String[] models = {"k-anonymity","l-diversity","t-closeness"};//remove delta presence for now,"δ-presence"};
		String[] attributeTypes = {"Identifying", "Quasi-identifying", "Sensitive", "Insensitive"}; 
		for (String mod : models) {
			System.out.println(mod + ",");//testing if models in array
		}
		//object of type Data cannot be handled by the jsp, as such needs to be recreated between each view
		System.out.println("ModelsChosen length: " + anonForm.getModelsChosen().length);
		System.out.println("AttributesChosen length: " + anonForm.getAttributesChosen().length);
		System.out.println("ValuesChosen length: " + anonForm.getValuesForModels().length);
		
		model.addAttribute("anonForm", anonForm);
		model.addAttribute("models", models);
		//model.addAttribute("models", AnonModel.values());
		model.addAttribute("attributes", attributeTypes);
		model.addAttribute("dataRows", dataRows);
	    return "setAnonDetails";
   }
	
	// attempting to handle file uploads successfully
		@RequestMapping("/anonymizeData")
		public String performAnonymization(@Valid @ModelAttribute("anonForm") AnonymizationObject anonForm, 
				BindingResult bindResult, Model model) 
				throws IOException, ParseException, SQLException, ClassNotFoundException, NoSuchAlgorithmException {
			
			//Data cannot be passed through form, needs to be created locally, create and instantiate hierarchies also
			//String fileName = anonForm.getFileName();
			//Data source = dataAspectsHelper.createDataAndHierarchies(fileName);//attempt to recreate the data object locally
			String[] modelsChosen = anonForm.getModelsChosen();
			String[] attributesChosen = anonForm.getAttributesChosen();
			double[] valuesForModels = anonForm.getValuesForModels();
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
			System.out.println("Printing attribute types chosen");
			for(String anAttribute : attributesChosen) {
				System.out.println("The model is: " + anAttribute);
			}
			System.out.println("Possibly proved concept?");
			
			DataHandle handle = sourceData.getHandle();
			int i = 0;
			
			//define attribute type & determine the column's data type
			for(String header : headerRow) {
				//determine the type of the specific field
				if((attributesChosen[i].equals("Identifying")) || (attributesChosen[i].equals("- - NONE - -"))){//identifying by default
					sourceData.getDefinition().setAttributeType(header, AttributeType.IDENTIFYING_ATTRIBUTE);
				}
				else if(attributesChosen[i].equals("Quasi-Identifying")){
					sourceData.getDefinition().setAttributeType(header, AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
				}
				else if(attributesChosen[i].equals("Sensitive")){
					sourceData.getDefinition().setAttributeType(header, AttributeType.SENSITIVE_ATTRIBUTE);
				}
				else if((attributesChosen[i].equals("Insensitive"))){
					sourceData.getDefinition().setAttributeType(header, AttributeType.INSENSITIVE_ATTRIBUTE);
				}
				sourceData.getDefinition().setDataType(header, determineDataType(handle, i));
				++i;
			}
				
		    // Create an instance of the anonymizer
	        ARXAnonymizer anonymizer = new ARXAnonymizer();//create object
	        ARXConfiguration anonymizationConfiguration = ARXConfiguration.create();//defining the privacy model
	        
	        i = 0;
	        
	        //specifying everything variable to do with an anonymization, and the anonymizationConfiguration
	        for(String header : headerRow) {//can be run anywhere from 1 to x times per dataset size
	        	if(modelsChosen[i].equals("k-anonymity")){//can eventually be a large number of algorithms
	        		anonymizationConfiguration.addPrivacyModel(new KAnonymity((int)valuesForModels[i]));//for whole dataset, not tied to attribute
	        	}
	        	else if (modelsChosen[i].equals("l-diversity")) {
	        		anonymizationConfiguration.addPrivacyModel(new DistinctLDiversity(header, (int)valuesForModels[i]));
	        	}
	        	else if (modelsChosen[i].equals("t-closeness")) {
	        		anonymizationConfiguration.addPrivacyModel(new OrderedDistanceTCloseness(header, valuesForModels[i]));
	        	}
	        	++i;//needed sentinel i to control algorithm aspect
	        }
	        //anonymizationConfiguration.addPrivacyModel(new KAnonymity(2));//add privacy model, with supplied severity
	        //config.addPrivacyModel(new KAnonymity(2));//create k anonymity model with the anonymity value
	        anonymizationConfiguration.setMaxOutliers(0d);

	        result = anonymizer.anonymize(sourceData, anonymizationConfiguration);
	        
	        //// Display data collection
	        Iterator<String[]> itHandle = handle.iterator();
	        
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
	        	System.out.println("There was no solution to the provided configuration");
	        	String errorMessage = "There was no solution to the provided configuration";
	        	model.addAttribute("errorMessage", errorMessage);//implement in jsp
	        	return "compareSets";//return prematurely due to lack of correct result, avoid error failure
	        }
	        
	        String[] nameSplitFromExtension = file.split("\\.");
	        String dataSetName = nameSplitFromExtension[0];//save anonymized file to the project structure, allowed as it is secure.
	        anonymizedDataFileName = dataSetName + "_anonymized.csv";//anonymized file will always be in a csv format for now
	        result.getOutput(false).save("src/main/resources/templates/outputs/" + anonymizedDataFileName, ';');
	        //### Reanonymizing copies the above file back into the data file, need to restore hierarchies though
	        // maybe leave the hierarchies as part of the application but remove the original data for the user's protection?
	        
	        //attempting to implement some utility metrics, just printing atm
	        for (ARXNode[] level : result.getLattice().getLevels()) {
	            for (ARXNode node : level) {
	                Iterator<String[]> transformed = result.getOutput(node, false).iterator();
	                System.out.println("Transformation : "+Arrays.toString(node.getTransformation()));
	                System.out.println("InformationLoss: "+node.getHighestScore());
	                while (transformed.hasNext()) {
	                    System.out.print("   ");
	                    System.out.println(Arrays.toString(transformed.next()));
	                }
	            }
	        }
	        
	        Iterator<String[]> transformed = result.getOutput(false).iterator();
	        
	        String flubRow = Arrays.toString(transformed.next());// to remove header from answer also
	       
	        while((transformed.hasNext()) && (i % 801 != 0)) {
				String row = Arrays.toString(transformed.next());
				String[] dataTemp = row.split("[\\[\\],]");//all data needs formatting to remove empty columns
				String[] data = new String[dataTemp.length - 1];
				for(int j = 0; j < dataTemp.length - 1; j++) {
					data[j] = dataTemp[j+1].trim();
				}
				anonyRows.add(data);
				++i;
			}
	        ////// Display data collection
	        
	        printResult(result, sourceData);
	        
			//throw into model for display	/// now adding statistical information about the sets
	        
	        model.addAttribute("headerRow", headerRow);
		    model.addAttribute("dataRows", dataRows);
		    model.addAttribute("anonyRows", anonyRows);
		    
		return "compareSets";
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
	        model.addAttribute("countries", countries);
	        
			return "showRisks";
		}
		
		//method used to analyze the utility of a dataset 
		@RequestMapping("/analyseUtility")
		public String analyseUtility() {
			return "showUtilities";
		}
		
	//method that allows the user to delete all files related to their anonymization from the final page
		@RequestMapping("/deleteDataAndHierarchies")//available on first page? to allow users to cancel securely, remove all traces
		public void deleteDataAndHierarchies() throws IOException {
			//when work complete and user wants record gone, need to remove from file system
			try {
				System.out.println("Just before delete");
				dataAspectsHelper.deleteFiles();
				System.out.println("Just after delete");
			}
			catch (IOException failure) {
				System.out.println("Error deleting your files: " + failure.getLocalizedMessage());
			}
			sourceData = null;//remove reference to object so that memory is garbage collected, fully remove data from the application
			
		}
		
		//method that allows the user to delete the anonymized file from the application
				@RequestMapping("/deleteAnonymizedData")//available on first page? to allow users to cancel securely, remove all traces
				public void deleteAnonymizedData(@RequestParam("deleteName") String deleteName) throws IOException {
					//when work complete and user wants record gone, need to remove from file system
					try {//will need to search through directory to get list for combo box
						deleteName = deleteName + ".csv";
						System.out.println("Just before delete");
						dataAspectsHelper.deleteAnonFile(deleteName);
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
		        String fullPath = "src/main/resources/templates/outputs/" + anonymizedDataFileName;      
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
			HashMap<String, String> outputValues = new HashMap<String, String>();
			
			distribution = dataHandle.getStatistics().getFrequencyDistribution(i, false);//can split this to individual values?
			System.out.println("   " + Arrays.toString(distribution.values));
			System.out.println("   " + Arrays.toString(distribution.frequency));
			String[] values = distribution.values;//get unique values of input
			double[] frequency = distribution.frequency;//get frequency of input
				System.out.print("   ");
				for(int j = 0; j < frequency.length; ++j) {
					DecimalFormat f = new DecimalFormat("##.00");
					String tempFreq = f.format(frequency[j]*100.0) + "%";
				    System.out.print(tempFreq);
				    inputValues.put(values[j], tempFreq);
				}
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
					String tempFreq = f.format(frequency[j]*100.0) + "%";
				    System.out.print(tempFreq);
				    outputValues.put(values[j], tempFreq);
				}
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
	 */
	private RiskObject analyzeDataRisk(DataHandle handle, String populationRegion, double THRESHOLD, RiskObject riskObject) {   
	    
		String[] proseJourn = new String[3];//temp string array for prosecutor/journalist to put into riskObject
		
		riskObject.setThreshold(getPercent(THRESHOLD));
		
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
	    proseJourn[1] = getPercent(risks.getProsecutorRisk().getHighestRisk());
	    proseJourn[2] = getPercent(risks.getProsecutorRisk().getSuccessRate());
	    //write prosecutor risks
	    riskObject.setProsecutorStats(proseJourn);
	    
	    //journalist risk adding to riskObject
	    System.out.println(" * Journalist attacker model");
	    System.out.println("   - Records at risk: " + getPercent(risks.getJournalistRisk().getRecordsAtRisk()));
	    System.out.println("   - Highest risk: " + getPercent(risks.getJournalistRisk().getHighestRisk()));
	    System.out.println("   - Success rate: " + getPercent(risks.getJournalistRisk().getSuccessRate()));
	    //overwrite with journalist model values
	    proseJourn[0] = getPercent(risks.getJournalistRisk().getRecordsAtRisk());
	    proseJourn[1] = getPercent(risks.getJournalistRisk().getHighestRisk());
	    proseJourn[2] = getPercent(risks.getJournalistRisk().getSuccessRate());
	    
	    riskObject.setJournalistStats(proseJourn);
	    
	    System.out.println(" * Marketer attacker model");
	    System.out.println("   - Success rate: " + getPercent(risks.getMarketerRisk().getSuccessRate()));
	    
	    riskObject.setMarketerStat(getPercent(risks.getMarketerRisk().getSuccessRate()));
	   
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
	    * Returns a formatted string
	    * @param value
	    * @return
	    */
	   private static String getPercent(double value) {
	       return (int)(Math.round(value * 100)) + "%";
	   }
		   
		   
	@SuppressWarnings("unused")
   @RequestMapping("/anonymize")
   public String index(Model model) throws IOException, ParseException, SQLException, ClassNotFoundException, NoSuchAlgorithmException {
	   int anArray[] = {4,5,6,7,8,9,10};
	   int secArray[];
	   secArray = calcFigures(anArray);
	   model.addAttribute("anonMessage", "This is where the anonymization will be placed");
	   model.addAttribute("figures", secArray);
	   //////////////////////// added in this trying to find methods
	  
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// very useful for later	   
	   
	   // 1. List all data types
       for (DataTypeDescription<?> type : DataType.list()){
           
           // Print basic information
           System.out.println(" - Label : " + type.getLabel());
           System.out.println("   * Class: " + type.getWrappedClass());
           System.out.println("   * Format: " + type.hasFormat());
           if (type.hasFormat()){
               System.out.println("   * Formats: " + type.getExampleFormats());
           }
           
           // Create an instance without a format string
           DataType<?> instance = type.newInstance();
           
           // Create an instance with format string
           if (type.hasFormat() && !type.getExampleFormats().isEmpty()) {
               instance = type.newInstance(type.getExampleFormats().get(0));
           }
       }
       // 2. Obtain specific data type
       DataTypeDescription<Date> entry = DataType.list(Date.class);
       DataTypeDescription<String> entry1 = DataType.list(String.class);
       DataTypeDescription<Long> entry2 = DataType.list(Long.class);
       DataTypeDescription<Double> entry3 = DataType.list(Double.class);
       
       // 3. Obtain data in specific formats	
       Data theData = getData(); 
       
       theData.getDefinition().setDataType("zip", DataType.createDecimal("#,##0"));
       theData.getDefinition().setDataType("sen", DataType.createDate("dd.MM.yyyy"));
       
       DataHandle handle = theData.getHandle();
       double value1 = handle.getDouble(2, 2);
       Date value2 = handle.getDate(2, 5);

       System.out.println("Double: "+ value1);
       System.out.println("Date: "+ value2);   
	   
///////////// this is where anonymization begins	   
       //Data data = Data.create("src/main/resources/templates/data/medical_test_data.csv", StandardCharsets.UTF_8, ';');
       
// Define public dataset
	   //DefaultData data = Data.create();	   
	   Data data = getTheData();
	   
// Define research subset
       ///// Define research subset by directly selecting specific indexes of that set
       //DataSubset subset = DataSubset.create(data, new HashSet<Integer>(Arrays.asList(1, 2, 5, 7, 8)));
       
       ///// can create a subset through variability 
       //DataSelector selector = DataSelector.create(data).field("sen").equals("1");
       
       //DataSubset subset = DataSubset.create(data, selector);
       
       // Obtain a handle
/*	   DataHandle inHandle = data.getHandle();

       // Read the encoded data
       System.out.println("inHandle rows name is " + inHandle.getNumRows());
       System.out.println("inHandle columns name is " + inHandle.getNumColumns());
       System.out.println("inHandle field name is " + inHandle.getAttributeName(0));
       System.out.println("inHandle value of the field is " + inHandle.getValue(0, 0));
*/
       
// Define how field effects identifiability, can be taken in variably from screen
       //data.getDefinition().setAttributeType("age", AttributeType.SENSITIVE_ATTRIBUTE);
       //data.getDefinition().setAttributeType("age", AttributeType.IDENTIFYING_ATTRIBUTE);
       //data.getDefinition().setAttributeType("gender", AttributeType.INSENSITIVE_ATTRIBUTE);
       
// Define a field's type, 1 of 5 supported
       //data.getDefinition().setDataType("age", DataType.DECIMAL);
       //data.getDefinition().setDataType("gender", DataType.STRING);
       //data.getDefinition().setDataType("zipcode", DataType.DECIMAL);
	   
// Define input hierarchy files
       //data.getDefinition().setAttributeType("age", Hierarchy.create("src/main/resources/templates/hierarchy/test_age.csv", StandardCharsets.UTF_8, ';'));
       //data.getDefinition().setAttributeType("gender", Hierarchy.create("src/main/resources/templates/hierarchy/test_gender.csv", StandardCharsets.UTF_8, ';'));
       //data.getDefinition().setAttributeType("phoneno", Hierarchy.create("src/main/resources/templates/hierarchy/test_phoneno.csv", StandardCharsets.UTF_8, ';'));
       
       // set the minimal generalization height
       /*data.getDefinition().setMinimumGeneralization("zipcode", 3);
       data.getDefinition().setMaximumGeneralization("zipcode", 3);
       data.getDefinition().setMinimumGeneralization("gender", 1);*/
       //data.getDefinition().setAttributeType("disease", AttributeType.SENSITIVE_ATTRIBUTE);
/////this might be very very very important to look into 
       /*HierarchyBuilderRedactionBased<?> builder1 = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
               Order.RIGHT_TO_LEFT,
               ' ',
               '*');
       HierarchyBuilderRedactionBased<?> builder2 = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
               Order.RIGHT_TO_LEFT,
               ' ',
    		   '*');*/ // moving to some differing hierarchies generated on via supplied values
	   
	   HierarchyBuilderIntervalBased<Long> builder1 = HierarchyBuilderIntervalBased.create(
               DataType.INTEGER,
               new Range<Long>(0l,0l,0l),
               new Range<Long>(99l,99l,99l));

		// Define base intervals
		builder1.setAggregateFunction(DataType.INTEGER.createAggregate().createIntervalFunction(true, false));
		builder1.addInterval(0l, 20l);
		builder1.addInterval(20l, 33l);
		
		// Define grouping fanouts
		builder1.getLevel(0).addGroup(2);
		builder1.getLevel(1).addGroup(3);
		
		HierarchyBuilderRedactionBased<?> builder2 = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
		                                                Order.RIGHT_TO_LEFT,
		                                                ' ',
														'*');
		builder2.setAlphabetSize(10, 5);
    // Define attribute types
       /*data.getDefinition().setAttributeType("age", age);  //commented out for example 24
       data.getDefinition().setAttributeType("gender", gender);
       data.getDefinition().setAttributeType("zipcode", zipcode);*/
       
       data.getDefinition().setAttributeType("age", builder1);
       data.getDefinition().setAttributeType("gender", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       data.getDefinition().setAttributeType("zipcode", builder2);
       
       
       // Create an instance of the anonymizer
       ARXAnonymizer anonymizer = new ARXAnonymizer();
       ARXConfiguration config = ARXConfiguration.create();
       config.addPrivacyModel(new KAnonymity(3));
       config.setMaxOutliers(0d);
       config.setQualityModel(__MetricV2.createLossMetric());
       config.setSuppressionAlwaysEnabled(false);
       
   // dynamically determine data types for dataset. Would be done using while, for?
       System.out.println("Determining data types:");
       determineDataType(data.getHandle(), 0);
       determineDataType(data.getHandle(), 1);
       determineDataType(data.getHandle(), 2);
 
    // NDS-specific settings
       /*config.setMaxOutliers(1d); // Recommended default: 1d //commented for example 24
       config.setAttributeWeight("age", 0.5d); // attribute weight
       config.setAttributeWeight("gender", 0.3d); // attribute weight
       config.setAttributeWeight("zipcode", 0.5d); // attribute weight
       config.setQualityModel(Metric.createLossMetric(0.5d)); // suppression/generalization-factor*/
       
       // Execute the algorithm
       ARXResult result = anonymizer.anonymize(data, config);
       
       // Print input
       System.out.println(" - Input data:");
       print(data.getHandle().iterator());
       
    // Process results
       System.out.println(" - Example 25 ARXNode printing lattice:");
       for (ARXNode[] level : result.getLattice().getLevels()) {
           for (ARXNode node : level) {
               Iterator<String[]> transformed = result.getOutput(node, false).iterator();
               System.out.println("Transformation : "+Arrays.toString(node.getTransformation()));
               System.out.println("InformationLoss: "+node.getHighestScore());
               while (transformed.hasNext()) {
                   System.out.print("   ");
                   System.out.println(Arrays.toString(transformed.next()));
               }
           }
       }

       // Print input
       ////////System.out.println(" - Input research subset:");
       ////////print(data.getHandle().getView().iterator());
       // Obtain a handle for the transformed data
       //DataHandle outHandle = result.getOutput(false);
       //implicitly done to both handle objects
       //outHandle.sort(false, 2);
       
       /*System.out.println("outHandle rows is " + outHandle.getNumRows());
       System.out.println("outHandle columns is " + outHandle.getNumColumns());
       System.out.println("outHandle field name is " + outHandle.getAttributeName(0));
       System.out.println("outHandle value of the field is " + outHandle.getValue(0, 0));
       */
       
/// from here  
       // Print info
       printResult(result, data);

       ///////// allows access to the data's statistics
       
       
       //was just printing out the values
       

       // Print contingency tables
       StatisticsContingencyTable contingency;
       System.out.println(" - Contingency of attribute 'gender' and 'zipcode' in input:");
       contingency = data.getHandle().getStatistics().getContingencyTable(0, true, 2, true);
       System.out.println("   " + Arrays.toString(contingency.values1));
       System.out.println("   " + Arrays.toString(contingency.values2));
       while (contingency.iterator.hasNext()) {
           Entry e = contingency.iterator.next();
           System.out.println("   [" + e.value1 + ", " + e.value2 + ", " + e.frequency + "]");
       }

       // Print contingency tables
       System.out.println(" - Contingency of attribute 'gender' and 'zipcode' in output:");
       contingency = result.getOutput(false).getStatistics().getContingencyTable(0, true, 2, true);
       System.out.println("   " + Arrays.toString(contingency.values1));
       System.out.println("   " + Arrays.toString(contingency.values2));
       while (contingency.iterator.hasNext()) {
           Entry e = contingency.iterator.next();
           System.out.println("   [" + e.value1 + ", " + e.value2 + ", " + e.frequency + "]");
       }
       
       redactionBased();
       intervalBased();
       orderBased();
       ldlCholesterol();
       dates();
       loadStore();
/// to here seems to be a constant   
       
       aggregate(new String[]{"xaaa", "xxxbbb", "xxcccc"}, DataType.STRING);
       aggregate(new String[]{"1", "2", "5", "11", "12", "3"}, DataType.STRING);
       aggregate(new String[]{"1", "2", "5", "11", "12", "3"}, DataType.INTEGER);
       
//////////////////self-contained test, now for EXAMPLE 39
  /*     String[] features = new String[] {
               "sex",
               "age",
               "race",
               "marital-status",
               "education",
               "native-country",
               "workclass",
               "occupation",
               "salary-class"
       };

       String clazz = "marital-status";

       Data tData = createData("adult");
       //tData.getDefinition().setAttributeType("occupation", AttributeType.SENSITIVE_ATTRIBUTE);
       //example 39
       tData.getDefinition().setAttributeType("marital-status", AttributeType.INSENSITIVE_ATTRIBUTE);
       tData.getDefinition().setDataType("age", DataType.INTEGER);
       
       System.out.println("Input dataset for example 39");
       ///////////////////
       
       ARXAnonymizer anonymize = new ARXAnonymizer();
       ARXConfiguration conf = ARXConfiguration.create();
       //conf.addPrivacyModel(new EntropyLDiversity("occupation", 5));
       conf.addPrivacyModel(new KAnonymity(5));
       //conf.setMaxOutliers(0.04d);
       conf.setMaxOutliers(1d);
       conf.setQualityModel(Metric.createLossMetric());
       //conf.setQualityModel(Metric.createEntropyMetric());
       
       // Anonymize
       ARXResult res = anonymize.anonymize(tData, conf);
       System.out.println(" - Just after anonymizing using example 22...");
       //printResult(res, tData);
       System.out.println("5-anonymous dataset for example 39");
       System.out.println(res.getOutput().getStatistics().getClassificationPerformance(features, clazz, ARXLogisticRegressionConfiguration.create()));
*/
//////////////////self-contained test
       // Write results to file
       
       Data moreData = getMoreData();
       Hierarchy disease = getDisease();
       Hierarchy zipecod = getZipcode();
       Hierarchy agee = getAge();
       
       moreData.getDefinition().setAttributeType("zipcode", zipecod);
       moreData.getDefinition().setAttributeType("disease", AttributeType.SENSITIVE_ATTRIBUTE);
       moreData.getDefinition().setAttributeType("age", AttributeType.SENSITIVE_ATTRIBUTE);
       
    // Create an instance of the anonymizer
       ARXAnonymizer anon = new ARXAnonymizer();
       ARXConfiguration con = ARXConfiguration.create();
       con.addPrivacyModel(new KAnonymity(5));
       con.addPrivacyModel(new DistinctLDiversity("disease", 4));
       con.addPrivacyModel(new DistinctLDiversity("age", 6));
       con.setMaxOutliers(0.1d);
       con.setQualityModel(Metric.createEntropyMetric());

       // Now anonymize
       ARXResult resu = anon.anonymize(moreData, con);

       // Print info
       printResult(resu, moreData);

       // Process results
       if (resu.isResultAvailable()) {
           System.out.println(" - Transformed data for example 23 using distinct l diversity:");
           Iterator<String[]> transformed = resu.getOutput(false).iterator();
           while (transformed.hasNext()) {
               System.out.print("   ");
               System.out.println(Arrays.toString(transformed.next()));
           }
       }
       
/*///////////////////////////////////////////////////// creating a data object from a data source object example 28
       
       DataSource source = DataSource.createCSVSource("src/main/resources/templates/data/test.csv", StandardCharsets.UTF_8, ';', true);
       source.addColumn("age", DataType.INTEGER, true);			//dataset csv noted above has been removed
     
       Data sourceData = Data.create(source);
       
       sourceData.getDefinition().setAttributeType("age", getMoreAge());
       
       // Print to console
       print(sourceData.getHandle());
       System.out.println("\n");
       
       // Anonymize
       ARXAnonymizer anonymizerize = new ARXAnonymizer();
       ARXConfiguration configu = ARXConfiguration.create();
       configu.addPrivacyModel(new KAnonymity(3));
       configu.setMaxOutliers(0d);
       ARXResult resulting = anonymizerize.anonymize(sourceData, configu);
       
       // Print results
       System.out.println("Output from example 28, finally, written to file number 31:");
       print(resulting.getOutput(false));
       
*///////////////////////////////////////////////////////////////////////////////////
/*////////////////////////////////////////////////////////////
  ///////////////////////////////////// EXAMPLE 29
       //example 29, using the same dataset as others(the varying datasets is actually quite annoying to follow
       Data dataFor29 = getTheData();
       
       dataFor29.getDefinition().setAttributeType("age", getAgeHier());
       dataFor29.getDefinition().setAttributeType("gender", getGender());
       dataFor29.getDefinition().setAttributeType("zipcode", getZip());
       
    // Perform risk analysis
       System.out.println("\n - Input data");
       print(data.getHandle());
       System.out.println("\n - Quasi-identifiers sorted by risk:");
       analyzeAttributes(data.getHandle());
       System.out.println("\n - Risk analysis:");
       analyzeData(data.getHandle());
       
       // Create an instance of the anonymizer
       ARXAnonymizer anonymiz = new ARXAnonymizer();
       ARXConfiguration configuration = ARXConfiguration.create();
       configuration.addPrivacyModel(new AverageReidentificationRisk(0.5d));
       configuration.setMaxOutliers(1d);

       // Anonymize
       ARXResult answer = anonymizer.anonymize(dataFor29, configuration);

       // Perform risk analysis
       System.out.println("\n - Output data");
       print(answer.getOutput());
       System.out.println("\n - Risk analysis:");
       analyzeData(answer.getOutput());
//////////////////////////////////////////////////////////*/
     ////////////////////////////////////// EXAMPLE 30
      /* Data dataFor30 = getData30();
       
       // Print everything
       System.out.println("***************************");
       System.out.println("* Dumping the whole object*");
       System.out.println("***************************");
       System.out.println(dataFor30.getHandle().getStatistics().getSummaryStatistics(true));
       
       // Alter definition
       dataFor30.getDefinition().setDataType("age", DataType.INTEGER);
       dataFor30.getDefinition().setDataType("zipcode", DataType.DECIMAL);
       dataFor30.getDefinition().setDataType("date", DataType.DATE);

       // Print everything
       System.out.println("");
       System.out.println("***************************");
       System.out.println("* Dumping the whole object*");
       System.out.println("***************************");
       System.out.println(dataFor30.getHandle().getStatistics().getSummaryStatistics(true));
       
       // Access individual measures
       Map<String, StatisticsSummary<?>> statistics = dataFor30.getHandle().getStatistics().getSummaryStatistics(true);
       
       // For age
       System.out.println("");
       System.out.println("***************************");
       System.out.println("* Individual statistics   *");
       System.out.println("***************************");
       StatisticsSummary<Long> statisticsAge = (StatisticsSummary<Long>)statistics.get("age");
       if (statisticsAge.isGeometricMeanAvailable()) {
           System.out.println("Geometric mean of age");
           System.out.println(" - As double: " + statisticsAge.getGeometricMeanAsDouble());
           System.out.println(" - As value : " + statisticsAge.getGeometricMeanAsValue());
           System.out.println(" - As string: " + statisticsAge.getGeometricMeanAsString());
       }
       
       // For date
       System.out.println("");
       System.out.println("***************************");
       System.out.println("* Individual statistics   *");
       System.out.println("***************************");
       StatisticsSummary<Date> statisticsDate = (StatisticsSummary<Date>)statistics.get("date");
       if (statisticsDate.isSampleVarianceAvailable()) {
           System.out.println("Sample variance of date");
           System.out.println(" - As double: " + statisticsDate.getSampleVarianceAsDouble());
           System.out.println(" - As value : " + statisticsDate.getSampleVarianceAsValue());
           System.out.println(" - As string: " + statisticsDate.getSampleVarianceAsString());
       }
       if (statisticsDate.isArithmeticMeanAvailable()) {
           System.out.println("Arithmetic mean of date");
           System.out.println(" - As double: " + statisticsDate.getArithmeticMeanAsDouble());
           System.out.println(" - As value : " + statisticsDate.getArithmeticMeanAsValue());
           System.out.println(" - As string: " + statisticsDate.getArithmeticMeanAsString());
       }
       if (statisticsDate.isRangeAvailable()) {
           System.out.println("Range of date");
           System.out.println(" - As double: " + statisticsDate.getRangeAsDouble());
           System.out.println(" - As value : " + statisticsDate.getRangeAsValue());
           System.out.println(" - As string: " + statisticsDate.getRangeAsString());
       }
       */
       ////////////// EXAMPLE 31, now 32
       Data dataFor31 = getData30();
       
       dataFor31.getDefinition().setDataType("age", DataType.INTEGER);
       dataFor31.getDefinition().setDataType("zipcode", DataType.DECIMAL);
       dataFor31.getDefinition().setDataType("date", DataType.DATE);
       
       Hierarchy gend = getGender();
       
       Hierarchy zippie = getZippie();
       // additions from example 33
       /* *******************************
        * Define attribute types
        *********************************/
       dataFor31.getDefinition().setAttributeType("age", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       dataFor31.getDefinition().setAttributeType("gender", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       dataFor31.getDefinition().setAttributeType("zipcode", AttributeType.INSENSITIVE_ATTRIBUTE);
       dataFor31.getDefinition().setAttributeType("date", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);

       /* *******************************
        * Define transformation methods
        *********************************/
       
       
       //for 31
       //dataFor31.getDefinition().setAttributeType("age", MicroAggregationFunction.createGeometricMean());
       //dataFor31.getDefinition().setAttributeType("zipcode", zippie);
       //dataFor31.getDefinition().setAttributeType("date", MicroAggregationFunction.createArithmeticMean());
       //dataFor31.getDefinition().setAttributeType("zipcode", MicroAggregationFunction.createGeneralization());//added for 32! removed for 33       
	   //for 34?
       //dataFor31.getDefinition().setMicroAggregationFunction("age", MicroAggregationFunction.createMode());
	   //dataFor31.getDefinition().setMicroAggregationFunction("date", MicroAggregationFunction.createMedian());
	   //dataFor31.getDefinition().setAttributeType("gender", gend);
       //for 36
       dataFor31.getDefinition().setAttributeType("age", MicroAggregationFunction.createGeometricMean());
       dataFor31.getDefinition().setAttributeType("gender", gend);
       dataFor31.getDefinition().setAttributeType("zipcode", zippie);
       dataFor31.getDefinition().setAttributeType("date", MicroAggregationFunction.createArithmeticMean());
       
       // Create an instance of the anonymizer
       ARXAnonymizer anonymi = new ARXAnonymizer();
       ARXConfiguration configure = ARXConfiguration.create();
       //adding heuristic search via example 34
       configure.setHeuristicSearchEnabled(true);
       configure.setHeuristicSearchTimeLimit(10);
       configure.addPrivacyModel(new KAnonymity(2));
       //configure.setMaxOutliers(0.5d);
       configure.setMaxOutliers(1d);
       configure.setAttributeWeight("age", 100d);
       configure.setUtilityBasedMicroaggregation(true);
       configure.setQualityModel(Metric.createLossMetric());

       // Obtain result
       ARXResult outputs = anonymi.anonymize(dataFor31, configure);

       // Print info
       printResult(outputs, dataFor31);

       // Process results
       System.out.println(" - Transformed data for example 31:");
       Iterator<String[]> transformed = outputs.getOutput(false).iterator();
       while (transformed.hasNext()) {
           System.out.print("   ");
           System.out.println(Arrays.toString(transformed.next()));
       }
       
       ///////////////////// EXAMPLE 35
       /*Data.DefaultData dataFor35 = getData35();
       
       DataHandle handleFor35 = dataFor35.getHandle();
       
       HIPAAIdentifierMatch[] warnings = handleFor35.getRiskEstimator(ARXPopulationModel.create(Region.USA))
                                               .getHIPAAIdentifiers();
       System.out.println(" - HIPAAs Identified...");
       printWarnings(warnings);*/
       
       /////////// EXAMPLE 36, 37, 40
       /*String[] features40 = new String[] { // EXAMPLE 40
               "gender", "zipcode"
       };
       
       String clazz40 = "age";*/
       
       Data dataFor36 = getTheData();
       
       Hierarchy agie = getAgeHier();
       Hierarchy gendie = getGender();
       Hierarchy zips = getZip();
       dataFor36.getDefinition().setAttributeType("age", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       dataFor36.getDefinition().setAttributeType("gender", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       dataFor36.getDefinition().setAttributeType("zipcode", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       dataFor36.getDefinition().setHierarchy("age", agie);
       dataFor36.getDefinition().setHierarchy("gender", gendie);
       dataFor36.getDefinition().setHierarchy("zipcode", zips);
       
       /*System.out.println("Input dataset");

       Iterator<String[]> input = dataFor36.getHandle().iterator();
       while (input.hasNext()) {
           System.out.print("   ");
           System.out.println(Arrays.toString(input.next()));
       }*/

       //System.out.println(dataFor36.getHandle().getStatistics().getClassificationPerformance(features40, clazz40, ARXLogisticRegressionConfiguration.create()));
       
       // Create an instance of the anonymizer
       ARXAnonymizer anonymiza = new ARXAnonymizer();

       // Create a differential privacy criterion
       EDDifferentialPrivacy criterion = new EDDifferentialPrivacy(2d, 0.00001d,
       DataGeneralizationScheme.create(dataFor36,GeneralizationDegree.MEDIUM));
       
       ARXConfiguration configie = ARXConfiguration.create();
       //configie.addPrivacyModel(new KAnonymity(3));
       configie.addPrivacyModel(criterion);
       configie.setMaxOutliers(1d);
       ARXResult for36 = anonymiza.anonymize(dataFor36, config);
       
       /*System.out.println("3-anonymous dataset"); //for EXAMPLE 40

       Iterator<String[]> transformersInDisguise = for36.getOutput().iterator();
       while (transformersInDisguise.hasNext()) {
           System.out.print("   ");
           System.out.println(Arrays.toString(transformersInDisguise.next()));
       }*/
       
      // System.out.println("EXAMPLE 40 CLASSIFICATION PERFORMANCE");
      // System.out.println(for36.getOutput().getStatistics().getClassificationPerformance(features40, clazz40, ARXLogisticRegressionConfiguration.create()));    

       // Access output
       DataHandle optimal = for36.getOutput();

       System.out.println(for36.isResultAvailable());

       // Print input
       System.out.println(" - Input data:");
       printHandle(dataFor36.getHandle());

       System.out.println(" - Result:");
       printHandle(optimal);
       //// EXAMPLE 37
       
       ////// EXAMPLE 38
       Data dataFor38 = getData38();
       
       Hierarchy age38 = getAge38();
       Hierarchy zip38 = getZip38();
       Hierarchy gender38 = getGender();
       
       dataFor38.getDefinition().setAttributeType("age", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       dataFor38.getDefinition().setAttributeType("gender", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       dataFor38.getDefinition().setAttributeType("zipcode", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       dataFor38.getDefinition().setHierarchy("age", age38);
       dataFor38.getDefinition().setHierarchy("gender", gender38);
       dataFor38.getDefinition().setHierarchy("zipcode", zip38);

       // Create an instance of the anonymizer
       ARXAnonymizer anonymizer38 = new ARXAnonymizer();

       // Create a config which favors suppression over generalization
       ARXConfiguration config38 = ARXConfiguration.create();
       config38.addPrivacyModel(new KAnonymity(2));
       config38.setMaxOutliers(1d);
       config38.setQualityModel(Metric.createLossMetric(0.25d));

       // Print input
       System.out.println(" - Input data:");
       printHandle(dataFor38.getHandle());

       // Compute the result
       ARXResult result38 = anonymizer38.anonymize(dataFor38, config38);
       

       // Print result of global recoding
       DataHandle optimum38 = result38.getOutput();
       System.out.println(" - Global recoding:");
       printHandle(optimum38);

       try {     
           // Now apply local recoding to the result
           result38.optimizeIterative(optimum38, 0.05d, 100, 0.05d);

           // Print result of local recoding
           System.out.println(" - Local recoding:");
           printHandle(optimum38);
           
       } catch (RollbackRequiredException e) {
           // This part is important to ensure that privacy is preserved, even in case of exceptions
           optimum38 = result38.getOutput();
       }
///////////////////////////////// EXAMPLE 41
       //Data data41 = getData41();
       // EXAMPLE 43
       Data data43 = getData43(); 
       
       //Hierarchy zip41 = getZip41();
       Hierarchy zip43 = getZip();
       Hierarchy age43 = getAgeHier();
       //Hierarchy age41 = getAge41();
       Hierarchy nation41 = getNation41();
    // Define research subset
       //DataSubset subset = DataSubset.create(data41, new HashSet<Integer>(Arrays.asList(1, 2, 5, 7, 8)));
       //DataSubset subset = DataSubset.create(data43, new HashSet<Integer>(Arrays.asList(1, 2, 5)));
       
    // Set data attribute types, for example 41 and 42, commented out for 43
       /*data41.getDefinition().setAttributeType("identifier", AttributeType.IDENTIFYING_ATTRIBUTE);
       data41.getDefinition().setAttributeType("name", AttributeType.IDENTIFYING_ATTRIBUTE);
       data41.getDefinition().setAttributeType("zip", zip41);
       data41.getDefinition().setAttributeType("age", age41);
       data41.getDefinition().setAttributeType("nationality", nation41);
       data41.getDefinition().setAttributeType("sen", AttributeType.INSENSITIVE_ATTRIBUTE);

       // Create an instance of the anonymizer
       ARXAnonymizer anonymizer41 = new ARXAnonymizer();
       ARXConfiguration config41 = ARXConfiguration.create();
       config41.addPrivacyModel(new KMap(3, subset));
       //////////// added for 42
       config41.addPrivacyModel(new DPresence(1d / 2d, 2d / 3d, subset));
       //////////// added for 42 ^
       config41.setMaxOutliers(1d);
       config41.setQualityModel(Metric.createLossMetric());

       // Now anonymize
       ARXResult result41 = anonymizer41.anonymize(data41, config41);

       // Print input
       System.out.println(" - Input data:");
       print(data41.getHandle().iterator());

       // Print input
       System.out.println(" - Input research subset for example 41:");
       print(data41.getHandle().getView().iterator());

       // Print info
       printResult(result41, data41);

       // Print results
       System.out.println(" - Transformed data for example 41:");
       print(result41.getOutput(false).iterator());

       // Print results
       System.out.println(" - Transformed research subset for 41 and 42:");
       print(result41.getOutput(false).getView().iterator());*/
       
       data43.getDefinition().setAttributeType("age", age43);
       data43.getDefinition().setAttributeType("gender", gendie);
       data43.getDefinition().setAttributeType("zipcode", zip43);
       
       /*ARXAnonymizer anonymizer43 = new ARXAnonymizer(); // EXAMPLE 43 does all of the risk statistics
       ARXConfiguration config43 = ARXConfiguration.create();
       config43.addPrivacyModel(new Inclusion(subset));
       config43.addPrivacyModel(new KAnonymity(2));
       config43.setMaxOutliers(1d);

       // Anonymize
       ARXResult result43 = anonymizer43.anonymize(data43, config43);

       // Perform risk analysis
       System.out.println("\n - Input data");
       print(data43.getHandle().getView());
       System.out.println("\n - Risk analysis:");
       analyzeData(data43.getHandle());
       
       // Perform risk analysis
       System.out.println("\n - Output data");
       print(result43.getOutput().getView());
       System.out.println("\n - Risk analysis:");
       analyzeData(result43.getOutput());*/
       
       /////////////////////////// EXAMPLE 44 risk models
       /*ARXPopulationModel populationmodel44 = ARXPopulationModel.create(data.getHandle().getNumRows(), 0.01d);
       
       // Create an instance of the anonymizer
       ARXAnonymizer anonymizer44 = new ARXAnonymizer();
       ARXConfiguration config44 = ARXConfiguration.create();
       config44.addPrivacyModel(new KMap(50, 0.1d, populationmodel44));
       config44.setMaxOutliers(1d);
       
       // Anonymize
       ARXResult result44 = anonymizer44.anonymize(data43, config44);
       
       // Perform risk analysis
       System.out.println("- Output data");
       print(result44.getOutput());*/
       
       ARXPopulationModel populationmodel45 = ARXPopulationModel.create(data43.getHandle().getNumRows(), 0.01d);
       
       // Create an instance of the anonymizer
       ARXAnonymizer anonymizer45 = new ARXAnonymizer();
       ARXConfiguration config45 = ARXConfiguration.create();
       config45.addPrivacyModel(new KMap(5, 0.1d, populationmodel45));
       config45.setMaxOutliers(1d);
       
       // Anonymize
       ARXResult result45 = anonymizer45.anonymize(data43, config45);

       // Perform risk analysis
       
       System.out.println("- Input data");
       print(data43.getHandle());
       System.out.print("\n- Records at 50% risk: " + data43.getHandle().getRiskEstimator(populationmodel45).getSampleBasedRiskDistribution().getFractionOfRecordsAtRisk(0.5d));
       System.out.println("\n- Records at <=50% risk: " + data43.getHandle().getRiskEstimator(populationmodel45).getSampleBasedRiskDistribution().getFractionOfRecordsAtCumulativeRisk(0.5d));
       
       // Perform risk analysis
       System.out.println("\n- Output data");
       print(result45.getOutput());
       System.out.print("\n- Records at 50% risk: " + result45.getOutput().getRiskEstimator(populationmodel45).getSampleBasedRiskDistribution().getFractionOfRecordsAtRisk(0.5d));
       System.out.print("\n- Records at <=50% risk: " + result45.getOutput().getRiskEstimator(populationmodel45).getSampleBasedRiskDistribution().getFractionOfRecordsAtCumulativeRisk(0.5d));

       
       // EXAMPLE 45 risk models
       /*System.out.println("- Input data");
       print(data43.getHandle());
       System.out.println("\n- Mixed risks");
       System.out.println("  * Prosecutor re-identification risk: " + data43.getHandle().getRiskEstimator(populationmodel45).getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk());
       System.out.println("  * Journalist re-identification risk: " + data43.getHandle().getRiskEstimator(populationmodel45).getSampleBasedReidentificationRisk().getEstimatedJournalistRisk());
       System.out.println("  * Marketer re-identification risk: " + data43.getHandle().getRiskEstimator(populationmodel45).getSampleBasedReidentificationRisk().getEstimatedMarketerRisk());
       
       // Perform risk analysis
       System.out.println("- Output data");
       print(result45.getOutput());
       System.out.println("\n- Mixed risks");
       System.out.println("  * Prosecutor re-identification risk: " + result45.getOutput().getRiskEstimator(populationmodel45).getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk());
       System.out.println("  * Journalist re-identification risk: " + result45.getOutput().getRiskEstimator(populationmodel45).getSampleBasedReidentificationRisk().getEstimatedJournalistRisk());
       System.out.println("  * Marketer re-identification risk: " + result45.getOutput().getRiskEstimator(populationmodel45).getSampleBasedReidentificationRisk().getEstimatedMarketerRisk());
		*/
       
       Data data46 = loadData();

       // Flag every attribute as quasi identifier
       for (int i = 0; i < data46.getHandle().getNumColumns(); i++) {
           data46.getDefinition().setAttributeType(data46.getHandle().getAttributeName(i), AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       }

       // Perform risk analysis
       System.out.println("\n - Input data for 46");
       print(data46.getHandle());

       System.out.println("\n - Quasi-identifiers with values (in percent) for 46:");
       analyzeAttributes(data46.getHandle());
       
       ///////////////////////////////////////////////////
       // EXAMPLE 48 IS USEFUL
       ///////////////////////////////////////////////////
       
       
       
       //////////////////////////////////////////
       // EXAMPLE 49 HERE
       //////////////////////////////////////////
       // EXAMPLE 50 HERE
       //////////////////////////////////////////
       
       /*Data data49 = createData("adult");// used for EXAMPLE 49, 50, 51 
       
       // added through example 50, removed for EXAMPLE 51 to improve computation time
       DataSubset subset = DataSubset.create(data49, DataSelector.create(data49).field("sex").equals("Male"));
        
        // Config from PLOS|ONE paper
        solve(data49, ARXCostBenefitConfiguration.create()
                                               .setAdversaryCost(4d)
                                               .setAdversaryGain(300d)
                                               .setPublisherLoss(300d)
                                               .setPublisherBenefit(1200d), subset);

        // Larger publisher loss
        solve(data49, ARXCostBenefitConfiguration.create()
                                               .setAdversaryCost(4d)
                                               .setAdversaryGain(300d)
                                               .setPublisherLoss(600d)
                                               .setPublisherBenefit(1200d), subset);

        // Even larger publisher loss
        solve(data49, ARXCostBenefitConfiguration.create()
                                               .setAdversaryCost(4d)
                                               .setAdversaryGain(300d)
                                               .setPublisherLoss(1200d)
                                               .setPublisherBenefit(1200d), subset);

        // Larger publisher loss and less adversary costs
        solve(data49, ARXCostBenefitConfiguration.create()
                                               .setAdversaryCost(2d)
                                               .setAdversaryGain(300d)
                                               .setPublisherLoss(600d)
											   .setPublisherBenefit(1200d), subset); // */
       
       // Config from PLOS|ONE paper
       // From another example
        /* solve(data49, ARXCostBenefitConfiguration.create()
                                              .setAdversaryCost(4d)
                                              .setAdversaryGain(300d)
                                              .setPublisherLoss(300d)
                                              .setPublisherBenefit(1200d));

       // Larger publisher loss
       solve(data49, ARXCostBenefitConfiguration.create()
                                              .setAdversaryCost(4d)
                                              .setAdversaryGain(300d)
                                              .setPublisherLoss(600d)
                                              .setPublisherBenefit(1200d));

       // Even larger publisher loss
       solve(data49, ARXCostBenefitConfiguration.create()
                                              .setAdversaryCost(4d)
                                              .setAdversaryGain(300d)
                                              .setPublisherLoss(1200d)
                                              .setPublisherBenefit(1200d));

       // Larger publisher loss and less adversary costs
       solve(data49, ARXCostBenefitConfiguration.create()
                                              .setAdversaryCost(2d)
                                              .setAdversaryGain(300d)
                                              .setPublisherLoss(600d)
                                              .setPublisherBenefit(1200d));
       
       // Config from PLOS|ONE paper but publisher loss and benefit changed with each other
       solve(data49, ARXCostBenefitConfiguration.create()
                                              .setAdversaryCost(4d)
                                              .setAdversaryGain(300d)
                                              .setPublisherLoss(1200d)
                                              .setPublisherBenefit(300d)); // */
       
       System.out.println("Just after the solve functions");
       //////////////////////////////////////////////////// ARX EXAMPLE 53
       /*
       Data data53 = createData("adult");
       
       ARXAnonymizer anonymizer53 = new ARXAnonymizer();
       ARXConfiguration config53 = ARXConfiguration.create();
       config53.addPrivacyModel(new KAnonymity(5));
       config53.setMaxOutliers(1d);
       config53.setQualityModel(Metric.createLossMetric());
       
       ARXResult result53 = anonymizer53.anonymize(data53, config53);

       //printResult(result53, data53);
       System.out.println("Just after printResult 53");
       // Create certificate
       ARXCertificate certificate = ARXCertificate.create(data53.getHandle(), data53.getDefinition(),
                                                         config53, result53, result53.getGlobalOptimum(), 
                                                         result53.getOutput(),
                                                         new CSVSyntax());

       if(certificate == null)
       {
    	   System.out.println("certificate is null");
       }
       System.out.println("After creating the certificate thing");
       File file = File.createTempFile("arx", ".pdf");
       System.out.println("After creating the file");
       certificate.save(file);
       System.out.println("After saving the file");
       
       // Open
       if (Desktop.isDesktopSupported()) {
    	   System.out.println("Trying to open the file");
           Desktop.getDesktop().open(file);
           System.out.println("After opening the file");
       }
       */
       //////////////////////////////////////////////////// ARX EXAMPLE 53

       //////////////////////////////////////////////////// ARX EXAMPLE 54
       /*
       Data data54 = createData("adult");
       
       data54.getDefinition().setDataType("age", DataType.INTEGER);
       data54.getDefinition().setMicroAggregationFunction("age", MicroAggregationFunction.createArithmeticMean());
       
       ARXAnonymizer anonymizer54 = new ARXAnonymizer();
       ARXConfiguration config54 = ARXConfiguration.create();
       config54.addPrivacyModel(new KAnonymity(5));
       config54.setMaxOutliers(1d);
       config54.setQualityModel(Metric.createLossMetric(0d));
       
       ARXResult result54 = anonymizer54.anonymize(data54, config54);
       DataHandle output54 = result54.getOutput();
       result54.optimizeIterativeFast(output54, 0.01d);
       System.out.println("Done optimizing");

       // Access statistics
       StatisticsQuality utility = data54.getHandle().getStatistics().getQualityStatistics();
       System.out.println("Input:");
       System.out.println(" - Ambiguity: " + utility.getAmbiguity().getValue());
       System.out.println(" - AECS: " + utility.getAverageClassSize().getValue());
       System.out.println(" - Discernibility: " + utility.getDiscernibility().getValue());
       System.out.println(" - Granularity: " + utility.getGranularity().getArithmeticMean(false));
       System.out.println(" - Attribute-level SE: " + utility.getAttributeLevelSquaredError().getArithmeticMean(false));
       System.out.println(" - KL-Divergence: " + utility.getKullbackLeiblerDivergence().getValue());
       System.out.println(" - Non-Uniform Entropy: " + utility.getNonUniformEntropy().getArithmeticMean(false));
       System.out.println(" - Precision: " + utility.getGeneralizationIntensity().getArithmeticMean(false));
       System.out.println(" - Record-level SE: " + utility.getRecordLevelSquaredError().getValue());
       
       // Access statistics
       utility = output54.getStatistics().getQualityStatistics();
       System.out.println("Output:");
       System.out.println(" - Ambiguity: " + utility.getAmbiguity().getValue());
       System.out.println(" - AECS: " + utility.getAverageClassSize().getValue());
       System.out.println(" - Discernibility: " + utility.getDiscernibility().getValue());
       System.out.println(" - Granularity: " + utility.getGranularity().getArithmeticMean(false));
       System.out.println(" - MSE: " + utility.getAttributeLevelSquaredError().getArithmeticMean(false));
       System.out.println(" - KL-Divergence: " + utility.getKullbackLeiblerDivergence().getValue());
       System.out.println(" - Non-Uniform Entropy: " + utility.getNonUniformEntropy().getArithmeticMean(false));
       System.out.println(" - Precision: " + utility.getGeneralizationIntensity().getArithmeticMean(false));
       System.out.println(" - SSE: " + utility.getRecordLevelSquaredError().getValue());
       */
       //////////////////////////////////////////////////// ARX EXAMPLE 54
       
       ////////////////////////////////////////////////////ARX EXAMPLE 55
       Data data55 = getData38();
       
       Hierarchy age55 = getAge38();
       
       Hierarchy gend55 = getGender(); 
       
       Hierarchy zip55 = getZip38();
       
       data55.getDefinition().setAttributeType("age", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       data55.getDefinition().setAttributeType("gender", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       data55.getDefinition().setAttributeType("zipcode", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
       data55.getDefinition().setHierarchy("age", age55);
       data55.getDefinition().setHierarchy("gender", gend55);
       data55.getDefinition().setHierarchy("zipcode", zip55);

       // Create an instance of the anonymizer
       ARXAnonymizer anonymizer55 = new ARXAnonymizer();

       // Define relative number of records to be generalized in each iteration
       double oMin = 0.01d;
       
       // Define a parameter for the quality model which only considers generalization
       double gsFactor = 0d;
       
       // Create a configuration
       ARXConfiguration config55 = ARXConfiguration.create();
       config55.addPrivacyModel(new KAnonymity(2));
       config55.setMaxOutliers(1d - oMin);
       config55.setQualityModel(Metric.createLossMetric(gsFactor));

       // Print input
       System.out.println(" - Input data:");
       printHandle(data55.getHandle());

       // Compute the result
       ARXResult result55 = anonymizer55.anonymize(data55, config55);
       

       // Print result of global recoding
       DataHandle optimum = result55.getOutput();
       System.out.println(" - Global recoding for:");
       printHandle(optimum);

       try {
           
           // Now apply local recoding to the result
           result55.optimizeIterativeFast(optimum, oMin);

           // Print result of local recoding
           System.out.println(" - Local recoding for 55:");
           printHandle(optimum);
           
       } catch (RollbackRequiredException e) {
           
           // This part is important to ensure that privacy is preserved, even in case of exceptions
           optimum = result55.getOutput();
       }
       
       System.out.print(" - Writing data...");
       //result45.getOutput(false).save("src/main/resources/templates/output/test_anonymized46.csv", ';');
       System.out.println("Done!");
       
      return "anonymize";
   }
   
/**************************************************************************
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *    end of main method
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 **************************************************************************/  
   
   
   
   private static void solve(Data data, ARXCostBenefitConfiguration config) throws IOException {
       
       // Release
       data.getHandle().release();
       
       // Configure
       ARXConfiguration arxconfig = ARXConfiguration.create();
       arxconfig.setCostBenefitConfiguration(config);
       
       // Create model for measuring publisher's benefit
       MetricSDNMPublisherPayout maximizePublisherPayout = Metric.createPublisherPayoutMetric(false);
       
       // Create privacy model for the game-theoretic approach
       //		Here there can be several different privacy models
       // EXAMPLE 50
       //ProfitabilityProsecutorNoAttack profitability = new ProfitabilityProsecutorNoAttack();
       // EXAMPLE 51
       ProfitabilityProsecutor profitability = new ProfitabilityProsecutor();
       
       // Configure ARX
       arxconfig.setMaxOutliers(1d);
       arxconfig.setQualityModel(maximizePublisherPayout);
       arxconfig.addPrivacyModel(profitability);

       // Anonymize
       ARXAnonymizer anonymizer = new ARXAnonymizer();
       ARXResult result = anonymizer.anonymize(data, arxconfig);
       ARXNode node = result.getGlobalOptimum();
       DataHandle handle = result.getOutput(node, false).getView();
       
       // Print stuff
       System.out.println("Data: " + data.getHandle().getView().getNumRows() + " records with " + data.getDefinition().getQuasiIdentifyingAttributes().size() + " quasi-identifiers");
       System.out.println(" - Configuration: " + config.toString());
       System.out.println(" - Policies available: " + result.getLattice().getSize());
       System.out.println(" - Solution: " + Arrays.toString(node.getTransformation()));
       System.out.println("   * Optimal: " + result.getLattice().isComplete());
       System.out.println("   * Time needed: " + result.getTime() + "[ms]");
       for (QualityMetadata<?> metadata : node.getLowestScore().getMetadata()) {
           System.out.println("   * " + metadata.getParameter() + ": " + metadata.getValue());
       }
       System.out.println("   * Suppressed records: " + handle.getStatistics().getEquivalenceClassStatistics().getNumberOfOutlyingTuples());

   }
   
   //////////////// example 50
   private static void solve(Data data, ARXCostBenefitConfiguration config, DataSubset subset) throws IOException {
       
       // Release
       data.getHandle().release();
       
       // Configure
       ARXConfiguration arxconfig = ARXConfiguration.create();
       arxconfig.setCostBenefitConfiguration(config);
       
       // Create model for measuring publisher's benefit
       MetricSDNMPublisherPayout maximizePublisherPayout = Metric.createPublisherPayoutMetric(true);
       
       // Create privacy model for the game-theoretic approach
       //EXAMPLE 49, 52
       //ProfitabilityJournalistNoAttack profitability = new ProfitabilityJournalistNoAttack(subset);
       ProfitabilityJournalist profitability = new ProfitabilityJournalist(subset);
       
       // Configure ARX
       arxconfig.setMaxOutliers(1d);
       arxconfig.setQualityModel(maximizePublisherPayout);
       arxconfig.addPrivacyModel(profitability);

       // Anonymize
       ARXAnonymizer anonymizer = new ARXAnonymizer();
       ARXResult result = anonymizer.anonymize(data, arxconfig);
       ARXNode node = result.getGlobalOptimum();
       DataHandle handle = result.getOutput(node, false).getView();
       
       // Print stuff
       System.out.println("Data example 50: " + data.getHandle().getView().getNumRows() + " records with " + data.getDefinition().getQuasiIdentifyingAttributes().size() + " quasi-identifiers");
       System.out.println(" - Configuration: " + config.toString());
       System.out.println(" - Policies available: " + result.getLattice().getSize());
       System.out.println(" - Solution: " + Arrays.toString(node.getTransformation()));
       System.out.println("   * Optimal: " + result.getLattice().isComplete());
       System.out.println("   * Time needed: " + result.getTime() + "[ms]");
       for (QualityMetadata<?> metadata : node.getLowestScore().getMetadata()) {
           System.out.println("   * " + metadata.getParameter() + ": " + metadata.getValue());
       }
       System.out.println("   * Suppressed records: " + handle.getStatistics().getEquivalenceClassStatistics().getNumberOfOutlyingTuples());

   }
   
   
   
//take in file through variable means 
   // used for EXAMPLE 39
   
   /**
    * Perform risk analysis FROM EXAMPLE NOT 46
    * @param handle
    */
  /* private static void analyzeAttributes(DataHandle handle) {
       ARXPopulationModel populationmodel = ARXPopulationModel.create(Region.USA);
       RiskEstimateBuilder builder = handle.getRiskEstimator(populationmodel);
       RiskModelAttributes riskmodel = builder.getAttributeRisks();
       for (QuasiIdentifierRisk risk : riskmodel.getAttributeRisks()) {
           System.out.println("   * Distinction: " + risk.getDistinction() + ", Separation: " + risk.getSeparation() + ", Identifier: " + risk.getIdentifier());
       }
   }*/
       
  /* /**
    * Perform risk analysis
    * @param handle
    /
   private static void analyzeData(DataHandle handle) {
       
       ARXPopulationModel populationmodel = ARXPopulationModel.create(Region.USA);
       RiskEstimateBuilder builder = handle.getRiskEstimator(populationmodel);
       RiskModelHistogram classes = builder.getEquivalenceClassModel();
       RiskModelSampleRisks sampleReidentifiationRisk = builder.getSampleBasedReidentificationRisk();
       RiskModelSampleUniqueness sampleUniqueness = builder.getSampleBasedUniquenessRisk();
       RiskModelPopulationUniqueness populationUniqueness = builder.getPopulationBasedUniquenessRisk();
       
       int[] histogram = classes.getHistogram();
       
       System.out.println("   * Equivalence classes:");
       System.out.println("     - Average size: " + classes.getAvgClassSize());
       System.out.println("     - Num classes : " + classes.getNumClasses());
       System.out.println("     - Histogram   :");
       for (int i = 0; i < histogram.length; i += 2) {
           System.out.println("        [Size: " + histogram[i] + ", count: " + histogram[i + 1] + "]");
       }
       System.out.println("   * Risk estimates:");
       System.out.println("     - Sample-based measures");
       System.out.println("       + Average risk     : " + sampleReidentifiationRisk.getAverageRisk());
       System.out.println("       + Lowest risk      : " + sampleReidentifiationRisk.getLowestRisk());
       System.out.println("       + Tuples affected  : " + sampleReidentifiationRisk.getFractionOfTuplesAffectedByLowestRisk());
       System.out.println("       + Highest risk     : " + sampleReidentifiationRisk.getHighestRisk());
       System.out.println("       + Tuples affected  : " + sampleReidentifiationRisk.getFractionOfTuplesAffectedByHighestRisk());
       System.out.println("       + Sample uniqueness: " + sampleUniqueness.getFractionOfUniqueTuples());
       System.out.println("     - Population-based measures");
       System.out.println("       + Population unqiueness (Zayatz): " + populationUniqueness.getFractionOfUniqueTuples(PopulationUniquenessModel.ZAYATZ));
   }
   */

   
   ///////// exaMPLE 46
   private static void analyzeAttributes(DataHandle handle) {
       ARXPopulationModel populationmodel = ARXPopulationModel.create(ARXPopulationModel.Region.USA);
       RiskEstimateBuilder builder = handle.getRiskEstimator(populationmodel);
       RiskModelAttributes riskmodel = builder.getAttributeRisks();

       // output
       printPrettyTable(riskmodel.getAttributeRisks());
   }
   
   private static Data loadData() {
       // Define data
       Data.DefaultData data = Data.create();
       data.add("age", "sex", "state");
       data.add("20", "Female", "CA");
       data.add("30", "Female", "CA");
       data.add("40", "Female", "TX");
       data.add("20", "Male", "NY");
       data.add("40", "Male", "CA");
       data.add("53", "Male", "CA");
       data.add("76", "Male", "EU");
       data.add("40", "Female", "AS");
       data.add("32", "Female", "CA");
       data.add("88", "Male", "CA");
       data.add("48", "Female", "AS");
       data.add("76", "Male", "UU");
       return data;
   }
   
   private static void printPrettyTable(RiskModelAttributes.QuasiIdentifierRisk[] quasiIdentifiers) {
       
       // get char count of longest quasi-identifier
       int charCountLongestQi = quasiIdentifiers[quasiIdentifiers.length-1].getIdentifier().toString().length();

       // make sure that there is enough space for the table header strings
       charCountLongestQi = Math.max(charCountLongestQi, 12);

       // calculate space needed
       String leftAlignFormat = "| %-" + charCountLongestQi + "s | %13.2f | %12.2f |%n";

       // add 2 spaces that are in the string above on the left and right side of the first pattern
       charCountLongestQi += 2;

       // subtract the char count of the column header string to calculate
       // how many spaces we need for filling up to the right columnborder
       int spacesAfterColumHeader = charCountLongestQi - 12;

       System.out.format("+" + StringUtils.repeat("-", charCountLongestQi) + "+---------------+--------------+%n");
       System.out.format("| Identifier " + StringUtils.repeat(" ", spacesAfterColumHeader) + "|   Distinction |   Separation |%n");
       System.out.format("+" + StringUtils.repeat("-", charCountLongestQi) + "+---------------+--------------+%n");
       for (RiskModelAttributes.QuasiIdentifierRisk quasiIdentifier : quasiIdentifiers) {
           // print every Quasi-Identifier
           System.out.format(leftAlignFormat, quasiIdentifier.getIdentifier(), quasiIdentifier.getDistinction() * 100, quasiIdentifier.getSeparation() * 100);
       }
       System.out.format("+" + StringUtils.repeat("-", charCountLongestQi) + "+---------------+--------------+%n");
   	}
   
   
   /**
    * Prints a list of matching data types
    * @param handle
    * @param column
    */
   private static DataType<?> determineDataType(DataHandle handle, int column) {
       //System.out.println(" - Potential data types for attribute: "+handle.getAttributeName(column));
       List<Pair<DataType<?>, Double>> types = handle.getMatchingDataTypes(column);

       // Print entries sorted by match percentage
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
   
   
   private static void aggregate(String[] args, DataType<?> type){
       
       AggregateFunctionBuilder<?> builder = type.createAggregate();
       System.out.println("Input: "+Arrays.toString(args) + " as "+type.getDescription().getLabel()+"s");
       System.out.println(" - Set                         :"+builder.createSetFunction().aggregate(args));
       System.out.println(" - Set of prefixes             :"+builder.createSetOfPrefixesFunction().aggregate(args));
       System.out.println(" - Set of prefixes of length 2 :"+builder.createSetOfPrefixesFunction(2).aggregate(args));
       System.out.println(" - Set of prefixes of length 3 :"+builder.createSetOfPrefixesFunction(3).aggregate(args));
       System.out.println(" - Common prefix               :"+builder.createPrefixFunction().aggregate(args));
       System.out.println(" - Common prefix with redaction:"+builder.createPrefixFunction('*').aggregate(args));
       System.out.println(" - Bounds                      :"+builder.createBoundsFunction().aggregate(args));
       System.out.println(" - Interval                    :"+builder.createIntervalFunction().aggregate(args));
       System.out.println();
   }
   
   private static Data getData() {
       DefaultData data = Data.create();
       data.add("identifier", "name", "zip", "age", "nationality", "sen");
       data.add("a", "Alice", "47906", "35", "USA", "1.1.2013");
       data.add("b", "Bob", "47903", "59", "Canada", "1.1.2013");
       data.add("c", "Christine", "47906", "42", "USA", "1.1.2013");
       data.add("d", "Dirk", "47630", "18", "Brazil", "1.1.2013");
       data.add("e", "Eunice", "47630", "22", "Brazil", "1.1.2013");
       data.add("f", "Frank", "47633", "63", "Peru", "1.1.2013");
       data.add("g", "Gail", "48973", "33", "Spain", "1.1.2013");
       data.add("h", "Harry", "48972", "47", "Bulgaria", "1.1.2013");
       data.add("i", "Iris", "48970", "52", "France", "1.1.2013");
       return data;
   }
   
   private static Data getData30() { //also for example 31
	   DefaultData data = Data.create();
       data.add("age", "gender", "zipcode", "date");
       data.add("45", "female", "81675", "01.01.1982");
       data.add("34", "male", "81667", "11.05.1982");
       data.add("NULL", "male", "81925", "31.08.1982");
       data.add("70", "female", "81931", "02.07.1982");
       data.add("34", "female", "NULL", "05.01.1982");
       data.add("70", "male", "81931", "24.03.1982");
       data.add("45", "male", "81931", "NULL");
       return data;
   }
   ///////////////////// for example 35
   private static Data.DefaultData getData35() {
       Data.DefaultData data = Data.create();
       data.add("first name", "age", "gender", "code", "birth", "email-address", "SSN", "Bank", "Vehicle", "URL", "IP", "phone");
       data.add("Max", "34", "male", "81667", "2008-09-02", "", "123-45-6789", "GR16 0110 1250 0000 0001 2300 695", "", "http://demodomain.com", "8.8.8.8", "+49 1234566");
       data.add("Max", "45", "female", "81675", "2008-09-02", "user@arx.org", "", "", "WDD 169 007-1J-236589", "", "2001:db8::1428:57ab", "");
       data.add("Max", "66", "male", "89375", "2008-09-02", "demo@email.com", "", "", "", "", "", "");
       data.add("Max", "70", "female", "81931", "2008-09-02", "", "", "", "", "", "", "");
       data.add("Max", "34", "female", "81931", "2008-09-02", "", "", "", "", "", "", "");
       data.add("Max", "90", "male", "81931", "2008-09-02", "", "", "", "", "", "", "");
       data.add("Max", "45", "male", "81931", "2008-09-02", "", "", "", "", "", "", "");
       return data;
   }
   
   private static Data.DefaultData getData38() {
	   final DefaultData data = Data.create();
	   data.add("age", "gender", "zipcode");
	   data.add("34", "male", "81667");
	   data.add("45", "female", "81675");
	   data.add("66", "male", "81925");
	   data.add("70", "female", "81931");
	   data.add("73", "male", "92922");
	   data.add("34", "female", "81931");
	   data.add("70", "male", "81931");
	   data.add("45", "male", "81931");
	   return data;
   }
   
   private static Data.DefaultData getData43() {
	   DefaultData data = Data.create();
       data.add("age", "gender", "zipcode");
       data.add("45", "female", "81675");
       data.add("34", "male", "81667");
       data.add("66", "male", "81925");
       data.add("70", "female", "81931");
       data.add("34", "female", "81931");
       data.add("70", "male", "81931");
       data.add("45", "male", "81931");
       return data;
   }
   
   private static Data getData41() {
	   DefaultData data = Data.create();
       data.add("identifier", "name", "zip", "age", "nationality", "sen");
       data.add("a", "Alice", "47906", "35", "USA", "0");
       data.add("b", "Bob", "47903", "59", "Canada", "1");
       data.add("c", "Christine", "47906", "42", "USA", "1");
       data.add("d", "Dirk", "47630", "18", "Brazil", "0");
       data.add("e", "Eunice", "47630", "22", "Brazil", "0");
       data.add("f", "Frank", "47633", "63", "Peru", "1");
       data.add("g", "Gail", "48973", "33", "Spain", "0");
       data.add("h", "Harry", "48972", "47", "Bulgaria", "1");
       data.add("i", "Iris", "48970", "52", "France", "1");
       return data;
   }
   
   private static void printWarnings(HIPAAIdentifierMatch[] warnings) {
       if (warnings.length == 0) {
           System.out.println("No warnings");
       } else {
           for (HIPAAIdentifierMatch w : warnings) {
               System.out.println(w.toString());
           }
       }
   }
   //////////////////////////// for example 35, 40

   private static Data getTheData() {
       DefaultData data = Data.create();
       data.add("age", "gender", "zipcode");
       data.add("34", "male", "81667");
       data.add("45", "female", "81675");
       data.add("66", "male", "81925");
       data.add("70", "female", "81931");
       data.add("34", "female", "81931");
       data.add("70", "male", "81931");
       data.add("45", "male", "81931");
       return data;
   }
   
   private static Data getMoreData() {
	   DefaultData data = Data.create();
       data.add("zipcode", "age", "disease");
       data.add("47677", "22", "gastric ulcer");
       data.add("47602", "23", "gastritis");
       data.add("47678", "24", "stomach cancer");
       data.add("47605", "25", "bronchitis");
       data.add("47607", "26", "pneumonia");
       data.add("47673", "26", "pneumonia");
       data.add("47905", "23", "gastritis");
       data.add("47909", "27", "flu");
       data.add("47906", "25", "bronchitis");
       return data;
   }
   
   private static Hierarchy getGender() {
	   DefaultHierarchy gender = Hierarchy.create();
       gender.add("male", "*");
       gender.add("female", "*");
       return gender;
   }
   
   private static Hierarchy getAgeHier() {
	   DefaultHierarchy age = Hierarchy.create();
	   age.add("34", "<50", "*");
	   age.add("45", "<50", "*");
	   age.add("66", ">=50", "*");
	   age.add("70", ">=50", "*");
	   return age;
   }
   
   private static Hierarchy getAge38() {
	   DefaultHierarchy age = Hierarchy.create();
	   age.add("34", "<50", "*");
	   age.add("45", "<50", "*");
	   age.add("66", ">=50", "*");
	   age.add("70", ">=50", "*");
	   age.add("73", ">=50", "*");
	   return age;
   }
   
   private static Hierarchy getMoreAge() {
	   DefaultHierarchy age = Hierarchy.create();
       age.add("34", "<50", "*");
       age.add("45", "<50", "*");
       age.add("66", ">=50", "*");
       age.add("70", ">=50", "*");
       age.add("99", ">=50", "*");
       age.add("NULL", "NULL", "*");
	   return age;
   }
   
   private static Hierarchy getAge() {
	   DefaultHierarchy age = Hierarchy.create();
       age.add("29", "<=40", "*");
       age.add("22", "<=40", "*");
       age.add("27", "<=40", "*");
       age.add("43", ">40", "*");
       age.add("52", ">40", "*");
       age.add("47", ">40", "*");
       age.add("30", "<=40", "*");
       age.add("36", "<=40", "*");
       age.add("32", "<=40", "*");
       return age;
   }
   
   private static Hierarchy getAge41() {
	   DefaultHierarchy age = Hierarchy.create();
	   age.add("18", "1*", "<=40", "*");
	   age.add("22", "2*", "<=40", "*");
	   age.add("33", "3*", "<=40", "*");
	   age.add("35", "3*", "<=40", "*");
	   age.add("42", "4*", ">40", "*");
	   age.add("47", "4*", ">40", "*");
	   age.add("52", "5*", ">40", "*");
	   age.add("59", "5*", ">40", "*");
	   age.add("63", "6*", ">40", "*");
	   return age;
   }
   
   private static Hierarchy getZip() {
	   DefaultHierarchy zipcode = Hierarchy.create();
	   zipcode.add("81667", "8166*", "816**", "81***", "8****", "*****");
       zipcode.add("81675", "8167*", "816**", "81***", "8****", "*****");
       zipcode.add("81925", "8192*", "819**", "81***", "8****", "*****");
       zipcode.add("81931", "8193*", "819**", "81***", "8****", "*****");
	   return zipcode;
   }
   
   private static Hierarchy getZipcode() {
	   DefaultHierarchy zipcode = Hierarchy.create();
       zipcode.add("47677", "4767*", "476**", "47***", "4****", "*****");
       zipcode.add("47602", "4760*", "476**", "47***", "4****", "*****");
       zipcode.add("47678", "4767*", "476**", "47***", "4****", "*****");
       zipcode.add("47905", "4790*", "479**", "47***", "4****", "*****");
       zipcode.add("47909", "4790*", "479**", "47***", "4****", "*****");
       zipcode.add("47906", "4790*", "479**", "47***", "4****", "*****");
       zipcode.add("47605", "4760*", "476**", "47***", "4****", "*****");
       zipcode.add("47673", "4767*", "476**", "47***", "4****", "*****");
       zipcode.add("47607", "4760*", "476**", "47***", "4****", "*****");
       return zipcode;
   }
   
   private static Hierarchy getZippie() {
	   DefaultHierarchy zipcode = Hierarchy.create();
       zipcode.add("81667", "8166*", "816**", "81***", "8****", "*****");
       zipcode.add("81675", "8167*", "816**", "81***", "8****", "*****");
       zipcode.add("81925", "8192*", "819**", "81***", "8****", "*****");
       zipcode.add("81931", "8193*", "819**", "81***", "8****", "*****");
       zipcode.add("NULL", "NULL", "NULL", "NULL", "NULL", "*****");
       return zipcode;
   }
   
   
   private static Hierarchy getZip38() {
	   final DefaultHierarchy zipcode = Hierarchy.create();
	   zipcode.add("81667", "8166*", "816**", "81***", "8****", "*****");
	   zipcode.add("81675", "8167*", "816**", "81***", "8****", "*****");
	   zipcode.add("81925", "8192*", "819**", "81***", "8****", "*****");
	   zipcode.add("81931", "8193*", "819**", "81***", "8****", "*****");
	   zipcode.add("92922", "9292*", "929**", "92***", "9****", "*****");
	   return zipcode;
   }
   
   private static Hierarchy getZip41() {
	   DefaultHierarchy zip = Hierarchy.create();
       zip.add("47630", "4763*", "476*", "47*", "4*", "*");
       zip.add("47633", "4763*", "476*", "47*", "4*", "*");
       zip.add("47903", "4790*", "479*", "47*", "4*", "*");
       zip.add("47906", "4790*", "479*", "47*", "4*", "*");
       zip.add("48970", "4897*", "489*", "48*", "4*", "*");
       zip.add("48972", "4897*", "489*", "48*", "4*", "*");
       zip.add("48973", "4897*", "489*", "48*", "4*", "*");
       return zip;
   }
   
   private static Hierarchy getNation41() {
	   DefaultHierarchy nationality = Hierarchy.create();
       nationality.add("Canada", "N. America", "America", "*");
       nationality.add("USA", "N. America", "America", "*");
       nationality.add("Peru", "S. America", "America", "*");
       nationality.add("Brazil", "S. America", "America", "*");
       nationality.add("Bulgaria", "E. Europe", "Europe", "*");
       nationality.add("France", "W. Europe", "Europe", "*");
       nationality.add("Spain", "W. Europe", "Europe", "*");
       return nationality;
   }
   
   private static int[] calcFigures(int[] anArray)
   {
	   for(int i = 0; i < anArray.length; ++i)
	   {
		   anArray[i] = anArray[i]*2;
	   }
	   return anArray;
   }
   
   private static Hierarchy getDisease() {
	   DefaultHierarchy disease = Hierarchy.create();
       disease.add("flu",
                   "respiratory infection",
                   "vascular lung disease",
                   "respiratory & digestive system disease");
       disease.add("pneumonia",
                   "respiratory infection",
                   "vascular lung disease",
                   "respiratory & digestive system disease");
       disease.add("bronchitis",
                   "respiratory infection",
                   "vascular lung disease",
                   "respiratory & digestive system disease");
       disease.add("pulmonary edema",
                   "vascular lung disease",
                   "vascular lung disease",
                   "respiratory & digestive system disease");
       disease.add("pulmonary embolism",
                   "vascular lung disease",
                   "vascular lung disease",
                   "respiratory & digestive system disease");
       disease.add("gastric ulcer",
                   "stomach disease",
                   "digestive system disease",
                   "respiratory & digestive system disease");
       disease.add("stomach cancer",
                   "stomach disease",
                   "digestive system disease",
                   "respiratory & digestive system disease");
       disease.add("gastritis",
                   "stomach disease",
                   "digestive system disease",
                   "respiratory & digestive system disease");
       disease.add("colitis",
                   "colon disease",
                   "digestive system disease",
                   "respiratory & digestive system disease");
       disease.add("colon cancer",
                   "colon disease",
                   "digestive system disease",
    		   		"respiratory & digestive system disease");
	   return disease;
   }
   
   /**
    * Exemplifies the use of the order-based builder.
    */
   private static void dates() {

   	String stringDateFormat = "yyyy-MM-dd HH:mm";
   	
   	DataType<Date> dateType = DataType.createDate(stringDateFormat);
   	
       // Create the builder
       HierarchyBuilderOrderBased<Date> builder = HierarchyBuilderOrderBased.create(dateType, false);

       // Define grouping fanouts
       builder.getLevel(0).addGroup(10, dateType.createAggregate().createIntervalFunction());
       builder.getLevel(1).addGroup(2, dateType.createAggregate().createIntervalFunction());

       // Alternatively
       // builder.setAggregateFunction(AggregateFunction.INTERVAL(DataType.INTEGER));
       // builder.getLevel(0).addFanout(10);
       // builder.getLevel(1).addFanout(2);
       
       System.out.println("---------------------");
       System.out.println("ORDER-BASED DATE HIERARCHY");
       System.out.println("---------------------");
       System.out.println("");
       System.out.println("SPECIFICATION");
       
       // Print specification
       for (Level<Date> level : builder.getLevels()) {
           System.out.println(level);
       }
       
       // Print info about resulting groups
       System.out.println("Resulting levels: "+Arrays.toString(builder.prepare(getExampleDateData(stringDateFormat))));
       
       System.out.println("");
       System.out.println("RESULT");
       
       // Print resulting hierarchy
       printArray(builder.build().getHierarchy());
       System.out.println("");
   }

   /**
    * Returns example data.
    *
    * @return
    */
   private static String[] getExampleData(){

       String[] result = new String[15];
       for (int i=0; i< result.length; i++){
           result[i] = String.valueOf(i);
       }
       return result;
   }

   /**
    * Returns example date data.
    *
    * @param stringFormat
    * @return
    */
   private static String[] getExampleDateData(String stringFormat){

   	SimpleDateFormat format = new SimpleDateFormat(stringFormat);
   	
       String[] result = new String[15];
       for (int i=0; i< result.length; i++){
       	
       	Calendar date = GregorianCalendar.getInstance();
       	date.add(Calendar.HOUR, i);
       	
           result[i] = format.format(date.getTime());
       }
       return result;
   }

   /**
    * Returns example data.
    *
    * @return
    */
   private static String[] getExampleLDLData() {

       String[] result = new String[15];
       for (int i=0; i< result.length; i++){
           result[i] = String.valueOf(Math.random() * 9.9d);
       }
       return result;
   }
   
   /**
    * Exemplifies the use of the interval-based builder.
    */
   private static void intervalBased() {


       // Create the builder
       HierarchyBuilderIntervalBased<Long> builder = HierarchyBuilderIntervalBased.create(
                                                         DataType.INTEGER,
                                                         new Range<Long>(0l,0l,Long.MIN_VALUE / 4),
                                                         new Range<Long>(100l,100l,Long.MAX_VALUE / 4));
       
       // Define base intervals
       builder.setAggregateFunction(DataType.INTEGER.createAggregate().createIntervalFunction(true, false));
       builder.addInterval(0l, 20l);
       builder.addInterval(20l, 33l);
       
       // Define grouping fanouts
       builder.getLevel(0).addGroup(2);
       builder.getLevel(1).addGroup(3);
       

       System.out.println("------------------------");
       System.out.println("INTERVAL-BASED HIERARCHY");
       System.out.println("------------------------");
       System.out.println("");
       System.out.println("SPECIFICATION");
       
       // Print specification
       for (Interval<Long> interval : builder.getIntervals()){
           System.out.println(interval);
       }

       // Print specification
       for (Level<Long> level : builder.getLevels()) {
           System.out.println(level);
       }
       
       // Print info about resulting levels
       System.out.println("Resulting levels: "+Arrays.toString(builder.prepare(getExampleData())));

       System.out.println("");
       System.out.println("RESULT");

       // Print resulting hierarchy
       printArray(builder.build().getHierarchy());
       System.out.println("");
   }

   /**
    * Exemplifies the use of the interval-based builder for LDL cholesterol
    * in mmol/l.
    */
   private static void ldlCholesterol() {


       // Create the builder
       HierarchyBuilderIntervalBased<Double> builder = HierarchyBuilderIntervalBased.create(DataType.DECIMAL);
       
       // Define base intervals
       builder.addInterval(0d, 1.8d, "very low");
       builder.addInterval(1.8d, 2.6d, "low");
       builder.addInterval(2.6d, 3.4d, "normal");
       builder.addInterval(3.4d, 4.1d, "borderline high");
       builder.addInterval(4.1d, 4.9d, "high");
       builder.addInterval(4.9d, 10d, "very high");
       
       // Define grouping fanouts
       builder.getLevel(0).addGroup(2, "low").addGroup(2, "normal").addGroup(2, "high");
       builder.getLevel(1).addGroup(2, "low-normal").addGroup(1, "high");

       System.out.println("--------------------------");
       System.out.println("LDL-CHOLESTEROL HIERARCHY");
       System.out.println("--------------------------");
       System.out.println("");
       System.out.println("SPECIFICATION");
       
       // Print specification
       for (Interval<Double> interval : builder.getIntervals()){
           System.out.println(interval);
       }

       // Print specification
       for (Level<Double> level : builder.getLevels()) {
           System.out.println(level);
       }
       
       // Print info about resulting levels
       System.out.println("Resulting levels: "+Arrays.toString(builder.prepare(getExampleLDLData())));
       
       System.out.println("");
       System.out.println("RESULT");
       
       // Print resulting hierarchy
       printArray(builder.build().getHierarchy());
       System.out.println("");
   }
   
   /**
    * Shows how to load and store hierarchy specifications.
    */
   private static void loadStore() {
       try {
           HierarchyBuilderRedactionBased<?> builder = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
                                                                                             Order.RIGHT_TO_LEFT,
                                                                                             ' ', '*');
           builder.save("test.ahs");
           
           HierarchyBuilder<?> loaded = HierarchyBuilder.create("test.ahs");
           if (loaded.getType() == Type.REDACTION_BASED) {
               
               builder = (HierarchyBuilderRedactionBased<?>)loaded;
               
               System.out.println("-------------------------");
               System.out.println("REDACTION-BASED HIERARCHY");
               System.out.println("-------------------------");
               System.out.println("");
               System.out.println("SPECIFICATION");
               
               // Print info about resulting groups
               System.out.println("Resulting levels: "+Arrays.toString(builder.prepare(getExampleData())));
               
               System.out.println("");
               System.out.println("RESULT");
               
               // Print resulting hierarchy
               printArray(builder.build().getHierarchy());
               System.out.println("");
           } else {
               System.out.println("Incompatible type of builder");
           }
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
   
   /**
    * Exemplifies the use of the order-based builder.
    */
   private static void orderBased() {

       // Create the builder
       HierarchyBuilderOrderBased<Long> builder = HierarchyBuilderOrderBased.create(DataType.INTEGER, false);

       // Define grouping fanouts
       builder.getLevel(0).addGroup(10, DataType.INTEGER.createAggregate().createIntervalFunction());
       builder.getLevel(1).addGroup(2, DataType.INTEGER.createAggregate().createIntervalFunction());

       // Alternatively
       // builder.setAggregateFunction(AggregateFunction.INTERVAL(DataType.INTEGER));
       // builder.getLevel(0).addFanout(10);
       // builder.getLevel(1).addFanout(2);
       
       System.out.println("---------------------");
       System.out.println("ORDER-BASED HIERARCHY");
       System.out.println("---------------------");
       System.out.println("");
       System.out.println("SPECIFICATION");
       
       // Print specification
       for (Level<Long> level : builder.getLevels()) {
           System.out.println(level);
       }
       
       // Print info about resulting groups
       System.out.println("Resulting levels: "+Arrays.toString(builder.prepare(getExampleData())));
       
       System.out.println("");
       System.out.println("RESULT");
       
       // Print resulting hierarchy
       printArray(builder.build().getHierarchy());
       System.out.println("");
   }
   
   /**
    * Exemplifies the use of the redaction-based builder.
    */
   private static void redactionBased() {

       // Create the builder
       HierarchyBuilderRedactionBased<?> builder = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
                                                                                   Order.RIGHT_TO_LEFT,
                                                                                   ' ', '*');

       System.out.println("-------------------------");
       System.out.println("REDACTION-BASED HIERARCHY");
       System.out.println("-------------------------");
       System.out.println("");
       System.out.println("SPECIFICATION");
       
       // Print info about resulting groups
       System.out.println("Resulting levels: "+Arrays.toString(builder.prepare(getExampleData())));
       
       System.out.println("");
       System.out.println("RESULT");
       
       // Print resulting hierarchy
       printArray(builder.build().getHierarchy());
       System.out.println("");
   }
}