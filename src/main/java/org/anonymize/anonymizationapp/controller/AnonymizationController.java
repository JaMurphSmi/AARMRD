package org.anonymize.anonymizationapp.controller;


// Importing ARX required modules, dependencies etc
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.DataType;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.criteria.RecursiveCLDiversity;
import org.deidentifier.arx.metric.Metric;
import org.anonymize.anonymizationapp.model.AnonymizationBase;
// ARX related stuff 

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnonymizationController extends AnonymizationBase {

   @RequestMapping("/anonymize")
   public String index(Model model) throws IOException {
	   int anArray[] = {4,5,6,7,8,9,10};
	   int secArray[];
	   secArray = calcFigures(anArray);
	   model.addAttribute("anonMessage", "This is where the anonymization will be placed");
	   model.addAttribute("figures", secArray);
	   
	   //adding some hardcoded examples for ARX
	   ///////removed for input from file, also shortens code substantially
       //adding some harcoded examples for ARX
	   
       //implementing a hardcoded, local, imported file here
       //will never be multiple 'data', or 'anonymizer', or 'config', or 'result' instance
       Data data = Data.create("src/main/resources/templates/data/test_data.csv", StandardCharsets.UTF_8, ';');
       
       
       // Obtain a handle
       DataHandle inHandle = data.getHandle();

       // Read the encoded data
       System.out.println("inHandle rows name is " + inHandle.getNumRows());
       System.out.println("inHandle columns name is " + inHandle.getNumColumns());
       System.out.println("outHandle field name is " + inHandle.getAttributeName(0));
       System.out.println("outHandle field value is " + inHandle.getValue(0, 0));
       
       // Define how field effects identifiability
       data.getDefinition().setAttributeType("age", AttributeType.SENSITIVE_ATTRIBUTE);
       //data.getDefinition().setAttributeType("age", AttributeType.IDENTIFYING_ATTRIBUTE);
       data.getDefinition().setAttributeType("gender", AttributeType.INSENSITIVE_ATTRIBUTE);
       // Define a field's type, 1 of 5 supported
       data.getDefinition().setDataType("zipcode", DataType.DECIMAL);
       
       // Define input hierarchy files
       //data.getDefinition().setAttributeType("age", Hierarchy.create("src/main/resources/templates/hierarchy/test_age.csv", StandardCharsets.UTF_8, ';'));
       data.getDefinition().setAttributeType("gender", Hierarchy.create("src/main/resources/templates/hierarchy/test_gender.csv", StandardCharsets.UTF_8, ';'));
       data.getDefinition().setAttributeType("zipcode", Hierarchy.create("src/main/resources/templates/hierarchy/test_zipcode.csv", StandardCharsets.UTF_8, ';'));
       data.getDefinition().setAttributeType("phoneno", Hierarchy.create("src/main/resources/templates/hierarchy/test_phoneno.csv", StandardCharsets.UTF_8, ';'));

       // Create an instance of the anonymizer
       ARXAnonymizer anonymizer = new ARXAnonymizer();
       
       // Execute the algorithm
       ARXConfiguration config = ARXConfiguration.create();
       config.addPrivacyModel(new RecursiveCLDiversity("age", 3, 2));
       config.addPrivacyModel(new KAnonymity(2));
       config.setMaxOutliers(0d);
       // setting a height metric
       config.setQualityModel(Metric.createHeightMetric());
       
       ARXResult result = anonymizer.anonymize(data, config);
       
       // Obtain a handle for the transformed data
       DataHandle outHandle = result.getOutput(false);
       //implicitly done to both handle objects
       outHandle.sort(false, 2);
       
       System.out.println("outHandle rows is " + outHandle.getNumRows());
       System.out.println("outHandle columns is " + outHandle.getNumColumns());
       System.out.println("outHandle field name is " + outHandle.getAttributeName(0));
       System.out.println("outHandle value of the field is " + outHandle.getValue(0, 0));
       
/// from here        
       // Print info
       printResult(result, data);
       
       // Write results to file
       System.out.print(" - Writing data...");
       result.getOutput(false).save("src/main/resources/templates/output/test_anonymized6.csv", ';');
       System.out.println("Done!");
       
    // Process results
       System.out.println(" - Transformed data:");
       Iterator<String[]> transformed = result.getOutput(false).iterator();
       while (transformed.hasNext()) {
           System.out.print("   ");
           System.out.println(Arrays.toString(transformed.next()));
       }  
/// to here seems to be a constant       
       
      return "anonymize";
   }
   
   
   
   private static int[] calcFigures(int[] anArray)
   {
	   for(int i = 0; i < anArray.length; ++i)
	   {
		   anArray[i] = anArray[i]*2;
	   }
	   return anArray;
   }
   
   /*private static int[] calcFigures(int[] anArray)
   {
	   for (int aValue : anArray) {
		    aValue = aValue*2;
		}
	   return anArray;
   }*/ //enhanced for more used for viewing/assessing values within objects to shorten a list/array
}