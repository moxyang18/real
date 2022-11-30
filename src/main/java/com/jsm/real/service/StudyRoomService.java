package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.StudyRoom;

public interface StudyRoomService {
	// add/update a study room
	void addRoom(StudyRoom room);
	// get all rooms list
	List<StudyRoom> getRoomList();
	// delete a room
	void deleteRoom(StudyRoom room);
}
