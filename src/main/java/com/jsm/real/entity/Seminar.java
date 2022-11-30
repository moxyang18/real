package com.jsm.real.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="jsm_seminar")
//@DiscriminatorValue("S")
public class Seminar {
	@Id private Long event_id;
	private Integer SEM_CAPACITY;
	public Long getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}
	public Integer getSEM_CAPACITY() {
		return SEM_CAPACITY;
	}
	public void setSEM_CAPACITY(int sEM_CAPACITY) {
		SEM_CAPACITY = sEM_CAPACITY;
	}
	
}
