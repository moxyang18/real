package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.SeminarSponsor;

@Repository
public interface SeminarSponsorDao extends JpaRepository<SeminarSponsor, Long> {

}