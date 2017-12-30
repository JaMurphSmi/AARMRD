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
import org.deidentifier.arx.DataSelector;
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
	   //DefaultData data = Data.create();	   
	   Data data = getData();
	   
// Define research subset
       ///// Define research subset by directly selecting specific indexes of that set
       //DataSubset subset = DataSubset.create(data, new HashSet<Integer>(Arrays.asList(1, 2, 5, 7, 8)));
       
       ///// can create a subset through variability 
       //DataSelector selector = DataSelector.create(data).field("sen").equals("1");
       
       ///// complex subset selector -> does not work

       //DataSubset subset = DataSubset.create(data, selector);
       
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
       age.add("34", "<50", "*");
       age.add("45", "<50", "*");
       age.add("66", ">=50", "*");
       age.add("70", ">=50", "*");
       
       DefaultHierarchy gender = Hierarchy.create();
       gender.add("male", "*");
       gender.add("female", "*");
       
       DefaultHierarchy zipcode = Hierarchy.create();
       zipcode.add("81667", "8166*", "816**", "81***", "8****", "*****");
       zipcode.add("81675", "8167*", "816**", "81***", "8****", "*****");
       zipcode.add("81925", "8192*", "819**", "81***", "8****", "*****");
       zipcode.add("81931", "8193*", "819**", "81***", "8****", "*****");
       
       
       //Hierarchy disease = Hierarchy.create("src/main/resources/templates/hierarchy/medical_test_disease.csv", StandardCharsets.UTF_8, ';');
       
       // set the minimal generalization height
       /*data.getDefinition().setMinimumGeneralization("zipcode", 3);
       data.getDefinition().setMaximumGeneralization("zipcode", 3);
       data.getDefinition().setMinimumGeneralization("gender", 1);*/
       //data.getDefinition().setAttributeType("disease", AttributeType.SENSITIVE_ATTRIBUTE);
       
    // Define attribute types
       data.getDefinition().setAttributeType("age", age);
       data.getDefinition().setAttributeType("gender", gender);
       data.getDefinition().setAttributeType("zipcode", zipcode);
       
       // Create an instance of the anonymizer
       ARXAnonymizer anonymizer = new ARXAnonymizer();
       ARXConfiguration config = ARXConfiguration.create();
       config.addPrivacyModel(new KAnonymity(3));
       /*config.addPrivacyModel(new HierarchicalDistanceTCloseness("disease1", 0.2d, getHierarchyDisease()));
       config.addPrivacyModel(new RecursiveCLDiversity("disease2", 3d, 2));
       config.setMaxOutliers(0d);
       config.setQualityModel(Metric.createEntropyMetric());*/
       
    // NDS-specific settings
       config.setMaxOutliers(1d); // Recommended default: 1d
       config.setAttributeWeight("age", 0.5d); // attribute weight
       config.setAttributeWeight("gender", 0.3d); // attribute weight
       config.setAttributeWeight("zipcode", 0.5d); // attribute weight
       config.setQualityModel(Metric.createLossMetric(0.5d)); // suppression/generalization-factor
       
       // Execute the algorithm
       ARXResult result = anonymizer.anonymize(data, config);
       
       // Print input
       System.out.println(" - Input data:");
       print(data.getHandle().iterator());

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
       System.out.println(" Just about to hit 'printResult()'");
       printResult(result, data);
       
       // Write results to file
       System.out.print(" - Writing data...");
       result.getOutput(false).save("src/main/resources/templates/output/test_anonymized19.csv", ';');
       System.out.println("Done!");
       
    // Process results
       if (result.getGlobalOptimum() != null) {
           System.out.println(" - Transformed data:");
           Iterator<String[]> transformed = result.getOutput(false).iterator();
           while (transformed.hasNext()) {
               System.out.print("   ");
               System.out.println(Arrays.toString(transformed.next()));
           }
}
       
    // Print results
	       /*System.out.println(" - Transformed data:");
	       print(result.getOutput(false).iterator());
	
	       // Print results
	       System.out.println(" - Transformed research subset:");
	       print(result.getOutput(false).getView().iterator());*/
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
   
   private static Data getData() {
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
   private static Hierarchy getHierarchyDisease() {
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