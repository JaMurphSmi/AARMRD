<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Iterator" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/resources/css/style.css">
<script type="text/javascript" src="/resources/js/app.js"></script>
<title>Spring Boot</title>
</head>
<body>
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
<table>
	<tr>
		<c:forEach items="${header}" var="head">
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
<div>
<table>
	<tr>
		<th>Hierarchy FileNames</th>
	</tr>
	<c:forEach items="${hierNames}" var="hier">
		<tr>
			<td>
				${hier}
			</td>
		</tr>
	</c:forEach>
</table>
</div>
</div>
<div>
<table>
	<tr>
		<th>Anonymized Data</th>
	</tr>
	<c:forEach items="${anonyRows}" var="anony">
		<tr>
			<td>
				${anony}
			</td>
		</tr>
	</c:forEach>
</table>
</div>
  <div class="form">
    <form action="home" method="post" onsubmit="return validate()">
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