<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/style.css" />
<title>AARMRD-Home</title>
</head>
<body>
  <div id="topHead"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h1>Set Anonymization Details</h1><br><br><br>
  <hr>
	  <div style="margin-left:50px">
	  <h2>Your Submitted File Contents</h2>
	 <div>
	<table>
	    <tr>
	        <td>Data FileName</td>
	        <td>${fileName}</td>
	    </tr>
	</table>
	<br>
	<div>
		<div id="selectDetails">
				<div id="selectDetailsInner">
					<table style="border-collapse: collapse;">
						<tr>
							<c:forEach items="${headerRow}" var="head">
								<th id="dataHead" align="center">
									${head}
								</th>
							</c:forEach>
						</tr>
						    <c:forEach items="${dataRows}" var="dataRow">
							    <tr>
							    	<c:forEach items="${dataRow}" var="dataItem">
									    <td id="dataBody" align="center">
									       	${dataItem}     
									    </td>
									</c:forEach>    
							    </tr>
						    </c:forEach>
					</table>
				</div>
		</div>	
		<br>
		<div id="detailOptionsContainer">
			<h2>Set the Attribute details for your data set!</h2>
			<div class="tooltipsAD">&#10068;
  			<span class="tooltiptext">
  			Listed below are all fields in your dataset. You can specify an anonymization algorithm for each of these fields.<br>
  			Facts about these algorithms : <br>
  			Attributes are Indentifying by default
  			Generalization(done by default) will replace an attribute with '*' if marked as Identifying<br>
  			<b>k-anonymity</b> will effect <u><b><i>every</i></b></u> attribute in the dataset marked as Quasi-Identifying<br>
  			<b>k-anonymity</b> needs to only be defined <u><b><i>once per anonymization</i></b></u>, more than one could cause conflicts<br>
  			<b>l-diversity</b> will uniquely effect the single attribute it is defined for if it is marked as Sensitive<br>
  			<b>t-closeness</b> will uniquely effect the single attribute it is defined for if it is marked as Sensitive<br>
  			<b>k-anonymity</b> and <b>l-diversity</b> take integer values
  			<b>t-closeness</b> takes a double value
  			The value input field increments in steps of 0.1
  			</span>
			</div>
			<form:form modelAttribute="anonForm" action="anonymizeData" method="POST">
					<form:hidden path="fileName"/>
					<c:forEach items="${headerRow}" var="head" varStatus="fieldNumber">
						${head}:&nbsp&nbsp&nbsp&nbsp
						<form:select path="modelsChosen[${fieldNumber.index}]">
								<form:option value="- - NONE - -">- - NONE - -</form:option>
							<c:forEach items="${models}" var="algo">
								<form:option value="${algo}">${algo}</form:option>
							</c:forEach>
						</form:select>
						&nbsp&nbspValue for algorithm: <form:input type="number" path="valuesForModels[${fieldNumber.index}]" step="0.1"/>
						&nbsp&nbspField Attribute Type: 
						<form:select path="attributesChosen[${fieldNumber.index}]"><!-- attributes will be identifying by default -->
							<c:forEach items="${attributes}" var="anAttribute"> 
								<form:option value="${anAttribute}">${anAttribute}</form:option>
							</c:forEach>
						</form:select>
						<br><br>
					</c:forEach>
					<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					<input type="submit" value="Submit Your Values"></input>
				<br><br>
			</form:form>
		</div>
	</div>
<script>

</script>
</body>
</html>