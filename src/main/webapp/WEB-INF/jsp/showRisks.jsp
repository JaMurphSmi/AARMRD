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
<style>
.tooltip {
    position: relative;
    display: inline-block;
    border-bottom: 1px dotted black;
}

.tooltip .tooltiptext {
    visibility: hidden;
    width: 650px;
    background-color: #555;
    color: #fff;
    text-align: left;
    border-radius: 6px;
    padding: 5px 0;
    position: absolute;
    z-index: 1;
    bottom: 125%;
    left: 50%;
    margin-left: -325px;
    opacity: 0;
    transition: opacity 0.3s;
}

.tooltip .tooltiptext::after {
    content: "";
    position: absolute;
    top: 100%;
    left: 50%;
    margin-left: -5px;
    border-width: 5px;
    border-style: solid;
    border-color: #555 transparent transparent transparent;
}

.tooltip:hover .tooltiptext {
    visibility: visible;
    opacity: 1;
}
</style>
<title>Risk Metrics</title>
</head>
<body>
	<h3>Review Risk Metrics</h3>
	<hr>
<div>
	<div style="float:left;margin-left: 100px;max-width:550px;height:400px;overflow:scroll;">
			<table style="border-collapse: collapse;">
				<tr>
					<c:forEach items="${headerRow}" var="head"><!-- maybe add a percentage column? -->
						<th align="center" style="width:35px;border:1px solid #ddd;background-color: #e16830;">
							${head}
						</th>
					</c:forEach>
				</tr>
				    <c:forEach items="${anonyRows}" var="anonRow">
					    <tr>
					    	<c:forEach items="${anonRow}" var="anonItem">
							    <td align="center" style="border:1px solid #d6aa44;">
							       	${anonItem}     
							    </td>
							</c:forEach>    
					    </tr>
				    </c:forEach>
			</table>
	</div>
	<div style="float:left;margin-left: 100px;max-width:550px;overflow:scroll;">
			<form action="/analyseRisks" method="POST">
				<label for="populationRegion">Select Region : </label>
					<select name="populationRegion">
						<c:forEach items="${countries}" var="country">
							<option value="${country}">${country}</option>
						</c:forEach>
					</select>
				<label for="THRESHOLD">Threshold : </label>
					<input name="THRESHOLD" id="THRESHOLD" type="number" min="0" max="100" step="0.1"/>
					<div class="tooltip">&#10068;
  			<span class="tooltiptext">
  			This page measures percentage risk of your dataset<br>
  			The country selection box denotes the region that you are testing against<br>
  			The threshold is the percentage risk that a record must not surpass. ie threshold set to 50%<br>
  			success if 47%, failure if 53%. It is to allow the user to control the risk posed to their records.
  			</span>
			</div>
					<br><br>
					<input type="submit" value="Submit Your Files">
			</form>
	</div>
</div>	
</body>
</html>