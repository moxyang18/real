package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Event;
import com.jsm.real.entity.Invitation;
import com.jsm.real.entity.Registration;

public interface InvitationService {
	// create an invitation
	Long addInvitation(Invitation invitation);
	// query invitation List
	List<Invitation> getEventList(Invitation invitation);
	// delete an invitation
	void delInvitation(Invitation invitation);
	
	// query all invitation info of an author
	void getInviInfoById(Long uid, List<Invitation> regParts, List<Event> semParts);
	// query an author's own invitation
	void queryOwnInvitation(Long uid, Long inviId, Event event, List<Invitation> inviParts, List<Event> semParts);
}
