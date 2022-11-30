package com.jsm.real.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.PaymentDao;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.Invoice;
import com.jsm.real.entity.Payment;
import com.jsm.real.entity.RentalRecord;
import com.jsm.real.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentDao pmtDao;
	private SessionFactory sf;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Payment.class)
			.addAnnotatedClass(RentalRecord.class)
			.addAnnotatedClass(Invoice.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10"); 
	
	@Override
	public Long savePayment(Payment pmt) {
		if(pmt==null) return null;
		return pmtDao.save(pmt).getPid();
	}

	@Override
	public void fetchAllPayments(List<Payment> pmtParts, List<Invoice> invoiceParts, List<RentalRecord> recParts) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Payment as t0, Invoice as t1, RentalRecord as t2 where "
				+ "t0.invoice_id = t1.invoice_id and t1.rental_id = t2.rental_id and ");
		sb.append("1 = 1 order by 1 asc");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			pmtParts.add((Payment)resPart[0]);
			invoiceParts.add((Invoice)resPart[1]);
			recParts.add((RentalRecord)resPart[2]);
		}
		tx.commit();
		session.close();
	}

	@Override
	public void queryPaymentsBy(Payment rec, Long custId, List<Payment> pmtParts, List<Invoice> invoiceParts,
			List<RentalRecord> recParts) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Payment as t0, Invoice as t1, RentalRecord as t2 where "
				+ "t0.invoice_id = t1.invoice_id and t1.rental_id = t2.rental_id and ");
		if(rec!=null&&!rec.getCard_hdr_fname().equals("")) {
			sb.append("t0.card_hdr_fname LIKE '" + rec.getCard_hdr_fname() + "%' and ");
		}
		if(rec!=null&&!rec.getCard_hdr_lname().equals("")) {
			sb.append("t0.card_hdr_lname LIKE '" + rec.getCard_hdr_lname() + "%' and ");
		}
		if(rec!=null&&rec.getInvoice_id()!=null) {
			sb.append("t0.invoice_id = " + rec.getInvoice_id() + " and ");
		}
		if(rec!=null&&rec.getPaid_amt()!=null) {
			sb.append("t0.paid_amt = " + rec.getPaid_amt() + " and ");
		}
		if(rec!=null&&rec.getPaid_date()!=null) {
			sb.append("t0.paid_date = '" + rec.getPaid_date() + "' and ");
		}
		if(rec!=null&&rec.getPAID_METHOD()!=null) {
			sb.append("t0.PAID_METHOD = '" + rec.getPAID_METHOD() + "' and ");
		}
		if(rec!=null&&rec.getPid()!=null) {
			sb.append("t0.pid = " + rec.getPid() + " and ");
		}
		if(custId!=null) {
			sb.append("t2.UID = " + custId + " and ");
		}
		sb.append("1 = 1 order by 1 asc");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			pmtParts.add((Payment)resPart[0]);
			invoiceParts.add((Invoice)resPart[1]);
			recParts.add((RentalRecord)resPart[2]);
		}
		tx.commit();
		session.close();
	}

}
