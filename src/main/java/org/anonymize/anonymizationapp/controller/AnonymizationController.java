package org.anonymize.anonymizationapp.controller;


import java.io.File;
import java.io.FilenameFilter;
// Importing ARX required modules, dependencies etc
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.nio.charset.StandardCharsets;
//import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXLattice.ARXNode;
import org.deidentifier.arx.ARXPopulationModel;
import org.deidentifier.arx.ARXPopulationModel.Region;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.AttributeType.MicroAggregationFunction;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.Data.DefaultData;
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
import org.deidentifier.arx.criteria.HierarchicalDistanceTCloseness;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.criteria.EntropyLDiversity;
import org.deidentifier.arx.criteria.RecursiveCLDiversity;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.deidentifier.arx.metric.Metric;
import org.deidentifier.arx.metric.v2.__MetricV2;
import org.deidentifier.arx.risk.RiskEstimateBuilder;
import org.deidentifier.arx.risk.RiskModelAttributes;
import org.deidentifier.arx.risk.RiskModelAttributes.QuasiIdentifierRisk;
import org.deidentifier.arx.risk.RiskModelHistogram;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness;
import org.deidentifier.arx.risk.RiskModelPopulationUniqueness.PopulationUniquenessModel;
import org.deidentifier.arx.risk.RiskModelSampleRisks;
import org.deidentifier.arx.risk.RiskModelSampleUniqueness;
import org.anonymize.anonymizationapp.model.AnonymizationBase;
// ARX related stuff 
import org.apache.commons.math3.util.Pair;
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
   public String index(Model model) throws IOException, ParseException, SQLException, ClassNotFoundException {
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
       
       /*DefaultHierarchy age = Hierarchy.create(); //commented out for example 24
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
       zipcode.add("81931", "8193*", "819**", "81***", "8****", "*****");*/
       
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
       //determineDataType(data.getHandle(), 3);
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
       
       aggregate(new String[]{"xaaa", "xxxbbb", "xxcccc"}, DataType.STRING);
       aggregate(new String[]{"1", "2", "5", "11", "12", "3"}, DataType.STRING);
       aggregate(new String[]{"1", "2", "5", "11", "12", "3"}, DataType.INTEGER);
       
//////////////////self-contained test
       Data tData = createData("adult");
       tData.getDefinition().setAttributeType("occupation", AttributeType.SENSITIVE_ATTRIBUTE);
       
       ARXAnonymizer anonymize = new ARXAnonymizer();
       ARXConfiguration conf = ARXConfiguration.create();
       conf.addPrivacyModel(new EntropyLDiversity("occupation", 5));
       conf.setMaxOutliers(0.04d);
       conf.setQualityModel(Metric.createEntropyMetric());
       
       // Anonymize
       ARXResult res = anonymize.anonymize(tData, conf);
       System.out.println(" - Just after anonymizing using example 22...");
       printResult(res, tData);
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
       ////////////// EXAMPLE 31
       Data dataFor31 = getData30();
       
       dataFor31.getDefinition().setDataType("age", DataType.INTEGER);
       dataFor31.getDefinition().setDataType("zipcode", DataType.DECIMAL);
       dataFor31.getDefinition().setDataType("date", DataType.DATE);
       
       Hierarchy gend = getGender();
       
       Hierarchy zippie = getZippie();
       
       dataFor31.getDefinition().setAttributeType("age", MicroAggregationFunction.createGeometricMean());
       dataFor31.getDefinition().setAttributeType("gender", gend);
       data.getDefinition().setAttributeType("zipcode", MicroAggregationFunction.createGeneralization());//added for 32! 
       //dataFor31.getDefinition().setAttributeType("zipcode", zippie);
       dataFor31.getDefinition().setAttributeType("date", MicroAggregationFunction.createArithmeticMean());
       
       // Create an instance of the anonymizer
       ARXAnonymizer anonymi = new ARXAnonymizer();
       ARXConfiguration configure = ARXConfiguration.create();
       configure.addPrivacyModel(new KAnonymity(2));
       configure.setMaxOutliers(0.5d);

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
       
       System.out.print(" - Writing data...");
       outputs.getOutput(false).save("src/main/resources/templates/output/test_anonymized34.csv", ';');
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
   
   
   
   
   
   
   
//take in file through variable means   
   public static Data createData(final String dataset) throws IOException {

       Data data = Data.create("src/main/resources/templates/data/" + dataset + ".csv", StandardCharsets.UTF_8, ';');

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
       // Create definition
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
   
   /**
    * Perform risk analysis
    * @param handle
    */
   private static void analyzeAttributes(DataHandle handle) {
       ARXPopulationModel populationmodel = ARXPopulationModel.create(Region.USA);
       RiskEstimateBuilder builder = handle.getRiskEstimator(populationmodel);
       RiskModelAttributes riskmodel = builder.getAttributeRisks();
       for (QuasiIdentifierRisk risk : riskmodel.getAttributeRisks()) {
           System.out.println("   * Distinction: " + risk.getDistinction() + ", Separation: " + risk.getSeparation() + ", Identifier: " + risk.getIdentifier());
       }
   }
       
   /**
    * Perform risk analysis
    * @param handle
    */
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

   
   /**
    * Prints a list of matching data types
    * @param handle
    * @param column
    */
   private static void determineDataType(DataHandle handle, int column) {
       System.out.println(" - Potential data types for attribute: "+handle.getAttributeName(column));
       List<Pair<DataType<?>, Double>> types = handle.getMatchingDataTypes(column);

       // Print entries sorted by match percentage
       for (Pair<DataType<?>, Double> entry : types) {
           System.out.print("   * ");
           System.out.print(entry.getKey().getDescription().getLabel());
           if (entry.getKey().getDescription().hasFormat()) {
               System.out.print("[");
               System.out.print(((DataTypeWithFormat) entry.getKey()).getFormat());
               System.out.print("]");
           }
           System.out.print(": ");
           System.out.println(entry.getValue());
       }
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