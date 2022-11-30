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
import com.jsm.real.entity.Invitation;
import com.jsm.real.service.InvitationService;
import com.jsm.real.util.StringUtils;

@Controller
public class InvitationController extends BaseController {
	@Autowired
	InvitationService invitationService;
	
	@RequestMapping("/inviList") 
	public String inviList(Model model) {
		model.addAttribute("inviList", invitationService.getEventList(new Invitation()));
		return "inviListPage";
	}	
	
	@RequestMapping("/queryInvitation") 
	public String queryInvitation(Model model, Invitation data) {
		model.addAttribute("inviList", invitationService.getEventList(data));
		return "inviListPage";
	}
	
	@Transactional
	@RequestMapping("/addInvitation")
	public String addInvitation(Model model, Invitation data) {
		if(data.getEvent_id()==null||data.getUID()==null) {
			model.addAttribute("submit_res", "The invitation cannot be made! Please specify seminar id and author id.");
			return "inviListPage";
		}
		// do not want to modify
		data.setInv_id(null);
		invitationService.addInvitation(data);
		model.addAttribute("submit_res", "The invitation was successfully made!");
		// TODO this page for the employee's management, can see all authors' invitations
		model.addAttribute("inviList", invitationService.getEventList(new Invitation()));
		return "inviListPage";
	}
	
	@Transactional
	@RequestMapping("/delInvitation")
	public String delInvitation(Model model, Invitation data) {
		if(data.getInv_id()==null) {
			model.addAttribute("submit_res", "The invitation cannot be deleted! Please specify invitation id.");
			return "inviListPage";		
		}
		invitationService.delInvitation(data);
		model.addAttribute("submit_res", "The invitation was successfully deleted!");
		// TODO this page for the employee's management, can see all authors' invitations
		model.addAttribute("inviList", invitationService.getEventList(new Invitation()));
		return "inviListPage";
	}
	
	
	// for each customer to view their own registration records
	@Transactional
	@RequestMapping("/viewOwnInvitation")
	public String viewOwnInvitation(Model model, HttpSession session) {
		List<Invitation> invParts = new ArrayList<>();
		List<Event> semParts = new ArrayList<>();
		invitationService.getInviInfoById(this.getUidFromSession(session), invParts, semParts);
		model.addAttribute("inviList", invParts);
		model.addAttribute("semList", semParts);
		return "author/ownInviPage";
	}
	
	// for each customer to search their own registration records
	@Transactional
	@RequestMapping("/queryOwnInvitation")
	public String queryOwnInvitation(Model model, @RequestParam(name="invId", required=false) Long invId, EventSemExhibiMute eventMute, HttpSession session) {
		// sanitize input
		Event event  = new Event();
		if(eventMute.getStart_datetime()!=null&&!eventMute.getStart_datetime().trim().equals("")) {
			event.setStart_datetime(Timestamp.valueOf(eventMute.getStart_datetime()));
		}
		if(eventMute.getStop_datetime()!=null&&!eventMute.getStop_datetime().trim().equals("")) {
			event.setStop_datetime(Timestamp.valueOf(eventMute.getStop_datetime()));
		}
		event.setEvent_name(StringUtils.sqlSanitize(eventMute.getEvent_name()));
		
		List<Invitation> invParts = new ArrayList<>();
		List<Event> semParts = new ArrayList<>();
		invitationService.queryOwnInvitation(this.getUidFromSession(session), invId, event, invParts, semParts);
		model.addAttribute("inviList", invParts);
		model.addAttribute("semList", semParts);
		return "author/ownInviPage";
	}
	
	// for each customer to cancel their own registration records
	@Transactional
	@RequestMapping("/cancelOwnInvitation")
	public String cancelOwnInvitation(Model model, @RequestParam(name="invId", required=false) Long invId, HttpSession session) {
		List<Invitation> invParts = new ArrayList<>();
		List<Event> semParts = new ArrayList<>();
		invitationService.getInviInfoById(this.getUidFromSession(session), invParts, semParts);
		model.addAttribute("regiList", invParts);
		model.addAttribute("exhbList", semParts);
		// if the registration number is invalid
		if(invId==null) {
			model.addAttribute("submit_res", "The registration cannot be deleted! Please specify registration Number.");
			return "author/ownInviPage";
		}
		// or does not exist
		Invitation inv = new Invitation();
		inv.setInv_id(invId);
		List<Invitation> tempRes = invitationService.getEventList(inv);
		if(tempRes==null||tempRes.size()<1) {
			model.addAttribute("submit_res", "The registration does not exist!");
			return "author/ownInviPage";		
		}
		// or does not happen in the future, cannot cancel
		Timestamp now  = new Timestamp(System.currentTimeMillis());
		invParts = new ArrayList<>();
		semParts = new ArrayList<>();
		invitationService.queryOwnInvitation(this.getUidFromSession(session), invId, null, invParts, semParts);
		Timestamp happenTime = semParts.get(0).getStart_datetime();
		if(happenTime.before(now)) {
			model.addAttribute("submit_res", "Cannot cancel a past or happening invitation! Only invitation for a future seminar can be canceled.");
			return "author/ownInviPage";
		}	
		inv = new Invitation();
		inv.setInv_id(invId);
		invitationService.delInvitation(inv);
		model.addAttribute("submit_res", "The invitation was successfully deleted!");
		return "author/ownInviPage";
	}
	
	
	
	
}
