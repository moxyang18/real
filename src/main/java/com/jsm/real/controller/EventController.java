package com.jsm.real.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.jsm.real.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jsm.real.entity.Event;
import com.jsm.real.entity.EventSemExhibiMute;
import com.jsm.real.entity.Exhibition;
import com.jsm.real.entity.Registration;
import com.jsm.real.entity.Seminar;
import com.jsm.real.entity.Topic;
import com.jsm.real.service.EventService;
import com.jsm.real.service.RegistrationService;
import com.jsm.real.service.TopicService;

@Controller
public class EventController extends BaseController {
	@Autowired
	EventService eventService;
	@Autowired
	RegistrationService registrationService;
	@Autowired
	TopicService topicService;
	
	@RequestMapping("/eventList") 
	public String eventList(Model model) {
		// TODO modify the hard coded limit
		Event event  = new Event();
		List<Event> eventList = eventService.getEventList(event);
		model.addAttribute("eventList", eventList);
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		return "eventListPage";
	}		
	
	@Transactional
	@RequestMapping("/editSeminar")
	public String editSeminar(Model model, EventSemExhibiMute data) throws ParseException {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		
		// TODO sanitize input
		// validate input
	
		// pre-populate the entered values
		model.addAttribute("event_name", data.getEvent_name());
		model.addAttribute("event_type", data.getEvent_type());
		model.addAttribute("start_datetime", data.getStart_datetime());
		model.addAttribute("stop_datetime", data.getStop_datetime());
		model.addAttribute("topic_id", data.getTopic_id());
		model.addAttribute("SEM_CAPACITY", data.getSEM_CAPACITY());
		List<Event> eventList = eventService.getEventList(new Event());
		model.addAttribute("eventList", eventList);
		// validate the event input and insert
		if(data.getEvent_id()==null||data.getEvent_name()==null||data.getEvent_name().trim()==""||data.getEvent_type()==null||
				data.getStart_datetime()==null||data.getStop_datetime()==null||data.getTopic_id()==null||data.getTopic_id()==0) {
			model.addAttribute("submit_res", "Invalid. The event cannot be edited! Specify all required fields.");
			return "eventListPage";
		}
		// if event id does not exist, or event is not seminar, prompt error and return
		Seminar seminar = new Seminar();
		seminar.setEvent_id(data.getEvent_id());
		List<Seminar> queried = eventService.getSeminarList(seminar);
		if(queried==null||queried.size()!=1) {
			model.addAttribute("submit_res", "Invalid. The event cannot be edited! Seminar does not exist!");
			return "eventListPage";			
		} 
		
		// populate event entity with data
		Event event = new Event();
		event.setEvent_id(data.getEvent_id());
		event.setEvent_name(data.getEvent_name().trim());
		event.setEvent_type(data.getEvent_type());
		event.setStart_datetime(StringUtils.ToSqlTimeStamp(data.getStart_datetime()));
		event.setStop_datetime(StringUtils.ToSqlTimeStamp(data.getStop_datetime()));
		event.setTopic_id(data.getTopic_id());
		
		Long eventId = eventService.saveEvent(event);		
		// validate the seminar required field and insert
		if(data.getSEM_CAPACITY()==null) {
			model.addAttribute("submit_res", "Invalid. The seminar cannot be edited! Specify all required fields.");
			return "eventListPage";
		}
		seminar = new Seminar();
		seminar.setSEM_CAPACITY(data.getSEM_CAPACITY());
		seminar.setEvent_id(eventId);
		eventService.saveSeminar(seminar);
		model.addAttribute("submit_res", "The seminar entry was successfully edited!");
		eventList = eventService.getEventList(new Event());
		model.addAttribute("eventList", eventList);
		return "eventListPage";
	}
			
