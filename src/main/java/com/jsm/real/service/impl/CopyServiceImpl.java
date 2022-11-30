package com.jsm.real.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.CopyDao;
import com.jsm.real.entity.Author;
import com.jsm.real.entity.AuthorBook;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.Topic;
import com.jsm.real.service.CopyService;

@Service
public class CopyServiceImpl implements CopyService {
	private SessionFactory sf;
	@Autowired
	CopyDao copyDao;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Copy.class)
			.addAnnotatedClass(Book.class)
			.addAnnotatedClass(Topic.class)
			.addAnnotatedClass(AuthorBook.class)
			.addAnnotatedClass(Author.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10"); 
	
	@Transactional
	@Override
	public void addCopy(Copy copy) {
		copy.setCopy_id(null);
		copyDao.save(copy);
	}

	@Override
	public void deleteCopy(Copy copy) {
		copyDao.delete(copy);
	}

	@Transactional
	@Override
	public List<Copy> getCopyList(Copy copy, int limit) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Copy where ");
		if(copy.getCopy_id()!=null) {
			sb.append("copy_id = " + copy.getCopy_id() + " and ");
		}
		if(copy.getBid()!=null) {
			sb.append("bid = " + copy.getBid() + " and ");
		}
		if(copy.getStatus()!=null) {
			sb.append("status = '" + copy.getStatus() + "' and ");
		}
		sb.append("1 = 1 order by 1 asc");
		List<Copy> res = session.createQuery(sb.toString(), Copy.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public void getBookCopyList(Copy copy, String bookName, Integer topicId, int limit, List<Copy> copyParts,
			List<Book> bookParts, List<Topic> topicParts) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Copy as t0, Book as t1, Topic as t2 where t0.bid=t1.bid and t1.topic_id=t2.topic_id and ");
		if(copy!=null&&copy.getCopy_id()!=null) {
			sb.append("t0.copy_id = " + copy.getCopy_id() + " and ");
		}
		if(copy!=null&&copy.getBid()!=null) {
			sb.append("t0.bid = " + copy.getBid() + " and ");
		}
		if(copy!=null&&copy.getStatus()!=null) {
			sb.append("t0.status = '" + copy.getStatus() + "' and ");
		}
		if(bookName!=null&&!bookName.trim().equals("")) {
			sb.append("t1.book_name LIKE '" + bookName.trim() + "%' and ");
		}
		if(topicId!=null) {
			sb.append("t2.topic_id = " + topicId + " and ");
		}
		sb.append("1 = 1 order by 1 asc");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			copyParts.add((Copy)resPart[0]);
			bookParts.add((Book)resPart[1]);
			topicParts.add((Topic)resPart[2]);
		}
		tx.commit();
		session.close();
		
	}

	@Override
	public void getBookCopyListBetter(Copy copy, String bookName, Integer topicId, String authPenName, Long authId,
			int limit, List<Copy> copyParts, List<Book> bookParts, List<Topic> topicParts,
			List<AuthorBook> authBookParts, List<Author> authParts) {
		// TODO Auto-generated method stub
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Copy as t0, Book as t1, Topic as t2, AuthorBook as t3, Author as t4 "
				+ "where t0.bid = t1.bid and t1.topic_id = t2.topic_id and "
				+ "t0.bid = t3.bid and t3.UID = t4.uid and ");
		if(copy!=null&&copy.getCopy_id()!=null) {
			sb.append("t0.copy_id = " + copy.getCopy_id() + " and ");
		}
		if(copy!=null&&copy.getBid()!=null) {
			sb.append("t0.bid = " + copy.getBid() + " and ");
		}
		if(copy!=null&&copy.getStatus()!=null) {
			sb.append("t0.status = '" + copy.getStatus() + "' and ");
		}
		if(bookName!=null&&!bookName.trim().equals("")) {
			sb.append("t1.book_name LIKE '" + bookName.trim() + "%' and ");
		}
		if(topicId!=null&&topicId!=0) {
			sb.append("t2.topic_id = " + topicId + " and ");
		}
		if(authPenName!=null&&!authPenName.trim().equals("")) {
			sb.append("t4.pen_name LIKE '" + authPenName.trim() + "%' and ");
		}
		if(authId!=null) {
			sb.append("t3.UID = " + authId + " and ");
		}
		sb.append("1 = 1 order by 1 asc");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			copyParts.add((Copy)resPart[0]);
			bookParts.add((Book)resPart[1]);
			topicParts.add((Topic)resPart[2]);
			authBookParts.add((AuthorBook)resPart[3]);
			authParts.add((Author)resPart[4]);
		}
		tx.commit();
		session.close();
	}

	@Override
	public boolean isAvailable(Long copyId) {
		if(copyId==null) return false;
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<Copy> res = session.createQuery("from Copy where copy_id = " + copyId, Copy.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) {
			return false;
		}
		Copy copy = res.get(0);
		if(copy.getStatus()=='Y') return true;
		return false;
	}

	@Override
	public Long saveCopy(Copy copy) {
		return copyDao.save(copy).getCopy_id();
	}

	@Override
	public Copy getCopyById(Long copyId) {
		if(copyId==null) return null;
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<Copy> res = session.createQuery("from Copy where copy_id = " + copyId, Copy.class).getResultList();
		tx.commit();
		session.close();
		if(res==null||res.size()<1) {
			return null;
		}
		Copy copy = res.get(0);
		return copy;
	}

}
