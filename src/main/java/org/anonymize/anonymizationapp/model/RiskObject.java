package org.anonymize.anonymizationapp.model;

import java.util.HashMap;

public class RiskObject {
	//Map<String, double> ;
	public HashMap<String, HashMap<String, String>> dataSetInputDistributionMetrics;// = new ArrayList<new HashMap<String, double>>();
	public HashMap<String, HashMap<String, String>> dataSetOutputDistributionMetrics;
	public String country;
	public String threshold;
	public String[] prosecutorStats = new String[3];
	public String[] journalistStats = new String[3];
	public String marketerStat;
	
	public RiskObject() {
		 this.dataSetInputDistributionMetrics = new HashMap<String, HashMap<String, String>>();
		 this.dataSetOutputDistributionMetrics = new HashMap<String, HashMap<String, String>>();
	}

	public HashMap<String, HashMap<String, String>> getDataSetInputDistributionMetrics() {
		return dataSetInputDistributionMetrics;
	}

	public void setDataSetInputDistributionMetrics(HashMap<String, HashMap<String, String>> dataSetInputDistributionMetrics) {
		this.dataSetInputDistributionMetrics = dataSetInputDistributionMetrics;
	}
	
	public HashMap<String, HashMap<String, String>> getDataSetOutputDistributionMetrics() {
		return dataSetOutputDistributionMetrics;
	}

	public void setDataSetOutputDistributionMetrics(HashMap<String, HashMap<String, String>> dataSetOutputDistributionMetrics) {
		this.dataSetOutputDistributionMetrics = dataSetOutputDistributionMetrics;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
