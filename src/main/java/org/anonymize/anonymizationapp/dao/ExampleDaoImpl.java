package org.anonymize.anonymizationapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.anonymize.anonymizationapp.model.ExampleObject;
import org.anonymize.anonymizationapp.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
 
@Component
public class ExampleDaoImpl implements ExampleDAO {
 final static Logger logger = LoggerFactory.getLogger(ExampleDaoImpl.class);

 public Connection connect() {
  // SQLite connection string
  String dbFile = "src/main/resources/activity_log.db";
  String url = "jdbc:sqlite:"+dbFile;
  Connection conn = null;
  try {
   conn = DriverManager.getConnection(url);
   logger.info("Connected to database");
  } catch (SQLException e) {
   logger.info(e.getMessage());
  }
  return conn;
 }

 public boolean fetch(Person exampleObject) {
  String sql = "SELECT count(*) FROM userDetails  WHERE username=? AND password=? AND company=?";

  try (Connection conn = this.connect();
    PreparedStatement pstmt = conn.prepareStatement(sql)) {

   pstmt.setString(1,exampleObject.getEmpName());
   pstmt.setString(2,exampleObject.getEmpPass());
   pstmt.setString(3,exampleObject.getOrgName());

   ResultSet res = pstmt.executeQuery();
   if (res.next()) {
	   int numRows = res.getInt(1);
	   logger.info("has a result row :)");
	   if (numRows >= 1) {
		   return true;
	   } else {
		   return false;
	   }
   }
   else {
	   System.out.println("connection problem");
   }
  } catch (SQLException e) {
   System.out.println(e.getMessage());
  }
  return false;
 }
}