	@Transactional
	@RequestMapping("/editExhibition")
	public String editExhibition(Model model, EventSemExhibiMute data) throws ParseException {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO sanitize input
		// validate input

		model.addAttribute("event_name", data.getEvent_name());
		model.addAttribute("event_type", data.getEvent_type());
		model.addAttribute("start_datetime", data.getStart_datetime());
		model.addAttribute("stop_datetime", data.getStop_datetime());
		model.addAttribute("topic_id", data.getTopic_id());
		model.addAttribute("expense", data.getExpense());
		List<Event> eventList = eventService.getEventList(new Event());
		model.addAttribute("eventList", eventList);
		
		// validate the event input and insert
		if(data.getEvent_id()==null||data.getEvent_name()==null||data.getEvent_name().trim()==""||data.getEvent_type()==null||
				data.getStart_datetime()==null||data.getStop_datetime()==null||data.getTopic_id()==null||data.getTopic_id()==0) {
			model.addAttribute("submit_res", "Invalid. The event cannot be edited! Specify all required fields.");
			return "eventListPage";
		}
		
		// if event id does not exist, or event is not exhibition, prompt error and return
		Exhibition exhibition = new Exhibition();
		exhibition.setEvent_id(data.getEvent_id());
		List<Exhibition> queried = eventService.getExhibitionList(exhibition);
		if(queried==null||queried.size()!=1) {
			model.addAttribute("submit_res", "Invalid. The event cannot be edited! Exibition does not exist!");
			return "eventListPage";			
		} 
		
		// populate event entity with data
		Event event = new Event();
		event.setEvent_id(data.getEvent_id());
		event.setEvent_name(data.getEvent_name().trim());
		event.setEvent_type(data.getEvent_type());
		event.setStart_datetime(StringUtils.ToSqlTimeStamp(data.getStart_datetime()));
		event.setStop_datetime(StringUtils.ToSqlTimeStamp(data.getStop_datetime()));
		event.setTopic_id(data.getTopic_id());
		
		Long eventId = eventService.saveEvent(event);				
		// validate the exhibition required field and insert
		if(data.getExpense()==null) {
			model.addAttribute("submit_res", "Invalid. The exhibition cannot be edited! Specify all required fields.");
			return "eventListPage";
		}
		exhibition = new Exhibition();
		exhibition.setExpense(data.getExpense());
		exhibition.setEvent_id(eventId);
		eventService.saveExhibition(exhibition);
		model.addAttribute("submit_res", "The exhibition entry was successfully edited!");
		
		//back on exhibiListPage
		event  = new Event();
		eventList = eventService.getEventList(event);
		model.addAttribute("eventList", eventList);
		return "eventListPage";
	}
	
	
	@RequestMapping("/queryEvent") 
	public String queryEvent(Model model, EventSemExhibiMute data) throws ParseException {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO modify the hard coded limit
		Event event = new Event();
		if(data.getStart_datetime()!=null&&data.getStart_datetime().trim()!="") {
			event.setStart_datetime(StringUtils.ToSqlTimeStamp(data.getStart_datetime().trim()));
		}
		if(data.getStop_datetime()==null||data.getStop_datetime().trim()!="") {
			event.setStop_datetime(StringUtils.ToSqlTimeStamp(data.getStop_datetime().trim()));
		}
		if(data.getTopic_id()!=null&&data.getTopic_id()==0) {
			event.setTopic_id(null);
		} else {
			event.setTopic_id(data.getTopic_id());
		}
		event.setEvent_id(data.getEvent_id());
		if(data.getEvent_name()!=null&&data.getEvent_name().trim()!="") {
			event.setEvent_name(data.getEvent_name().trim());
		}
		event.setEvent_type(data.getEvent_type());
		List<Event> eventList = eventService.getEventList(event);
		model.addAttribute("eventList", eventList);
		return "eventListPage";
	}
	
	// get all future seminars
	@RequestMapping("/futureSeminars") 
	public String futureSeminars(Model model) throws ParseException {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO modify the hard coded limit
		List<Event> eventList = eventService.getAllFutureSems();
		model.addAttribute("eventList", eventList);
		return "author/futureSeminarPage";
	}	
	
