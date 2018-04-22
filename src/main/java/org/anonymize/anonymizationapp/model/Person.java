package org.anonymize.anonymizationapp.model;

public class Person {
	
	public Person() {
		this.setName("");
		this.setCompany("");
		this.setPassword("");
		
	}
	
	public Person(String name, String company, String password) {
		this.setName(name);
		this.setCompany(company);
		this.setPassword(password);
	}

// @Id
// @Column(name = "ID")
// @GeneratedValue(strategy = GenerationType.IDENTITY)
 
// @Column(name = "FIRST_NAME")
 private String name;
 
// @Column(name = "LAST_NAME")
 private String company;
 
 private String password;

 
public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getCompany() {
	return company;
}

public void setCompany(String company) {
	this.company = company;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}
}