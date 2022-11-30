package com.jsm.real.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.SponsorDao;
import com.jsm.real.entity.Sponsor;
import com.jsm.real.service.SponsorService;

@Service
public class SponsorServiceImpl implements SponsorService {
	@Autowired
	SponsorDao sponsorDao;
	
	// add a new sponsor
	@Override
	public void addSponsor(Sponsor sponsor) {
		sponsorDao.save(sponsor);
	}
	// get the lists of all sponsors
	@Override
	public List<Sponsor> getSponsorList() {
		List<Sponsor> spList = sponsorDao.findAll();
		return spList;
	}
}
