package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Invoice;

@Repository
public interface InvoiceDao extends JpaRepository<Invoice, Long> {

}
