package com.jsm.real.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.InvoiceDao;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.Invoice;
import com.jsm.real.entity.RentalRecord;
import com.jsm.real.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {
	@Autowired
	InvoiceDao invoiceDao;
	private SessionFactory sf;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Copy.class)
			.addAnnotatedClass(Book.class)
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
	public Long saveInvoice(Invoice invoice) {
		return invoiceDao.save(invoice).getInvoice_id();
	}

	@Override
	public void fetchAllInvoices(List<Invoice> invoiceParts, List<RentalRecord> recParts, List<Copy> copyParts,
			List<Book> bookParts) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Invoice as t0, RentalRecord as t1, Copy as t2, Book as t3 where "
				+ "t0.rental_id = t1.rental_id and t1.copy_id = t2.copy_id and t2.bid = t3.bid and ");
		sb.append("1 = 1 order by 1 asc");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			invoiceParts.add((Invoice)resPart[0]);
			recParts.add((RentalRecord)resPart[1]);
			copyParts.add((Copy)resPart[2]);
			bookParts.add((Book)resPart[3]);
		}
		tx.commit();
		session.close();
	}

	@Override
	public void queryInvoicesBy(Invoice invoice, Long copyId, Long custId, List<Invoice> invoiceParts,
			List<RentalRecord> recParts, List<Copy> copyParts, List<Book> bookParts) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Invoice as t0, RentalRecord as t1, Copy as t2, Book as t3 where "
				+ "t0.rental_id = t1.rental_id and t1.copy_id = t2.copy_id and t2.bid = t3.bid and ");
		if(invoice!=null&&invoice.getInvoice_amt()!=null) {
			sb.append("t0.invoice_amt = " + invoice.getInvoice_amt() + " and ");
		}
		if(invoice!=null&&invoice.getInvoice_date()!=null) {
			sb.append("t0.invoice_date = '" + invoice.getInvoice_date() + "' and ");
		}
		if(invoice!=null&&invoice.getInvoice_id()!=null) {
			sb.append("t0.invoice_id = " + invoice.getInvoice_id() + " and ");
		}
		if(invoice!=null&&invoice.getRental_id()!=null) {
			sb.append("t0.rental_id = " + invoice.getRental_id() + " and ");
		}		
		if(copyId!=null) {
			sb.append("t2.copy_id = " + copyId + " and ");
		}
		if(custId!=null) {
			sb.append("t1.UID = " + custId + " and ");
		}
		if(invoice!=null&&invoice.getAmt_to_pay()!=null) {
			sb.append("t0.amt_to_pay = " + invoice.getAmt_to_pay() + " and ");
		}
		if(invoice!=null&&!invoice.getPaid_status().equals("")) {
			sb.append("t0.paid_status = '" + invoice.getPaid_status() +"' and ");
		}
		sb.append("1 = 1 order by 1 asc");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			invoiceParts.add((Invoice)resPart[0]);
			recParts.add((RentalRecord)resPart[1]);
			copyParts.add((Copy)resPart[2]);
			bookParts.add((Book)resPart[3]);
		}
		tx.commit();
		session.close();
	}

	@Override
	public Invoice getInvoiceById(Long invoice_id) {
		if(invoice_id==null) return null;
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<Invoice> res = session.createQuery("from Invoice where invoice_id = " + invoice_id, Invoice.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) {
			return null;
		}
		return res.get(0);
	}

}
