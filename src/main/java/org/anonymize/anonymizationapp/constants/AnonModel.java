package org.anonymize.anonymizationapp.constants;

public enum AnonModel {
	K_ANONYMITY("k-anonymity"),
	L_DIVERSITY("l-diversity"),
	T_CLOSENESS("t-closeness"),
	Δ_PRESENCE("δ-presence");
	
	private final String method;
	
	AnonModel (String model) {
		method = model;
	}
}