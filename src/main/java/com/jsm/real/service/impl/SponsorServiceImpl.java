package com.jsm.real.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.SeminarSponsorDao;
import com.jsm.real.dao.SponsorDao;
import com.jsm.real.entity.Author;
import com.jsm.real.entity.AuthorBook;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Event;
import com.jsm.real.entity.Exhibition;
import com.jsm.real.entity.Seminar;
import com.jsm.real.entity.SeminarSponsor;
import com.jsm.real.entity.Sponsor;
import com.jsm.real.service.SponsorService;

@Service
public class SponsorServiceImpl implements SponsorService {
	@Autowired
	SponsorDao sponsorDao;
	@Autowired	
	SeminarSponsorDao semSponsorDao;
	private SessionFactory sf;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Sponsor.class)
			.addAnnotatedClass(SeminarSponsor.class)
			.addAnnotatedClass(Event.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10");	
	
	
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
	@Override
	public void getSemSponsorList(int limit, SeminarSponsor semSponsor, List<SeminarSponsor> semSponsorParts, 
			List<Sponsor> sponsorParts, List<Event> eventParts) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from SeminarSponsor as t0, Sponsor as t1, Event as t2 where t0.spid=t1.spid and t0.event_id=t2.event_id and ");
		if(semSponsor!=null&&semSponsor.getEvent_id()!=null) {
			sb.append("t0.event_id = " + semSponsor.getEvent_id() + " and ");
		}
		if(semSponsor!=null&&semSponsor.getSpid()!=null) {
			sb.append("t0.spid = " + semSponsor.getSpid() + " and ");
		}
		if(semSponsor!=null&&semSponsor.getSponsor_amt()!=null) {
			sb.append("t0.sponsor_amt = " + semSponsor.getSponsor_amt() + " and ");
		}
		sb.append("1 = 1");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			semSponsorParts.add((SeminarSponsor)resPart[0]);
			sponsorParts.add((Sponsor)resPart[1]);
			eventParts.add((Event)resPart[2]);
		}
		tx.commit();
		session.close();
		
	}
	@Override
	public void addSemSponsor(SeminarSponsor semSponsor) {
		semSponsorDao.save(semSponsor);
	}
	@Override
	public void delSemSponsor(SeminarSponsor semSponsor) {
		SeminarSponsor sb = new SeminarSponsor();
		sb.setEvent_id(semSponsor.getEvent_id());
		sb.setSpid(semSponsor.getSpid());
		semSponsorDao.delete(semSponsor);
	}
	@Override
	public boolean existsSponsor(Long spid) {
		sf = cfg.buildSessionFactory();			
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<Sponsor> res = session.createQuery("from Sponsor where spid = "+spid, Sponsor.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) return false;
		return true;
	}
	@Override
	public boolean existsSemSponsor(Long spid, Long eventId) {
		sf = cfg.buildSessionFactory();			
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<SeminarSponsor> res = session.createQuery("from SeminarSponsor where event_id = "+eventId+"and spid = "+spid, SeminarSponsor.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) return false;
		return true;
	}
	@Override
	public SeminarSponsor findEntityBy(Long eventId, Long spid) {
		sf = cfg.buildSessionFactory();			
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<SeminarSponsor> res = session.createQuery("from SeminarSponsor where event_id = "+eventId+"and spid = "+spid, SeminarSponsor.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) return null;
		return res.get(0);		
	}
	
}
