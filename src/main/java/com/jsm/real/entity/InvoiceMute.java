package com.jsm.real.entity;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InvoiceMute {
	@Id private Long invoice_id;
	private String invoice_date;
	private Double invoice_amt;
	private Long rental_id;
	private Double amt_to_pay;
	private String paid_status;
	public Long getInvoice_id() {
		return invoice_id;
	}
	public void setInvoice_id(Long invoice_id) {
		this.invoice_id = invoice_id;
	}
	public String getInvoice_date() {
		return invoice_date;
	}
	public void setInvoice_date(String invoice_date) {
		this.invoice_date = invoice_date;
	}
	public Double getInvoice_amt() {
		return invoice_amt;
	}
	public void setInvoice_amt(Double invoice_amt) {
		this.invoice_amt = invoice_amt;
	}
	public Long getRental_id() {
		return rental_id;
	}
	public void setRental_id(Long rental_id) {
		this.rental_id = rental_id;
	}
	public Double getAmt_to_pay() {
		return amt_to_pay;
	}
	public void setAmt_to_pay(Double amt_to_pay) {
		this.amt_to_pay = amt_to_pay;
	}
	public String getPaid_status() {
		return paid_status;
	}
	public void setPaid_status(String paid_status) {
		this.paid_status = paid_status;
	}

}