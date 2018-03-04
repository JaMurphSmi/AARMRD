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
<title>Spring Boot</title>
</head>
<body style="background-color:#faf6b8;">
<div>
  <h3>Anonymization Results</h3>
  	<br>
			<table>
			    <tr>
			        <td>Data FileName</td>
			        <td>${fileName}</td>
			    </tr>
			    <tr>File contents</tr>
			</table>
		<div style="float:left;margin-left: 100px; max-width:550px;height:400px;overflow:scroll;">
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
	<div style="float:left;margin-left: 100px; max-width:550px;height:400px;overflow:scroll;">
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
</div>
<form action="/goToRiskPage" method="post">
	<input type="submit" value="Go To Risk Assessment"/> 
</form>
<button id="deleteDAH" >Delete Data and Hierarchies</button>&nbsp&nbsp&nbsp<br>
<a href="/downloadAnonymizedFile">Download Anonymized Data</a>
  <div class="form">
    <form action="/" method="post">
      <table>
        <tr><!-- need to do more here, replace dataset with result, have a retry button to link to beginning and drop everything -->
          <td><input type="submit" value="Anonymize"></td>
        </tr>
      </table>
    </form>
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