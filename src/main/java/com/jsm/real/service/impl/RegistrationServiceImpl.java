package com.jsm.real.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.RegistrationDao;
import com.jsm.real.entity.Event;
import com.jsm.real.entity.Exhibition;
import com.jsm.real.entity.Registration;
import com.jsm.real.entity.User;
import com.jsm.real.service.RegistrationService;

@Service
public class RegistrationServiceImpl implements RegistrationService {
	@Autowired
	RegistrationDao registrationDao;
	private SessionFactory sf;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Registration.class)
			.addAnnotatedClass(User.class)
			.addAnnotatedClass(Event.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10"); 
	
	@Override
	public Long addRegistration(Registration registration) {
		return registrationDao.save(registration).getReg_id();
	}

	@Override
	public List<Registration> getEventList(Registration registration) {

		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Registration where ");
		if(registration.getReg_id()!=null) {
			sb.append("reg_id = " + registration.getReg_id() + " and ");
		}
		if(registration.getEvent_id()!=null) {
			sb.append("event_id = " + registration.getEvent_id() + " and ");
		}
		if(registration.getUID()!=null) {
			sb.append("UID = " + registration.getUID() + " and ");
		}
		sb.append("1 = 1");
		List<Registration> res = session.createQuery(sb.toString(), Registration.class).getResultList();
		tx.commit();
		session.close();
		return res;	
	}

	
	@Override
	public void delRegistration(Registration registration) {
		registrationDao.delete(registration);
	}

	@Override
	public void getRegInfoById(Long uid, List<Registration> regParts, List<Event> exbParts) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Registration as t0, Event as t1 where t0.event_id=t1.event_id and t0.UID = " + uid + "");
		List<Object> res = session.createQuery(sb.toString()).list();
		tx.commit();
		session.close();
		if(res==null) return;
		for(int i = 0; i<res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			regParts.add((Registration) resPart[0]);
			exbParts.add((Event) resPart[1]);
		}
	}

	@Override
	public void queryOwnRegistration(Long uid, Long regId, Event event, List<Registration> regParts, List<Event> exbParts) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Registration as t0, Event as t1 where t0.event_id=t1.event_id and t0.UID = " + uid + " and ");
		if(regId!=null) {
			sb.append("t0.reg_id = " + regId + " and ");
		}
		if(event!=null&&event.getEvent_id()!=null) {
			sb.append("t1.event_id = " + event.getEvent_id() + " and ");
		}
		if(event!=null&&event.getEvent_name()!=null&&!event.getEvent_name().trim().equals("")) {
			sb.append("t1.event_name LIKE '" + event.getEvent_name().trim() + "%' and ");
		}
		if(event!=null&&event.getStart_datetime()!=null) {
			sb.append("t1.start_datetime = '" + event.getStart_datetime() + "' and ");
		}
		if(event!=null&&event.getStop_datetime()!=null) {
			sb.append("t1.stop_datetime = '" + event.getStop_datetime() + "' and ");
		}
		sb.append("1=1");
		List<Object> res = session.createQuery(sb.toString()).list();
		tx.commit();
		session.close();
		if(res==null) return;
		for(int i = 0; i<res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			regParts.add((Registration) resPart[0]);
			exbParts.add((Event) resPart[1]);
		}
		
	}

}
