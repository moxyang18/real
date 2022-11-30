package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.Invitation;

@Repository
public interface InvitationDao extends JpaRepository<Invitation, Long> {

}