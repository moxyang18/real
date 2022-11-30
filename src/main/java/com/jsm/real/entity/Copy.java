package com.jsm.real.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="jsm_copy")
public class Copy {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY) private Long copy_id;
	private Character status = 'Y';
	private Long bid;
	public Long getCopy_id() {
		return copy_id;
	}
	public void setCopy_id(Long copy_id) {
		this.copy_id = copy_id;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Long getBid() {
		return bid;
	}
	public void setBid(Long bid) {
		this.bid = bid;
	}	

}
