package com.jsm.real.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends BaseController {
	@RequestMapping("/login")
	public final String loginPage() {
		return "login";
	}
	@RequestMapping("/logout")
	public String logout(Model model, HttpSession session) {
		session.invalidate();
		return "login";
	}	


}

