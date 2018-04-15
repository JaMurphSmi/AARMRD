<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/style.css" />
<script type="text/javascript" src="js/app.js"></script>
<style>
</style>
<title>Spring Boot - Error</title>
</head>
<body>
	<div class="americasNextTopDiv"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h1>Error</h1>
  <hr>
  <div align=center>You have encountered an error<br>
  apologies for inconvenience caused</div>
  <table id="tableErr">
        <tr>
            <td>Date</td>
            <td>${timestamp}</td>
        </tr>
        <tr>
            <td>Error</td>
            <td>${error}</td>
        </tr>
        <tr>
            <td>Status</td>
            <td>${status}</td>
        </tr>
        <tr>
            <td>Message</td>
            <td>${message}</td>
        </tr>
        <tr>
            <td>Exception</td>
            <td>${exception}</td>
        </tr>
        <tr>
            <td>Trace</td>
            <td>
                <pre>${trace}</pre>
            </td>
        </tr>
    </table><br><br>
  <div class="form">
    <form action="/" method="post" onsubmit="return validate()">
      <input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
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