package com.jsm.real.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(IdSeminarSponsor.class)
@Table(name="jsm_seminar_sponsor")
public class SeminarSponsor {
	@Id 
	private Long spid;
	@Id 
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
