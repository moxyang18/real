package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Event;
import com.jsm.real.entity.Exhibition;
import com.jsm.real.entity.Registration;

public interface RegistrationService {
	// create an registration
	Long addRegistration(Registration registration);
	// query registration List
	List<Registration> getEventList(Registration registration);
	// delete an registration
	void delRegistration(Registration registration);
	// query registration info of a customer
	void getRegInfoById(Long uid, List<Registration> regParts, List<Event> exbParts);
	// query a customer's own registration
	void queryOwnRegistration(Long uid, Long regId, Event event, List<Registration> regParts, List<Event> exbParts);
}

