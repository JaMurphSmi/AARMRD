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
<link rel="icon" href="https://i.imgur.com/AEUtoVg.png" sizes="32x32" type="image/png">
</head>
<body>
	<div class="americasNextTopDiv"><img src="https://i.imgur.com/DpvFqJW.png"/></div>
  <h1>AARMRD - Homepage</h1><br>
  <p>Hello! Let's get started with your data anonymization, please select your data file ( which has a maximum size of 1GB) and your hierarchy file(s) for each field, then submit!</p>
  <hr>
	<div align="center" class="inputFile">
	    <form action="uploadFiles" method="post" enctype="multipart/form-data">
	    <p><b>Note</b> that AARMRD currently only produces a csv file separated by a ';' as the resulting file format</p>
	    	<br>
	    	<div id="import">
		  		<input type="file" id="dataFile" name="dataFile" class="inputForFile" onchange="changeIt()">
		  		<label for="dataFile"><strong>Choose Data File</strong></label>
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
		  		<input type="file" id="hierFiles" name="hierFiles" class="inputForFile" data-multiple-caption="{count} files selected" multiple="multiple">
		  		<label for="hierFiles"><strong>Choose hierarchy file(s)</strong></label>
		  	</div>
		  	<br>
		  	<br>
		  	<input type="submit" value="Submit Your Files">
	    </form>
	    <br>
    </div>
<script>
var inputs = document.querySelectorAll( '#hierFile' );
Array.prototype.forEach.call( inputs, function( input )
{
	var label	 = input.nextElementSibling,
		labelVal = label.innerHTML;

	input.addEventListener('change', function( e )
	{
		var fileName = '';
		if(this.files && this.files.length > 1)
			fileName = (this.getAttribute('data-multiple-caption') || '').replace('{count}', this.files.length);
		else
			fileName = e.target.value.split('\\').pop();

		if( fileName )
			label.querySelector('span').innerHTML = fileName;
		else
			label.innerHTML = labelVal;
	});
});

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