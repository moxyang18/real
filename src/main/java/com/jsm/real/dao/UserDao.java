package com.jsm.real.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {// extends CommonDao<User> {
	
}
