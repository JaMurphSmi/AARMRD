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
<title>AARMRD Login</title>
<link rel="icon" href="https://i.imgur.com/AEUtoVg.png" sizes="32x32" type="image/png">
</head>
<body>
<h1>Identification</h1>
<form action="/logout" id="logoutForm" method="post">
		      <input type="submit" class="formSubmitButton loginSubmit" value="Logout">
		      <input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
		    </form>
<hr>
	<div class="armedGuard"><img src="https://i.imgur.com/eKAi0aJ.png"/></div><br>
	<c:if test="${errorMsg}">
		<c:out value="${errorMsg}"/>
	</c:if>
  <p id="blerb" class="centred"><strong>Halt! Before you enter, please let us know who we're dealing with</strong></p>
  <div class="loginForm">
    <form action="/home" id="innerLoginForm" method="post" onsubmit="return validate()">
    	<label class="text" for="name">Employee Name</label>
                <input type="text" id="empName" name="empName"/><br><br>       
        <label class="text" for="orgName">Organization Name</label>
                <input type="text" id="orgName" name="orgName"/><br><br>
        <label class="text" for="empPass">Personal Password</label>
                <input type="password" id="empPass" name="empPass"/>
    	<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /><br><br>
        <input type="submit" class="formSubmitButton loginSubmit" value="Enter"/>
    </form>
  </div>
</body>
</html>