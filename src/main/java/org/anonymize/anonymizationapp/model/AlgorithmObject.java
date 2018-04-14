package org.anonymize.anonymizationapp.model;



public class AlgorithmObject {
	
	private String fieldName;
	
	private String algorithm;
	
	private String value;
	
	private String attributeType;
	
	
	public AlgorithmObject() {//never a situation where this will be called, simply for completeness
		
	}

	public AlgorithmObject(String fieldName) {
		this.setFieldName(fieldName);
		this.setAlgorithm("");
		this.setValue("");
		this.setAttributeType("");
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}