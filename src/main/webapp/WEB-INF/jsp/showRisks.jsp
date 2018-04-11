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
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<style>
.button {
	  background-color: ForestGreen;  
	  border-radius: 5px;
	  color: white;
	  padding: .5em;
	  text-decoration: none;
	}

	.button:focus,
	.button:hover {
	  background-color: DarkGreen;
	}

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
 <div style="z-index: 10;position: absolute;right: 30px;top: 0px;"><img src="https://i.imgur.com/hLCDoAZ.png"/></div>
	<h1>Review Risk Metrics</h1><br>
  	<hr>
	<div style="overflow:hidden;">
		<div style="float:left;margin-left: 100px;max-width:550px;height:400px;overflow:auto;">
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
					<input type="submit" class="button" value="Submit Your Files">
			</form><br><br>
			<form action="/returnSender" method="post">
				<input type="submit" class="button" value="Return to Comparison"/> 
			</form>
		</div>
	</div>
<div>
	<c:if test="${not empty riskObject}">
		<div style="float:center;margin-left: 100px;max-width:1050px;padding: 15px 15px 15px 15px;overflow: scrollable;">
				<a href="/doJasperReport" class="button">Generate Report</a><br><br>
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
			</div>
				<br><br><br>
				<!-- from here down is all risk metrics -->
				<c:if test="${not empty riskObject.dataSetInputDistributionMetrics && not empty riskObject.dataSetOutputDistributionMetrics}">
					<div align="center" style="width:1455px;">
						<div style="border-color:green;margin-left:50px;float:left;width:700px;overflow:hidden;">
							Input Data Set Value Distribution Statistics <br><br>
							<!-- make the whole table in a loop? --> 
							<c:forEach items="${riskObject.dataSetInputDistributionMetrics}" var="inputMapEntry">
								<div style="float:left;width:700px;overflow:hidden;">
									<div style="float:left;width:225px;overflow:auto;">
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
													<td><c:out value="${inputMapValueEntry.value}%"/></td>
												</tr>
											</c:forEach>
										</table>
									</div>
									&nbsp<!-- generate divs for piecharts at the end of each iteration -->
									<div style="float:left;" id="${inputMapEntry.key}"></div>
								</div>
							</c:forEach>
						</div>
						<div style="border-color:green;float:left;width:700px;overflow:hidden;">
							 Output Data Set Value Distribution Statistics <br><br>
							<c:forEach items="${riskObject.dataSetOutputDistributionMetrics}" var="outputMapEntry">
								<div style="float:left;width:700px;overflow:hidden;">
									<div style="float:left;width:225px;overflow:auto;">
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
												<td><c:out value="${outputMapValueEntry.value}%"/></td>
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
	$("#makeJasperReport").click(function(){
	    $.ajax({
	        url : 'doJasperReport',
	        method : 'GET',
	        async : false,
	        complete : function(data) {
	            console.log(data.responseText);
	        }
	    });
	});
});
</script>
</body>
</html>