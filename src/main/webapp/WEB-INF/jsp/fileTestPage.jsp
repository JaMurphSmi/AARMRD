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
  <h2>Submitted File</h2>
<table>
    <tr>
        <td>Data FileName</td>
        <td>${fileName}</td>
    </tr>
    <c:forEach items="${dataCols}" var="col">
	    <tr>
		    <td>
		       	${col}     
		    </td>
	    </tr>
    </c:forEach>
    
</table>
<table>
	<tr>
		<th>Hierarchy FileNames</th>
	<tr>
	<c:forEach items="${hierNames}" var="hier">
		<tr>
			<td>
				${hier}
			</td>
		</tr>
	</c:forEach>
</table>
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