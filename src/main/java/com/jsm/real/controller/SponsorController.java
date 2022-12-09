package com.jsm.real.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsm.real.entity.Author;
import com.jsm.real.entity.AuthorBook;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Event;
import com.jsm.real.entity.SeminarSponsor;
import com.jsm.real.entity.Sponsor;
import com.jsm.real.service.EventService;
import com.jsm.real.service.SponsorService;
import com.jsm.real.util.StringUtils;

@Controller
public class SponsorController extends BaseController {
	@Autowired
	SponsorService sponsorService;
	@Autowired
	EventService eventService;
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

	@RequestMapping("/sponsorSemList")
	public String sponsorSemList(Model model, HttpSession session) {
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		List<SeminarSponsor> semSponsorParts = new ArrayList<>();
		List<Event> eventParts = new ArrayList<>();
		List<Sponsor> sponsorParts = new ArrayList<>();
		sponsorService.getSemSponsorList(2000, null, semSponsorParts, sponsorParts, eventParts);
		model.addAttribute("semSponsorList", semSponsorParts);
		model.addAttribute("eventList", eventParts);
		model.addAttribute("sponsorList", sponsorParts);
		return "sponsorSemPage";		
	}
	
	// this request redirects to add a sponsor page
	@RequestMapping("/querySponsorSem")
	public String querySponsorSem(Model model, SeminarSponsor semSponsor, HttpSession session) {	
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		List<SeminarSponsor> semSponsorParts = new ArrayList<>();
		List<Event> eventParts = new ArrayList<>();
		List<Sponsor> sponsorParts = new ArrayList<>();
		sponsorService.getSemSponsorList(2000, semSponsor, semSponsorParts, sponsorParts, eventParts);
		model.addAttribute("semSponsorList", semSponsorParts);
		model.addAttribute("eventList", eventParts);
		model.addAttribute("sponsorList", sponsorParts);
		return "sponsorSemPage";
	}
	// this request redirects to add a sponsor page
	@RequestMapping("/addSponsorSem")
	public String addSponsorSem(Model model, SeminarSponsor semSponsor, HttpSession session) {
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		List<SeminarSponsor> semSponsorParts = new ArrayList<>();
		List<Event> eventParts = new ArrayList<>();
		List<Sponsor> sponsorParts = new ArrayList<>();
		sponsorService.getSemSponsorList(2000, null, semSponsorParts, sponsorParts, eventParts);
		model.addAttribute("semSponsorList", semSponsorParts);
		model.addAttribute("eventList", eventParts);
		model.addAttribute("sponsorList", sponsorParts);
		// only care about the mapping relationship between author and book
		// both needs to be specified
		if(semSponsor.getEvent_id()==null||semSponsor.getSpid()==null||semSponsor.getSponsor_amt()==null||semSponsor.getSponsor_amt()<=0) {
			model.addAttribute("submit_res", "Cannot add the sponsor seminar relationship! Please specify all required fields");
			return "sponsorSemPage";
		}
		// both author and book needs to exist
		if(!sponsorService.existsSponsor(semSponsor.getSpid())||!eventService.existsSeminar(semSponsor.getEvent_id())) {
			model.addAttribute("submit_res", "Cannot add the sponsor seminar relationship! Please provide valid spid and seminar id");
			return "sponsorSemPage";
		}
		// the mapping needs to not exist already
		if(sponsorService.existsSemSponsor(semSponsor.getSpid(), semSponsor.getEvent_id())) {
			model.addAttribute("submit_res", "Duplicate! Cannot add the sponsor seminar relationship!");
			return "sponsorSemPage";			
		}
		// after input validation, can now insert the mapping
		sponsorService.addSemSponsor(semSponsor);
		model.addAttribute("submit_res", "Successfully added an sponsor/seminar entry!");
		semSponsorParts = new ArrayList<>();
		eventParts = new ArrayList<>();
		sponsorParts = new ArrayList<>();
		sponsorService.getSemSponsorList(2000, null, semSponsorParts, sponsorParts, eventParts);
		model.addAttribute("semSponsorList", semSponsorParts);
		model.addAttribute("eventList", eventParts);
		model.addAttribute("sponsorList", sponsorParts);
		return "sponsorSemPage";
	}	
	// this request redirects to add a sponsor page
	@RequestMapping("/deleteSponsorSem")
	public String deleteSponsorSem(Model model, SeminarSponsor semSponsor, HttpSession session) {
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		List<SeminarSponsor> semSponsorParts = new ArrayList<>();
		List<Event> eventParts = new ArrayList<>();
		List<Sponsor> sponsorParts = new ArrayList<>();
		sponsorService.getSemSponsorList(2000, null, semSponsorParts, sponsorParts, eventParts);
		model.addAttribute("semSponsorList", semSponsorParts);
		model.addAttribute("eventList", eventParts);
		model.addAttribute("sponsorList", sponsorParts);	
		// only care about the mapping relationship between author and book
		// both needs to be specified
		if(semSponsor.getEvent_id()==null||semSponsor.getSpid()==null) {
			model.addAttribute("submit_res", "Cannot delete the author book relationship!  Please provide valid spid and seminar id");
			return "sponsorSemPage";
		}		
		// the mapping needs to exist already
		if(!sponsorService.existsSemSponsor(semSponsor.getSpid(), semSponsor.getEvent_id())) {
			model.addAttribute("submit_res", "The sponsor Seminar relationship does not exist!");
			return "sponsorSemPage";			
		}
		// after input validation, can now delete the mapping
		semSponsor = sponsorService.findEntityBy(semSponsor.getEvent_id(),semSponsor.getSpid());
		sponsorService.delSemSponsor(semSponsor);
		model.addAttribute("submit_res", "Successfully deleted an sponsor/Seminar entry!");
		semSponsorParts = new ArrayList<>();
		eventParts = new ArrayList<>();
		sponsorParts = new ArrayList<>();
		sponsorService.getSemSponsorList(2000, null, semSponsorParts, sponsorParts, eventParts);
		model.addAttribute("semSponsorList", semSponsorParts);
		model.addAttribute("eventList", eventParts);
		model.addAttribute("sponsorList", sponsorParts);
		return "sponsorSemPage";
	}
	
	
}
