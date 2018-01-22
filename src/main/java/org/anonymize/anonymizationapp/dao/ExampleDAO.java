package org.anonymize.anonymizationapp.dao;

import java.sql.Connection;

import org.anonymize.anonymizationapp.model.ExampleObject;

public interface ExampleDAO {
 public Connection connect();
 
 public void save(ExampleObject exampleObject);

}