package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Exhibition;

@Repository
public interface ExhibitionDao extends JpaRepository<Exhibition, Long> {

}
