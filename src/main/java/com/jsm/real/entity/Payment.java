package com.jsm.real.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="jsm_payment")
public class Payment {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY) private Long pid;
	private Date paid_date;
	private Character PAID_METHOD;
	private Double paid_amt;
	private String card_hdr_fname;
	private String card_hdr_lname;
	private Long invoice_id;
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Date getPaid_date() {
		return paid_date;
	}
	public void setPaid_date(Date paid_date) {
		this.paid_date = paid_date;
	}
	public Character getPAID_METHOD() {
		return PAID_METHOD;
	}
	public void setPAID_METHOD(Character pAID_METHOD) {
		PAID_METHOD = pAID_METHOD;
	}
	public Double getPaid_amt() {
		return paid_amt;
	}
	public void setPaid_amt(Double paid_amt) {
		this.paid_amt = paid_amt;
	}
	public String getCard_hdr_fname() {
		return card_hdr_fname;
	}
	public void setCard_hdr_fname(String card_hdr_fname) {
		this.card_hdr_fname = card_hdr_fname;
	}
	public String getCard_hdr_lname() {
		return card_hdr_lname;
	}
	public void setCard_hdr_lname(String card_hdr_lname) {
		this.card_hdr_lname = card_hdr_lname;
	}
	public Long getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(Long invoice_id) {
		this.invoice_id = invoice_id;
	}
	
}
