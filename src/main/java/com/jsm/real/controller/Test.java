package com.jsm.real.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@Controller
public class Test {
	@RequestMapping("/test")
	//@ResponseBody
	public String hello() {
		return "hello"; 
	}
	
	 @RequestMapping("/index")//value = "/index", method = RequestMethod.GET)    
	 public String index(Model model) {           
		 model.addAttribute("name", "jack");       
		 model.addAttribute("age", 20);       
		 model.addAttribute("info", "我是一个爱学习的好青年");    
		 System.out.println("------------------reached this");
		 return "index";
	 }
		
}
