<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>

<!-- Static content -->
<link rel="stylesheet" href="css/style.css" type="text/css"/>
<script src="js/app.js" type="text/javascript"></script>
<title>AARMRD</title>
</head>
<body>
	<div class="americasNextTopDiv"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h1>AARMRD - Homepage</h1><br>
  Hello! Let's get started with your data anonymization, please select your data file and your hierarchy files for each field, then submit!<br>
  <br><br>
  <hr>
	<div align="center" class="inputFile">
	    <form action="uploadFiles" method="post" enctype="multipart/form-data">
	    <b>Note</b> that AARMRD currently only produces a csv file separated by a ';' as the resulting file format
	    	<br><br>
	    	<div id="import">
		  		<label for="dataFile">Choose a data file(maximum size 1GB)</label>
		  		<input type="file" id="dataFile" name="dataFile" title="This is a test file upload to prove concept" value="Put your data here" onchange="changeIt()">
		  		<div class="tableDetails" id="hiddenTable">
		  			Details for JDBC : <br>
			  		<label for="tableName">Table: </label>
			  			<input type="text" name="tableName" id="tableName">
			  		<label for="userName">User: </label>
			  			<input type="text" name="userName" id="userName">
			  		<label for="password">Password: </label>
			  			<input type="password" name="password" id="password">
		  		</div>
		  	</div>
		  	<br><br>
		  	<div id="import">
		  		<label for="hierFile">Choose hierarchy file(s)</label>
		  		<input type="file" id="hierFile" name="hierFiles" title="This is a test file upload to prove concept" value="Put your hierarchies here" multiple="multiple">
		  	</div>
		  	<br>
		  	<br>
		  	<input type="submit" value="Submit Your Files">
	    </form>
	    <br>
    </div>
<script>
function changeIt() {
	var fullPath = document.getElementById('dataFile').value;
	if (fullPath) {
	    var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
	    var filename = fullPath.substring(startIndex);
	    if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
	        filename = filename.substring(1);
	    }
	}
	var extension;
	extension = filename.slice((Math.max(0, filename.lastIndexOf(".")) || Infinity) + 1);//handles even where extension does not exist
	if(extension === 'db') {
		document.getElementById("hiddenTable").style.display="inline";
	}
	else if(extension !== 'db') {
		document.getElementById("hiddenTable").style.display="none";
	}
}
</script>
</body>
</html>