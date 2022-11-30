package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Payment;
@Repository
public interface PaymentDao extends JpaRepository<Payment, Long> {

}
