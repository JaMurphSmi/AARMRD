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

function checkFieldNeededAndType(indexOfChange) {
	var algoSelectBox = document.getElementById("modChosen_" + indexOfChange).value;
	
	if (algoSelectBox === "recursive cl-diversity") {
		document.getElementById("exValues_" + indexOfChange).readOnly = false;
		document.getElementById("exValues_" + indexOfChange).style.backgroundColor = '#ffffff';
		document.getElementById("attValues_" + indexOfChange).selectedIndex = "1";
		return true;
	}
	else if (algoSelectBox === "k-anonymity") {
		document.getElementById("valuesForMod_" + indexOfChange).step = '1';
		document.getElementById("attValues_" + indexOfChange).selectedIndex = "1";
		document.getElementById("exValues_" + indexOfChange).readOnly = true;
		document.getElementById("exValues_" + indexOfChange).style.backgroundColor = '#d1d1d1';
	}
	else if (algoSelectBox === "- - NONE - -") {
		document.getElementById("attValues_" + indexOfChange).selectedIndex = "0";
		document.getElementById("valuesForMod_" + indexOfChange).step = '0.1';
		document.getElementById("exValues_" + indexOfChange).value = '0';
		document.getElementById("exValues_" + indexOfChange).readOnly = true;
		document.getElementById("exValues_" + indexOfChange).style.backgroundColor = '#d1d1d1';
	}
	else {
		document.getElementById("attValues_" + indexOfChange).selectedIndex = "2";
		document.getElementById("valuesForMod_" + indexOfChange).step = '0.1';
		document.getElementById("exValues_" + indexOfChange).value = '0';
		document.getElementById("exValues_" + indexOfChange).readOnly = true;
		document.getElementById("exValues_" + indexOfChange).style.backgroundColor = '#d1d1d1';
		return false;
	}
}