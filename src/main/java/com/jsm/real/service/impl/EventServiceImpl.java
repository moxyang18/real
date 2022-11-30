package com.jsm.real.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsm.real.dao.EventDao;
import com.jsm.real.dao.ExhibitionDao;
import com.jsm.real.dao.SeminarDao;
import com.jsm.real.entity.Event;
import com.jsm.real.entity.Exhibition;
import com.jsm.real.entity.Seminar;
import com.jsm.real.service.EventService;

@Service
public class EventServiceImpl implements EventService{
	@Autowired
	EventDao eventDao;
	@Autowired
	ExhibitionDao exhibitionDao;
	@Autowired
	SeminarDao seminarDao;
	private SessionFactory sf;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Seminar.class)
			.addAnnotatedClass(Event.class)
			.addAnnotatedClass(Exhibition.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10");
	
	
	@Override
	public Long saveEvent(Event event) {
		eventDao.save(event);
		return event.getEvent_id();
	}

	@Override
	public Long saveExhibition(Exhibition exhibition) {
		exhibitionDao.save(exhibition);
		return exhibition.getEvent_id();
	}

	@Override
	public Long saveSeminar(Seminar seminar) {
		seminarDao.save(seminar);
		return seminar.getEvent_id();
	}

	@Transactional
	@Override
	public List<Event> getEventList(Event event) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Event where ");
		if(event.getEvent_id()!=null) {
			sb.append("event_id = " + event.getEvent_id() + " and ");
		}
		if(event.getEvent_name()!=null&&event.getEvent_name().trim()!="") {
			sb.append("event_name LIKE '" + event.getEvent_name().trim().toUpperCase() + "%' and ");
		}
		if(event.getEvent_type()!=null) {
			sb.append("event_type = '" + event.getEvent_type() + "' and ");
		}
		if(event.getStart_datetime()!=null) {
			sb.append("start_datetime = '" + event.getStart_datetime() + "' and ");
		}
		if(event.getStop_datetime()!=null) {
			sb.append("stop_datetime = '" + event.getStop_datetime() + "' and ");
		}		
		if(event.getTopic_id()!=null) {
			sb.append("topic_id = " + event.getTopic_id() + " and ");
		}
		sb.append("1 = 1");
		List<Event> res = session.createQuery(sb.toString(), Event.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<Exhibition> getExhibitionList(Exhibition exhibition) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Exhibition where ");
		if(exhibition.getEvent_id()!=null) {
			sb.append("event_id = " + exhibition.getEvent_id() + " and ");
		}
		if(exhibition.getExpense()!=null) {
			sb.append("expense = " + exhibition.getExpense() + " and ");
		}
		sb.append("1 = 1");
		List<Exhibition> res = session.createQuery(sb.toString(), Exhibition.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<Seminar> getSeminarList(Seminar seminar) { 
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Seminar where ");
		if(seminar.getEvent_id()!=null) {
			sb.append("event_id = " + seminar.getEvent_id() + " and ");
		}
		if(seminar.getSEM_CAPACITY()!=null) {
			sb.append("SEM_CAPACITY = " + seminar.getSEM_CAPACITY() + " and ");
		}
		sb.append("1 = 1");
		List<Seminar> res = session.createQuery(sb.toString(), Seminar.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public void deleteExhibition(Event event, Exhibition exhibition) {
		exhibitionDao.delete(exhibition);
		eventDao.delete(event);
	}

	@Override
	public void deleteSeminar(Event event, Seminar seminar) {
		seminarDao.delete(seminar);
		eventDao.delete(event);
	}

	@Override
	public List<Event> getAllFutureSems() {
		sf = cfg.buildSessionFactory();		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Event where ");
		List<Event> res = session.createQuery("from Event where event_type = 'S' and start_datetime > NOW()", Event.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<Event> getAllFutureSemsBy(Event event) {
		sf = cfg.buildSessionFactory();		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Event where event_type = 'S' and start_datetime > NOW() and ");
		if(event.getEvent_id()!=null) {
			sb.append("event_id = " + event.getEvent_id() + " and ");
		}
		if(event.getEvent_name()!=null&&event.getEvent_name().trim()!="") {
			sb.append("event_name LIKE '" + event.getEvent_name().trim().toUpperCase() + "%' and ");
		}
		if(event.getEvent_type()!=null) {
			sb.append("event_type = '" + event.getEvent_type() + "' and ");
		}
		if(event.getStart_datetime()!=null) {
			sb.append("start_datetime = '" + event.getStart_datetime() + "' and ");
		}
		if(event.getStop_datetime()!=null) {
			sb.append("stop_datetime = '" + event.getStop_datetime() + "' and ");
		}		
		if(event.getTopic_id()!=null) {
			sb.append("topic_id = " + event.getTopic_id() + " and ");
		}
		sb.append("1 = 1");
		List<Event> res = session.createQuery(sb.toString(), Event.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<Event> getAllFutureExhbs() {
		sf = cfg.buildSessionFactory();			
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Event where ");
		List<Event> res = session.createQuery("from Event where event_type = 'E' and start_datetime > NOW()", Event.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<Event> getAllFutureExhbsBy(Event event) {
		sf = cfg.buildSessionFactory();		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Event where event_type = 'E' and start_datetime > NOW() and ");
		if(event.getEvent_id()!=null) {
			sb.append("event_id = " + event.getEvent_id() + " and ");
		}
		if(event.getEvent_name()!=null&&event.getEvent_name().trim()!="") {
			sb.append("event_name LIKE '" + event.getEvent_name().trim().toUpperCase() + "%' and ");
		}
		if(event.getEvent_type()!=null) {
			sb.append("event_type = '" + event.getEvent_type() + "' and ");
		}
		if(event.getStart_datetime()!=null) {
			sb.append("start_datetime = '" + event.getStart_datetime() + "' and ");
		}
		if(event.getStop_datetime()!=null) {
			sb.append("stop_datetime = '" + event.getStop_datetime() + "' and ");
		}		
		if(event.getTopic_id()!=null) {
			sb.append("topic_id = " + event.getTopic_id() + " and ");
		}
		sb.append("1 = 1");
		List<Event> res = session.createQuery(sb.toString(), Event.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public boolean existsFutureExhibition(Long event_id) {
		sf = cfg.buildSessionFactory();			
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Event where ");
		List<Event> res = session.createQuery("from Event where event_type = 'E' and start_datetime > NOW() and event_id = "+event_id+" ", Event.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) return false;
		return true;
	}

}
