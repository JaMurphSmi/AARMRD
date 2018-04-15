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
	<div class="armedGuard"><img src="https://i.imgur.com/lf2lect.png"/></div><br>
	<c:if test="${param.error}">
	<div class="alert alert-error">    
         <p>Invalid username and password.</p>
    </div></c:if>
    <c:if test="${param.logout}"><div class="alert alert-success"> 
         <p>You have been logged out. Thank you for using AARMRD</p>
    </div></c:if>
  <div id="blerbDiv"><p id="blerb"><strong>Halt! Before you enter, please let us know who we're dealing with</strong></p></div>
  <div class="loginForm">
    <form action="/home" method="post" onsubmit="return validate()">
    	<label for="empName">Employee Name</label>
                <input type="text" id="empName" name="empName"/><br>       
                <label for="orgName">Organization Name</label>
                <input type="text" id="orgName" name="orgName"/>
    	<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
        <input type="submit" class="formSubmitButton" value="Enter"/>
    </form>
  </div>
</body>
</html>