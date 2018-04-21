<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/style.css">
<script type="text/javascript" src="js/app.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<title>Risk Metrics</title>
<link rel="icon" href="https://i.imgur.com/AEUtoVg.png" sizes="32x32" type="image/png">
</head>
<body>
 <div class="americasNextTopDiv"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
	<h1>Review Risk Metrics</h1><br>
  	<hr>
  	<!-- styled to float over the submit box -->
  	<div style="overflow:hidden;">
			<div style="float:left;margin-left:50px;max-width:550px;height:400px;overflow:auto;">
					<table id="tablesR">
						<tr>
							<c:forEach items="${headerRow}" var="head"><!-- maybe add a percentage column? -->
								<th class="cADTH" align="center">
									<p class="tableText">${head}</p>
								</th>
							</c:forEach>
						</tr>
						    <c:forEach items="${anonyRows}" var="anonRow">
							    <tr>
							    	<c:forEach items="${anonRow}" var="anonItem">
									    <td class="cellsSR" align="center">
									       	<p class="tableText">${anonItem}</p>     
									    </td>
									</c:forEach>    
							    </tr>
						    </c:forEach>
					</table>
			</div>
			<div id="setRiskDetails">
				<h2><p class="tableText">Set your Risk Assessment Details</p></h2>			
				<form action="/analyseRisks" method="POST">
					<label for="populationRegion"><span style="color: #f4dc24;">Select Region : </span></label>
						<select name="populationRegion">
							<c:forEach items="${countries}" var="country">
								<option value="${country}">${country}</option>
							</c:forEach>
						</select> 
						<div class="tooltipsR">&#10068;
							<span class="tooltiptext">
								This page measures percentage risk of your dataset<br>
								The country selection box denotes the region that you are testing against<br>
								The threshold is the percentage risk that a record must not surpass, begins at 0, max 1(100%). ie threshold set to 0.5(50%)<br>
								success if 47%, failure if 53%. It is to allow the user to control the risk posed to their records.
							</span>
						</div>
					<label for="THRESHOLD"><span style="color: #f4dc24;">Threshold : </span></label>
						<input name="THRESHOLD" id="THRESHOLD" type="number" min="0" max="1" step="0.01"/>
						<input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" />
						<br><br>
						<input type="submit" class="buttonsR" value="Submit Your Files">
				</form><br><br>
				<form action="/returnSender" method="post">
					<input type="hidden" name="${_csrf.parameterName}"
							value="${_csrf.token}" />
					<input type="submit" class="buttonsR" value="Return to Comparison"/> 
				</form>
				
			</div>
		</div>
<div>
	<c:if test="${not empty riskObject}">
		<div id="theRiskyDiv">
				<button id="generateReport" class="buttonsR" onclick="makeDownloadButtonEnabled()">Generate Risk Report</button>
				&nbsp&nbsp 
				<div id="downloadDiv" style="display:none;"><a href="/doPDFDownload" class="buttonsR">Download Risk Report</a></div>
				<br><br><div class="form"><p class="tableText1">
				<c:if test="${not empty riskObject.threshold}">
					Threshold Specified : ${riskObject.threshold}		
				</c:if>&nbsp&nbsp&nbsp
				<c:if test="${not empty riskObject.country}">
					Country : ${riskObject.country}<br>
				</c:if></p></div>
				<c:if test="${not empty riskObject.prosecutorStats && not empty riskObject.journalistStats && not empty riskObject.marketerStat}">
					<h2>Attacker Models Statistics</h2> 
					<table style="float:left;border-collapse: collapse;">
						<tr>
							<th></th>
							<th class="sRTh"><p class="tableText">Prosecutor Model</p></th>
							<th class="sRTh"><p class="tableText">Journalist Model</p></th>
							<th class="sRTh"><p class="tableText">Marketer Model</p></th>
							<th class="sRTh"><p class="tableText">GDPR Compliant</p></th>
						</tr>
						<tr>
							<td class="sRTh"><p class="tableText">Records at Risk</p></td>
							<td class="srTd"><p class="tableText">${riskObject.prosecutorStats[0]}</p></td>
							<td class="srTd"><p class="tableText">${riskObject.journalistStats[0]}</p></td>
							<td></td>
						</tr>
						<tr>
							<td class="sRTh"><p class="tableText">Highest Risk</p></td>
							<td class="srTd"><p class="tableText">${riskObject.prosecutorStats[1]}</p></td>
							<td class="srTd"><p class="tableText">${riskObject.journalistStats[1]}</p></td>
							<td></td>
						</tr>
						<tr>
							<td class="sRTh"><p class="tableText">Success Rate</p></td>
							<td class="srTd"><p class="tableText">${riskObject.prosecutorStats[2]}</p></td>
							<td class="srTd"><p class="tableText">${riskObject.journalistStats[2]}</p></td>
							<td class="srTd"><p class="tableText">${riskObject.marketerStat}</p></td>
							<td></td>
						</tr>
						<tr>
							<td class="sRTh"><p class="tableText">GDPR Compliant</p></td>
							<td class="srTdGDPR" style="background-color:${riskObject.prosecutorStats[4]};"><p class="tableText" id="GDPR1">${riskObject.prosecutorStats[3]}</p></td>
							<td class="srTdGDPR" style="background-color:${riskObject.journalistStats[4]};"><p class="tableText" id="GDPR2">${riskObject.journalistStats[3]}</p></td>
							<td></td>
						</tr>
					</table>
				</c:if>
			</div>
				<br><br><br><br><br><br>
				<!-- from here down is all risk metrics -->
				<c:if test="${not empty riskObject.dataSetInputDistributionMetrics && not empty riskObject.dataSetOutputDistributionMetrics}">
					<div id="riskGraphDiv" align="center">
						<div id="riskInnerDivLeft">
							<p class="textTable">Input Data Set Value Distribution Statistics</p> <br><br>
							<!-- make the whole table in a loop? --> 
							<c:forEach items="${riskObject.dataSetInputDistributionMetrics}" var="inputMapEntry">
								<div class="riskInnerDivItem">
									<div class="riskInnerDivFreqTable">
										<table class="riskTable">
											<tr>
												<th colspan="2" class="cellsSR"><c:out value="${inputMapEntry.key}"/></th>
											</tr>
											<tr>
												<td class="cellsSR">Value</td>
												<td class="cellsSR">Frequency</td>
											</tr>
											<c:forEach items="${inputMapEntry.value}" var="inputMapValueEntry">
												<tr>
													<td class="cellsSR"><c:out value="${inputMapValueEntry.key}"/></td>
													<td class="cellsSR"><c:out value="${inputMapValueEntry.value}%"/></td>
												</tr>
											</c:forEach>
										</table>
									</div>
									&nbsp<!-- generate divs for piecharts at the end of each iteration -->
									<div style="float:left;" id="${inputMapEntry.key}"></div>
								</div>
							</c:forEach>
						</div>
						<div id="riskInnerDivRight">
							 <p class="textTable">Output Data Set Value Distribution Statistics</p> <br><br>
							<c:forEach items="${riskObject.dataSetOutputDistributionMetrics}" var="outputMapEntry">
								<div class="riskInnerDivItem">
									<div class="riskInnerDivFreqTable">
										<table class="riskTable">
											<tr>
												<th colspan="2" class="cellsSR"><c:out value="${outputMapEntry.key}"/></th>
											</tr>
											<tr>
												<td class="cellsSR">Value</td>
												<td class="cellsSR">Frequency</td>
											</tr>
											<c:forEach items="${outputMapEntry.value}" var="outputMapValueEntry">
												<tr>
													<td class="cellsSR"><c:out value="${outputMapValueEntry.key}"/></td>
													<td class="cellsSR"><c:out value="${outputMapValueEntry.value}%"/></td>
												</tr>
											</c:forEach>
										</table>
									</div>
									&nbsp
									<div style="float:left;" id="${outputMapEntry.key}_1"></div>
								</div>
							</c:forEach>
						</div>
					</div>
				</c:if>
	</c:if>
