package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.RentalRecord;

@Repository
public interface RentalRecordDao extends JpaRepository<RentalRecord, Long> {

}