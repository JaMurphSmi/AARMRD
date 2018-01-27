<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/resources/css/style.css">
<script type="text/javascript" src="/resources/js/app.js"></script>
<title>Spring Boot</title>
</head>
<body>
  <h1>Anonymization application Homepage</h1>
  <hr>

  <h2>${message}.</h2><br>
  There's nothing here atm, but hopefully this will have<br>
  the components needed to do my first anonymization at some point!
  <div class="form">
    <form action="uploadFiles" method="post" enctype="multipart/form-data" onsubmit="return validate()">
    	<div id="import">
	  		<input type="file" id="testFile" name="testFile" title="This is a test file upload to prove concept" value="Get your data here" onmouseover="displayBasic()">
	  	</div>
	  	<div id="import">
	  		<input type="file" id="testFile" name="testHier" title="This is a test file upload to prove concept" value="Get your data here" multiple="multiple" onmouseover="displayBasic()">
	  	</div>
	  	<input type="submit" value="Test Submit">
    </form>
    <form action="anonymize" method="post" enctype="multipart/file-data" onsubmit="return validate()">
	  <div id="data">
	  	<div id="import">
	  		<input type="file" id="dataFile" title="This is where the data will go" value="get your data here" onmouseover="displayBasic()">
	  	</div>
	  This is where the data should go eventually
	  </div>
	  <div id="hierarchy">
	  	<div id="import">
	  		<input type="file" id="hierFile" title="This is where hierarchies will go" value="get your hierarchy here" onmouseover="displayBasic1()">
	  	</div>
	  This is where hierarchies should go eventually
	  </div>
	    <table>
	      <tr>
	        <td>Continue to skeleton Anonymization Screen</td>
	        <td><input type="submit" value="Move On"></td>
	      </tr>
	    </table>
    </form>
  </div>
<script>
function displayBasic() {
    document.getElementById("dataFile").style.color = "green";
}
function displayBasic1() {
    document.getElementById("hierFile").style.color = "green";
}
</script>
</body>
</html>