package org.anonymize.anonymizationapp.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PieChartGenerator {
	public void makePieChart(String title) {

	    // Create dataset
	    PieDataset dataset = createDataset();

	    // Create chart
	    JFreeChart chart = ChartFactory.createPieChart(
	        "Pie Chart Example",
	        dataset,
	        true, 
	        true,
	        false);

	    //Format Label
	    PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
	        "Marks {0} : ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
	    ((PiePlot) chart.getPlot()).setLabelGenerator(labelGenerator);
	    
	    try {
			ChartUtilities.saveChartAsJPEG(new File("path/piechart.png"),chart,400, 300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

	  private PieDataset createDataset() {

	    DefaultPieDataset dataset=new DefaultPieDataset();
	    dataset.setValue("80-100", 120);
	    dataset.setValue("60-79", 80);
	    dataset.setValue("40-59", 20);
	    dataset.setValue("20-39", 7);
	    dataset.setValue("0-19", 3);
	    return dataset;
	  }
}
