function validate() {
	var orgName = document.getElementById("orgName").value;
	var empName = document.getElementById("empName").value;
	if (orgName == '') {
		alert('Please enter a valid organization name.');
		return false;
	} 
	else if (empName == '') {
		alert('Please enter a valid employee name.');
		return false;
	}
	else {
		return true;
	}
}