package org.anonymize.anonymizationapp.controller;


// Importing ARX required modules, dependencies etc
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.criteria.KAnonymity;
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
	// Define data
       DefaultData data = Data.create();
       data.add("age", "gender", "zipcode", "phoneno");
       data.add("34", "male", "81667", "083127869");
       data.add("45", "female", "81675", "083125963");
       data.add("66", "male", "81925", "084437546");
       data.add("70", "female", "81931", "084431278");
       data.add("34", "female", "81931", "085155078");
       data.add("70", "male", "81931", "085155412");
       data.add("45", "male", "81931", "085153436");
       data.add("32", "male", "81931", "083127569");

       // Manually define hierarchies
       DefaultHierarchy age = Hierarchy.create();
       age.add("32", "<50", "*");
       age.add("34", "<50", "*");
       age.add("45", "<50", "*");
       age.add("66", ">=50", "*");
       age.add("70", ">=50", "*");

       DefaultHierarchy gender = Hierarchy.create();
       gender.add("male", "*");
       gender.add("female", "*");

       // Need to add a hierarchy for each individual field
       DefaultHierarchy zipcode = Hierarchy.create();
       zipcode.add("81667", "8166*", "816**", "81***", "8****", "*****");
       zipcode.add("81675", "8167*", "816**", "81***", "8****", "*****");
       zipcode.add("81925", "8192*", "819**", "81***", "8****", "*****");
       zipcode.add("81931", "8193*", "819**", "81***", "8****", "*****");
       
       DefaultHierarchy phoneno = Hierarchy.create();
       phoneno.add("083127869", "0831278**", "08312****", "083******");
       phoneno.add("083125963", "0831259**", "08312****", "083******"); 
       phoneno.add("083127569", "0831275**", "08312****", "083******");
       phoneno.add("084437546", "0844375**", "08443****", "084******");
       phoneno.add("084431278", "0844312**", "08443****", "084******");
       phoneno.add("085155078", "0851550**", "08515****", "085******"); 
       phoneno.add("085155412", "0851554**", "08515****", "085******");
       phoneno.add("085153436", "0851534**", "08515****", "085******");
       
       
       
       data.getDefinition().setAttributeType("age", age);
       data.getDefinition().setAttributeType("gender", gender);
       data.getDefinition().setAttributeType("zipcode", zipcode);
       data.getDefinition().setAttributeType("phoneno", phoneno);

       // Create an instance of the anonymizer
       ARXAnonymizer anonymizer = new ARXAnonymizer();
       ARXConfiguration config = ARXConfiguration.create();
       config.addPrivacyModel(new KAnonymity(3));
       config.setMaxOutliers(0d);

       ARXResult result = anonymizer.anonymize(data, config);

       // Print info
       printResult(result, data);

       // Process results
       System.out.println(" - Transformed data:");
       Iterator<String[]> transformed = result.getOutput(false).iterator();
       while (transformed.hasNext()) {
           System.out.print("   ");
           System.out.println(Arrays.toString(transformed.next()));
  }   
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