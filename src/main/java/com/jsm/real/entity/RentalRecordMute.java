package com.jsm.real.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
public class RentalRecordMute {
	@Id private Long rental_id;
	private Character rental_status;
	private String borrow_date;
	private String exp_ret_date;
	private String act_ret_date;
	private Long copy_id;
	private Long UID;
	public Long getRental_id() {
		return rental_id;
	}
	public void setRental_id(Long rental_id) {
		this.rental_id = rental_id;
	}
	public Character getRental_status() {
		return rental_status;
	}
	public void setRental_status(Character rental_status) {
		this.rental_status = rental_status;
	}
	public String getBorrow_date() {
		return borrow_date;
	}
	public void setBorrow_date(String borrow_date) {
		this.borrow_date = borrow_date;
	}
	public String getExp_ret_date() {
		return exp_ret_date;
	}
	public void setExp_ret_date(String exp_ret_date) {
		this.exp_ret_date = exp_ret_date;
	}
	public String getAct_ret_date() {
		return act_ret_date;
	}
	public void setAct_ret_date(String act_ret_date) {
		this.act_ret_date = act_ret_date;
	}
	public Long getCopy_id() {
		return copy_id;
	}
	public void setCopy_id(Long copy_id) {
		this.copy_id = copy_id;
	}
	public Long getUID() {
		return UID;
	}
	public void setUID(Long uID) {
		UID = uID;
	}

}