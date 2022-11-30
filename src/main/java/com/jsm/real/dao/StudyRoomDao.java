package com.jsm.real.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jsm.real.entity.StudyRoom;

@Repository
public interface StudyRoomDao extends JpaRepository<StudyRoom, Integer> {

}
