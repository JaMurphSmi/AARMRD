<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/style.css" />
<script type="text/javascript" src="js/app.js"></script>
<title>AARMRD-Home</title>
<link rel="icon" href="https://i.imgur.com/AEUtoVg.png" sizes="32x32" type="image/png">
</head>
<body>
  <div id="topHead"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h1>Set Anonymization Details</h1><br><br><br>
  <hr>
	  <div style="margin-left:50px">
	  <h2>Your Submitted File Contents</h2>
	<br>
	<div>
		<div id="selectDetails">
				<div id="selectDetailsInner">
					<table style="border-collapse: collapse;">
						<tr>
							<c:forEach items="${headerRow}" var="head">
								<th id="dataHead" align="center">
									<p class="tableText">${head}</p>
								</th>
							</c:forEach>
						</tr>
						    <c:forEach items="${dataRows}" var="dataRow">
							    <tr>
							    	<c:forEach items="${dataRow}" var="dataItem">
									    <td id="dataBody" align="center">
									       	<p class="tableText">${dataItem}</p>     
									    </td>
									</c:forEach>    
							    </tr>
						    </c:forEach>
					</table>
				</div>
		</div>	
		<br><br><br>
		<div id="detailOptionsContainer" style="color:#ecec00;">
			<h2>Set the Attribute details for your data set!</h2>
			<div class="tooltipsAD">&#10068;
  			<span class="tooltiptext">
  			Listed below are all fields in your dataset. You can specify an anonymization algorithm for each of these fields.<br>
  			Facts about these algorithms : <br>
  			Attributes are Indentifying by default.
  			As such Generalization(done by default) will replace an attribute with '*' if marked as Identifying<br>
  			<b>k-anonymity</b> and <b>recursive cl-diversity</b> will effect <u><b><i>every</i></b></u> attribute in the dataset marked as Quasi-Identifying<br>
  			<b>both above</b> need to only be defined <u><b><i>once per anonymization</i></b></u>, more than one could cause conflicts<br>
  			<b>l-diversity</b> will uniquely effect the single attribute it is defined for if it is marked as Sensitive<br>
  			<b>t-closeness</b> will uniquely effect the single attribute it is defined for if it is marked as Sensitive<br>
  			<b>k-anonymity</b> and <b>l-diversity</b> take integer values
  			<b>t-closeness</b> takes a double value
  			The value input field increments in steps of 0.1 by default<br>
  			If you select k-anonymity, the field will be in steps of 1 by default
  			</span>
			</div><br><br>
			<form:form modelAttribute="anonForm" action="anonymizeData" method="POST">
					<form:hidden path="fileName"/>
					<c:forEach items="${headerRow}" var="head" varStatus="fieldNumber">
						${head}:&nbsp&nbsp&nbsp&nbsp
						<form:select path="modelsChosen[${fieldNumber.index}]" id="modChosen_${fieldNumber.index}" onchange="checkFieldNeededAndType(${fieldNumber.index})">
								<form:option value="- - NONE - -">- - NONE - -</form:option>
							<c:forEach items="${models}" var="algo">
								<form:option value="${algo}">${algo}</form:option>
							</c:forEach>
						</form:select>
						&nbsp&nbspValue: <form:input type="number" id="valuesForMod_${fieldNumber.index}" path="valuesForModels[${fieldNumber.index}]" step="0.1"/>
						<!-- span initially hidden -->
						&nbsp&nbspSecond Value: <form:input type="number" id="exValues_${fieldNumber.index}" style="background:#d1d1d1;" path="extraForModels[${fieldNumber.index}]" step="0.1" readOnly="true"/>
						&nbsp&nbspAttribute Type: 
						<form:select path="attributesChosen[${fieldNumber.index}]" id="attValues_${fieldNumber.index}"><!-- attributes will be identifying by default -->
							<c:forEach items="${attributes}" var="anAttribute"> 
								<form:option value="${anAttribute}">${anAttribute}</form:option>
							</c:forEach>
						</form:select>
						<br><br>
					</c:forEach>
					<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					<input type="submit" class="formSubmitButton loginSubmit" value="Submit Your Values"></input>
				<br><br>
			</form:form>
		</div>
		<br><br>
	</div>
</body>
</html>