<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<style>

.button {
  background-color: ForestGreen;  
  border-radius: 5px;
  color: white;
  padding: .5em;
  text-decoration: none;
}

.button:focus,
.button:hover {
  background-color: DarkGreen;
}

.tooltip {
    position: relative;
    display: inline-block;
    border-bottom: 1px dotted black;
}

.tooltip .tooltiptext {
    visibility: hidden;
    width: 400px;
    background-color: #555;
    color: #fff;
    text-align: left;
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

.tooltip .tooltiptext1 {
    visibility: hidden;
    width: 400px;
    background-color: #555;
    color: #fff;
    text-align: left;
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

.tooltip .tooltiptext1::after {
    content: "";
    position: absolute;
    top: 100%;
    left: 50%;
    margin-left: -5px;
    border-width: 5px;
    border-style: solid;
    border-color: #555 transparent transparent transparent;
}

.tooltip:hover .tooltiptext1 {
    visibility: visible;
    opacity: 1;
}
</style>
<title>Spring Boot</title>
</head>
<body style="background-color:#faf6b8;">
 <div style="z-index: 10;position: absolute;right: 30px;top: 0px;"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h3>Anonymization Results</h3>
<div style="float:left;height=400px;max-width=1050px;overflow=hidden;">
  	<br>
  	<c:if test="${not empty errorMessage}"><div>${errorMessage}</div></c:if>
			<table>
			    <tr>
			        <td>Data FileName</td>
			        <td>${fileName}</td>
			    </tr>
			    <tr>File contents</tr>
			</table>
		<c:if test="${not empty dataRows}">
		<div style="float:left;margin-left: 100px; max-width:425px;height:400px;overflow:auto;">
			<table style="border-collapse: collapse;">
				<tr>
					<c:forEach items="${headerRow}" var="head">
						<th align="center" style="width:50px;border:1px solid #ddd;background-color: #e16830;">
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
		</c:if>
		<c:if test="${not empty anonyRows}">
		<div style="float:left;margin-left: 100px; max-width:425px;height:400px;overflow:auto;">
			<table style="border-collapse: collapse;">
				<tr>
					<c:forEach items="${headerRow}" var="head">
						<th align="center" style="width:35px;border:1px solid #ddd;background-color: #35d733;">
							${head}
						</th>
					</c:forEach>
				</tr>
				<c:forEach items="${anonyRows}" var="anonyRow">
					<tr>
						<c:forEach items="${anonyRow}" var="anony">
							<td align="center" style="border:1px solid #d6aa44;">
								${anony}
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
		</div>
		</c:if>
</div>
	<div style="float:left;margin:25px 25px 25px 50px;">
		<form action="/goToRiskPage" method="post">
			<input type="submit" class="button" value="Go To Risk Assessment"/> 
		</form><br><br><br>
		<button id="deleteDAH" class="button" >Delete Data and Hierarchies</button>
		<div class="tooltip">&#10068;<br>
				<span class="tooltiptext">
				  	This option is provided to adhere to the General Data Protection Regulation.<br> 
				  	Clicking allows you to explicitly delete all content associated with your anonymization.<br>
				  	This is also automatically performed when you download your new data file.
				</span>
		</div><br><br><br><br>
			<a href="/downloadAnonymizedFile" class="button">Download Anonymized Data</a>
			<div class="tooltip">&#10068;
				<span class="tooltiptext1">
				  	Note that for your data's security, and to comply with the General Data Protection Regulation<br>
				  	Pressing this button concludes your service, and once your anonymized file is downloaded<br>
				  	to your machine, all data you have uploaded for this transaction will be erased from the application
				</span>
			</div><br><br><br>
		  <div class="form">
		    <form action="/" method="post">
		      <input type="submit" class="button" value="Begin Again">
		    </form>
		  </div>
	</div>
<script>
$(document).ready(function(){
	$("#deleteDAH").click(function(){
	    $.ajax({
	        url : 'deleteDataAndHierarchies',
	        method : 'GET',
	        async : false,
	        complete : function(data) {
	            console.log(data.responseText);
	        }
	    });
	});
});
</script>
</body>
</html>