</div>	
<script type="text/javascript">

    // Load the Visualization API and the piechart package.
    google.load('visualization', '1.0', {
        'packages' : [ 'corechart' ]
    });
 
    // Set a callback to run when the Google Visualization API is loaded.
    google.setOnLoadCallback(drawChartEverywhere);
 
    // Callback that creates and populates a data table,
    // instantiates the pie chart, passes in the data and
    // draws it.
    function drawChartEverywhere(value, index) {
 		//might be a crazy attempt, but going to try it anyway
    	<c:forEach items="${riskObject.dataSetInputDistributionMetrics}" var="entry">
		      //Create every chart in one visit to the javascript
    	
    			// Create the data table.    
		        var data = google.visualization.arrayToDataTable([
		                                                              ['Unique Value', 'Distribution']
		                                                              
		                                                                <c:forEach items="${entry.value}" var="entryValue">  
		                                                              		,[ '${entryValue.key}', ${entryValue.value} ]
		                                                              	</c:forEach>
		                                                        ]);
		        // Set chart options
		        var options = {
		            'title' : 'Distributions of Unique Values in Data Fields',
		            is3D : true,
		            pieSliceText: 'label',
		            tooltip :  {showColorCode: true},
		            'width' : 300,
		            'height' : 250,
		            backgroundColor: 'transparent'
		        };
		 
		        // Instantiate and draw our chart, passing in some options.
		        var chart = new google.visualization.PieChart(document.getElementById('${entry.key}'));//put chart in div via variable id
		        chart.draw(data, options);
        </c:forEach>
        <c:forEach items="${riskObject.dataSetOutputDistributionMetrics}" var="outEntry">
	      //Create every chart in one visit to the javascript
	
			// Create the data table.    
	        var data = google.visualization.arrayToDataTable([
	                                                              ['Unique Value', 'Distribution']
	                                                              
	                                                                <c:forEach items="${outEntry.value}" var="outEntryValue">  
	                                                              		,[ '${outEntryValue.key}', ${outEntryValue.value} ]
	                                                              	</c:forEach>
	                                                        ]);
	        // Set chart options
	        var options = {
	            'title' : 'Distributions of Unique Values in Data Fields',
	            is3D : true,
	            pieSliceText: 'label',
	            tooltip :  {showColorCode: true},
	            'width' : 300,
	            'height' : 250,
	            backgroundColor: 'transparent'
	        };
	 
	        // Instantiate and draw our chart, passing in some options.
	        var chart = new google.visualization.PieChart(document.getElementById('${outEntry.key}_1'));//put chart in div via variable id
	        chart.draw(data, options);
  		</c:forEach>
    }
    
</script><!-- need to throw a lot more in here to mould it to correct format -->
<script>
$(document).ready(function(){
	$("#generateReport").click(function(){
	    $.ajax({
	        url : 'makeJasperReport',
	        method : 'GET',
	        async : false,
	        complete : function(data) {
	            console.log(data.responseText);
	        }
	    });
	});									
});
</script>
<script>
function makeDownloadButtonEnabled() {
	document.getElementById('downloadDiv').style.display = 'inline';
}
</script>
</body>
</html>