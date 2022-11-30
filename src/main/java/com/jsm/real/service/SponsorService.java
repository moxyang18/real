package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Sponsor;

public interface SponsorService {
	// add a new sponsor
	void addSponsor(Sponsor sponsor);
	// get the lists of all sponsors
	List<Sponsor> getSponsorList();
}
