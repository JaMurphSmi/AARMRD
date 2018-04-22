package org.anonymize.anonymizationapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;
import org.springframework.validation.BindingResult;
import org.anonymize.anonymizationapp.dao.ExampleDaoImpl;
import org.anonymize.anonymizationapp.model.ExampleObject;
import org.anonymize.anonymizationapp.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;


//import org.anonymize.anonymizationapp.controller.AnonymizationController;
//won't work, must be an object to do this
//@Autowired
//AnonymizationController anonControl;


@Controller
public class LoginController {//potentially for utility AND risk analysis
	final static Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired 
	ExampleDaoImpl exampleDaoImpl;

	
	

	
   @RequestMapping("/wakeme") 
   public String getDetails(@RequestParam("orgName") String orgName, @RequestParam("empName") String empName,
		   Model model) {
	   //using HtmlUtils to clean the user input from login screen, avoid XSS reflection, do not touch
	   //a db to avoid potential persistent XSS and DOM based also
	   orgName = HtmlUtils.htmlEscape(orgName);	  
	   empName = HtmlUtils.htmlEscape(empName);
	   
	   
	   model.addAttribute("orgName", orgName);
	   model.addAttribute("empName", empName);
	   return "index";
   }
	
	@RequestMapping("/review")
   public String index(Model model) {
	   model.addAttribute("revMessage", "This is where the reviews will be placed");
	   
	   /*List<Person> people = new ArrayList<Person>();//make a few objects and shit
	   people.add(new Person(1,"Ted", "Dunphy", "Manager", "Ireland"));
	   people.add(new Person(2,"Fred", "Duffy", "Sales Assistant", "Ireland"));
	   people.add(new Person(3,"Fuad", "Ganasse", "Sales Assistant", "Spain"));
	   people.add(new Person(4,"Tovski", "Yobaniskov", "Sales Assistant", "Russia"));
	   */
	   //model.addAttribute("weThePeople", people);
	   model.addAttribute("testExample", new ExampleObject());
	   
	   	// we record this request in the database
	     ExampleObject exampObject = new ExampleObject();
	     exampObject.setDateAccessed(new Date());
	     exampObject.setPhotosSent("Http-dskjn.jeg");   
	     
	     
	     String username = "user1";
	     exampObject.setUsername(username);
	     exampObject.setUserIp("180.0.101.126");
	     
	     //exampleDaoImpl.save(exampObject);
	   
      return "review";
   }
   
   @RequestMapping(value = "/testORM", method = RequestMethod.POST)
   public String testORM(@Valid @ModelAttribute("testExample") ExampleObject testExample, BindingResult result, Model model) {
	   
	   testExample.setDateAccessed(new Date());
	   logger.info("The value of testExample username is: " + testExample.getUsername() +
			   ", userIp is: " + testExample.getUserIp() + ", photos_sent is: " + testExample.getPhotosSent()
			   + ", date is: " + testExample.getDateAccessed());
	   if (result.hasErrors()) {
	        return "error";
	    }
	   //attempting to request map object from jsp input and add it to the database
	   //exampleDaoImpl.save(testExample);
	   
	   return "review";
   }
}
