package org.anonymize.anonymizationapp.model;

import java.util.HashMap;

public class RiskObject {
	//Map<String, double> ;
	private HashMap<String, HashMap<Double, String>> dataSetDistributionMetrics;// = new ArrayList<new HashMap<String, double>>();
	private String threshold;
	private String[] prosecutorStats = new String[3];
	private String[] journalistStats = new String[3];
	private String marketerStat;
	
	public void RiskObject() {
		 this.dataSetDistributionMetrics = new HashMap<String, HashMap<Double, String>>();
	}

	public HashMap<String, HashMap<Double, String>> getDataSetDistributionMetrics() {
		return dataSetDistributionMetrics;
	}

	public void setDataSetDistributionMetrics(HashMap<String, HashMap<Double, String>> dataSetDistributionMetrics) {
		this.dataSetDistributionMetrics = dataSetDistributionMetrics;
	}

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String[] getProsecutorStats() {
		return prosecutorStats;
	}

	public void setProsecutorStats(String[] prosecutorStats) {
		this.prosecutorStats = prosecutorStats;
	}

	public String[] getJournalistStats() {
		return journalistStats;
	}

	public void setJournalistStats(String[] journalistStats) {
		this.journalistStats = journalistStats;
	}

	public String getMarketerStat() {
		return marketerStat;
	}

	public void setMarketerStat(String marketerStat) {
		this.marketerStat = marketerStat;
	}

}
