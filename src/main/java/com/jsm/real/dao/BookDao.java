package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Book;

@Repository
public interface BookDao extends JpaRepository<Book, Long> {

}