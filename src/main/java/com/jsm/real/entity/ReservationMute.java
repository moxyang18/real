package com.jsm.real.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ReservationMute {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY) private Integer resv_no;
	private String resv_date;
	private Integer time_slot;
	private Long UID;
	private Integer room_id;
	public Integer getResv_no() {
		return resv_no;
	}
	public void setResv_no(Integer resv_no) {
		this.resv_no = resv_no;
	}
	public String getResv_date() {
		return resv_date;
	}
	public void setResv_date(String resv_date) {
		this.resv_date = resv_date;
	}
	public Integer getTime_slot() {
		return time_slot;
	}
	public void setTime_slot(Integer time_slot) {
		this.time_slot = time_slot;
	}
	public Long getUID() {
		return UID;
	}
	public void setUID(Long uID) {
		UID = uID;
	}
	public Integer getRoom_id() {
		return room_id;
	}
	public void setRoom_id(Integer room_id) {
		this.room_id = room_id;
	}
}
