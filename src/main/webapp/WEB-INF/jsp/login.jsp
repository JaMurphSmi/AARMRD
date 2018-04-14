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
	<div class="armedGuard"><img src="https://i.imgur.com/lf2lect.png"/></div>
  <div align=center><p id="blerb"><strong>Halt! Before you enter, please let us know who we're dealing with</strong></p></div>
  <div class="loginForm">
    <form action="/home" method="post" onsubmit="return validate()">
    	<input name="orgName" type="text" value="Please enter your organization's name" required/>
    	<input name="empName" type="text" value="Please enter your name/personnel number" required/>
        <input type="submit" value="Enter"/>
    </form>
  </div>
</body>
</html>