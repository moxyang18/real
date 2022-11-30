package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Copy;

@Repository
public interface CopyDao extends JpaRepository<Copy, Long> {

}