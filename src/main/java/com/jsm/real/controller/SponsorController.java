package com.jsm.real.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsm.real.entity.Sponsor;
import com.jsm.real.service.SponsorService;

@Controller
public class SponsorController extends BaseController {
	@Autowired
	SponsorService sponsorService;
	@Transactional
	@RequestMapping("/addSponsor")
	public String addSponsor(Model model, Sponsor sponsor){
		// check null input
		if(sponsor.getFname()==null||sponsor.getLname()==null) {
			model.addAttribute("submit_res", "Invalid attempt! Please enter the valid sponsor name!");
			return "addSponsorPage";
		}
		// sanitize the input
		//TODO: PREVENT SQL INJECTION
		
		String fName = sponsor.getFname().trim().toUpperCase();
		String lName = sponsor.getLname().trim().toUpperCase();
		// on invalid input, or if containing non-alphabetic chars
		if(fName.length()<1||lName.length()<1||!fName.matches("[a-zA-Z]+")||!lName.matches("[a-zA-Z]+")) {
			model.addAttribute("submit_res", "Invalid attempt! Please enter the valid sponsor name!");
		} else {
			sponsorService.addSponsor(sponsor);
			model.addAttribute("submit_res", "You have successfully added a new sponsor.");
		}
		return "addSponsorPage";
	}
	// this GET request goes to the sponsor list page
	@RequestMapping("/sponsorList")
	public String showTopics(Model model) {
		List<Sponsor> spList = sponsorService.getSponsorList();
		model.addAttribute("sponsorList", spList);
		return "sponsorsPage";
	}
	// this request redirects to add a sponsor page
	@RequestMapping("/toAddSponsor")
	public String toAddSponsor() {
		return "addSponsorPage";
	}
	
}
