package org.anonymize.anonymizationapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloController {

   @RequestMapping("/")
   public String index() {
      return "index";
   }
   //first proper page created in this Spring project
   @RequestMapping("/hello")//requestparam only needed if drawing a specific named attribute from calling jsp
   public String sayHello(@RequestParam("name") String name, Model model) {
      model.addAttribute("name", name);
      return "hello";
   }
   
   @RequestMapping("/home")
   public String welcomeHome(Model model) {
	   
	   model.addAttribute("message", "This will be the home page");
	   return "home";
   }
}