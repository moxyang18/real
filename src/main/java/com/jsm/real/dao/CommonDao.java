package com.jsm.real.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Topic;

@Repository
public interface CommonDao extends JpaRepository<Topic, Long> {//<T extends BaseEntity> extends JpaRepository<T, Long>{
	
}
