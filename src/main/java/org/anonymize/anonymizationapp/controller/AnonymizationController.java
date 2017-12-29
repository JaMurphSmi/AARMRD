package org.anonymize.anonymizationapp.controller;


// Importing ARX required modules, dependencies etc
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
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
import org.deidentifier.arx.DataSubset;
import org.deidentifier.arx.DataType;
import org.deidentifier.arx.criteria.DPresence;
import org.deidentifier.arx.criteria.HierarchicalDistanceTCloseness;
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
	   
       //Data data = Data.create("src/main/resources/templates/data/medical_test_data.csv", StandardCharsets.UTF_8, ';');
       
	   // Define public dataset
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
	   
// Define research subset
       DataSubset subset = DataSubset.create(data, new HashSet<Integer>(Arrays.asList(1, 2, 5, 7, 8)));
       
       // Obtain a handle
/*	   DataHandle inHandle = data.getHandle();

       // Read the encoded data
       System.out.println("inHandle rows name is " + inHandle.getNumRows());
       System.out.println("inHandle columns name is " + inHandle.getNumColumns());
       System.out.println("inHandle field name is " + inHandle.getAttributeName(0));
       System.out.println("inHandle value of the field is " + inHandle.getValue(0, 0));
*/
       
       
// Define how field effects identifiability
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

       DefaultHierarchy nationality = Hierarchy.create();
       nationality.add("Canada", "N. America", "America", "*");
       nationality.add("USA", "N. America", "America", "*");
       nationality.add("Peru", "S. America", "America", "*");
       nationality.add("Brazil", "S. America", "America", "*");
       nationality.add("Bulgaria", "E. Europe", "Europe", "*");
       nationality.add("France", "W. Europe", "Europe", "*");
       nationality.add("Spain", "W. Europe", "Europe", "*");

       DefaultHierarchy zip = Hierarchy.create();
       zip.add("47630", "4763*", "476*", "47*", "4*", "*");
       zip.add("47633", "4763*", "476*", "47*", "4*", "*");
       zip.add("47903", "4790*", "479*", "47*", "4*", "*");
       zip.add("47906", "4790*", "479*", "47*", "4*", "*");
       zip.add("48970", "4897*", "489*", "48*", "4*", "*");
       zip.add("48972", "4897*", "489*", "48*", "4*", "*");
       zip.add("48973", "4897*", "489*", "48*", "4*", "*");
       //Hierarchy disease = Hierarchy.create("src/main/resources/templates/hierarchy/medical_test_disease.csv", StandardCharsets.UTF_8, ';');
       
       // set the minimal generalization height
       /*data.getDefinition().setMinimumGeneralization("zipcode", 3);
       data.getDefinition().setMaximumGeneralization("zipcode", 3);
       data.getDefinition().setMinimumGeneralization("gender", 1);*/
       //data.getDefinition().setAttributeType("disease", AttributeType.SENSITIVE_ATTRIBUTE);
       
    // Set data attribute types
       data.getDefinition().setAttributeType("identifier", AttributeType.IDENTIFYING_ATTRIBUTE);//will be suppressed
       data.getDefinition().setAttributeType("name", AttributeType.IDENTIFYING_ATTRIBUTE);
       data.getDefinition().setAttributeType("zip", zip);//generalization will be applied
       data.getDefinition().setAttributeType("age", age);
       data.getDefinition().setAttributeType("nationality", nationality);
       data.getDefinition().setAttributeType("sen", AttributeType.INSENSITIVE_ATTRIBUTE);
       
// Create and configure instance of the anonymizer
       ARXAnonymizer anonymizer = new ARXAnonymizer();
       ARXConfiguration config = ARXConfiguration.create();
       //config.addPrivacyModel(new RecursiveCLDiversity("age", 3, 2));
       config.addPrivacyModel(new KAnonymity(2));
       config.addPrivacyModel(new DPresence(1d / 2d, 2d / 3d, subset));
       config.setMaxOutliers(0d);
       // setting a height metric
       config.setQualityModel(Metric.createHeightMetric());
       
       // Execute the algorithm
       ARXResult result = anonymizer.anonymize(data, config);
       
    // Print input
       System.out.println(" - Input data:");
       print(data.getHandle().iterator());

       // Print input
       System.out.println(" - Input research subset:");
       print(data.getHandle().getView().iterator());
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
       
       // Write results to file
       System.out.print(" - Writing data...");
       result.getOutput(false).save("src/main/resources/templates/output/test_anonymized11.csv", ';');
       System.out.println("Done!");
       
    // Print results
       System.out.println(" - Transformed data:");
       print(result.getOutput(false).iterator());

       // Print results
       System.out.println(" - Transformed research subset:");
       print(result.getOutput(false).getView().iterator());
       
       // Process results
       /*System.out.println(" - Transformed data:");
       Iterator<String[]> transformed = result.getOutput(false).iterator();
       while (transformed.hasNext()) {
           System.out.print("   ");
           System.out.println(Arrays.toString(transformed.next()));
       } 
       */ 
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