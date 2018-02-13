<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/static/css/style.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/static/js/app.js"></script>
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
	<div style="overflow:hidden;">
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
			<h2>Set the Attributes details for your data set!</h2>
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
						&nbsp&nbspValue for algorithm: <form:input type="number" path="valuesForModels[${fieldNumber.index}]"/>
						&nbsp&nbspFiled Attribute Type: 
						<form:select path="attributesChosen[${fieldNumber.index}]">
							<form:option value="- - NONE - -">- - NONE - -</form:option>
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