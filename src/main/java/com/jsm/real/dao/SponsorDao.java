package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Sponsor;
@Repository
public interface SponsorDao extends JpaRepository<Sponsor, Long>{

}
