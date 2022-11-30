package com.jsm.real.service.impl;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.AuthorDao;
import com.jsm.real.dao.CustomerDao;
import com.jsm.real.dao.UserDao;
import com.jsm.real.entity.Author;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Customer;
import com.jsm.real.entity.Topic;
import com.jsm.real.entity.User;
import com.jsm.real.service.UserService;


@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private AuthorDao authDao;
	@Autowired
	private CustomerDao custDao;
	private SessionFactory sf;
	private Configuration cfg = new Configuration()
			.addAnnotatedClass(User.class)
			.addAnnotatedClass(Author.class)
			.addAnnotatedClass(Customer.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10"); 
	@Override
	public Long saveUser(User user) {
		return userDao.save(user).getUid();
	}
	
//	@Transactional
	@Override
	public void saveAuthor(Author author) {
		authDao.save(author);
	}
	
	@Transactional
	@Override
	public void saveCustomer(Customer cust) {
		custDao.save(cust);
	}

	@Override
	public boolean isActiveAccount(User user) {
		if(!userDao.existsById(user.getUid())) return false;
		return queryUserList(user).get(0).getAccnt_status().equals("ACTIVE");
	}

	@Override
	public List<User> fetchAllUsers() {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from User ");
		List<User> res = session.createQuery(sb.toString(), User.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<User> fetchAllSystemUsers() {
		// fetch the users who have account status not as "HOLDER"
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from User where accnt_status in ('ACTIVE', 'FROZEN')");
		List<User> res = session.createQuery(sb.toString(), User.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<User> queryUserList(User user) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from User where ");
		if(user.getUid()!=null) {
			sb.append("uid = " + user.getUid() + " and ");
		}
		if(user.getUsername()!=null&&user.getUsername().trim()!="") {
			sb.append("username LIKE '" + user.getUsername().trim() + "%' and ");
		}
		if(user.getAccnt_status()!=null&&user.getAccnt_status().trim()!="") {
			sb.append("accnt_status LIKE '" + user.getAccnt_status().trim() + "%' and ");
		}
		if(user.getFname()!=null&&user.getFname().trim()!="") {
			sb.append("fname LIKE '" + user.getFname().trim() + "%' and ");
		}
		if(user.getLname()!=null&&user.getLname().trim()!="") {
			sb.append("lname LIKE '" + user.getLname().trim() + "%' and ");
		}
		if(user.getEmail()!=null&&user.getEmail().trim()!="") {
			sb.append("email LIKE '" + user.getEmail().trim() + "%' and ");
		}
		if(user.getPhone_no()!=null) {
			sb.append("phone_no = " + user.getPhone_no() + " and ");
		}
		if(user.getUser_type()!=null) {
			sb.append("user_type = '" + user.getUser_type() + "' and ");
		}
		sb.append("1 = 1");
		List<User> res = session.createQuery(sb.toString(), User.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<Customer> queryCustList(Customer cust) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Customer where ");
		if(cust.getUid()!=null) {
			sb.append("uid = " + cust.getUid() + " and ");
		}
		if(cust.getId_no()!=null&&cust.getId_no().trim()!="") {
			sb.append("id_no LIKE '" + cust.getId_no().trim() + "%' and ");
		}
		if(cust.getId_type()!=null) {
			sb.append("id_type = '" + cust.getId_type() + "' and ");
		}
		sb.append("1 = 1");
		List<Customer> res = session.createQuery(sb.toString(), Customer.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public void queryAuthorList(Author author, String fName, String lName, List<User> userParts, List<Author> authParts) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();

		
		sb.append("from Author as t0, User as t1 where t0.uid=t1.uid and ");
		
		if(author!=null&&author.getUid()!=null) {
			sb.append("t0.uid = " + author.getUid() + " and ");
		}
		if(author!=null&&author.getPen_name()!=null&&author.getPen_name().trim()!="") {
			sb.append("t0.pen_name LIKE '" + author.getPen_name().trim() + "%' and ");
		}
		if(fName!=null&&fName.trim()!="") {
			sb.append("t1.fname LIKE '" + fName.trim() + "%' and ");
		}
		if(lName!=null&&lName.trim()!="") {
			sb.append("t1.lname LIKE '" + lName.trim() + "%' and ");
		}
		sb.append("1 = 1");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			authParts.add((Author)resPart[0]);
			userParts.add((User)resPart[1]);
		}
		tx.commit();
		session.close();
	}
	
	@Override
	public List<Author> queryAuthList(Author author) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Author where ");
		if(author.getUid()!=null) {
			sb.append("uid = " + author.getUid() + " and ");
		}
		if(author.getCity()!=null&&author.getCity().trim()!="") {
			sb.append("city LIKE = '" + author.getCity().trim() + "%' and ");
		}
		if(author.getState()!=null&&author.getState().trim()!="") {
			sb.append("state LIKE = '" +  author.getState().trim() + "%' and ");
		}
		if(author.getStreet()!=null&&author.getStreet().trim()!="") {
			sb.append("street LIKE = '" + author.getStreet().trim() + "%' and ");
		}
		if(author.getCountry()!=null&&author.getCountry().trim()!="") {
			sb.append("country LIKE = '" + author.getCountry().trim() + "%' and ");
		}
		if(author.getPen_name()!=null&&author.getPen_name().trim()!="") {
			sb.append("pen_name LIKE = '" + author.getPen_name().trim() + "%' and ");
		}
		if(author.getZipcode()!=null&&author.getZipcode().trim()!="") {
			sb.append("zipcode LIKE = '" + author.getZipcode().trim() + "%' and ");
		}
		sb.append("1 = 1");
		List<Author> res = session.createQuery(sb.toString(), Author.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public void activateAccount(User user) {
		List<User> res = null;
		if(user.getUid()!=null) {
			res = this.findUserByUid(user);
		} else if(user.getUsername()!=null) {
			res = this.findUserByUsername(user);			
		}
		user = res.get(0);
		user.setAccnt_status("ACTIVE");
		userDao.save(user);
	}

	@Override
	public void deactivateAccount(User user) {
		List<User> res = null;
		if(user.getUid()!=null) {
			res = this.findUserByUid(user);
		} else if(user.getUsername()!=null) {
			res = this.findUserByUsername(user);			
		}
		user = res.get(0);
		user.setAccnt_status("FROZEN");
		userDao.save(user);		
	}

	@Override
	public List<User> findUserByUsername(User user) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		String username = "INVALID";
		if(user.getUsername()!=null&&user.getUsername().trim()!="") {
			username = user.getUsername().trim();
		}
		sb.append("from User where username = '" + username +"' ");
		List<User> res = session.createQuery(sb.toString(), User.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<User> findUserByUid(User user) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		Long uid = (long) -1;
		if(user.getUid()!=null) {
			uid = user.getUid();
		}
		sb.append("from User where uid = " + uid +" ");
		List<User> res = session.createQuery(sb.toString(), User.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public boolean existsAuthor(Long uid) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		List<Author> res = session.createQuery("from Author where uid = "+ uid, Author.class).getResultList();
		tx.commit();
		session.close();
		if(res!=null&&res.size()>0) return true;
		return false;
	}

	@Override
	public void queryCustomerList(Customer customer, String fName, String lName, List<User> userParts,
			List<Customer> custParts) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();

		sb.append("from Customer as t0, User as t1 where t0.uid=t1.uid and ");
		
		if(customer!=null&&customer.getUid()!=null) {
			sb.append("t0.uid = " + customer.getUid() + " and ");
		}
		if(customer!=null&&customer.getId_no()!=null&&customer.getId_no().trim()!="") {
			sb.append("t0.pen_name LIKE '" + customer.getId_no().trim() + "%' and ");
		}
		if(customer!=null&&customer.getId_type()!=null) {
			sb.append("t0.pen_name LIKE '" + customer.getId_type() + "%' and ");
		}		
		if(fName!=null&&fName.trim()!="") {
			sb.append("t1.fname LIKE '" + fName.trim() + "%' and ");
		}
		if(lName!=null&&lName.trim()!="") {
			sb.append("t1.lname LIKE '" + lName.trim() + "%' and ");
		}
		sb.append("1 = 1");
		List<Object> res = session.createQuery(sb.toString()).list();
		if(res==null) return;
		for(int i = 0; i< res.size(); i++) {
			Object[] resPart = (Object[]) res.get(i);
			custParts.add((Customer)resPart[0]);
			userParts.add((User)resPart[1]);
		}
		tx.commit();
		session.close();
	}
}
