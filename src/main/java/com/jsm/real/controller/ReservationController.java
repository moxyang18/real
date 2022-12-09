package com.jsm.real.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsm.real.entity.Reservation;
import com.jsm.real.entity.ReservationMute;
import com.jsm.real.service.ReservationService;
import com.jsm.real.util.StringUtils;

@Controller
public class ReservationController extends BaseController {
	@Autowired
	ReservationService resvService;
	@Transactional
	@RequestMapping("/makeResv")
	public String makeResv(Model model, ReservationMute resvMute) {
		// sanitize input
		// validate input
		if(resvMute.getUID()==null||resvMute.getResv_date()==null||resvMute.getRoom_id()==null||resvMute.getTime_slot()==null||resvMute.getTime_slot()==0) {
			model.addAttribute("submit_res", "Invalid Input. Your reservation cannot be made!");
			model.addAttribute("resv_date", resvMute.getResv_date());
			model.addAttribute("room_id", resvMute.getRoom_id());
			model.addAttribute("UID", resvMute.getUID());
			return "customer/makeResvPage";
		}
		Reservation resv = new Reservation();
	    //format the string, and then convert to sql Date type
		resv.setResv_date(StringUtils.ToSqlDate(resvMute.getResv_date()));
		resv.setRoom_id(resvMute.getRoom_id());
		resv.setTime_slot(resvMute.getTime_slot());
		resv.setUID(resvMute.getUID());
		resvService.addResv(resv);
		model.addAttribute("submit_res", "Your reservation was successfully made!");
		return "customer/makeResvPage";
	}
	
	@RequestMapping("/fetchSlots")
	public String fetchSlots(Model model, ReservationMute resvMute, HttpSession session) {
		String endPage;
		if(this.getRoleFromSession(session).equals("CUSTOMER")) {
			endPage = "customer/makeResvPage";
		} else {
			endPage = "makeResvPage";
		}
		Set<Integer> slots = new HashSet<>(Arrays.asList(1,2,3,4));
		// if not both filled in, cannot check time slot availability
		if(resvMute.getResv_date()==null||resvMute.getResv_date().trim()==""||resvMute.getRoom_id()==null) {
			model.addAttribute("submit_res", "Invalid Input. Your reservation cannot be made!");
			model.addAttribute("resv_date", resvMute.getResv_date());
			model.addAttribute("room_id", resvMute.getRoom_id());
			model.addAttribute("UID", resvMute.getUID());
			model.addAttribute("submit_res", "Specify both date and room number to check availability!");
			return endPage;			
		}
		Reservation resv = new Reservation();
		resv.setRoom_id(resvMute.getRoom_id());
	    //format the string, and then convert to sql Date type
		resv.setResv_date(StringUtils.ToSqlDate(resvMute.getResv_date()));
		List<Reservation> resvList = resvService.getResvListByRoomDate(resv);
		model.addAttribute(("submit_res"), resvList.size());
		for( int i = 0; i<resvList.size(); i++) {
			slots.remove(((Reservation)resvList.get(i)).getTime_slot());
		}
		List<Reservation> resSlots = new ArrayList<>();
		List<Integer> res2 = new ArrayList<>();
		for(Integer slot: slots) {
			resv = new Reservation();
			resv.setTime_slot(slot);
			resSlots.add(resv);
			res2.add(slot);
		}
		model.addAttribute("avai_slots", resSlots);
		model.addAttribute("slot_options", res2);
		model.addAttribute("submit_res", "Fetched! Select Time Slots Available!");
		model.addAttribute("resv_date", resvMute.getResv_date());
		model.addAttribute("room_id", resvMute.getRoom_id());
		model.addAttribute("UID", resvMute.getUID());
		return endPage;
	}
	
