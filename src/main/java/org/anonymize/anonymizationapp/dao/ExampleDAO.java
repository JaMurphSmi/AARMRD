package org.anonymize.anonymizationapp.dao;

import java.sql.Connection;

import org.anonymize.anonymizationapp.model.ExampleObject;
import org.anonymize.anonymizationapp.model.Person;

public interface ExampleDAO {
 public Connection connect();
 
 public boolean fetch(Person exampleObject);

}