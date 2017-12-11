package org.anonymize.anonymizationapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.anonymize.anonymizationapp.model.Person;
import org.anonymize.anonymizationapp.dao.ExampleDaoImpl;
import org.anonymize.anonymizationapp.model.ExampleObject;


//import org.anonymize.anonymizationapp.controller.AnonymizationController;
//won't work, must be an object to do this
//@Autowired
//AnonymizationController anonControl;


@Controller
public class ReviewController {//potentially for utility AND risk analysis
	
	@Autowired 
	ExampleDaoImpl exampleDaoImpl;

   @RequestMapping("/review")
   public String index(Model model) {
	   model.addAttribute("revMessage", "This is where the reviews will be placed");
	   
	   List<Person> people = new ArrayList<Person>();//make a few objects and shit
	   people.add(new Person(1,"Ted", "Dunphy", "Manager", "Ireland"));
	   people.add(new Person(2,"Fred", "Duffy", "Sales Assistant", "Ireland"));
	   people.add(new Person(3,"Fuad", "Ganasse", "Sales Assistant", "Spain"));
	   people.add(new Person(4,"Tovski", "Yobaniskov", "Sales Assistant", "Russia"));
	   
	   model.addAttribute("weThePeople", people);
	   
	// we record this request in the database
	     ExampleObject exampObject = new ExampleObject();
	     exampObject.setDateAccessed(new Date());
	     exampObject.setPhotosSent("Http-dskjn.jeg");   
	     
	     //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     //model.sendUsername()//how to get the 
	     String username = "Lads";
	     exampObject.setUsername(username);
	     exampObject.setUserIp("180.0.101.126");
	     
	       exampleDaoImpl.save(exampObject);
	   
      return "review";
   }
}
