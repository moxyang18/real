package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Event;

@Repository
public interface EventDao extends JpaRepository<Event, Long> {

}