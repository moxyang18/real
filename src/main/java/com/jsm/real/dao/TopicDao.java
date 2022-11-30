package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Topic;
@Repository
public interface TopicDao extends JpaRepository<Topic, Long>{
	
}
