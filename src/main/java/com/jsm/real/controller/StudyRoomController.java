package com.jsm.real.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsm.real.entity.StudyRoom;
import com.jsm.real.service.StudyRoomService;

@Controller
public class StudyRoomController extends BaseController {
	@Autowired
	StudyRoomService studyRoomService;
	@Transactional
	@RequestMapping("/addRoom")
	public String addRoom(Model model, StudyRoom studyRoom){
		// check null input
		if(studyRoom.getCapacity()==null || studyRoom.getCapacity()<1) {
			model.addAttribute("submit_res", "Invalid attempt! Please enter the valid capacity!");
			return "addRoomPage";
		}
		// sanitize the input
		//TODO: PREVENT SQL INJECTION
		
		studyRoomService.addRoom(studyRoom);
		model.addAttribute("submit_res", "You have successfully added a new room.");
		return "addRoomPage";
	}
	// this GET request goes to the study room list page
	@RequestMapping("/roomList")
	public String showTopics(Model model) {
		List<StudyRoom> rmList = studyRoomService.getRoomList();
		model.addAttribute("roomList", rmList);
		return "roomsPage";
	}
	// this request redirects to add a room page
	@RequestMapping("/toAddRoom")
	public String toAddRoom() {
		return "addRoomPage";
	}
	
}