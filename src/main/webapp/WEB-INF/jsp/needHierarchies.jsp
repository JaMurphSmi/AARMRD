<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/style.css" />
<title>AARMRD-Need Hierarchies</title>
</head>
<body>
  <div id="logoTop"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h1>Some Attribute Hierarchies Missing</h1><br><br><br>
  <hr>
	  <div id="nHMessage">
	  <h2>Your Data is missing some hierarchies</h2>
	 <div>
	<div>
		<div id="nHContainer" >
			<h2>Would you like to create hierarchies for these data fields?</h2>
			<h3>Caution!</h3> AARMRD can automatically construct hierarchies for fields that are numbers <i>only</i>  ie age, zipcode.<br>
			Fields containing text may cause an error and corrupt your attempt to anonymize. If text field contains sensitive<br>
			information, it is recommended to simply set attribute as "identifying" on the next screen.<br><br><br>
			
			<form action="uploadFiles" method="POST"><!-- return to the upload method to finish data creation -->
					<c:forEach items="${headerRow}" var="head" varStatus="fieldNumber">
						<c:if test="${noHierArr[fieldNumber.index] == -1}"><!-- check that hierarchy DOESN'T exist -->
							<input type="checkbox" name="hierChecks" value ="${fieldNumber.index}"/>
							&nbsp&nbsp${head}
						</c:if>
					</c:forEach>
					<input type="submit" value="Create These Hierarchies"></input>
				<br><br>
			</form>
		</div>
	</div>
<script>

</script>
</body>
</html>