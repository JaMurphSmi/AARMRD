package org.anonymize.anonymizationapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.anonymize.anonymizationapp.model.ExampleObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
 
@Component
public class ExampleDaoImpl implements ExampleDAO {
 final static Logger logger = LoggerFactory.getLogger(ExampleDaoImpl.class);

 public Connection connect() {
  // SQLite connection string
  String dbFile = "C:\\sqlite\\db\\activity_log.db";
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

 public  void save(ExampleObject exampleObject) {
  String sql = "INSERT INTO app_activity_log (username, user_ip, date_accessed,photos_sent) "
    + "values (?,?,?,?)";

  try (Connection conn = this.connect();
    PreparedStatement pstmt = conn.prepareStatement(sql)) {

   pstmt.setString(1,exampleObject.getUsername());
   pstmt.setString(2,exampleObject.getUserIp());
   pstmt.setString(3,exampleObject.getDateAccessed().toString());
   pstmt.setString(4,exampleObject.getPhotosSent());

   pstmt.executeUpdate();
   logger.info("Activity recorded");
  } catch (SQLException e) {
   System.out.println(e.getMessage());
  }
 }


}

