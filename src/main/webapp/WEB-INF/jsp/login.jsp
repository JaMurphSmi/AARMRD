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
<h1>Login</h1>
<hr>
	<div class="armedGuard"><img src="https://i.imgur.com/zIg1yrH.png"/></div><br>
	<c:if test="${param.error}">
	<div class="alert alert-error">    
         <p>Invalid username and password.</p>
    </div></c:if>
    <c:if test="${param.logout}"><div class="alert alert-success"> 
         <p>You have been logged out. Thank you for using AARMRD</p>
    </div></c:if>
  <p id="blerb" class="centred"><strong>Halt! Before you enter, please let us know who we're dealing with</strong></p>
  <div class="loginForm">
    <form action="/home" id="innerLoginForm" method="post" onsubmit="return validate()">
    	<label class="text" for="empName">Employee Name</label>
                <input type="text" id="empName" name="empName"/><br><br>       
        <label class="text" for="orgName">Organization Name</label>
                <input type="text" id="orgName" name="orgName"/>
    	<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" /><br><br>
        <input type="submit" class="formSubmitButton loginSubmit" value="Enter"/>
    </form>
  </div>
</body>
</html>