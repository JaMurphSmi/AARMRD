package org.anonymize.anonymizationapp.controller;


// Importing ARX required modules, dependencies etc
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
import java.util.Date;
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
import org.deidentifier.arx.DataType.DataTypeDescription;
import org.deidentifier.arx.aggregates.StatisticsContingencyTable;
import org.deidentifier.arx.aggregates.StatisticsContingencyTable.Entry;
import org.deidentifier.arx.aggregates.StatisticsFrequencyDistribution;
import org.deidentifier.arx.aggregates.HierarchyBuilder;
import org.deidentifier.arx.aggregates.HierarchyBuilder.Type;
import org.deidentifier.arx.aggregates.HierarchyBuilderGroupingBased.Level;
import org.deidentifier.arx.aggregates.HierarchyBuilderIntervalBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderIntervalBased.Interval;
import org.deidentifier.arx.aggregates.HierarchyBuilderIntervalBased.Range;
import org.deidentifier.arx.aggregates.HierarchyBuilderOrderBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased.Order;
import org.deidentifier.arx.criteria.DPresence;
import org.deidentifier.arx.criteria.HierarchicalDistanceTCloseness;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.criteria.RecursiveCLDiversity;
import org.deidentifier.arx.metric.Metric;
import org.anonymize.anonymizationapp.model.AnonymizationBase;
// ARX related stuff 

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cern.colt.Arrays;

@Controller
public class AnonymizationController extends AnonymizationBase {

   @SuppressWarnings("unused")
   @RequestMapping("/anonymize")
   public String index(Model model) throws IOException, ParseException {
	   int anArray[] = {4,5,6,7,8,9,10};
	   int secArray[];
	   secArray = calcFigures(anArray);
	   model.addAttribute("anonMessage", "This is where the anonymization will be placed");
	   model.addAttribute("figures", secArray);
	   
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
       System.out.println("Outside of the advanced for loop");
       // 2. Obtain specific data type
       DataTypeDescription<Date> entry = DataType.list(Date.class);
       System.out.println("got date type");
       DataTypeDescription<String> entry1 = DataType.list(String.class);
       System.out.println("got string type");
       DataTypeDescription<Long> entry2 = DataType.list(Long.class);
       System.out.println("got long type");
       DataTypeDescription<Double> entry3 = DataType.list(Double.class);
       System.out.println("got double type");
       
       // 3. Obtain data in specific formats
       Data theData = getData();
       System.out.println("got the data");
       
       
       theData.getDefinition().setDataType("zip", DataType.createDecimal("#,##0"));
       System.out.println("set the data type to decimal");
       theData.getDefinition().setDataType("sen", DataType.createDate("dd.MM.yyyy"));
       System.out.println("set the data type to date");
       
       DataHandle handle = theData.getHandle();
       double value1 = handle.getDouble(2, 2);
       Date value2 = handle.getDate(2, 5);

       System.out.println("Double: "+ value1);
       System.out.println("Date: "+ value2);
	   
	   
///////////// this is where anonymization begins	   
	   
	   
/// data set originally created below	   
	   
	   //Data data = Data.create("src/main/resources/templates/data/medical_test_data.csv", StandardCharsets.UTF_8, ';');
       
// Define public dataset
	   //DefaultData data = Data.create();	   
	   Data data = getTheData();
	   
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
       config.addPrivacyModel(new KAnonymity(2));
       
       
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
       printResult(result, data);
       
       // Write results to file
       System.out.print(" - Writing data...");
       result.getOutput(false).save("src/main/resources/templates/output/test_anonymized25.csv", ';');
       System.out.println("Done!");

       ///////// allows access to the data's statistics
       // Print input
       System.out.println(" - Input data:");
       Iterator<String[]> original = data.getHandle().iterator();
       while (original.hasNext()) {
           System.out.print("   ");
           System.out.println(Arrays.toString(original.next()));
       }

       // Print results
       if (result.getGlobalOptimum() != null) {
           System.out.println(" - Transformed data:");
           Iterator<String[]> transformed = result.getOutput(false).iterator();
           while (transformed.hasNext()) {
               System.out.print("   ");
               System.out.println(Arrays.toString(transformed.next()));
           }
   		}

       // Print frequencies
       StatisticsFrequencyDistribution distribution;
       System.out.println(" - Distribution of attribute 'age' in input:");
       distribution = data.getHandle().getStatistics().getFrequencyDistribution(0, false);
       System.out.println("   " + Arrays.toString(distribution.values));
       System.out.println("   " + Arrays.toString(distribution.frequency));

       // Print frequencies
       System.out.println(" - Distribution of attribute 'age' in output:");
       distribution = result.getOutput(false).getStatistics().getFrequencyDistribution(0, true);
       System.out.println("   " + Arrays.toString(distribution.values));
       System.out.println("   " + Arrays.toString(distribution.frequency));

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
       
      return "anonymize";
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
   @SuppressWarnings("unused")
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
   @SuppressWarnings("unused")
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

       String[] result = new String[35];
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
   	
       String[] result = new String[35];
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

       String[] result = new String[35];
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