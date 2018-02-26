<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/main/resources/static/css/style.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/main/resources/static/js/app.js"></script>
<title>Spring Boot - Error</title>
</head>
<body style="background-color:#faf6b8;">
  <h1>Error</h1>
  <hr>
  <div align=center>You have encountered an error<br>
  apologies for inconvenience caused</div>
  <div class="form">
    <form action="/" method="post" onsubmit="return validate()">
      <table>
        <tr>
          <td>Return to Home</td>
          <td><input type="submit" value="Return"></td>
        </tr>
      </table>
    </form>
  </div>
</body>
</html>