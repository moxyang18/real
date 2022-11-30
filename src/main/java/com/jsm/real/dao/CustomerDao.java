package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Customer;
@Repository
public interface CustomerDao extends JpaRepository<Customer, Long> { //extends CommonDao<Customer> {

}
