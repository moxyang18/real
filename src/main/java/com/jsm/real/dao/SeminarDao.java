package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Seminar;

@Repository
public interface SeminarDao extends JpaRepository<Seminar, Long> {

}