	// get all upcoming seminars based on filters
	@RequestMapping("/qryUpcomingSem") 
	public String qryUpcomingSem(Model model, EventSemExhibiMute data) throws ParseException {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO modify the hard coded limit
		Event event = new Event();
		if(data.getStart_datetime()!=null&&data.getStart_datetime().trim()!="") {
			event.setStart_datetime(StringUtils.ToSqlTimeStamp(data.getStart_datetime().trim()));
		}
		if(data.getStop_datetime()==null||data.getStop_datetime().trim()!="") {
			event.setStop_datetime(StringUtils.ToSqlTimeStamp(data.getStop_datetime().trim()));
		}
		event.setEvent_id(data.getEvent_id());
		if(data.getEvent_name()!=null&&data.getEvent_name().trim()!="") {
			event.setEvent_name(data.getEvent_name().trim());
		}
		event.setEvent_type(data.getEvent_type());
		event.setTopic_id(data.getTopic_id());
		List<Event> eventList = eventService.getAllFutureSemsBy(event);
		model.addAttribute("eventList", eventList);
		return "author/futureSeminarPage";
	}

	// get all future exhibitions
	@RequestMapping("/futureExhibitions") 
	public String futureExhibitions(Model model) throws ParseException {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO modify the hard coded limit
		List<Event> eventList = eventService.getAllFutureExhbs();
		model.addAttribute("eventList", eventList);
		return "customer/futureExhibitionPage";
	}	
	
