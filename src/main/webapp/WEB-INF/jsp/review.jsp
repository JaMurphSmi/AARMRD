<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

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
  <br><br>
  <form:form action="/testORM" method="POST" modelAttribute="testExample">
	  <table>
        <tr>
		  <td><form:label path="username">Username: </form:label></td>
		  <td><form:input path="username"/></td>
		</tr>
		<tr>
		  <td><form:label path="userIp">User Ip: </form:label></td>
		  <td><form:input path="userIp"/></td>
		</tr>
		<tr>
		  <td><form:label path="photosSent">Photo Sent: </form:label></td>
		  <td><form:input path="photosSent"/></td>
		 </tr>
		 <tr><td><input type="submit" value="Submit"></td></tr>
		</table>
  </form:form>
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
  <br><br>
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