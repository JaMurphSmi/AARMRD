package org.anonymize.anonymizationapp.model;

import java.util.Date;

//import javax.persistence.*;

//import org.springframework.data.annotation.Id;

public class ExampleObject {
 private String username;
 private String userIp;
 private String photosSent;
 private Date dateAccessed;
 
 public ExampleObject(String username, String userIp, String photosSent, Date dateAccessed) {
  super();
  this.username = username;
  this.userIp = userIp;
  this.photosSent = photosSent;
  this.dateAccessed = dateAccessed;
 }
 public ExampleObject() {
  super();
 }
 /*@Id
 @Column(name = "log_id")
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 public int getLog_id() {
  return log_id;
 }
 public void setLog_id(int log_id) {
  this.log_id = log_id;
 }*/
 //@Column(name = "username")
 public String getUsername() {
  return username;
 }
 public void setUsername(String username) {
  this.username = username;
 }
 //@Column(name = "user_ip")
 public String getUserIp() {
  return userIp;
 }
 public void setUserIp(String userIp) {
  this.userIp = userIp;
 }
 //@Column(name = "photos_sent")
 public String getPhotosSent() {
  return photosSent;
 }
 public void setPhotosSent(String photosSent) {
  this.photosSent = photosSent;
 }
 //@Column(name = "date_accessed")
 public Date getDateAccessed() {
  return dateAccessed;
 }
 public void setDateAccessed(Date dateAccessed) {
  this.dateAccessed = dateAccessed;
 }

}