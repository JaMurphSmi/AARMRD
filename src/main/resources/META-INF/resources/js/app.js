function validate() {
	var orgName = document.getElementById("orgName").value;
	var empName = document.getElementById("empName").value;
	if (orgName === '') {
		alert('Please enter a valid organization name.');
		return false;
	} 
	else if (empName === '') {
		alert('Please enter a valid employee name.');
		return false;
	}
	else {
		return true;
	}
}

function checkFieldNeeded(indexOfChange) {
	var algoSelectBox = document.getElementById("modChosen_" + indexOfChange).value;
	
	if (algoSelectBox === "recursive cl-diversity") {
		document.getElementById("exValues_" + indexOfChange).readOnly = false;
		return true;
	}
	else if (algoSelectBox === "k-anonymity") {
		document.getElementByID("exValues_" + indexOfChange).steps = '1';
	}
	else {
		document.getElementById("exValues_" + indexOfChange).steps = '0.1';
		document.getElementById("exValues_" + indexOfChange).value = '0';
		document.getElementById("exValues_" + indexOfChange).readOnly = true;
		return false;
	}
}