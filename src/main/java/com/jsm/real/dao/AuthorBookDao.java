package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.AuthorBook;

@Repository
public interface AuthorBookDao extends JpaRepository<AuthorBook, Long> {

}