package com.jsm.real.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="jsm_invitation")
public class Invitation {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY) private Long inv_id;
	private Long event_id;
	private Long UID;
	public Long getInv_id() {
		return inv_id;
	}
	public void setInv_id(Long inv_id) {
		this.inv_id = inv_id;
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
