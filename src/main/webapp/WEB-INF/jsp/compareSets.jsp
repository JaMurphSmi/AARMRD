<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<title>Compare Sets</title>
<link rel="icon" href="https://i.imgur.com/AEUtoVg.png" sizes="32x32" type="image/png">
</head>
<body style="background-color:#faf6b8;">
 <div style="z-index: 10;position: absolute;right: 30px;top: 0px;"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h1>Anonymization Results</h1>
<div style="float:left;height=400px;max-width=1050px;overflow=hidden;">
  	<br>
  	<c:if test="${not empty errorMessage}"><div><p class="tableText">${errorMessage}</p></div></c:if>
			
		<c:if test="${not empty dataRows}">
		<div id="origin" style="float:left;margin-left: 25px; max-width:425px;height:400px;overflow:auto;">
			<table style="border-collapse: collapse;">
				<tr>
					<c:forEach items="${headerRow}" var="head">
						<th align="center" class="cSDTH">
							<p class="tableText">${head}</p>
						</th>
					</c:forEach>
				</tr>
				    <c:forEach items="${dataRows}" var="dataRow">
					    <tr>
					    	<c:forEach items="${dataRow}" var="dataItem">
							    <td align="center" style="border:1px solid #d6aa44;">
							       	<p class="tableText">${dataItem}</p>     
							    </td>
							</c:forEach>    
					    </tr>
				    </c:forEach>
			</table>
		</div>
		</c:if>
		<c:if test="${not empty anonyRows}">
		<div id="culmination" style="float:left;margin-left: 25px; max-width:425px;height:400px;overflow:auto;">
			<table style="border-collapse: collapse;">
				<tr>
					<c:forEach items="${headerRow}" var="head">
						<th align="center" class="cADTH">
							${head}
						</th>
					</c:forEach>
				</tr>
				<c:forEach items="${anonyRows}" var="anonyRow">
					<tr>
						<c:forEach items="${anonyRow}" var="anony">
							<td align="center" style="border:1px solid #d6aa44;">
								<p class="tableText">${anony}</p>
							</td>
						</c:forEach>
					</tr>
				</c:forEach>
			</table>
		</div>
		</c:if>
</div>
	<div style="float:right;margin:25px 25px 25px 50px;padding:10px 10px 10px 10px;border:3px solid;border-color:#d5ce66;">
		<form action="/goToRiskPage" method="post">
			<input type="submit" class="buttonsR" value="Go To Risk Assessment"/> 
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		</form><br>
		<div class="tooltipcompSets">&#10068;<br>
				<span class="tooltiptext2">
				  	This option is provided to adhere to the General Data Protection Regulation.<br> 
				  	Clicking allows you to explicitly delete all content associated with your anonymization.<br>
				  	This is also automatically performed when you download your new data file.
				</span>
		</div>
		<button id="deleteDAH" class="buttonsR" onclick="makeRepeatButtonsDisabled()">Delete Data and Hierarchies</button>
		<br><br>
			<div class="tooltipcompSets">&#10068;
				<span class="tooltiptext3">
				  	Note that for your data's security, and to comply with the General Data Protection Regulation<br>
				  	Pressing this button concludes your service, and once your anonymized file is downloaded<br>
				  	to your machine, all data you have uploaded for this transaction will be erased from the application
				</span>
			</div>
			<a href="/downloadAnonymizedFile" class="buttonsR" onclick="makeRepeatButtonsDisabled()">Download Anonymized Data</a>
			<br><br>
		  <div>
		    <form action="/" method="post">
		      <input id="deleteDAHAF" type="submit" class="buttonsR" value="Begin Again/Upload New Files">
		    	<input type="hidden" name="${_csrf.parameterName}"
				  value="${_csrf.token}" />
		    </form>
		  </div><br>
		  <div>
		    <form action="/uploadFiles" method="post">
		      <input type="submit" id="canBeNullifiedByDelete" class="buttonsR" value="Use Same Data Again">
		      <input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		    </form>
		  </div><br>
		  <div>
		  <form action="/logout" method="post">
		      <input type="submit" class="buttonsR" value="Logout">
		      <input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
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
$(document).ready(function(){
	$("#deleteDAHAF").click(function(){
		$.ajax({
			url : 'deleteDataAndHierarchiesAnonymizedFile',
			method : 'GET',
			async : false,
			complete : function(data) {
				console.log(data.responseText);
			}
		});
	});
});
</script>
<script>
$("#origin").on("scroll",function(){
    $("#culmination:not(this)").scrollTop($(this).scrollTop());
});
</script>
<script>
$("#culmination").on("scroll",function(){
    $("#origin:not(this)").scrollTop($(this).scrollTop());
});
</script>
<script>
$("#origin").on("scroll",function(){
    $("#culmination:not(this)").scrollLeft($(this).scrollLeft());
});
</script>
<script>
$("#culmination").on("scroll",function(){
    $("#origin:not(this)").scrollLeft($(this).scrollLeft());
});
</script>
<script>
function makeRepeatButtonsDisabled(){
	document.getElementById("deleteDAH").disabled = true;
	document.getElementById("deleteDAH").style.background = '#7a7a7a'; 
	document.getElementById("canBeNullifiedByDelete").disabled = true;
	document.getElementById("canBeNullifiedByDelete").style.background = '#7a7a7a';
}
</script>
</body>
</html>