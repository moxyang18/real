package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Event;
import com.jsm.real.entity.SeminarSponsor;
import com.jsm.real.entity.Sponsor;

public interface SponsorService {
	// add a new sponsor
	void addSponsor(Sponsor sponsor);
	// get the lists of all sponsors
	List<Sponsor> getSponsorList();
	// get seminarsponsor list
	void getSemSponsorList( int limit, SeminarSponsor semSponsor, List<SeminarSponsor> semSponsorParts, List<Sponsor> sponsorParts,
			List<Event> eventParts);
	
	void addSemSponsor(SeminarSponsor semSponsor);
	void delSemSponsor(SeminarSponsor semSponsor);
	boolean existsSponsor(Long spid);
	boolean existsSemSponsor(Long spid, Long eventId);
	SeminarSponsor findEntityBy(Long eventId, Long spid);
}