	// get all upcoming seminars based on filters
	@RequestMapping("/qryUpcomingExb") 
	public String qryUpcomingExb(Model model, EventSemExhibiMute data) throws ParseException {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO modify the hard coded limit
		Event event = new Event();
		if(data.getStart_datetime()!=null&&data.getStart_datetime().trim()!="") {
			event.setStart_datetime(StringUtils.ToSqlTimeStamp(data.getStart_datetime().trim()));
		}
		if(data.getStop_datetime()==null||data.getStop_datetime().trim()!="") {
			event.setStop_datetime(StringUtils.ToSqlTimeStamp(data.getStop_datetime().trim()));
		}
		event.setEvent_id(data.getEvent_id());
		if(data.getEvent_name()!=null&&data.getEvent_name().trim()!="") {
			event.setEvent_name(data.getEvent_name().trim());
		}
		event.setEvent_type(data.getEvent_type());
		event.setTopic_id(data.getTopic_id());
		List<Event> eventList = eventService.getAllFutureExhbsBy(event);
		model.addAttribute("eventList", eventList);
		return "customer/futureExhibitionPage";
	}

	
	@Transactional
	@RequestMapping("/regExhibition")
	public String regExhibition(Model model, EventSemExhibiMute data, HttpSession session) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		List<Event> eventList = eventService.getAllFutureExhbs();
		model.addAttribute("eventList", eventList);
		// if exhibition id not specified, return with error
		if(data.getEvent_id()==null) {
			model.addAttribute("submit_res", "The exhibition id is not specified!");
			return "customer/futureExhibitionPage";
		}
		// if the exhibition does not exist in the future, prompt error
		if(!eventService.existsFutureExhibition(data.getEvent_id())) {
			model.addAttribute("submit_res", "The exhibition id is invalid!");
			return "customer/futureExhibitionPage";		
		}
		// otherwise the exhibition is valid for making a registration
		Registration regi = new Registration();
	//	if(data.getEvent_id()!=null) {
	//		model.addAttribute("submit_res", "The registration was successfully made! "+data.getEvent_id());
	//		return "customer/futureExhibitionPage";
	//	}
		regi.setEvent_id(data.getEvent_id());
		regi.setUID(this.getUidFromSession(session));
		registrationService.addRegistration(regi); 
		model.addAttribute("submit_res", "The registration was successfully made!");
		return "customer/futureExhibitionPage";
	}	
	
	@Transactional
	@RequestMapping("/addSeminar")
	public String addSeminar(Model model, EventSemExhibiMute data) throws ParseException {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO sanitize input
		// validate input
		
		// validate the event input and insert
		if(data.getEvent_name()==null||data.getEvent_name().trim()==""||data.getEvent_type()==null||
				data.getStart_datetime()==null||data.getStop_datetime()==null||data.getTopic_id()==null||data.getTopic_id()==0) {
			model.addAttribute("submit_res", "Invalid. The event cannot be added! Specify all required fields.");
			model.addAttribute("event_name", data.getEvent_name());
			model.addAttribute("event_type", data.getEvent_type());
			model.addAttribute("start_datetime", data.getStart_datetime());
			model.addAttribute("stop_datetime", data.getStop_datetime());
			model.addAttribute("topic_id", data.getTopic_id());
			List<Event> eventList = eventService.getEventList(new Event());
			model.addAttribute("eventList", eventList);
			return "eventListPage";
		}
		// populate event entity with data
		Event event = new Event();
		event.setEvent_name(data.getEvent_name().trim());
		event.setEvent_type(data.getEvent_type());
		event.setStart_datetime(StringUtils.ToSqlTimeStamp(data.getStart_datetime()));
		event.setStop_datetime(StringUtils.ToSqlTimeStamp(data.getStop_datetime()));
		event.setTopic_id(data.getTopic_id());
		
		Long eventId = eventService.saveEvent(event);		
		// validate the seminar required field and insert
		if(data.getSEM_CAPACITY()==null) {
			model.addAttribute("submit_res", "Invalid. The seminar cannot be added! Specify all required fields.");
			model.addAttribute("SEM_CAPACITY", data.getSEM_CAPACITY());
			List<Event> eventList = eventService.getEventList(new Event());
			model.addAttribute("eventList", eventList);
			return "eventListPage";
		}
		Seminar seminar = new Seminar();
		seminar.setSEM_CAPACITY(data.getSEM_CAPACITY());
		seminar.setEvent_id(eventId);
		eventService.saveSeminar(seminar);
		model.addAttribute("submit_res", "The seminar entry was successfully added!");
		List<Event> eventList = eventService.getEventList(new Event());
		model.addAttribute("eventList", eventList);
		return "eventListPage";
	}
			
	@Transactional
	@RequestMapping("/addExhibition")
	public String addExhibition(Model model, EventSemExhibiMute data) throws ParseException {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO sanitize input
		// validate input
		// validate the event input and insert
		if(data.getEvent_name()==null||data.getEvent_name().trim()==""||data.getEvent_type()==null||
				data.getStart_datetime()==null||data.getStop_datetime()==null||data.getTopic_id()==null||data.getTopic_id()==0) {
			model.addAttribute("submit_res", "Invalid. The event cannot be added! Specify all required fields.");
			model.addAttribute("event_name", data.getEvent_name());
			model.addAttribute("event_type", data.getEvent_type());
			model.addAttribute("start_datetime", data.getStart_datetime());
			model.addAttribute("stop_datetime", data.getStop_datetime());
			model.addAttribute("topic_id", data.getTopic_id());
			List<Event> eventList = eventService.getEventList(new Event());
			model.addAttribute("eventList", eventList);
			return "eventListPage";
		}
		// populate event entity with data
		Event event = new Event();
		event.setEvent_name(data.getEvent_name().trim());
		event.setEvent_type(data.getEvent_type());
		event.setStart_datetime(StringUtils.ToSqlTimeStamp(data.getStart_datetime()));
		event.setStop_datetime(StringUtils.ToSqlTimeStamp(data.getStop_datetime()));
		event.setTopic_id(data.getTopic_id());
		
		Long eventId = eventService.saveEvent(event);				
		// validate the exhibition required field and insert
		if(data.getExpense()==null) {
			model.addAttribute("submit_res", "Invalid. The exhibition cannot be added! Specify all required fields.");
			model.addAttribute("expense", data.getExpense());
			List<Event> eventList = eventService.getEventList(new Event());
			model.addAttribute("eventList", eventList);
			return "eventListPage";
		}
		Exhibition exhibition = new Exhibition();
		exhibition.setExpense(data.getExpense());
		exhibition.setEvent_id(eventId);
		eventService.saveExhibition(exhibition);
		model.addAttribute("submit_res", "The exhibition entry was successfully added!");
		
		//back on exhibiListPage
		event  = new Event();
		List<Event> eventList = eventService.getEventList(event);
		model.addAttribute("eventList", eventList);
		return "eventListPage";
	}
	
	
	@Transactional
	@RequestMapping("/delSeminar")
	public String delSeminar(Model model, EventSemExhibiMute data) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO sanitize input
		// validate input
		if(data.getEvent_id()==null) {
			model.addAttribute("submit_res", "Invalid. The seminar cannot be deleted! Specify required fields.");
			return "eventListPage";
		}
		Event event = new Event();
		event.setEvent_id(data.getEvent_id());
		Seminar seminar = new Seminar();
		seminar.setEvent_id(data.getEvent_id());
		eventService.deleteSeminar(event, seminar);
		model.addAttribute("submit_res", "The seminar entry was successfully deleted!");
		return "eventListPage";
	}	

	@Transactional
	@RequestMapping("/delExhibition")
	public String delExhibition(Model model, EventSemExhibiMute data) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO sanitize input
		// validate input
		if(data.getEvent_id()==null) {
			model.addAttribute("submit_res", "Invalid. The exhibition cannot be deleted! Specify required fields.");
			return "eventListPage";
		}
		Event event = new Event();
		event.setEvent_id(data.getEvent_id());
		Exhibition exhibition = new Exhibition();
		exhibition.setEvent_id(data.getEvent_id());
		eventService.deleteExhibition(event, exhibition);
		model.addAttribute("submit_res", "The exhibition entry was successfully deleted!");
		return "eventListPage";
	}	
	
	
	
	@RequestMapping("/exhibitionList") 
	public String exhibitionList(Model model) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		Event event  = new Event();
		List<Event> eventList = eventService.getEventList(event);
		model.addAttribute("eventList", eventList);
		
		
		// TODO modify the hard coded limit
		Exhibition exhibition = new Exhibition();
		List<Exhibition> exhibiList = eventService.getExhibitionList(exhibition);
		model.addAttribute("exhibiList", exhibiList);
		return "exhibiListPage";
	}	
	
	@RequestMapping("/seminarList") 
	public String seminarList(Model model) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		Event event  = new Event();
		List<Event> eventList = eventService.getEventList(event);
		model.addAttribute("eventList", eventList);
		
		// TODO modify the hard coded limit
		Seminar seminar = new Seminar();
		List<Seminar> semList = eventService.getSeminarList(seminar);
		model.addAttribute("semList", semList);
		return "semListPage";
	}	
	
	@RequestMapping("/queryExhibition") 
	public String queryExhibition(Model model, Exhibition exhibition) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO modify the hard coded limit
		List<Exhibition> exhibiList = eventService.getExhibitionList(exhibition);
		model.addAttribute("exhibiList", exhibiList);
		return "exhibiListPage";
	}	
	
	@RequestMapping("/querySeminar") 
	public String querySeminar(Model model, Seminar seminar) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO modify the hard coded limit
		List<Seminar> semList = eventService.getSeminarList(seminar);
		model.addAttribute("semList", semList);
		return "semListPage";
	}	
	
	
	@Transactional
	@RequestMapping("/addEvent")
	public String addEvent(Model model, Event event, Exhibition exhibition) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// TODO sanitize input
		// validate input
		if(event.getEvent_name()==null||event.getEvent_name().trim()!=""||event.getEvent_type()==null||
		event.getStart_datetime()==null||event.getStop_datetime()==null||event.getTopic_id()==null) {
			model.addAttribute("submit_res", "Invalid. The event cannot be added! Specify all required fields.");
			model.addAttribute("event_name", event.getEvent_name());
			model.addAttribute("event_type", event.getEvent_type());
			model.addAttribute("start_datetime", event.getStart_datetime());
			model.addAttribute("stop_datetime", event.getStop_datetime());
			model.addAttribute("topic_id", event.getTopic_id());
			return "eventListPage";
		}
		eventService.saveEvent(event);
		model.addAttribute("submit_res", "The book entry was successfully added!");
		return "eventListPage";
	}		
	
	public static void main(String[] args) throws ParseException {
		EventController obj = new EventController();
		EventSemExhibiMute data = new EventSemExhibiMute();
		data.setEvent_name("Adventure Exhibition");
		data.setEvent_type('E');
		data.setStart_datetime("2022-11-23T21:20");
		data.setStop_datetime("2022-11-23T22:20");
		data.setExpense(5000.0);
		data.setTopic_id(1);
		obj.addExhibition(null, data);
	}
	
}
