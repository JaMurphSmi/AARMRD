# Application for the Anonymization of Relational Medical Research Data (AARMRD) Project

author: Jake Murphy Smith  

This Spring-Boot application is intended to provide users with access to a variety of utilities that provide and compliment the operation of data anonymization. This application is focused specifically at the anonymization of medical data for the purpose of secure distribution to third parties.  
  
To implement the anonymization functionality it is intended to make use of the open-source ARX Deidentification API library available @: https://github.com/arx-deidentifier/arx
ARX (C) 2012-2017 Fabian Prasser, Florian Kohlmayer and Contributors.  
  
In the future it is also hoped to incorporate other anonymization suites to extend functionality and diversify options.  

****File Format Notes:  
  
For best results  
  
----------------------------------------------------------------------------------------------------------------------------
Notes for input data file formats:
----------------------------------------------------------------------------------------------------------------------------.

CSV: Please ensure the data contained within each line is separated by a semi-colon, ';' character. This is to avoid errors caused by comma ',' separated data affecting names and large numbers  
	 Please ensure that your dataset contains a header line defining the headings of your fields at the top of the file ie age;gender;postcode;phoneno  
	   
Excel: No special conditions are imposed on an Excel file. Best results with a standard Excel Workbook .xlsx file.
	 
----------------------------------------------------------------------------------------------------------------------------
Notes for hierarchy file formats:
----------------------------------------------------------------------------------------------------------------------------.	 

The only format accepted currently is CSV  
CSV: Please name your file in the format of [your dataset file's name]_[field name].csv to make import as successful as possible  
No heading is required inside this data file, all that is required is your defined hierarchy, with values separated by a ';' ie age hierarchy: 34;<50;<=65;*
										  