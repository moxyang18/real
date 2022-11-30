package com.jsm.real.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.AuthorBookDao;
import com.jsm.real.dao.BookDao;
import com.jsm.real.entity.Author;
import com.jsm.real.entity.AuthorBook;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Topic;
import com.jsm.real.service.BookService;

@Service
public class BookServiceImpl implements BookService {
	@Autowired
	BookDao bookDao;
	@Autowired
	AuthorBookDao authBookDao;
	private SessionFactory sf;
	Configuration cfg = new Configuration()
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
	@Override
	public void saveBook(Book book) {
		// add entry in jsm_book,
		book.setBook_name(book.getBook_name().toUpperCase());
		bookDao.save(book);
	}
	
	@Override
	public void addAuthBook(Long bid, Long authId) {
		// add a corresponding entry in jsm_author_book
		AuthorBook authBook = new AuthorBook();
		authBook.setBid(bid);
		authBook.setUID(authId);
		authBookDao.save(authBook);
	}
	
	@Override
	public void deleteBook(Book book, Long authId) {
	}
	
	
	@Transactional
	@Override
	public void getBookList(Book book, String topicName, int limit, List<Book> bookParts, List<Topic> topicParts) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Book as t0, Topic as t1 where t0.topic_id=t1.topic_id and ");
		if(book.getBid()!=null) {
			sb.append("t0.bid = " + book.getBid() + " and ");
		}
		if(book.getBook_name()!=null&&book.getBook_name().trim()!="") {
			sb.append("t0.book_name LIKE '" + book.getBook_name().toUpperCase() + "%' and ");
		}
		if(book.getTopic_id()!=null) {
			sb.append("t0.topic_id = " + book.getTopic_id() + " and ");
		}
		if(topicName!=null&&!topicName.trim().equals("")) {
			sb.append("t1.topic_name LIKE '" + topicName.trim().toLowerCase() + "%' and ");
		}
		sb.append("1 = 1");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			bookParts.add((Book)resPart[0]);
			topicParts.add((Topic)resPart[1]);
		}
		tx.commit();
		session.close();
	}

	@Transactional
	@Override
	public List<Author> getAuthorList(Book book) {
		if(book.getBid()==null) return new ArrayList<>();
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from AuthorBook where bid = " + book.getBid());
		List<AuthorBook> res = session.createQuery(sb.toString(), AuthorBook.class).getResultList();
		sb = new StringBuilder();
		sb.append("from Author where UID in (");
		for(int i = 0; i<res.size(); i++) {
			sb.append(res.get(i).getUID());
			if(i!=res.size()-1) sb.append(", ");
			else sb.append(")");
		}
		List<Author> res2 = session.createQuery(sb.toString(), Author.class).getResultList();
		tx.commit();
		session.close();
		return res2;
	}

	@Override
	public void getAuthBookList(AuthorBook authBook, String bookName, String penName, int limit,
			List<AuthorBook> authBookParts, List<Book> bookParts, List<Author> authParts) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from AuthorBook as t0, Author as t1, Book as t2 where t0.bid=t2.bid and t0.UID=t1.uid and ");
		if(authBook!=null&&authBook.getBid()!=null) {
			sb.append("t0.bid = " + authBook.getBid() + " and ");
		}
		if(authBook!=null&&authBook.getUID()!=null) {
			sb.append("t0.UID = " + authBook.getUID() + " and ");
		}
		if(bookName!=null&&bookName.trim()!="") {
			sb.append("t2.book_name LIKE '" + bookName.toUpperCase() + "%' and ");
		}
		if(penName!=null&&penName.trim()!="") {
			sb.append("t1.pen_name LIKE '" + penName + "%' and ");
		}
		sb.append("1 = 1");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			authBookParts.add((AuthorBook)resPart[0]);
			bookParts.add((Book)resPart[2]);
			authParts.add((Author)resPart[1]);
		}
		tx.commit();
		session.close();
	}

	@Override
	public boolean existsBook(Long bid) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<Book> res = session.createQuery("from Book where bid = "+ bid, Book.class).getResultList();
		tx.commit();
		session.close();
		if(res!=null&&res.size()>0) return true;
		return false;
	}

	@Override
	public void delAuthBook(Long bid, Long authId) {
		// delete a corresponding entry in jsm_author_book
		AuthorBook authBook = new AuthorBook();
		authBook.setBid(bid);
		authBook.setUID(authId);
		authBookDao.delete(authBook);
	}

	@Override
	public boolean existsAuthBook(Long bid, Long authid) {
		sf = cfg.buildSessionFactory();		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<AuthorBook> res = session.createQuery("from AuthorBook where bid = "+ bid +" and UID = "+authid, AuthorBook.class).getResultList();
		tx.commit();
		session.close();
		if(res!=null&&res.size()>0) return true;
		return false;
	}
	
}
