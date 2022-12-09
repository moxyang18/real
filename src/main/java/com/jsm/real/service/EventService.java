package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Event;
import com.jsm.real.entity.Exhibition;
import com.jsm.real.entity.Seminar;

public interface EventService {
	// create or modify an event
	Long saveEvent(Event event);
	// create or modify an exhibition event
	Long saveExhibition(Exhibition exhibition);
	// create or modify a seminar event
	Long saveSeminar(Seminar seminar);
	// query event lists
	List<Event> getEventList(Event event);
	// query exhibition lists
	List<Exhibition> getExhibitionList(Exhibition exhibition);
	// query seminar list
	List<Seminar> getSeminarList(Seminar seminar);
	// delete an exhibition event
	void deleteExhibition(Event event, Exhibition exhibition);
	// delete a seminar event
	void deleteSeminar(Event event, Seminar seminar);
	
	// get all future seminars
	List<Event> getAllFutureSems();
	// get future seminars with filters
	List<Event> getAllFutureSemsBy(Event event);
	
	// get all future exhibitions
	List<Event> getAllFutureExhbs();
	// get future exhibitions with filters
	List<Event> getAllFutureExhbsBy(Event event);
	
	// check whether the event exists in the future
	boolean existsFutureExhibition(Long event_id);
	// check whether the seminar exists
	boolean existsSeminar(Long event_id);
	
}
