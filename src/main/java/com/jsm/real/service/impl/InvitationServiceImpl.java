package com.jsm.real.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.InvitationDao;
import com.jsm.real.entity.Event;
import com.jsm.real.entity.Invitation;
import com.jsm.real.entity.Registration;
import com.jsm.real.entity.Seminar;
import com.jsm.real.service.InvitationService;

@Service
public class InvitationServiceImpl implements InvitationService {
	@Autowired
	InvitationDao invitationDao;
	private SessionFactory sf;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Invitation.class)
			.addAnnotatedClass(Seminar.class)
			.addAnnotatedClass(Event.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10"); 
	@Override
	public Long addInvitation(Invitation invitation) {
		return invitationDao.save(invitation).getInv_id();
	}

	@Override
	public List<Invitation> getEventList(Invitation invitation) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Invitation where ");
		if(invitation.getInv_id()!=null) {
			sb.append("inv_id = " + invitation.getInv_id() + " and ");
		}
		if(invitation.getEvent_id()!=null) {
			sb.append("event_id = " + invitation.getEvent_id() + " and ");
		}
		if(invitation.getUID()!=null) {
			sb.append("UID = " + invitation.getUID() + " and ");
		}
		sb.append("1 = 1");
		List<Invitation> res = session.createQuery(sb.toString(), Invitation.class).getResultList();
		tx.commit();
		session.close();
		return res;	
	}

	@Override
	public void delInvitation(Invitation invitation) {
		invitationDao.delete(invitation);
	}

	@Override
	public void getInviInfoById(Long uid, List<Invitation> regParts, List<Event> semParts) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Invitation as t0, Event as t1 where t0.event_id=t1.event_id and t0.UID = " + uid + "");
		List<Object> res = session.createQuery(sb.toString()).list();
		tx.commit();
		session.close();
		if(res==null) return;
		for(int i = 0; i<res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			regParts.add((Invitation) resPart[0]);
			semParts.add((Event) resPart[1]);
		}
	}

	@Override
	public void queryOwnInvitation(Long uid, Long inviId, Event event, List<Invitation> inviParts,
			List<Event> semParts) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Invitation as t0, Event as t1 where t0.event_id=t1.event_id and t0.UID = " + uid + " and ");
		if(inviId!=null) {
			sb.append("t0.inv_id = " + inviId + " and ");
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
			inviParts.add((Invitation) resPart[0]);
			semParts.add((Event) resPart[1]);
		}
		
	}

}
