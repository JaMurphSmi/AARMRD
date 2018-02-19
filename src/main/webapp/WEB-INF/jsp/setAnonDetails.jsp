<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/static/css/style.css" />
<style>
.tooltip {
    position: relative;
    display: inline-block;
    border-bottom: 1px dotted black;
}

.tooltip .tooltiptext {
    visibility: hidden;
    width: 500px;
    background-color: #555;
    color: #fff;
    text-align: center;
    border-radius: 6px;
    padding: 5px 0;
    position: absolute;
    z-index: 1;
    bottom: 125%;
    left: 50%;
    margin-left: -200px;
    opacity: 0;
    transition: opacity 0.3s;
}

.tooltip .tooltiptext::after {
    content: "";
    position: absolute;
    top: 100%;
    left: 50%;
    margin-left: -5px;
    border-width: 5px;
    border-style: solid;
    border-color: #555 transparent transparent transparent;
}

.tooltip:hover .tooltiptext {
    visibility: visible;
    opacity: 1;
}
</style>
<title>AARMRD-Home</title>
</head>
<body  style="background-color:#faf6b8;">
  <div style="z-index: 10;position: absolute;right: 30px;top: 0px;"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h1>AARMRD Homepage</h1><br><br><br>
  <hr>

  <h2></h2><br>
	  <div style="margin-left:50px">
	  <h2>Submitted File</h2>
	 <div>
	 <br><br>
	<table>
	    <tr>
	        <td>Data FileName</td>
	        <td>${fileName}</td>
	    </tr>
	    <tr>File contents</tr>
	</table>
	<div style="overflow:visible;">
		<div style="float:left;margin-left: 100px;">
				<table style="border-collapse: collapse;">
					<tr>
						<c:forEach items="${headerRow}" var="head">
							<th align="center" style="width:35px;border:1px solid #ddd;background-color: #e16830;">
								${head}
							</th>
						</c:forEach>
					</tr>
					    <c:forEach items="${dataRows}" var="dataRow">
						    <tr>
						    	<c:forEach items="${dataRow}" var="dataItem">
								    <td align="center" style="border:1px solid #d6aa44;">
								       	${dataItem}     
								    </td>
								</c:forEach>    
						    </tr>
					    </c:forEach>
				</table>
		</div>	
		<div style="float:left;margin-left: 100px;padding:10px;border:3px solid;border-color:#d5ce66;">
			<h2>Set the Attribute details for your data set!</h2>
			<div class="tooltip">&#10068;
  			<span class="tooltiptext">
  			Listed below are all fields in your dataset. You can specify an anonymization algorithm for each of these fields.<br>
  			Facts about these algorithms : <br>
  			Attributes are Indentifying by default
  			Generalization(done by default) will replace an attribute with '*' if marked as Identifying<br>
  			k-anonymity will effect <u><b><i>every</i></b></u> attribute in the dataset marked as Quasi-Identifying<br>
  			k-anonymity needs to only be defined <u><b><i>once per anonymization</i></b></u>, more than one could cause conflicts<br>
  			l-diversity will uniquely effect the single attribute it is defined for if it is marked as Sensitive<br>
  			t-closeness will uniquely effect the single attribute it is defined for if it is marked as Sensitive<br>
  			k-anonymity and l-diversity take integer values
  			t-closeness takes a double value
  			The value input field increments in steps of 0.1
  			</span>
			</div>
			<form:form modelAttribute="anonForm" action="anonymizeData" method="POST">
					<form:hidden path="fileName"/>
					<form:hidden path="theHeaderRow"/>
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
					<input type="submit" value="Submit Your Values"></input>
				<br><br>
			</form:form>
		</div>
	</div>
<script>

</script>
</body>
</html>