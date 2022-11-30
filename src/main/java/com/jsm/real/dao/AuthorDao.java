package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Author;
@Repository
public interface AuthorDao extends JpaRepository<Author, Long> {// extends CommonDao<Author> {

}
