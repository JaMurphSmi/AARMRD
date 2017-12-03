<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/resources/css/style.css">
<script type="text/javascript" src="/resources/js/app.js"></script>
<title>Spring Boot</title>
</head>
<body>
  <h1>Anonymization application Review Page</h1>
  <hr>
  <table>
  <tr><th>First Name</th><th>Last Name</th><th>Title</th><th>Country</th></tr>
  <c:forEach items="${weThePeople}" var="person">
  <tr><td>${person.firstName}</td><td>${person.lastName}</td><td>${person.title}</td><td>${person.country}</td></tr>
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
  <div class="form">
    <form action="toDownload" method="post" onsubmit="return validate()">
      <table>
        <tr>
          <td>Continue to utility and risk assessment</td>
          <td><input type="submit" value="Continue"></td>
        </tr>
      </table>
    </form>
  </div>
</body>
</html>