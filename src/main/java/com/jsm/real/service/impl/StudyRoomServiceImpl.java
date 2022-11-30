package com.jsm.real.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.StudyRoomDao;
import com.jsm.real.entity.StudyRoom;
import com.jsm.real.service.StudyRoomService;

@Service
public class StudyRoomServiceImpl implements StudyRoomService {
	@Autowired
	StudyRoomDao studyRoomDao;
	
	@Override
	public void addRoom(StudyRoom room) {
		studyRoomDao.save(room);
	}

	@Override
	public List<StudyRoom> getRoomList() {
		return studyRoomDao.findAll();
	}

	@Override
	public void deleteRoom(StudyRoom room) {
		studyRoomDao.deleteById(room.getRoom_id());
	}
}
