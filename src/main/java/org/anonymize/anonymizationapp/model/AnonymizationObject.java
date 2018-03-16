package org.anonymize.anonymizationapp.model;



public class AnonymizationObject {
	
	private String fileName;
	
	// The field names of a given dataset, length of this used to dynamically create the correct length
	//private String[] theHeaderRow;//array in each case, ie 5 fields, array length 5, 100 fields, array length 100
	
	// the enum of algorithms available, may not be used for a specific thing ever, might just add a string list of models either
	//private AnonModel anonModels;
	
	// the array of models chosen from the select boxes in the view, each select hopefully pathed to an index position
	private String[] modelsChosen;
	
	// the array of values used for the selected models
	private double[] valuesForModels;
	
	private String[] attributesChosen;
	
	
	public AnonymizationObject() {//never a situation where this will be called, simply for completeness
		//do not initialize theHeaderRow
		//anonModels retrieve values from enum
		//do not initialize valuesForModels
	}

	public AnonymizationObject(String fileName, int theHeaderRowLength) {
		this.setFileName(fileName);
		this.setValuesForModels(new double[(theHeaderRowLength)]);//may be an awful way of initializing, but eh
		this.setAttributesChosen(new String[theHeaderRowLength]);//initialize with length of the headerRow, to ensure correct number of array positions created
		this.setModelsChosen(new String[theHeaderRowLength]);
	}

	public double[] getValuesForModels() {
		return valuesForModels;
	}

	public void setValuesForModels(double[] valuesForModels) {
		this.valuesForModels = valuesForModels;
	}

	public String[] getModelsChosen() {
		return modelsChosen;
	}

	public void setModelsChosen(String[] modelsChosen) {
		this.modelsChosen = modelsChosen;
	}

	public String[] getAttributesChosen() {
		return attributesChosen;
	}

	public void setAttributesChosen(String[] attributesChosen) {
		this.attributesChosen = attributesChosen;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}