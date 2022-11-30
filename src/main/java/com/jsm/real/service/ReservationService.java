package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Reservation;

public interface ReservationService {
	// add a new reservation
	void addResv(Reservation resv);
	// get the lists of all reservations
	List<Reservation> getResvList();
	// get the list of reservations of the room on a certain day
	List<Reservation> getResvListByRoomDate(Reservation resv);
	// filter reservations by
	List<Reservation> getResvListBy(Reservation resv);
	// delete a reservation
	void deleteResv(Integer resvId);
	// check whether the future reservation specified by params exists
	boolean existsFutureResvByResvIdUid(Integer resvNo, Long uid);
}
