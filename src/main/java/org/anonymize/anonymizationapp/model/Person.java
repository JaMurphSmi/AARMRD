package org.anonymize.anonymizationapp.model;

public class Person {
	
	public Person() {
		this.id = 0;
		this.firstName = "";
		this.lastName = "";
		this.title = "";
		this.country = "";
		
	}
	
	public Person(int id, String firstName, String lastName, String title, String country) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.country = country;
	}

// @Id
// @Column(name = "ID")
// @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Integer id;
 
// @Column(name = "FIRST_NAME")
 private String firstName;
 
// @Column(name = "LAST_NAME")
 private String lastName;
 
 private String title;
 
 private String country;
  
 public Integer getId() {
  return id;
 }
 public void setId(Integer id) {
  this.id = id;
 }
 public String getFirstName() {
  return firstName;
 }
 public void setFirstName(String firstName) {
  this.firstName = firstName;
 }
 public String getLastName() {
  return lastName;
 }
 public void setLastName(String lastName) {
  this.lastName = lastName;
 }
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getCountry() {
	return country;
}
public void setCountry(String country) {
	this.country = country;
}
}