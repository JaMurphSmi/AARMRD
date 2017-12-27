# AnonProject

author: Jake Murphy Smith

This Spring-Boot application is intended to provide users with access to a variety of utilities that provide and compliment the operation of data anonymization. This application is focused specifically at the anonymization of medical data for the purpose of secure distribution to third parties.

To implement the anonymization functionality it is intended to make use of the open-source ARX Deidentification API library available @: https://github.com/arx-deidentifier/arx
ARX (C) 2012-2017 Fabian Prasser, Florian Kohlmayer and Contributors.

In the future it is also hoped to incorporate other anonymization suites to extend functionality and diversify options.

****File Format Notes:

For best results

Notes for input data file formats: 
CSV: Please ensure the data contained within is separated by a semi-colon, ';' character and 	 	 	 distinguish lines via a carriage return
	 Place the headings for your fields on the top of the file ie age;gender;postcode;phoneno 
	 Then place all corresponding data rows beneath ie 35;male;84324;086143565
	 
Notes for hierarchy file formats:
CSV: No heading is required for this data file, all that is required is your defined hierarchy, 	 	 separated by a ';' ie age hierarchy: 34;<50;<=65;*
										  43;<50;<=65;*
										  24;<30;<50;<=65;*