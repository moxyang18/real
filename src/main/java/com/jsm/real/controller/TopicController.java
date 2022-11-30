package com.jsm.real.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsm.real.entity.Topic;
import com.jsm.real.service.TopicService;
import com.jsm.real.util.StringUtils;


@Controller
public class TopicController extends BaseController {
	@Autowired
	TopicService topicService;
	
	// this POST request adds a topic upon submitting
	// and redirects to the topic list display page
	@Transactional
	@RequestMapping("/addTopic")
	public String addTopic(Model model, Topic topic){
		// check null input
		if(topic.getTopic_name()==null) {
			model.addAttribute("submit_res", "Invalid attempt! Please specify a topic!");
			return "addTopicPage";
		}
		// sanitize the input
		// set the id field to be null
		topic.setTopic_id(null);
		//TODO: PREVENT SQL INJECTION
		String topicName = topic.getTopic_name().trim().toLowerCase();
		// on invalid input
		if(topicName.length()<=1) {
			model.addAttribute("submit_res", "Invalid attempt! Please specify a topic!");
		} else if (topicService.containsTopic(topicName)) {
			model.addAttribute("submit_res", "Invalid attempt! Duplicate topic!");
		} else {
			topicService.addTopic(topic);
			model.addAttribute("submit_res", "You have successfully added a new topic.");
		}
		return "addTopicPage";
	}
	
	// this GET request goes to the topic list page
	@RequestMapping("/topicList")
	public String showTopics(Model model) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		return "topicsPage";
	}
	
	// this request redirects to add a topic page
	@RequestMapping("/toAddTopic")
	public String toAddTopic() {
		return "addTopicPage";
	}
	
	// this request redirects to update a topic page
	@RequestMapping("/toUptTopic")
	public String toUptTopic(Model model) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		return "uptTopicPage";
	}	
	
	@RequestMapping("/uptTopic")
	public String uptTopic(Model model, Topic topic) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// check null input
		if(topic.getTopic_name()==null) {
			model.addAttribute("submit_res", "Invalid attempt! Please specify a topic!");
			return "uptTopicPage";
		}
		if(topic.getTopic_id()==0) {
			model.addAttribute("submit_res", "Invalid attempt! Please select an existing topic");
		}
		// sanitize the input
		//TODO: PREVENT SQL INJECTION
		String topicName = StringUtils.sqlSanitize(topic.getTopic_name().trim().toLowerCase());
		// on invalid input
		if(topicName.length()<=1) {
			model.addAttribute("submit_res", "Invalid attempt! Please specify a topic!");
		} else if (topicService.containsTopic(topicName)) {
			model.addAttribute("submit_res", "Invalid attempt! Duplicate topic!");
		} else {
			topic.setTopic_name(topicName);
			topicService.addTopic(topic);
			model.addAttribute("submit_res", "You have successfully updated a topic.");
		}
		return "uptTopicPage";
	}
}