	@RequestMapping("/resvList") 
	public String resvList(Model model) {
		List<Reservation> resvList = resvService.getResvList();
		model.addAttribute("resvList", resvList);
		return "resvsPage";
	}
	// this request redirects to add a sponsor page
	@RequestMapping("/toMakeResv")
	public String toMakeResv() {
		return "makeResvPage";
	}	
	
	
	// fetch customer's own reservation list
	@RequestMapping("/ownResvList")
	public String ownResvList(Model model, HttpSession session) {
		Reservation resv = new Reservation();
		resv.setUID(this.getUidFromSession(session));
		List<Reservation> resvList = resvService.getResvListBy(resv);
		model.addAttribute("resvList", resvList);
		return "customer/resvsPage";
	}
	
	// query customer's own reservation list by filter
	@RequestMapping("/queryOwnResvList")
	public String queryOwnResvList(Model model, ReservationMute resvMute, HttpSession session) {
		// sanitize the input
		Reservation resv = new Reservation();
		if(StringUtils.sqlSanitize(resvMute.getResv_date()).equals("")) {
			resv.setResv_date(null);
		} else {
			resv.setResv_date(StringUtils.toSqlDate(StringUtils.sqlSanitize(resvMute.getResv_date())));
		}
		resv.setResv_no(resvMute.getResv_no());
		resv.setRoom_id(resvMute.getRoom_id());
		resv.setTime_slot(resvMute.getTime_slot());
		resv.setUID(this.getUidFromSession(session));
		List<Reservation> resvList = resvService.getResvListBy(resv);
		model.addAttribute("submit_res", "Quried reservation list!");
		model.addAttribute("resvList", resvList);
		return "customer/resvsPage";
	}
	
	
	// query customer's own reservation list by filter
	@RequestMapping("/queryResvList")
	public String queryResvList(Model model, ReservationMute resvMute, HttpSession session) {
		// sanitize the input
		Reservation resv = new Reservation();
		if(StringUtils.sqlSanitize(resvMute.getResv_date()).equals("")) {
			resv.setResv_date(null);
		} else {
			resv.setResv_date(StringUtils.toSqlDate(StringUtils.sqlSanitize(resvMute.getResv_date())));
		}
		resv.setResv_no(resvMute.getResv_no());
		resv.setRoom_id(resvMute.getRoom_id());
		resv.setTime_slot(resvMute.getTime_slot());
		resv.setUID(resvMute.getUID());
		List<Reservation> resvList = resvService.getResvListBy(resv);
		model.addAttribute("submit_res", "Quried reservation list!");
		model.addAttribute("resvList", resvList);
		return "resvsPage";
	}
	
	// delete a reservation that happens in the future date
	@RequestMapping("/cancelOwnResv")
	public String cancelOwnResv(Model model, ReservationMute resvMute, HttpSession session) {
		Reservation resv = new Reservation();
		resv.setUID(this.getUidFromSession(session));
		List<Reservation> resvList = resvService.getResvListBy(resv);
		model.addAttribute("resvList", resvList);
		if(resvMute.getResv_no()==null) {
			model.addAttribute("submit_res", "Specify reservation number to cancel!");
			return "customer/resvsPage";
		}
		// CAN ONLY DELETE OWN RESERVATION that happens in the future
		// so need to filter the record by the registration id, customer uid, and reservation date
		if(!resvService.existsFutureResvByResvIdUid(resvMute.getResv_no(), this.getUidFromSession(session))) {
			model.addAttribute("submit_res", "Unable to cancel the reservation in the past");
			return "customer/resvsPage";
		}
		// otherwise can delete the reservation
		resvService.deleteResv(resvMute.getResv_no());
		resv.setUID(this.getUidFromSession(session));
		resvList = resvService.getResvListBy(resv);
		model.addAttribute("resvList", resvList);
		model.addAttribute("submit_res", "Canceled the reservation.");
		return "customer/resvsPage";
	}
	
	// this request redirects to add a sponsor page
	@RequestMapping("/toMakeOwnResv")
	public String toMakeOwnResv(Model model, HttpSession session) {
		model.addAttribute("UID", this.getUidFromSession(session));
		return "customer/makeResvPage";
	}	
}
