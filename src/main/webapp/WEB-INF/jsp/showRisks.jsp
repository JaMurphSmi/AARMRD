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
    width: 400px;
    background-color: #555;
    color: #fff;
    text-align: left;
    border-radius: 6px;
    padding: 5px 0;
    position: absolute;
    z-index: 1;
    bottom: 125%;
    left: 50%;
    margin-left: -200px;
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

table, th, td {
    border: 1px solid black;
     border-collapse: collapse;
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
	<div style="border-style:ridge;border-color:green;float:left;margin-left:100px;margin-top:50px; padding: 25px 25px 25px 25px;max-width:550px;">
			<h3>Set your Risk Assessment Details</h3>			
			<form action="/analyseRisks" method="POST">
				<label for="populationRegion">Select Region : </label>
					<select name="populationRegion">
						<c:forEach items="${countries}" var="country">
							<option value="${country}">${country}</option>
						</c:forEach>
					</select>
				<label for="THRESHOLD">Threshold : </label>
					<input name="THRESHOLD" id="THRESHOLD" type="number" min="0" max="1" step="0.01"/>
					<div class="tooltip">&#10068;
			  			<span class="tooltiptext">
			  			This page measures percentage risk of your dataset<br>
			  			The country selection box denotes the region that you are testing against<br>
			  			The threshold is the percentage risk that a record must not surpass, begins at 0, max 1(100%). ie threshold set to 0.5(50%)<br>
			  			success if 47%, failure if 53%. It is to allow the user to control the risk posed to their records.
			  			</span>
						</div>
					<br><br>
					<input type="submit" value="Submit Your Files">
			</form>
	</div>
	<c:if test="${not empty riskObject}">
		<div style="float:left;margin-left: 100px;max-width:1050px;padding: 15px 15px 15px 15px;overflow: scrollable;">
				<c:if test="${not empty riskObject.threshold}">
					Threshold Specified : ${riskObject.threshold}		
				</c:if>
				<c:if test="${not empty riskObject.country}">
					Country : ${riskObject.country} <br><br><br>
				</c:if>
				<c:if test="${not empty riskObject.prosecutorStats && not empty riskObject.journalistStats && not empty riskObject.marketerStat}">
					<h4>Attacker Models Statistics</h4> <br><br>
					<table style="float: left;">
						<tr>
							<th></th>
							<th>Prosecutor Attack Model</th>
							<th>Journalist Attack Model</th>
							<th>Marketer Attack Model</th>
						</tr>
						<tr>
							<td>Records at Risk</td>
							<td>${riskObject.prosecutorStats[0]}</td>
							<td>${riskObject.journalistStats[0]}</td>
							<td></td>
						</tr>
						<tr>
							<td>Highest Risk</td>
							<td>${riskObject.prosecutorStats[1]}</td>
							<td>${riskObject.journalistStats[1]}</td>
							<td></td>
						</tr>
						<tr>
							<td>Success Rate</td>
							<td>${riskObject.prosecutorStats[2]}</td>
							<td>${riskObject.journalistStats[2]}</td>
							<td>${riskObject.marketerStat}</td>
						</tr>
					</table>
				</c:if>
				<br>
				<!-- from here down is all risk metrics -->
				<c:if test="${not empty riskObject.dataSetInputDistributionMetrics && not empty riskObject.dataSetOutputDistributionMetrics}">
					<div align="center" style="float:left;">
						Input Data Set Value Distribution Statistics <br><br>
						<!-- make the whole table in a loop? --> 
						<c:forEach items="${riskObject.dataSetInputDistributionMetrics}" var="inputMapEntry">
							<table style="float: left;">
								<tr>
									<th colspan="2"><c:out value="${inputMapEntry.key}"/></th>
								</tr>
								<tr>
									<td>Value</td>
									<td>Frequency</td>
								</tr>
								<c:forEach items="${inputMapEntry.value}" var="inputMapValueEntry">
									<tr>
										<td><c:out value="${inputMapValueEntry.key}"/></td>
										<td><c:out value="${inputMapValueEntry.value}"/></td>
									</tr>
								</c:forEach>
							</table>
						</c:forEach>
					</div>
					<br><br>
					<div align="center" style="float:left;">
						Output Data Set Value Distribution Statistics <br><br>
						<c:forEach items="${riskObject.dataSetOutputDistributionMetrics}" var="outputMapEntry">
							<table style="float: left;">
								<tr>
									<th colspan="2"><c:out value="${outputMapEntry.key}"/></th>
								</tr>
								<tr>
									<td>Value</td>
									<td>Frequency</td>
								</tr>
								<c:forEach items="${outputMapEntry.value}" var="outputMapValueEntry">
									<tr>
										<td><c:out value="${outputMapValueEntry.key}"/></td>
										<td><c:out value="${outputMapValueEntry.value}"/></td>
									</tr>
								</c:forEach>
							</table>
						</c:forEach>
					</div>
				</c:if>
		</div>

	</c:if>
</div>	
</body>
</html>