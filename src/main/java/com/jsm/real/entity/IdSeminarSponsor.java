package com.jsm.real.entity;

import java.io.Serializable;

public class IdSeminarSponsor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4278547120985222800L;
	private Long spid;
	private Long event_id;
	private Double sponsor_amt;
	public Long getSpid() {
		return spid;
	}
	public void setSpid(Long spid) {
		this.spid = spid;
	}
	public Long getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}
	public Double getSponsor_amt() {
		return sponsor_amt;
	}
	public void setSponsor_amt(Double sponsor_amt) {
		this.sponsor_amt = sponsor_amt;
	}
}
