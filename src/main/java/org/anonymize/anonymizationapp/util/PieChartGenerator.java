package org.anonymize.anonymizationapp.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.springframework.stereotype.Component;

@Component
public class PieChartGenerator {
	
	public void makePieChart(String theTitle, HashMap<String, String> pieValues) {

	    // Create dataset
	    
	    DefaultPieDataset defaultDataset = new DefaultPieDataset();
	    for (HashMap.Entry<String, String> entry : pieValues.entrySet()) {
	    	defaultDataset.setValue(entry.getKey(), Double.parseDouble(entry.getValue()));
	    }   
	    
	    PieDataset dataset = defaultDataset;
	    
	    // Create chart
	    JFreeChart chart = ChartFactory.createPieChart(
	        theTitle,
	        dataset,
	        true, 
	        true,
	        false);

	    //Format Label
	    PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
	        "Value {0} : ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
	    ((PiePlot) chart.getPlot()).setLabelGenerator(labelGenerator);
	    
	    try {
			ChartUtilities.saveChartAsJPEG(new File("src/main/resources/META-INF/resources/images/"+ theTitle +".png"),chart,400, 300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
}
