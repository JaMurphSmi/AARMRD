<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>

<!-- Static content -->
<link rel="stylesheet" href="/resources/css/style.css">
<script type="text/javascript" src="/resources/js/app.js"></script>

<title>AARMRD</title>
</head>
<body>
  <h1>Spring Boot - MVC web application skeleton</h1><br>
  Hello! Let's get started with your data anonymization, please select your data file and your hierarchy files for each field, then submit!<br>
  <hr>
	<div class="form">
	    <form action="uploadFiles" method="post" enctype="multipart/form-data">
	    	<div id="import">
		  		<label for="dataFile">Choose a data file(maximum size 1GB)</label>
		  		<input type="file" id="dataFile" name="dataFile" title="This is a test file upload to prove concept" value="Put your data here" >
		  	</div>
		  	<br><br>
		  	<div id="import">
		  		<label for="hierFile">Choose hierarchy file(s)</label>
		  		<input type="file" id="hierFile" name="hierFiles" title="This is a test file upload to prove concept" value="Put your hierarchies here" multiple="multiple">
		  	</div>
		  	<br>
		  	<input type="submit" value="Submit Your Files">
	    </form>
    </div>
</body>
</html>