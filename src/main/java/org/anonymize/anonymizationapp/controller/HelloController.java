package org.anonymize.anonymizationapp.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.DataSource;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.anonymize.anonymizationapp.constants.AnonModel;
import org.anonymize.anonymizationapp.model.AnonymizationBase;
import org.anonymize.anonymizationapp.model.AnonymizationObject;
import org.anonymize.anonymizationapp.util.DataAspects;


@Controller
public class HelloController extends AnonymizationBase {
	
	@Autowired
	private DataAspects dataAspectsHelper;

   @RequestMapping("/")
   public String index() {
      return "index";
   }
   
   // will be a page to handle the uploading of files
   @RequestMapping("/uploadFiles")//only take in the files, establish data fields first, then allow user to select attribute types, algos etc
   public String getData(@RequestParam("dataFile") MultipartFile dataFile,
		   @RequestParam("hierFiles") MultipartFile[] hierFiles, Model model) throws IOException, ParseException {
		///////////////// Creating the data object
				
		//attempting to cast multipartfile object to file(can be modularized later if successful)
		//prepare dataset name
		String datasetFile = dataFile.getOriginalFilename();
		
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
		List<String> hierNames = new ArrayList<String>();//for hierarchy names to display
		//List<Hierarchy> hierarchies = new ArrayList<Hierarchy>();//list to hold the created hierarchy objects
		
		for(MultipartFile mulFile: hierFiles) {
			String fileName = mulFile.getOriginalFilename();
			File convertedHierFile = new File("src/main/resources/templates/hierarchy/" + fileName);
			
			convertedHierFile.createNewFile();
			
			FileOutputStream fost = new FileOutputStream(convertedHierFile);
			fost.write(mulFile.getBytes());
			fost.close();//works
			
			String[] tempArray = fileName.split("[\\_\\.]");//split by underscore and dot		  0         1         2
			hierNames.add(tempArray[2]);//add file name to list for display reasons further on [dataset]_hierarchy_[column]
		}
		
		System.out.println("before creating the data and hierarchies");
		Data source = dataAspectsHelper.createData(datasetFile);//hopefully this way works 
		System.out.println("after creating the data successfully");
		DataHandle handle = source.getHandle();//acquiring data handle
		
		Iterator<String[]> itHandle = handle.iterator();
		List<String[]> dataRows = new ArrayList<String[]>();
		String headerRow = Arrays.toString(itHandle.next());//get the header of the dataset to display in bold
		System.out.println(">" + headerRow + "<");
		String[] headerTemp = headerRow.split("[\\[\\],]");
		String[] header = new String[headerTemp.length - 1];
		for(int i = 0; i < headerTemp.length - 1; i++) {
			header[i] = headerTemp[i+1].trim();
		}//shift all values 
		model.addAttribute("headerData", headerTemp);
		model.addAttribute("headerRow", header);//only need to get the header once as it will do for the resulting dataset also
		for (String bit : header) {
			System.out.println(bit + ",");//testing if random space at the start
		}
		int i = 1;
		while((itHandle.hasNext()) && (i % 801 != 0)) {
			String row = Arrays.toString(itHandle.next());
			String[] data = row.split("[\\[\\],]");
			dataRows.add(data);
			++i;
		}
		
		source = null; //garbage collection, to avoid buildup of objects and memory growth
		
		//had to create object to push to jsp, to use modelAttribute
		//other variables instantiated as empty arrays based on the now constant header.length()
		AnonymizationObject anonForm = new AnonymizationObject(datasetFile, header);//constant for an individual user
		//////// use header row to allow user to set individual algorithms for each field
		String[] models = {"k-anonymity","l-diversity","t-closeness","δ-presence"};
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
   
}