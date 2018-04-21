package org.anonymize.anonymizationapp.model;

public class AnonymizationReport {
	private String fileName;
	private String country;
	private String threshold;
	private String prosecutorRecordsAtRisk;
	private String prosecutorHighestRisk;
	private String prosecutorSuccessRate;
	private String journalistRecordsAtRisk;
	private String journalistHighestRisk;
	private String journalistSuccessRate;
	private String marketerStat;
	private String prosecutorGDPR;
	private String journalistGDPR;
	private String informationLoss;
	private String timeTaken;
	
	public AnonymizationReport() {
		this.fileName = null;
		this.country = null;
		this.threshold = null;
		this.prosecutorRecordsAtRisk = null;
		this.prosecutorHighestRisk = null;
		this.prosecutorSuccessRate = null;
		this.journalistRecordsAtRisk = null;
		this.journalistHighestRisk = null;
		this.journalistSuccessRate = null;
		this.marketerStat = null;
		this.informationLoss = null;
		this.timeTaken = null;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public String getProsecutorRecordsAtRisk() {
		return prosecutorRecordsAtRisk;
	}
	public void setProsecutorRecordsAtRisk(String prosecutorRecordsAtRisk) {
		this.prosecutorRecordsAtRisk = prosecutorRecordsAtRisk;
	}
	public String getProsecutorHighestRisk() {
		return prosecutorHighestRisk;
	}
	public void setProsecutorHighestRisk(String prosecutorHighestRisk) {
		this.prosecutorHighestRisk = prosecutorHighestRisk;
	}
	public String getProsecutorSuccessRate() {
		return prosecutorSuccessRate;
	}
	public void setProsecutorSuccessRate(String prosecutorSuccessRate) {
		this.prosecutorSuccessRate = prosecutorSuccessRate;
	}
	public String getJournalistRecordsAtRisk() {
		return journalistRecordsAtRisk;
	}
	public void setJournalistRecordsAtRisk(String journalistRecordsAtRisk) {
		this.journalistRecordsAtRisk = journalistRecordsAtRisk;
	}
	public String getJournalistHighestRisk() {
		return journalistHighestRisk;
	}
	public void setJournalistHighestRisk(String journalistHighestRisk) {
		this.journalistHighestRisk = journalistHighestRisk;
	}
	public String getJournalistSuccessRate() {
		return journalistSuccessRate;
	}
	public void setJournalistSuccessRate(String journalistSuccessRate) {
		this.journalistSuccessRate = journalistSuccessRate;
	}
	public String getMarketerStat() {
		return marketerStat;
	}
	public void setMarketerStat(String marketerStat) {
		this.marketerStat = marketerStat;
	}
	public String getInformationLoss() {
		return informationLoss;
	}
	public void setInformationLoss(String informationLoss) {
		this.informationLoss = informationLoss;
	}
	public String getTimeTaken() {
		return timeTaken;
	}
	public void setTimeTaken(String timeTaken) {
		this.timeTaken = timeTaken;
	}

	public String getProsecutorGDPR() {
		return prosecutorGDPR;
	}

	public void setProsecutorGDPR(String prosecutorGDPR) {
		this.prosecutorGDPR = prosecutorGDPR;
	}

	public String getJournalistGDPR() {
		return journalistGDPR;
	}

	public void setJournalistGDPR(String journalistGDPR) {
		this.journalistGDPR = journalistGDPR;
	}
	
}
