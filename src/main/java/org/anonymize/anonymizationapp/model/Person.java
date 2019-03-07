package org.anonymize.anonymizationapp.model;

import org.springframework.web.util.HtmlUtils;

public class Person {
	
	public Person() {
		this.setName("");
		this.setOrgName("");
		this.setEmpPass("");
		
	}
	
	public Person(String name, String org, String pass) {
		this.setName(name);
		this.setOrgName(org);
		this.setEmpPass(pass);
	}

// @Id
// @Column(name = "ID")
// @GeneratedValue(strategy = GenerationType.IDENTITY)
 
// @Column(name = "FIRST_NAME")
 private String empName;
 
// @Column(name = "LAST_NAME")
 private String orgName;
 
 private String empPass;

 
public String getEmpName() {
	return empName;
}

public void setName(String empName) {
	this.empName = HtmlUtils.htmlEscape(empName);
}

public String getOrgName() {
	return orgName;
}

public void setOrgName(String orgName) {
	this.orgName = HtmlUtils.htmlEscape(orgName);
}

public String getEmpPass() {
	return empPass;
}

public void setEmpPass(String empPass) {
	this.empPass = HtmlUtils.htmlEscape(empPass);
}
}