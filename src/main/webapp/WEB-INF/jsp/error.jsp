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
<title>AARMRD Fatal Error</title>
<link rel="icon" href="https://i.imgur.com/AEUtoVg.png" sizes="32x32" type="image/png">
</head>
<body>
	<div class="americasNextTopDiv"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
  <h1>Error</h1>
  <hr>
  <div align=center>You have encountered an error<br>
  apologies for inconvenience caused</div>
  <div class="form">
    <form action="/hadAnEror" method="post" onsubmit="return validate()">
      <input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
      <table>
        <tr>
          <td><p class="tableText">Return to Home</p></td>
          <td><input type="submit" class="formSubmitButton loginSubmit" value="Return"></td>
        </tr>
      </table>
    </form>
  </div>
  <table id="tableErr">
        <tr>
            <td><p class="tableText">Date</p></td>
            <td><p class="tableText">${timestamp}</p></td>
        </tr>
        <tr>
            <td><p class="tableText">Error</p></td>
            <td><p class="tableText">${error}</p></td>
        </tr>
        <tr>
            <td><p class="tableText">Status</p></td>
            <td><p class="tableText">${status}</p></td>
        </tr>
        <tr>
            <td><p class="tableText">Message</td>
            <td><p class="tableText">${message}</p></td>
        </tr>
        <tr>
            <td><p class="tableText">Exception</p></td>
            <td><p class="tableText">${exception}</p></td>
        </tr>
        <tr>
            <td><p class="tableText">Trace</p></td>
            <td>
                <pre><p class="tableText">${trace}</p></pre>
            </td>
        </tr>
    </table><br><br>
</body>
</html>