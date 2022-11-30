package com.jsm.real.service.impl;

import java.sql.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jsm.real.dao.AuthorBookDao;
import com.jsm.real.dao.BookDao;
import com.jsm.real.dao.RentalRecordDao;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.RentalRecord;
import com.jsm.real.entity.Topic;
import com.jsm.real.service.RentalRecordService;

@Service
public class RentalRecordImpl implements RentalRecordService {
	@Autowired
	RentalRecordDao recDao;
	@Autowired
	AuthorBookDao authBookDao;
	private SessionFactory sf;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Book.class)
			.addAnnotatedClass(Copy.class)
			.addAnnotatedClass(RentalRecord.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10"); 
	@Transactional
	@Override
	public Long genRentalRec(RentalRecord rentalRec) {
		// generate new rental record with borrowed time = now
		return recDao.save(rentalRec).getRental_id();
	}

	@Override
	public Long uptRentalRec(RentalRecord rentalRec) {
		// update the rental record upon returning
		return recDao.save(rentalRec).getRental_id();
	}

	@Override
	public void fetchAllRentalRecs(List<RentalRecord> recParts, List<Copy> copyParts, List<Book> bookParts) {
		// TODO Auto-generated method stub
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from RentalRecord as t0, Copy as t1, Book as t2 where t0.copy_id=t1.copy_id and t1.bid = t2.bid order by 1 asc");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			recParts.add((RentalRecord)resPart[0]);
			copyParts.add((Copy)resPart[1]);
			bookParts.add((Book)resPart[2]);
		}
		tx.commit();
		session.close();
	}

	@Transactional
	@Override
	public void qryRentalRecordsBy(RentalRecord rec, Long bid, String bookName, List<RentalRecord> recParts,
			List<Copy> copyParts, List<Book> bookParts) {
		// TODO Auto-generated method stub
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from RentalRecord as t0, Copy as t1, Book as t2 where t0.copy_id=t1.copy_id and t1.bid = t2.bid and ");
		if(rec!=null&&rec.getAct_ret_date()!=null) {
			sb.append("t0.act_ret_date = '" + rec.getAct_ret_date() + "' and ");
		}
		if(rec!=null&&rec.getBorrow_date()!=null) {
			sb.append("t0.borrow_date = '" + rec.getBorrow_date() + "' and ");
		}
		if(rec!=null&&rec.getCopy_id()!=null) {
			sb.append("t0.copy_id = " + rec.getCopy_id() + " and ");
		}
		if(rec!=null&&rec.getExp_ret_date()!=null) {
			sb.append("t0.exp_ret_date = '" + rec.getExp_ret_date() + "' and ");
		}
		if(rec!=null&&rec.getRental_id()!=null) {
			sb.append("t0.rental_id = " + rec.getRental_id() + " and ");
		}
		if(rec!=null&&rec.getRental_status()!=null) {
			sb.append("t0.rental_status = '" + rec.getRental_status() + "' and ");
		}
		if(rec!=null&&rec.getUID()!=null) {
			sb.append("t0.UID = " + rec.getUID() + " and ");
		}
		if(bid!=null) {
			sb.append("t2.bid = " + bid + " and ");
		}
		if(bookName!=null&&!bookName.trim().equals("")) {
			sb.append("t2.book_name LIKE '" + bookName.trim() + "%' and ");
		}
		sb.append("1 = 1 order by 1 asc");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			recParts.add((RentalRecord)resPart[0]);
			copyParts.add((Copy)resPart[1]);
			bookParts.add((Book)resPart[2]);
		}
		tx.commit();
		session.close();
	}

	@Transactional
	@Override
	public boolean isBorrowed(Long recId) {
		if(recId==null) return false;
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<RentalRecord> res = session.createQuery("from RentalRecord where rental_id = " + recId, RentalRecord.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) {
			return false;
		}
		RentalRecord rec = res.get(0);
		if(rec.getRental_status()=='B') return true;
		return false;
	}

	@Transactional
	@Override
	public RentalRecord getRecById(Long recId) {
		if(recId==null) return null;
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<RentalRecord> res = session.createQuery("from RentalRecord where rental_id = " + recId, RentalRecord.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) {
			return null;
		}
		RentalRecord rentalRecord = res.get(0);
		return rentalRecord;
	}
}
