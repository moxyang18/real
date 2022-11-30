package com.jsm.real.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="jsm_registration")
public class Registration {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY) private Long reg_id;
	private Long event_id;
	private Long UID;
	public Long getReg_id() {
		return reg_id;
	}
	public void setReg_id(Long reg_id) {
		this.reg_id = reg_id;
	}
	public Long getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}
	public Long getUID() {
		return UID;
	}
	public void setUID(Long uID) {
		UID = uID;
	}
	
}
