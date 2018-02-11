<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
<div>
  <h2>Submitted File</h2>
  	<br><br>
			<table>
			    <tr>
			        <td>Data FileName</td>
			        <td>${fileName}</td>
			    </tr>
			    <tr>File contents</tr>
			</table>
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
	<div style="float:left;margin-left: 100px;">
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
  <div class="form">
    <form action="/" method="post" onsubmit="return validate()">
      <table>
        <tr>
          <td>Anonymize again</td>
          <td><input type="submit" value="Again"></td>
        </tr>
      </table>
    </form>
  </div>
</body>
</html>