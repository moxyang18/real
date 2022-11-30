package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Registration;

@Repository
public interface RegistrationDao extends JpaRepository<Registration, Long> {

}