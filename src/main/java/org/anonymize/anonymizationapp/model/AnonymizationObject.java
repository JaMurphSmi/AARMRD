package org.anonymize.anonymizationapp.model;

import org.deidentifier.arx.Data;
import org.deidentifier.arx.Data.DefaultData;
import org.anonymize.anonymizationapp.constants.AnonModel;

public class AnonymizationObject {
	

	// The source data being used for the application
	private Data theSourceData;
	
	// The field names of a given dataset, length of this used to dynamically create the correct length
	private String[] theHeaderRow;//array in each case, ie 5 fields, array length 5, 100 fields, array length 100
	
	// the enum of algorithms available, may not be used for a specific thing ever, might just add a string list of models either
	//private AnonModel anonModels;
	
	// the array of models chosen from the select boxes in the view, each select hopefully pathed to an index position
	private String[] modelsChosen;
	
	// the array of values used for the selected models
	private int[] valuesForModels;
	
	private String[] attributesChosen;
	
	
	public AnonymizationObject() {
		this.setTheSourceData(DefaultData.create());
		//do not initialize theHeaderRow
		//anonModels retrieve values from enum
		//do not initialize valuesForModels
	}
	
	public AnonymizationObject(Data theSourceData, String[] theHeaderRow) {
		this.setTheSourceData(theSourceData);
		this.setTheHeaderRow(theHeaderRow);
		this.setValuesForModels(new int[(theHeaderRow.length)]);//may be an awful way of initializing, but eh
		this.setModelsChosen(new String[(theHeaderRow.length)]);//initialize with length of the headerRow, to ensure correct number of array positions created
		this.setAttributesChosen(new String[theHeaderRow.length]);
	}

	public Data getTheSourceData() {
		return theSourceData;
	}

	public void setTheSourceData(Data theSourceData) {
		this.theSourceData = theSourceData;
	}

	public String[] getTheHeaderRow() {
		return theHeaderRow;
	}

	public void setTheHeaderRow(String[] theHeaderRow) {
		this.theHeaderRow = theHeaderRow;
	}

	public int[] getValuesForModels() {
		return valuesForModels;
	}

	public void setValuesForModels(int[] valuesForModels) {
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
}