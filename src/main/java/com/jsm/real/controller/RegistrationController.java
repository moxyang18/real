package com.jsm.real.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsm.real.entity.Event;
import com.jsm.real.entity.EventSemExhibiMute;
import com.jsm.real.entity.Registration;
import com.jsm.real.service.RegistrationService;
import com.jsm.real.util.StringUtils;
@Controller
public class RegistrationController extends BaseController {
	
	@Autowired
	RegistrationService registrationService;
	
	@RequestMapping("/regiList") 
	public String regiList(Model model) {
		model.addAttribute("regiList", registrationService.getEventList(new Registration()));
		return "regiListPage";
	}	
	
	@RequestMapping("/queryRegistration") 
	public String queryRegistration(Model model, Registration data) {
		model.addAttribute("regiList", registrationService.getEventList(data));
		return "regiListPage";
	}
	
	@Transactional
	@RequestMapping("/addRegistration")
	public String addRegistration(Model model, Registration data) {
		if(data.getEvent_id()==null||data.getUID()==null) {
			model.addAttribute("submit_res", "The registraition cannot be made! Please specify exhibition id and customer id.");
			return "regiListPage";
		}
		// do not want to modify
		data.setReg_id(null);
		registrationService.addRegistration(data);
		model.addAttribute("submit_res", "The invitation was successfully made!");
		// TODO this page for the employee's management, can see all users' registration
		model.addAttribute("inviList", registrationService.getEventList(new Registration()));
		return "regiListPage";
	}
	
	
	
	@Transactional
	@RequestMapping("/delRegistration")
	public String delRegistration(Model model, Registration data) {
		if(data.getReg_id()==null) {
			model.addAttribute("submit_res", "The registraition cannot be deleted! Please specify registration Number.");
			return "regiListPage";	
		}
		registrationService.delRegistration(data);
		model.addAttribute("submit_res", "The registraition was successfully deleted!");
		// TODO this page for the employee's management, can see all authors' invitations
		model.addAttribute("regiList", registrationService.getEventList(new Registration()));
		return "regiListPage";
	}
	
	
	// for each customer to view their own registration records
	@Transactional
	@RequestMapping("/viewOwnRegistration")
	public String viewOwnRegistration(Model model, HttpSession session) {
		List<Registration> regParts = new ArrayList<>();
		List<Event> exbParts = new ArrayList<>();
		registrationService.getRegInfoById(this.getUidFromSession(session), regParts, exbParts);
		model.addAttribute("regiList", regParts);
		model.addAttribute("exhbList", exbParts);
		return "customer/custRegPage";
	}
	
	// for each customer to search their own registration records
	@Transactional
	@RequestMapping("/queryOwnRegistration")
	public String queryOwnRegistration(Model model, @RequestParam(name="regId", required=false) Long regId, EventSemExhibiMute eventMute, HttpSession session) {
		// sanitize input
		Event event  = new Event();
		if(eventMute.getStart_datetime()!=null&&!eventMute.getStart_datetime().trim().equals("")) {
			event.setStart_datetime(Timestamp.valueOf(eventMute.getStart_datetime()));
		}
		if(eventMute.getStop_datetime()!=null&&!eventMute.getStop_datetime().trim().equals("")) {
			event.setStop_datetime(Timestamp.valueOf(eventMute.getStop_datetime()));
		}
		event.setEvent_name(StringUtils.sqlSanitize(eventMute.getEvent_name()));
		List<Registration> regParts = new ArrayList<>();
		List<Event> exbParts = new ArrayList<>();
		registrationService.queryOwnRegistration(this.getUidFromSession(session), regId, event, regParts, exbParts);
		model.addAttribute("regiList", regParts);
		model.addAttribute("exhbList", exbParts);
		return "customer/custRegPage";
	}
	
	// for each customer to cancel their own registration records
	@Transactional
	@RequestMapping("/cancelOwnRegistration")
	public String cancelOwnRegistration(Model model, @RequestParam(name="regId", required=false) Long regId, HttpSession session) {
		List<Registration> regParts = new ArrayList<>();
		List<Event> exbParts = new ArrayList<>();
		registrationService.getRegInfoById(this.getUidFromSession(session), regParts, exbParts);
		model.addAttribute("regiList", regParts);
		model.addAttribute("exhbList", exbParts);
		// if the registration number is invalid
		if(regId==null) {
			model.addAttribute("submit_res", "The registration cannot be deleted! Please specify registration Number.");
			return "customer/custRegPage";
		}
		// or does not exist
		Registration reg = new Registration();
		reg.setReg_id(regId);
		List<Registration> tempRes = registrationService.getEventList(reg);
		if(tempRes==null||tempRes.size()<1) {
			model.addAttribute("submit_res", "The registration does not exist!");
			return "customer/custRegPage";		
		}
		// or does not happen in the future, cannot cancel
		Timestamp now  = new Timestamp(System.currentTimeMillis());
		regParts = new ArrayList<>();
		exbParts = new ArrayList<>();
		registrationService.queryOwnRegistration( this.getUidFromSession(session), regId, null, regParts, exbParts);
		Timestamp happenTime = exbParts.get(0).getStart_datetime();
		if(happenTime.before(now)) {
			model.addAttribute("submit_res", "Cannot cancel a past or happening registration! Only registration for a future exhibition can be canceled.");
			return "customer/custRegPage";
		}	
		reg = new Registration();
		reg.setReg_id(regId);
		registrationService.delRegistration(reg);
		model.addAttribute("submit_res", "The registration was successfully deleted!");
		return "customer/custRegPage";
	}
	
	
}
