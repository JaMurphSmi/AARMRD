<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="/css/style.css" />
<script type="text/javascript" src="/resources/js/app.js"></script>
<title>Spring Boot</title>
</head>
<body style="background-color:#faf497;">
  <div style="z-index: 10;position: absolute;right: 50;top: 0;"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h1>AARMRD Homepage</h1>
  <hr>

  <h2></h2><br>
	  <div>
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
		
	<form:form modelAttribute="anonForm" action="anonymizeData" method="POST" onsubmit="return validate()">
		<div>
			<table>
				<tr>
					<c:forEach items="${headerData}" var="head">
						<th>
							${head}
						</th>
					</c:forEach>
				</tr>
				    <c:forEach items="${dataRows}" var="dataRow">
					    <tr>
					    	<c:forEach items="${dataRow}" var="dataItem">
							    <td>
							       	${dataItem}     
							    </td>
							</c:forEach>    
					    </tr>
				    </c:forEach>
			</table>
		</div>
		<div style="z-index: 9;position: absolute;right: 50;top: 175;">
			<c:forEach items="${headerRow}" var="head" varStatus="fieldNumber">
			${head}:  
				<form:select path="${modelsChosen[fieldNumber.index]}">
						<form:option value="">- - NONE - -</form:option>
					<c:forEach items="${models}" var="algo">
						<form:option value="${algo}">${algo}</form:option>
					</c:forEach>
				</form:select>
				&nbsp&nbspValue for algorithm: <form:input type="number" path="${valuesForModels[fieldNumber.index]}"/>
				&nbsp&nbspFiled Attribute Type: 
				<form:select path="${attributeTypes[fieldNumber.index]}">
					<c:forEach items="${attributes}" var="anAttribute"> 
						<form:option value="${anAttribute}">${anAttribute}</form:option>
					</c:forEach>
				</form:select>
				<br><br>
			</c:forEach>
		</div>
	</form:form>
<script>

</script>
</body>
</html>