package com.jsm.real.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PaymentMute {
	@Id private Long pid;
	private String paid_date;
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
	public String getPaid_date() {
		return paid_date;
	}
	public void setPaid_date(String paid_date) {
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
