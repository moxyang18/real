package com.jsm.real.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsm.real.dao.ReservationDao;
import com.jsm.real.entity.Reservation;
import com.jsm.real.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService{
	@Autowired 
	private ReservationDao resvDao;
	
	private SessionFactory sf;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Reservation.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10"); 
	@Override
	public void addResv(Reservation resv) {
		resvDao.save(resv);
	}

	@Override
	public List<Reservation> getResvList() {
		return resvDao.findAll();
	}

	@Override
	@Transactional
	public List<Reservation> getResvListByRoomDate(Reservation resv) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Reservation where ");
		if(resv.getRoom_id()!=null) {
			sb.append("room_id = " + resv.getRoom_id() + " and ");
		}
		if(resv.getResv_date()!=null) {
			sb.append("resv_date = '" + resv.getResv_date().toString() + "' and ");
		}
		sb.append("1 = 1");
		List<Reservation> res = session.createQuery(sb.toString(), Reservation.class).getResultList();
		tx.commit();
		session.close();
		//sf.close();
		return res;
	}

	@Override
	public List<Reservation> getResvListBy(Reservation resv) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Reservation where ");
		if(resv.getResv_no()!=null) {
			sb.append("resv_no = " + resv.getResv_no() + " and ");
		}
		if(resv.getRoom_id()!=null) {
			sb.append("room_id = " + resv.getRoom_id() + " and ");
		}
		if(resv.getUID() !=null) {
			sb.append("UID = " + resv.getUID() + " and ");
		}
		if(resv.getResv_date()!=null) {
			sb.append("resv_date = '" + resv.getResv_date().toString() + "' and ");
		}
		if(resv.getTime_slot()!=null) {
			sb.append("time_slot = " + resv.getTime_slot() + " and ");
		}
		
		sb.append("1 = 1");
		List<Reservation> res = session.createQuery(sb.toString(), Reservation.class).getResultList();
		tx.commit();
		session.close();
		//sf.close();
		return res;
	}

	@Override
	public void deleteResv(Integer resvId) {
		Reservation resv = new Reservation();
		resv.setResv_no(resvId);
		resvDao.delete(resv);
	}

	@Override
	public boolean existsFutureResvByResvIdUid(Integer resvNo, Long uid) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		List<Reservation> res = session.createQuery("from Reservation where resv_no = " + 
				resvNo + " and UID = " + uid + " and resv_date > curdate()", Reservation.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) return false;
		return true;
	}
	
	

}
