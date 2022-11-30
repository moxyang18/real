package com.jsm.real.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.TopicDao;
import com.jsm.real.entity.Topic;
import com.jsm.real.service.TopicService;

@Service
public class TopicServiceImpl implements TopicService {
	@Autowired
	private TopicDao topicDao;
	private SessionFactory sf;
	@Override
	public void addTopic(Topic topic) {
		topicDao.save(topic);
	}
	@Override
	public List<Topic> getTopicList() {
		List<Topic> topList = topicDao.findAll();
		return topList;
	}
	@Override
	public boolean containsTopic(String tName) {
		List<Topic> topList = topicDao.findAll();
		if(topList==null||topList.size()<1) return false;
		// return true if a matching topic name found
		for(Topic c: topList) {
			if(c.getTopic_name().equalsIgnoreCase(tName)) return true;
		}
		return false;
	}
	
	@Transactional
	@Override
	public List<Topic> getTopicList(Topic topic, int limit) {
		Configuration cfg = new Configuration()
				.addAnnotatedClass(Topic.class)
				.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
				.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
				.setProperty("hibernate.connection.username", "root")   
		        .setProperty("hibernate.connection.password", "1234")
		        .setProperty("hibernate.hbm2ddl.auto", "update")
		        .setProperty("hibernate.show_sql", "true")
		        .setProperty("hibernate.connection.pool_size", "10"); 
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Topic where ");
		if(topic.getTopic_id()!=null) {
			sb.append("topic_id = " + topic.getTopic_id() + " and ");
		}
		if(topic.getTopic_name()!=null&&topic.getTopic_name().trim()!="") {
			sb.append("topic_name LIKE '" + topic.getTopic_name().trim().toLowerCase() + "%' and ");
		}
		sb.append("1 = 1");
		List<Topic> res = session.createQuery(sb.toString(), Topic.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}	
	
	
	
}
