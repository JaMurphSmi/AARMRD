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
  <h1>Anonymization application Anonymization Page</h1>
  <hr>
  <table>
  <tr><th>Numbers to display sample jstl dynamic tag</th></tr>
  <c:forEach items="${figures}" var="arryNum">
  <tr><td>${arryNum}</td></tr>
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
    <form action="review" method="post" onsubmit="return validate()">
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